package com.mateuscancado.employee_attendance_history.dto;

import com.mateuscancado.employee_attendance_history.enums.AttendanceStatus;

import java.time.LocalDate;

public record AttendanceDTO(Long id,
                            Long employeeId,
                            LocalDate date,
                            String description,
                            AttendanceStatus status
) {}
