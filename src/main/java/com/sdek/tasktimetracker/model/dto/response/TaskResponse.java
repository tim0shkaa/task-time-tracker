package com.sdek.tasktimetracker.model.dto.response;

import com.sdek.tasktimetracker.model.enums.TaskStatus;
import lombok.Data;

@Data
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
}
