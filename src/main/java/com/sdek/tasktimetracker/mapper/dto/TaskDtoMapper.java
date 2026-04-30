package com.sdek.tasktimetracker.mapper.dto;

import com.sdek.tasktimetracker.model.dto.request.TaskCreateRequest;
import com.sdek.tasktimetracker.model.dto.response.TaskResponse;
import com.sdek.tasktimetracker.model.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskDtoMapper {

    Task toEntity(TaskCreateRequest request);

    TaskResponse toResponse(Task task);
}