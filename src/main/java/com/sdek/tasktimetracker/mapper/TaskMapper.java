package com.sdek.tasktimetracker.mapper;

import com.sdek.tasktimetracker.model.entity.Task;
import com.sdek.tasktimetracker.model.enums.TaskStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TaskMapper {
    void insert(Task task);
    Task findById(Long id);
    void updateStatus(@Param("id") Long id, @Param("status") TaskStatus status);
}