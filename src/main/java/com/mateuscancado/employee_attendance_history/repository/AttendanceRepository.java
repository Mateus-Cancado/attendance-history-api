package com.mateuscancado.employee_attendance_history.repository;

import com.mateuscancado.employee_attendance_history.constants.RepositoryConstants;
import com.mateuscancado.employee_attendance_history.enums.AttendanceStatus;
import com.mateuscancado.employee_attendance_history.exception.ResourceNotFoundException;
import com.mateuscancado.employee_attendance_history.model.Attendance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        log.debug("Executando consulta para buscar atendimento por ID: {}", id);
        return jdbcTemplate.query(QUERY_FIND_BY_ID, rowMapper, id).stream().findFirst();
    }

    public boolean existsById(Long id) {
        log.debug("Executando consulta para verificar se existe no banco, ID: {}", id);
        Boolean exists = jdbcTemplate.queryForObject(QUERY_EXISTS_BY_ID, Boolean.class, id);
        return Boolean.TRUE.equals(exists);
    }

    public List<Attendance> findByEmployeeId(Long employeeId) {
        log.debug("Executando consulta para buscar atendimentos por ID de funcionário: {}", employeeId);
        return jdbcTemplate.query(QUERY_FIND_BY_EMPLOYEE_ID, rowMapper, employeeId);
    }

    public Attendance insert(Attendance attendance) {
        log.debug("Executando a inserção de um novo atendimento no banco de dados");
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

        log.debug("Atendimento ID: {} inserido com sucesso.", attendance.getId());
        return attendance;
    }

    public void deleteById(Long id) {
        log.debug("Executando deletar atendimento por ID: {}", id);
        int rowsAffected = jdbcTemplate.update(RepositoryConstants.QUERY_DELETE_BY_ID, id);
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("Atendimento não encontrado por ID: " + id);
        }
        log.debug("Atendimento ID: {} deletado com sucesso.", id);
    }

    public void updateById(Attendance attendance, Long id) {
        log.debug("Executando atualizar atendimento por ID: {}", id);
        int rowsAffected = jdbcTemplate.update(
                RepositoryConstants.QUERY_UPDATE_BY_ID,
                attendance.getEmployeeId(),
                Date.valueOf(attendance.getDate()),
                attendance.getDescription(),
                attendance.getStatus().getDescription(),
                id);
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("Atendimento não encontrado por ID: " + id);
        }
        log.debug("Atendimento ID: {} atualizado com sucesso.", id);
    }
}
