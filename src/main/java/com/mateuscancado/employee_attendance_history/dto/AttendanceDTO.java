package com.mateuscancado.employee_attendance_history.dto;

import com.mateuscancado.employee_attendance_history.enums.AttendanceStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record AttendanceDTO(
        @Schema(description = "ID do registro de atendimento", example = "1")
        Long id,

        @Schema(description = "ID do funcionário atendido", example = "105")
        Long employeeId,

        @Schema(description = "Data do registro", example = "2026-09-10")
        LocalDate date,

        @Schema(description = "Descrição do atendimento", example = "Suporte técnico para configuração de VPN")
        String description,

        @Schema(description = "Status do atendimento", example = "OPEN")
        AttendanceStatus status
) {}
