package com.mateuscancado.employee_attendance_history.dto;

import java.time.Instant;

public record StandardErrorDTO(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path
) {}
