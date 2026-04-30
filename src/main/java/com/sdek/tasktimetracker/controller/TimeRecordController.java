package com.sdek.tasktimetracker.controller;

import com.sdek.tasktimetracker.model.dto.request.TimeRecordCreateRequest;
import com.sdek.tasktimetracker.model.dto.response.EmployeeTimeReportResponse;
import com.sdek.tasktimetracker.model.dto.response.TimeRecordResponse;
import com.sdek.tasktimetracker.service.TimeRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/time-records")
@RequiredArgsConstructor
public class TimeRecordController {

    private final TimeRecordService timeRecordService;

    @PostMapping
    public ResponseEntity<TimeRecordResponse> create(@RequestBody @Valid TimeRecordCreateRequest request,
                                                     Authentication authentication) {
        Long employeeId = Long.parseLong(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(timeRecordService.create(request, employeeId));
    }

    @GetMapping("/report/{employeeId}")
    public ResponseEntity<EmployeeTimeReportResponse> getReport(
            @PathVariable Long employeeId,
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to) {
        return ResponseEntity.ok(timeRecordService.getReport(employeeId, from, to));
    }
}
