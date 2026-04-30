package com.sdek.tasktimetracker.service.impl;

import com.sdek.tasktimetracker.exception.InvalidTimeRangeException;
import com.sdek.tasktimetracker.exception.TaskNotFoundException;
import com.sdek.tasktimetracker.mapper.dto.TimeRecordDtoMapper;
import com.sdek.tasktimetracker.mapper.mybatis.TaskMapper;
import com.sdek.tasktimetracker.mapper.mybatis.TimeRecordMapper;
import com.sdek.tasktimetracker.model.dto.request.TimeRecordCreateRequest;
import com.sdek.tasktimetracker.model.dto.response.EmployeeTimeReportResponse;
import com.sdek.tasktimetracker.model.dto.response.TimeRecordResponse;
import com.sdek.tasktimetracker.model.entity.TimeRecord;
import com.sdek.tasktimetracker.service.TimeRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeRecordServiceImpl implements TimeRecordService {

    private final TimeRecordMapper timeRecordMapper;
    private final TaskMapper taskMapper;
    private final TimeRecordDtoMapper timeRecordDtoMapper;

    @Override
    public TimeRecordResponse create(TimeRecordCreateRequest request, Long employeeId) {
        taskMapper.findById(request.getTaskId())
                .orElseThrow(() -> new TaskNotFoundException(request.getTaskId()));

        if (!request.getStartTime().isBefore(request.getEndTime())) {
            throw new InvalidTimeRangeException();
        }

        TimeRecord record = timeRecordDtoMapper.toEntity(request, employeeId);
        timeRecordMapper.insert(record);

        return timeRecordDtoMapper.toResponse(record);
    }

    @Override
    public EmployeeTimeReportResponse getReport(Long employeeId, LocalDateTime from, LocalDateTime to) {
        if (!from.isBefore(to)) {
            throw new InvalidTimeRangeException();
        }

        List<TimeRecord> records = timeRecordMapper.findByEmployeeIdAndPeriod(employeeId, from, to);
        List<TimeRecordResponse> responseList = records.stream()
                .map(timeRecordDtoMapper::toResponse)
                .toList();

        long totalMinutes = records.stream()
                .mapToLong(r -> {
                    LocalDateTime effectiveStart = r.getStartTime().isBefore(from) ? from : r.getStartTime();
                    LocalDateTime effectiveEnd = r.getEndTime().isAfter(to) ? to : r.getEndTime();
                    return Duration.between(effectiveStart, effectiveEnd).toMinutes();
                })
                .sum();

        EmployeeTimeReportResponse report = new EmployeeTimeReportResponse();
        report.setEmployeeId(employeeId);
        report.setTotalMinutes(totalMinutes);
        report.setRecords(responseList);

        return report;
    }
}
