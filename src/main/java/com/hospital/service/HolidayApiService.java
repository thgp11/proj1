package com.hospital.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HolidayApiService {
    private final String SERVICE_KEY = "발급받은_API_키";

    public List<LocalDate> fetchHolidays(int year) throws IOException {
        String url = "https://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo" +
                "?ServiceKey=" + SERVICE_KEY +
                "&solYear=" + year +
                "&numOfRows=100" +
                "&_type=json";

        URL requestUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response = reader.lines().collect(Collectors.joining());

        // JSON 파싱 (Jackson이나 Gson 사용 권장)
        // 예시 생략 — 필요 시 코드 제공 가능

        return List.of(); // 파싱된 LocalDate 리스트 반환
    }
}
