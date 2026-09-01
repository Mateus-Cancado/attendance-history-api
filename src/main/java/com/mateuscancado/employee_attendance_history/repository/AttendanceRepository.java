package com.mateuscancado.employee_attendance_history.repository;

import com.mateuscancado.employee_attendance_history.constants.RepositoryConstants;
import com.mateuscancado.employee_attendance_history.enums.AttendanceStatus;
import com.mateuscancado.employee_attendance_history.model.Attendance;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
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

    public Attendance insert(Attendance attendance) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    RepositoryConstants.QUERY_INSERT_ATTENDANCE,
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setLong(1, attendance.getEmployeeId());
            ps.setDate(2, Date.valueOf(attendance.getDate()));
            ps.setString(3, attendance.getDescription());
            ps.setString(4, attendance.getStatus().getDescription());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            attendance.setId(keyHolder.getKey().longValue());
        } else {
            throw new DataIntegrityViolationException("Falha ao recuperar o ID gerado para o atendimento.");
        }

        return attendance;
    }
}
