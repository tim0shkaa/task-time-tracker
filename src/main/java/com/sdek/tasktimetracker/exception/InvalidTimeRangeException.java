package com.sdek.tasktimetracker.exception;

public class InvalidTimeRangeException extends RuntimeException {
    public InvalidTimeRangeException() {
        super("Время начала должно быть раньше времени окончания");
    }
}
