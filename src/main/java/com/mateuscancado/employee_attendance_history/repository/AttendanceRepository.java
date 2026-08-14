package com.mateuscancado.employee_attendance_history.repository;

import com.mateuscancado.employee_attendance_history.enums.AttendanceStatus;
import com.mateuscancado.employee_attendance_history.model.Attendance;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    public List<Attendance> findAll() {
        String sql = "SELECT * " +
                "FROM tb_attendance_history";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Attendance> findById(Long id) {
        String sql = "SELECT * " +
                "FROM tb_attendance_history " +
                "WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst();
    }

    public List<Attendance> findByEmployeeId(Long employeeId) {
        String sql = "SELECT * " +
                "FROM tb_attendance_history " +
                "WHERE employee_id = ?";
        return jdbcTemplate.query(sql, rowMapper, employeeId);
    }
}
