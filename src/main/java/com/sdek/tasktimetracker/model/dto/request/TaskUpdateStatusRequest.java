package com.sdek.tasktimetracker.model.dto.request;

import com.sdek.tasktimetracker.model.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskUpdateStatusRequest {

    @NotNull(message = "Status must not be null")
    private TaskStatus status;
}
