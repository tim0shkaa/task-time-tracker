package com.sdek.tasktimetracker.service;

import com.sdek.tasktimetracker.exception.InvalidTimeRangeException;
import com.sdek.tasktimetracker.exception.TaskNotFoundException;
import com.sdek.tasktimetracker.mapper.dto.TimeRecordDtoMapper;
import com.sdek.tasktimetracker.mapper.mybatis.TaskMapper;
import com.sdek.tasktimetracker.mapper.mybatis.TimeRecordMapper;
import com.sdek.tasktimetracker.model.dto.request.TimeRecordCreateRequest;
import com.sdek.tasktimetracker.model.dto.response.EmployeeTimeReportResponse;
import com.sdek.tasktimetracker.model.dto.response.TimeRecordResponse;
import com.sdek.tasktimetracker.model.entity.Task;
import com.sdek.tasktimetracker.model.entity.TimeRecord;
import com.sdek.tasktimetracker.model.enums.TaskStatus;
import com.sdek.tasktimetracker.service.impl.TimeRecordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeRecordServiceImplTest {

    @Mock
    private TimeRecordMapper timeRecordMapper;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private TimeRecordDtoMapper timeRecordDtoMapper;

    @InjectMocks
    private TimeRecordServiceImpl timeRecordService;

    private Task task;
    private TimeRecord timeRecord;
    private TimeRecordCreateRequest createRequest;
    private TimeRecordResponse timeRecordResponse;

    @BeforeEach
    void setUp() {
        task = new Task(1L, "Тестовая задача", "Описание", TaskStatus.NEW);

        createRequest = new TimeRecordCreateRequest();
        createRequest.setTaskId(1L);
        createRequest.setStartTime(LocalDateTime.of(2024, 1, 15, 9, 0));
        createRequest.setEndTime(LocalDateTime.of(2024, 1, 15, 12, 0));
        createRequest.setDescription("Работа над задачей");

        timeRecord = new TimeRecord();
        timeRecord.setId(1L);
        timeRecord.setEmployeeId(1L);
        timeRecord.setTaskId(1L);
        timeRecord.setStartTime(createRequest.getStartTime());
        timeRecord.setEndTime(createRequest.getEndTime());
        timeRecord.setDescription("Работа над задачей");

        timeRecordResponse = new TimeRecordResponse();
        timeRecordResponse.setId(1L);
        timeRecordResponse.setEmployeeId(1L);
        timeRecordResponse.setTaskId(1L);
        timeRecordResponse.setStartTime(createRequest.getStartTime());
        timeRecordResponse.setEndTime(createRequest.getEndTime());
    }

    @Test
    void create_shouldReturnTimeRecordResponse() {
        when(taskMapper.findById(1L)).thenReturn(Optional.of(task));
        when(timeRecordDtoMapper.toEntity(createRequest, 1L)).thenReturn(timeRecord);
        when(timeRecordDtoMapper.toResponse(timeRecord)).thenReturn(timeRecordResponse);

        TimeRecordResponse result = timeRecordService.create(createRequest, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getTaskId());
        verify(timeRecordMapper).insert(timeRecord);
    }

    @Test
    void create_shouldThrowTaskNotFoundException_whenTaskNotExists() {
        when(taskMapper.findById(99L)).thenReturn(Optional.empty());
        createRequest.setTaskId(99L);

        assertThrows(TaskNotFoundException.class, () -> timeRecordService.create(createRequest, 1L));
    }

    @Test
    void create_shouldThrowInvalidTimeRangeException_whenStartTimeAfterEndTime() {
        when(taskMapper.findById(1L)).thenReturn(Optional.of(task));
        createRequest.setStartTime(LocalDateTime.of(2024, 1, 15, 12, 0));
        createRequest.setEndTime(LocalDateTime.of(2024, 1, 15, 9, 0));

        assertThrows(InvalidTimeRangeException.class, () -> timeRecordService.create(createRequest, 1L));
    }

    @Test
    void getReport_shouldReturnReportWithCorrectTotalMinutes() {
        LocalDateTime from = LocalDateTime.of(2024, 1, 15, 0, 0);
        LocalDateTime to = LocalDateTime.of(2024, 1, 15, 23, 59);

        when(timeRecordMapper.findByEmployeeIdAndPeriod(1L, from, to)).thenReturn(List.of(timeRecord));
        when(timeRecordDtoMapper.toResponse(timeRecord)).thenReturn(timeRecordResponse);

        EmployeeTimeReportResponse result = timeRecordService.getReport(1L, from, to);

        assertNotNull(result);
        assertEquals(1L, result.getEmployeeId());
        assertEquals(180L, result.getTotalMinutes());
        assertEquals(1, result.getRecords().size());
    }

    @Test
    void getReport_shouldThrowInvalidTimeRangeException_whenFromAfterTo() {
        LocalDateTime from = LocalDateTime.of(2024, 1, 15, 23, 0);
        LocalDateTime to = LocalDateTime.of(2024, 1, 15, 9, 0);

        assertThrows(InvalidTimeRangeException.class, () -> timeRecordService.getReport(1L, from, to));
    }

    @Test
    void getReport_shouldReturnZeroMinutes_whenNoRecords() {
        LocalDateTime from = LocalDateTime.of(2024, 1, 15, 0, 0);
        LocalDateTime to = LocalDateTime.of(2024, 1, 15, 23, 59);

        when(timeRecordMapper.findByEmployeeIdAndPeriod(1L, from, to)).thenReturn(List.of());

        EmployeeTimeReportResponse result = timeRecordService.getReport(1L, from, to);

        assertEquals(0L, result.getTotalMinutes());
        assertTrue(result.getRecords().isEmpty());
    }
}
