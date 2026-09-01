package com.mateuscancado.employee_attendance_history.mapper;

import com.mateuscancado.employee_attendance_history.dto.AttendanceDTO;
import com.mateuscancado.employee_attendance_history.dto.AttendanceRequestDTO;
import com.mateuscancado.employee_attendance_history.model.Attendance;
import org.springframework.stereotype.Component;

@Component
public class AttendanceMapper {

    // Transforma uma entidade de domínio em um DTO de Response.
    public AttendanceDTO toResponse(Attendance attendance) {
        if (attendance == null) return null;

        return new AttendanceDTO(
                attendance.getId(),
                attendance.getEmployeeId(),
                attendance.getDate(),
                attendance.getDescription(),
                attendance.getStatus()
        );
    }

    // Transforma um DTO de Request em entidade de domínio.
    public Attendance toEntity(AttendanceRequestDTO dto) {
        if (dto == null) return null;

        return new Attendance(
                null,
                dto.employeeId(),
                dto.date(),
                dto.description(),
                dto.status()
        );
    }
}
