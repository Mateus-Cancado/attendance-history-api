package com.mateuscancado.employee_attendance_history.mapper;

import com.mateuscancado.employee_attendance_history.dto.AttendanceDTO;
import com.mateuscancado.employee_attendance_history.model.Attendance;
import org.springframework.stereotype.Component;

@Component
public class AttendanceMapper {

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
}
