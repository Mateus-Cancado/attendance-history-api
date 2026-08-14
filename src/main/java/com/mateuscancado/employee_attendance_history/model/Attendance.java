package com.mateuscancado.employee_attendance_history.model;

import com.mateuscancado.employee_attendance_history.enums.AttendanceStatus;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Attendance {

    private Long id;
    private Long employeeId;
    private LocalDate date;
    private String description;
    private AttendanceStatus status;
}
