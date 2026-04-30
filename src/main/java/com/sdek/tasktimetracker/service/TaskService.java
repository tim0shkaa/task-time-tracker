package com.sdek.tasktimetracker.service;

import com.sdek.tasktimetracker.model.dto.request.TaskCreateRequest;
import com.sdek.tasktimetracker.model.dto.request.TaskUpdateStatusRequest;
import com.sdek.tasktimetracker.model.dto.response.TaskResponse;

public interface TaskService {
    TaskResponse create(TaskCreateRequest request);
    TaskResponse getById(Long id);
    TaskResponse updateStatus(Long id, TaskUpdateStatusRequest request);
}