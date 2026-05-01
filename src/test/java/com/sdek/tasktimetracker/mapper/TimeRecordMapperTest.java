package com.sdek.tasktimetracker.mapper;

import com.sdek.tasktimetracker.mapper.mybatis.TaskMapper;
import com.sdek.tasktimetracker.mapper.mybatis.TimeRecordMapper;
import com.sdek.tasktimetracker.model.entity.Task;
import com.sdek.tasktimetracker.model.entity.TimeRecord;
import com.sdek.tasktimetracker.model.enums.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@Testcontainers
@Disabled("Требует совместимой версии Docker окружения")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(LiquibaseAutoConfiguration.class)
class TimeRecordMapperTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TimeRecordMapper timeRecordMapper;

    @Autowired
    private TaskMapper taskMapper;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setTitle("Тестовая задача");
        task.setStatus(TaskStatus.NEW);
        taskMapper.insert(task);
    }

    @Test
    void insert_shouldSaveTimeRecordAndGenerateId() {
        TimeRecord record = new TimeRecord();
        record.setEmployeeId(1L);
        record.setTaskId(task.getId());
        record.setStartTime(LocalDateTime.of(2024, 1, 15, 9, 0));
        record.setEndTime(LocalDateTime.of(2024, 1, 15, 12, 0));
        record.setDescription("Работа над задачей");

        timeRecordMapper.insert(record);

        assertNotNull(record.getId());
    }

    @Test
    void findByEmployeeIdAndPeriod_shouldReturnRecords_whenInPeriod() {
        TimeRecord record = new TimeRecord();
        record.setEmployeeId(1L);
        record.setTaskId(task.getId());
        record.setStartTime(LocalDateTime.of(2024, 1, 15, 9, 0));
        record.setEndTime(LocalDateTime.of(2024, 1, 15, 12, 0));
        record.setDescription("Работа над задачей");
        timeRecordMapper.insert(record);

        List<TimeRecord> result = timeRecordMapper.findByEmployeeIdAndPeriod(
                1L,
                LocalDateTime.of(2024, 1, 15, 0, 0),
                LocalDateTime.of(2024, 1, 15, 23, 59)
        );

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getEmployeeId());
    }

    @Test
    void findByEmployeeIdAndPeriod_shouldReturnEmpty_whenOutOfPeriod() {
        TimeRecord record = new TimeRecord();
        record.setEmployeeId(1L);
        record.setTaskId(task.getId());
        record.setStartTime(LocalDateTime.of(2024, 1, 15, 9, 0));
        record.setEndTime(LocalDateTime.of(2024, 1, 15, 12, 0));
        timeRecordMapper.insert(record);

        List<TimeRecord> result = timeRecordMapper.findByEmployeeIdAndPeriod(
                1L,
                LocalDateTime.of(2024, 1, 20, 0, 0),
                LocalDateTime.of(2024, 1, 20, 23, 59)
        );

        assertTrue(result.isEmpty());
    }

    @Test
    void findByEmployeeIdAndPeriod_shouldReturnPartiallyOverlappingRecords() {
        TimeRecord record = new TimeRecord();
        record.setEmployeeId(1L);
        record.setTaskId(task.getId());
        record.setStartTime(LocalDateTime.of(2024, 1, 14, 22, 0));
        record.setEndTime(LocalDateTime.of(2024, 1, 15, 11, 0));
        timeRecordMapper.insert(record);

        List<TimeRecord> result = timeRecordMapper.findByEmployeeIdAndPeriod(
                1L,
                LocalDateTime.of(2024, 1, 15, 0, 0),
                LocalDateTime.of(2024, 1, 15, 23, 59)
        );

        assertEquals(1, result.size());
    }
}