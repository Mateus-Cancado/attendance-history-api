package com.mateuscancado.employee_attendance_history.mapper;

import com.mateuscancado.employee_attendance_history.dto.AttendanceDTO;
import com.mateuscancado.employee_attendance_history.dto.AttendanceRequestDTO;
import com.mateuscancado.employee_attendance_history.enums.AttendanceStatus;
import com.mateuscancado.employee_attendance_history.model.Attendance;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class AttendanceMapperTest {

    private final AttendanceMapper mapper = new AttendanceMapper();

    @Test
    void toResponse_ShouldReturnAttendanceDTO_WhenAttendanceIsNotNull() {
        // Cenário
        Attendance attendance = new Attendance(
                1L,
                100L,
                LocalDate.now(),
                "toResponse",
                AttendanceStatus.RESOLVED
        );
        AttendanceDTO expectedDTO = new AttendanceDTO(
                attendance.getId(),
                attendance.getEmployeeId(),
                attendance.getDate(),
                attendance.getDescription(),
                attendance.getStatus()
        );

        // Execução
        AttendanceDTO result = mapper.toResponse(attendance);

        // Verificação
        Assertions.assertThat(result).isNotNull().isEqualTo(expectedDTO);
    }

    @Test
    void toResponse_ShouldReturnNull_WhenAttendanceIsNull() {
        // Execução
        AttendanceDTO result = mapper.toResponse(null);

        // Verificação
        Assertions.assertThat(result).isNull();
    }

    @Test
    void toEntity_ShouldReturnAttendance_WhenAttendanceRequestDTOIsNotNull() {
        // Cenário
        AttendanceRequestDTO requestDTO = new AttendanceRequestDTO(
                100L,
                LocalDate.now(),
                "toEntity",
                AttendanceStatus.RESOLVED
        );
        Attendance attendance = new Attendance(
                null,
                requestDTO.employeeId(),
                requestDTO.date(),
                requestDTO.description(),
                requestDTO.status()
        );

        // Execução
        Attendance result = mapper.toEntity(requestDTO);

        // Verificação
        Assertions.assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(attendance);
    }

    @Test
    void toEntity_ShouldReturnNull_WhenAttendanceRequestDTOIsNull() {
        // Execução
        Attendance result = mapper.toEntity(null);

        // Verificação
        Assertions.assertThat(result).isNull();
    }
}
