package com.sdek.tasktimetracker.service.impl;

import com.sdek.tasktimetracker.exception.TaskNotFoundException;
import com.sdek.tasktimetracker.mapper.dto.TaskDtoMapper;
import com.sdek.tasktimetracker.mapper.mybatis.TaskMapper;
import com.sdek.tasktimetracker.model.dto.request.TaskCreateRequest;
import com.sdek.tasktimetracker.model.dto.request.TaskUpdateStatusRequest;
import com.sdek.tasktimetracker.model.dto.response.TaskResponse;
import com.sdek.tasktimetracker.model.entity.Task;
import com.sdek.tasktimetracker.model.enums.TaskStatus;
import com.sdek.tasktimetracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final TaskDtoMapper taskDtoMapper;

    @Override
    public TaskResponse create(TaskCreateRequest request) {
        Task task = taskDtoMapper.toEntity(request);
        task.setStatus(TaskStatus.NEW);
        taskMapper.insert(task);
        return taskDtoMapper.toResponse(task);
    }

    @Override
    public TaskResponse getById(Long id) {
        return taskDtoMapper.toResponse(
                taskMapper.findById(id).orElseThrow(() -> new TaskNotFoundException(id))
        );
    }

    @Override
    public TaskResponse updateStatus(Long id, TaskUpdateStatusRequest request) {
        Task task = taskMapper.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        taskMapper.updateStatus(id, request.getStatus());
        task.setStatus(request.getStatus());
        return taskDtoMapper.toResponse(task);
    }
}
