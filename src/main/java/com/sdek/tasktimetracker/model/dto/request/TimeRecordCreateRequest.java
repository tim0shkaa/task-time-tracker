package com.sdek.tasktimetracker.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeRecordCreateRequest {

    @NotNull(message = "Task ID must not be null")
    @Positive(message = "Task ID must be positive")
    private Long taskId;

    @NotNull(message = "Start time must not be null")
    private LocalDateTime startTime;

    @NotNull(message = "End time must not be null")
    private LocalDateTime endTime;

    private String description;
}
