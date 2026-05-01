package com.sdek.tasktimetracker.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskCreateRequest {

    @NotBlank(message = "Название задачи не должно быть пустым")
    @Size(max = 255, message = "Название задачи не должно превышать 255 символов")
    private String title;

    @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
    private String description;
}
