package com.hospital.controller;

import com.hospital.dto.TimeSlotResponseDTO;
import com.hospital.dto.WeeklyRecurringTimeSlotRequestDTO;
import com.hospital.entity.TimeSlot;
import com.hospital.service.TimeSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/timeslots")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    @PostMapping("/create")
    public ResponseEntity<List<TimeSlotResponseDTO>> registerWeeklySlots(@RequestBody WeeklyRecurringTimeSlotRequestDTO dto) {
        List<TimeSlot> slots = timeSlotService.generateWeeklyRecurringSlots(dto);
        List<TimeSlotResponseDTO> response = slots.stream()
                .map(TimeSlotResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(response);
    }
}
