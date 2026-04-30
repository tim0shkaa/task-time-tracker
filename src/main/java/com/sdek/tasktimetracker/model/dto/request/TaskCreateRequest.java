package com.sdek.tasktimetracker.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskCreateRequest {

    @NotBlank(message = "Title must not be blank")
    private String title;

    private String description;
}
