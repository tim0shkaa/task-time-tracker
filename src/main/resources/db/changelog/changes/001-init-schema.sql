--liquibase formatted sql

--changeset timokhin:1
CREATE TABLE task (
    id          BIGSERIAL    PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    status      VARCHAR(20)  NOT NULL DEFAULT 'NEW'
);

--changeset timokhin:2
CREATE TABLE time_record (
    id          BIGSERIAL PRIMARY KEY,
    employee_id BIGINT    NOT NULL,
    task_id     BIGINT    NOT NULL REFERENCES task(id),
    start_time  TIMESTAMP NOT NULL,
    end_time    TIMESTAMP NOT NULL,
    description TEXT
);