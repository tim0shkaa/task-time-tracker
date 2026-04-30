package com.sdek.tasktimetracker.mapper.mybatis;

import com.sdek.tasktimetracker.model.entity.Task;
import com.sdek.tasktimetracker.model.enums.TaskStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface TaskMapper {

    void insert(Task task);

    Optional<Task> findById(Long id);

    void updateStatus(@Param("id") Long id, @Param("status") TaskStatus status);
}