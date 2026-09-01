package com.mateuscancado.employee_attendance_history.repository;

import com.mateuscancado.employee_attendance_history.enums.AttendanceStatus;
import com.mateuscancado.employee_attendance_history.model.Attendance;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.mateuscancado.employee_attendance_history.constants.RepositoryConstants.*;

@Repository
@RequiredArgsConstructor
public class AttendanceRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Attendance> rowMapper = ((rs, rowNum) -> new Attendance(
            rs.getLong("id"),
            rs.getLong("employee_id"),
            rs.getDate("date").toLocalDate(),
            rs.getString("description"),
            AttendanceStatus.fromString(rs.getString("status"))
    ));

    public Optional<Attendance> findById(Long id) {
        return jdbcTemplate.query(QUERY_FIND_BY_ID, rowMapper, id).stream().findFirst();
    }

    public List<Attendance> findByEmployeeId(Long employeeId) {
        return jdbcTemplate.query(QUERY_FIND_BY_EMPLOYEE_ID, rowMapper, employeeId);
    }
}
