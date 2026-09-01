package com.mateuscancado.employee_attendance_history.dto;

import com.mateuscancado.employee_attendance_history.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AttendanceRequestDTO(
        @NotNull(message = "O ID do funcionário é obrigatório")
        Long employeeId,

        @NotNull(message = "A data é obrigatória")
        @PastOrPresent(message = "A data não pode ser no futuro")
        LocalDate date,

        @Size(max = 60, message = "A descrição deve ter no máximo 60 caracteres")
        String description,

        @NotNull(message = "O status do registro é obrigatório")
        AttendanceStatus status
) {}
