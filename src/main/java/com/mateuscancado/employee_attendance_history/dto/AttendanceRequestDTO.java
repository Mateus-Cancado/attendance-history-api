package com.mateuscancado.employee_attendance_history.dto;

import com.mateuscancado.employee_attendance_history.enums.AttendanceStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AttendanceRequestDTO(
        @Schema(description = "ID único do funcionário", example = "105")
        @NotNull(message = "O ID do funcionário é obrigatório")
        Long employeeId,

        @Schema(description = "Data de realização do atendimento", example = "2026-09-10")
        @NotNull(message = "A data é obrigatória")
        @PastOrPresent(message = "A data não pode ser no futuro")
        LocalDate date,

        @Schema(description = "Descrição detalhada do atendimento realizado", example = "Suporte técnico para configuração de VPN")
        @Size(max = 60, message = "A descrição deve ter no máximo 60 caracteres")
        String description,

        @Schema(description = "Status do registro de atendimento", example = "OPEN")
        @NotNull(message = "O status do registro é obrigatório")
        AttendanceStatus status
) {}
