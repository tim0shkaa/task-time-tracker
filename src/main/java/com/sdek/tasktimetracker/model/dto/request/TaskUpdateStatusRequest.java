package com.sdek.tasktimetracker.model.dto.request;

import com.sdek.tasktimetracker.model.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskUpdateStatusRequest {

    @NotNull(message = "Статус не должен быть пустым")
    private TaskStatus status;
}
