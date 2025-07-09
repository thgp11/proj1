package com.hospital.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="holidays")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String name; // 휴일 이름

    @Enumerated(EnumType.STRING)
    private HolidayType type; // 휴일 유형 (예: 공휴일, 병원 휴무 등)

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = true)
    private Doctor doctor; // 특정 의사에 대한 휴일, null일 경우 전체 병원 휴일
    private boolean isClosed = true; // false일 경우 병원 예외 영업일
}
