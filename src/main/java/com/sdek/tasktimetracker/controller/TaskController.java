package com.sdek.tasktimetracker.controller;

import com.sdek.tasktimetracker.model.dto.request.TaskCreateRequest;
import com.sdek.tasktimetracker.model.dto.request.TaskUpdateStatusRequest;
import com.sdek.tasktimetracker.model.dto.response.TaskResponse;
import com.sdek.tasktimetracker.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Задачи", description = "Управление задачами")
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Создать задачу", description = "Создаёт новую задачу со статусом NEW")
    @PostMapping
    public ResponseEntity<TaskResponse> create(@RequestBody @Valid TaskCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(request));
    }

    @Operation(summary = "Получить задачу по ID", description = "Возвращает задачу по указанному идентификатору")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getById(id));
    }

    @Operation(summary = "Изменить статус задачи", description = "Обновляет статус задачи: NEW, IN_PROGRESS, DONE")
    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateStatus(@PathVariable Long id,
                                                     @RequestBody @Valid TaskUpdateStatusRequest request) {
        return ResponseEntity.ok(taskService.updateStatus(id, request));
    }
}
