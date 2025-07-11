package com.hospital.entity;

public enum MemberRole {
    USER,
    DOCTOR,
    ADMIN;

    public boolean isDoctor() {
        return this == DOCTOR;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean isUser() {
        return this == USER;
    }
}
