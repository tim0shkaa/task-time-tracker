package com.sdek.tasktimetracker.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeRecordCreateRequest {

    @NotNull(message = "ID задачи не должен быть пустым")
    @Positive(message = "ID задачи должен быть положительным")
    private Long taskId;

    @NotNull(message = "Время начала не должно быть пустым")
    private LocalDateTime startTime;

    @NotNull(message = "Время окончания не должно быть пустым")
    private LocalDateTime endTime;

    @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
    private String description;
}
