package com.sdek.tasktimetracker.controller;

import com.sdek.tasktimetracker.model.dto.request.TaskCreateRequest;
import com.sdek.tasktimetracker.model.dto.request.TaskUpdateStatusRequest;
import com.sdek.tasktimetracker.model.dto.response.TaskResponse;
import com.sdek.tasktimetracker.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> create(@RequestBody @Valid TaskCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateStatus(@PathVariable Long id,
                                                     @RequestBody @Valid TaskUpdateStatusRequest request) {
        return ResponseEntity.ok(taskService.updateStatus(id, request));
    }
}
