package com.hospital.entity;

public enum ReservationStatus {
    RESERVED,       // 예약 완료
    CANCELED,       // 예약 취소
    COMPLETED,      // 진료 완료
    NO_SHOW         // 미방문
}
