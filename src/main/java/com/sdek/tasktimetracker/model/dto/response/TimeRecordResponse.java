package com.sdek.tasktimetracker.model.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeRecordResponse {
    private Long id;
    private Long employeeId;
    private Long taskId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
}
