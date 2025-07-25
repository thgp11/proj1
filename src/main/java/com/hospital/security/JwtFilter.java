package com.hospital.security;

import com.hospital.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = extractToken(request);
            if (token != null) {
                // 블랙리스트 확인
                if (jwtUtil.isBlacklisted(token)) {
                    logger.warn("블랙리스트에 등록된 JWT 토큰");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Token is blacklisted");
                    return;
                }
                // JWT 유효성 검사
                if (jwtUtil.validateToken(token)) {
                    String email = jwtUtil.extractEmail(token);
                    authenticateUser(email, token);
                    logger.info("인증 성공 : {}", email);
                } else {
                    logger.warn("유효하지 않은 JWT 토큰");
                }
            }
            } catch(Exception e){
                logger.error("JWT 처리 중 예외 발생 : ", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired JWT token");
                return;
            }
            filterChain.doFilter(request, response);
        }

        private String extractToken (HttpServletRequest request){
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            }
            return null;
        }

        private void authenticateUser (String email, String token){
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            Claims claims = jwtUtil.parseClaims(token);

            Object rolesObject = claims.get("roles");
            List<String> roles;

            if (rolesObject instanceof List<?>) {
                roles = ((List<?>) rolesObject).stream()
                        .map(Object::toString)
                        .collect(Collectors.toList());
            } else {
                roles = List.of(); // 빈리스트로 초기화
            }

            List<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }
