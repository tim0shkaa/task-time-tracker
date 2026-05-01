package com.sdek.tasktimetracker.service;

import com.sdek.tasktimetracker.exception.TaskNotFoundException;
import com.sdek.tasktimetracker.mapper.dto.TaskDtoMapper;
import com.sdek.tasktimetracker.mapper.mybatis.TaskMapper;
import com.sdek.tasktimetracker.model.dto.request.TaskCreateRequest;
import com.sdek.tasktimetracker.model.dto.request.TaskUpdateStatusRequest;
import com.sdek.tasktimetracker.model.dto.response.TaskResponse;
import com.sdek.tasktimetracker.model.entity.Task;
import com.sdek.tasktimetracker.model.enums.TaskStatus;
import com.sdek.tasktimetracker.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private TaskDtoMapper taskDtoMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private TaskResponse taskResponse;
    private TaskCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        task = new Task(1L, "Тестовая задача", "Описание", TaskStatus.NEW);
        taskResponse = new TaskResponse();
        taskResponse.setId(1L);
        taskResponse.setTitle("Тестовая задача");
        taskResponse.setDescription("Описание");
        taskResponse.setStatus(TaskStatus.NEW);

        createRequest = new TaskCreateRequest();
        createRequest.setTitle("Тестовая задача");
        createRequest.setDescription("Описание");
    }

    @Test
    void create_shouldReturnTaskResponse() {
        when(taskDtoMapper.toEntity(createRequest)).thenReturn(task);
        when(taskDtoMapper.toResponse(task)).thenReturn(taskResponse);

        TaskResponse result = taskService.create(createRequest);

        assertNotNull(result);
        assertEquals("Тестовая задача", result.getTitle());
        verify(taskMapper).insert(task);
    }

    @Test
    void getById_shouldReturnTaskResponse_whenTaskExists() {
        when(taskMapper.findById(1L)).thenReturn(Optional.of(task));
        when(taskDtoMapper.toResponse(task)).thenReturn(taskResponse);

        TaskResponse result = taskService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getById_shouldThrowTaskNotFoundException_whenTaskNotExists() {
        when(taskMapper.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getById(99L));
    }

    @Test
    void updateStatus_shouldReturnUpdatedTaskResponse() {
        TaskUpdateStatusRequest request = new TaskUpdateStatusRequest();
        request.setStatus(TaskStatus.IN_PROGRESS);

        TaskResponse updatedResponse = new TaskResponse();
        updatedResponse.setId(1L);
        updatedResponse.setStatus(TaskStatus.IN_PROGRESS);

        when(taskMapper.findById(1L)).thenReturn(Optional.of(task));
        when(taskDtoMapper.toResponse(task)).thenReturn(updatedResponse);

        TaskResponse result = taskService.updateStatus(1L, request);

        assertEquals(TaskStatus.IN_PROGRESS, result.getStatus());
        verify(taskMapper).updateStatus(1L, TaskStatus.IN_PROGRESS);
    }

    @Test
    void updateStatus_shouldThrowTaskNotFoundException_whenTaskNotExists() {
        TaskUpdateStatusRequest request = new TaskUpdateStatusRequest();
        request.setStatus(TaskStatus.IN_PROGRESS);

        when(taskMapper.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.updateStatus(99L, request));
    }
}
