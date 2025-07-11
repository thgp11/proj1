package com.hospital.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {
    private final Key key;
    private final long expirationTime;
    private final RedisTemplate<String, String> redisTemplate;

    public JwtUtil(@Value("${jwt.secret}") String secretKey,
                   @Value("${jwt.expiration}") long expirationTime,
                   RedisTemplate<String, String> redisTemplate) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.expirationTime = expirationTime;
        this.redisTemplate = redisTemplate;
    }

    // JWT 토큰 생성
    public String generateToken(String email, List<String> roles) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT 토큰에서 이메일 추출
    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    //JWT 토큰 검증
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("JWT 토큰 만료");
        } catch (JwtException e) {
            System.out.println("JWT 토큰이 유효하지 않음");
        }
        return false;
    }

    // JWT 토큰에서 Claims 추출
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //JWT 토큰 만료 확인
    public boolean isTokenExpired(String token){
        try {
            return parseClaims(token).getExpiration().before(new Date());
        }catch (Exception e){
            return true;
        }
    }

    // 블랙리스트에 JWT 토큰 추가
    public void blacklistToken(String token) {
        long expiration = parseClaims(token).getExpiration().getTime() - System.currentTimeMillis();
        redisTemplate.opsForValue().set(token, "blacklisted", expiration, TimeUnit.MILLISECONDS);
    }

    // 블랙리스트에 있는지 확인
    public boolean isBlacklisted(String token){
        return redisTemplate.hasKey(token);
    }
}
