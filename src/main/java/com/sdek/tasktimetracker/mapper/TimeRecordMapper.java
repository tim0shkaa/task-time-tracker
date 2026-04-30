package com.sdek.tasktimetracker.mapper;

import com.sdek.tasktimetracker.model.entity.TimeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TimeRecordMapper {
    void insert(TimeRecord record);
    List<TimeRecord> findByEmployeeIdAndPeriod(@Param("employeeId") Long employeeId,
                                               @Param("from") LocalDateTime from,
                                               @Param("to") LocalDateTime to);
}
