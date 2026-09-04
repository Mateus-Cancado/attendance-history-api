package com.mateuscancado.employee_attendance_history.repository;

import com.mateuscancado.employee_attendance_history.constants.RepositoryConstants;
import com.mateuscancado.employee_attendance_history.enums.AttendanceStatus;
import com.mateuscancado.employee_attendance_history.exception.ResourceNotFoundException;
import com.mateuscancado.employee_attendance_history.model.Attendance;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AttendanceRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private AttendanceRepository repository;

    @Test
    void findById_ShouldReturnAttendance_WhenIdExists() {
        // Cenário
        Long id = 1L;
        Attendance attendance = new Attendance(id, 1L, LocalDate.now(), "Atendimento", AttendanceStatus.OPEN);

        Mockito.when(jdbcTemplate.query(
                Mockito.eq(RepositoryConstants.QUERY_FIND_BY_ID),
                Mockito.any(RowMapper.class),
                Mockito.eq(id)
        )).thenReturn(List.of(attendance));

        // Execução
        Optional<Attendance> result = repository.findById(id);

        // Verificação
        Assertions.assertThat(result).isPresent().contains(attendance);
        Mockito.verify(jdbcTemplate, Mockito.times(1))
                .query(Mockito.eq(RepositoryConstants.QUERY_FIND_BY_ID), Mockito.any(RowMapper.class), Mockito.eq(id));
    }

    @Test
    void findById_ShouldReturnEmptyOptional_WhenIdDoesNotExist() {
        // Cenário
        Long id = 1L;

        Mockito.when(jdbcTemplate.query(
                Mockito.eq(RepositoryConstants.QUERY_FIND_BY_ID),
                Mockito.any(RowMapper.class),
                Mockito.eq(id)
        )).thenReturn(List.of());

        // Execução
        Optional<Attendance> result = repository.findById(id);

        // Verificação
        Assertions.assertThat(result).isEmpty();
        Mockito.verify(jdbcTemplate, Mockito.times(1))
                .query(Mockito.eq(RepositoryConstants.QUERY_FIND_BY_ID), Mockito.any(RowMapper.class), Mockito.eq(id));
    }

    @Test
    void findByEmployeeId_ShouldReturnList_WhenEmployeeExists() {
        // Cenário
        Long employeeId = 1L;
        Attendance att1 = new Attendance(1L, employeeId, LocalDate.now(), "Atendimento 1", AttendanceStatus.OPEN);
        Attendance att2 = new Attendance(2L, employeeId, LocalDate.now(), "Atendimento 2", AttendanceStatus.IN_PROGRESS);

        Mockito.when(jdbcTemplate.query(
                Mockito.eq(RepositoryConstants.QUERY_FIND_BY_EMPLOYEE_ID),
                Mockito.any(RowMapper.class),
                Mockito.eq(employeeId)
        )).thenReturn(List.of(att1, att2));

        // Execução
        List<Attendance> result = repository.findByEmployeeId(employeeId);

        // Verificação
        Assertions.assertThat(result).hasSize(2).containsExactly(att1, att2);
        Mockito.verify(jdbcTemplate, Mockito.times(1))
                .query(Mockito.eq(RepositoryConstants.QUERY_FIND_BY_EMPLOYEE_ID), Mockito.any(RowMapper.class), Mockito.eq(employeeId));
    }

    @Test
    void insert_ShouldReturnAttendanceWithGeneratedId_WhenSuccess() {
        // Cenário
        Attendance attendance = new Attendance();
        attendance.setEmployeeId(1L);
        attendance.setDate(LocalDate.now());
        attendance.setDescription("Atendimento duplicado");
        attendance.setStatus(AttendanceStatus.OPEN);

        Mockito.doAnswer(invocation -> {
            KeyHolder keyHolder = invocation.getArgument(1);
            keyHolder.getKeyList().add(java.util.Map.of("GENERATED_KEY", 10L));
            return 1;
        }).when(jdbcTemplate).update(
                Mockito.any(PreparedStatementCreator.class),
                Mockito.any(KeyHolder.class)
        );

        // Execução
        Attendance attendanceInserted = repository.insert(attendance);

        // Verificação
        Assertions.assertThat(attendanceInserted.getId()).isEqualTo(10L);
        Assertions.assertThat(attendanceInserted.getEmployeeId()).isEqualTo(1L);

        Mockito.verify(jdbcTemplate, Mockito.times(1))
                .update(Mockito.any(PreparedStatementCreator.class), Mockito.any(KeyHolder.class));
    }

    @Test
    void insert_ShouldThrowDuplicateKeyException_WhenKeyAlreadyExists() {
        // Cenário
        Attendance attendance = new Attendance();
        attendance.setEmployeeId(1L);
        attendance.setDate(LocalDate.now());
        attendance.setDescription("Atendimento duplicado");
        attendance.setStatus(AttendanceStatus.OPEN);

        Mockito.doThrow(new DuplicateKeyException("Chave duplicada no banco"))
                .when(jdbcTemplate)
                .update(Mockito.any(PreparedStatementCreator.class), Mockito.any(KeyHolder.class));

        // Execução
        Throwable error = Assertions.catchThrowable(() -> repository.insert(attendance));

        // Verificação
        Assertions.assertThat(error)
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessage("Chave duplicada no banco");
    }

    @Test
    void insert_ShouldThrowDataIntegrityViolationException_WhenKeyHolderReturnsNull() {
        // Cenário
        Attendance attendance = new Attendance();
        attendance.setEmployeeId(1L);
        attendance.setDate(LocalDate.now());
        attendance.setDescription("Teste de exceção");
        attendance.setStatus(AttendanceStatus.OPEN);

        Mockito.doAnswer(invocation -> null)
                .when(jdbcTemplate)
                .update(Mockito.any(PreparedStatementCreator.class), Mockito.any(KeyHolder.class));

        // Execução
        Throwable error = Assertions.catchThrowable(() -> repository.insert(attendance));

        // Verificação
        Assertions.assertThat(error)
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessage("Falha ao recuperar o ID gerado para o atendimento.");
    }

    @Test
    void delete_ShouldExecuteUpdate_WhenIdExists() {
        // Cenário
        Long id = 1L;
        Mockito.when(jdbcTemplate
                .update(Mockito.anyString(), Mockito.eq(id)))
                .thenReturn(1);

        // Execução
        repository.deleteById(id);

        // Verificação
        Mockito.verify(jdbcTemplate, Mockito.times(1))
                .update(RepositoryConstants.QUERY_DELETE_BY_ID, id);
    }

    @Test
    void delete_ShouldThrowResourceNotFoundException_WhenIdDoesNotExists() {
        // Cenário
        Long id = 1L;
        Mockito.when(jdbcTemplate
                .update(Mockito.anyString(), Mockito.eq(id)))
                .thenReturn(0);

        // Execução
        Throwable error = Assertions.catchThrowable(() -> repository.deleteById(id));

        // Verificação
        Assertions.assertThat(error)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Atendimento não encontrado por ID: " + id);
    }

    @Test
    void update_ShouldExecuteUpdate_WhenIdExists() {
        // Cenário
        Long id = 1L;
        Attendance attendance = new Attendance();
        attendance.setEmployeeId(1L);
        attendance.setDate(LocalDate.now());
        attendance.setDescription("Teste de atualização");
        attendance.setStatus(AttendanceStatus.CANCELLED);

        Mockito.when(jdbcTemplate.update(
                Mockito.anyString(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any()
        )).thenReturn(1);

        // Execução
         repository.updateById(attendance, id);

        // Verificação
        Mockito.verify(jdbcTemplate, Mockito.times(1))
                .update(
                        Mockito.eq(RepositoryConstants.QUERY_UPDATE_BY_ID),
                        Mockito.eq(attendance.getEmployeeId()),
                        Mockito.eq(Date.valueOf(attendance.getDate())),
                        Mockito.eq(attendance.getDescription()),
                        Mockito.eq(attendance.getStatus().getDescription()),
                        Mockito.eq(id)
                );
    }

    @Test
    void update_ShouldThrowResourceNotFoundException_WhenIdDoesNotExists() {
        // Cenário
        Long id = 1L;
        Attendance attendance = new Attendance();
        attendance.setEmployeeId(1L);
        attendance.setDate(LocalDate.now());
        attendance.setDescription("Teste de atualização");
        attendance.setStatus(AttendanceStatus.CANCELLED);

        Mockito.when(jdbcTemplate.update(
                Mockito.anyString(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any(),
                Mockito.any()
        )).thenReturn(0);

        // Execução
        Throwable error = Assertions.catchThrowable(() -> repository.updateById(attendance, id));

        // Verificação
        Assertions.assertThat(error)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Atendimento não encontrado por ID: " + id);
    }
}
