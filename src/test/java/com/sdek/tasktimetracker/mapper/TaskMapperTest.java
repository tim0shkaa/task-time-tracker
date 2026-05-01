package com.sdek.tasktimetracker.mapper;

import com.sdek.tasktimetracker.mapper.mybatis.TaskMapper;
import com.sdek.tasktimetracker.model.entity.Task;
import com.sdek.tasktimetracker.model.enums.TaskStatus;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@Testcontainers
@Disabled("Требует совместимой версии Docker окружения")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(LiquibaseAutoConfiguration.class)
class TaskMapperTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TaskMapper taskMapper;

    @Test
    void insert_shouldSaveTaskAndGenerateId() {
        Task task = new Task();
        task.setTitle("Тестовая задача");
        task.setDescription("Описание");
        task.setStatus(TaskStatus.NEW);

        taskMapper.insert(task);

        assertNotNull(task.getId());
    }

    @Test
    void findById_shouldReturnTask_whenExists() {
        Task task = new Task();
        task.setTitle("Тестовая задача");
        task.setDescription("Описание");
        task.setStatus(TaskStatus.NEW);
        taskMapper.insert(task);

        Optional<Task> result = taskMapper.findById(task.getId());

        assertTrue(result.isPresent());
        assertEquals("Тестовая задача", result.get().getTitle());
        assertEquals(TaskStatus.NEW, result.get().getStatus());
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        Optional<Task> result = taskMapper.findById(999L);

        assertTrue(result.isEmpty());
    }

    @Test
    void updateStatus_shouldChangeTaskStatus() {
        Task task = new Task();
        task.setTitle("Тестовая задача");
        task.setStatus(TaskStatus.NEW);
        taskMapper.insert(task);

        taskMapper.updateStatus(task.getId(), TaskStatus.IN_PROGRESS);

        Optional<Task> result = taskMapper.findById(task.getId());
        assertTrue(result.isPresent());
        assertEquals(TaskStatus.IN_PROGRESS, result.get().getStatus());
    }
}