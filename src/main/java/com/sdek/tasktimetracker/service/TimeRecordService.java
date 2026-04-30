package com.sdek.tasktimetracker.service;

import com.sdek.tasktimetracker.model.dto.request.TimeRecordCreateRequest;
import com.sdek.tasktimetracker.model.dto.response.EmployeeTimeReportResponse;
import com.sdek.tasktimetracker.model.dto.response.TimeRecordResponse;

import java.time.LocalDateTime;

public interface TimeRecordService {
    TimeRecordResponse create(TimeRecordCreateRequest request, Long employeeId);
    EmployeeTimeReportResponse getReport(Long employeeId, LocalDateTime from, LocalDateTime to);
}
