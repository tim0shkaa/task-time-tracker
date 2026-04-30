package com.sdek.tasktimetracker.model.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeTimeReportResponse {
    private Long employeeId;
    private Long totalMinutes;
    private List<TimeRecordResponse> records;
}
