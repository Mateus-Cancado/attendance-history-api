package com.mateuscancado.employee_attendance_history.service;

import com.mateuscancado.employee_attendance_history.dto.AttendanceDTO;
import com.mateuscancado.employee_attendance_history.dto.AttendanceRequestDTO;
import com.mateuscancado.employee_attendance_history.enums.AttendanceStatus;
import com.mateuscancado.employee_attendance_history.exception.ResourceNotFoundException;
import com.mateuscancado.employee_attendance_history.mapper.AttendanceMapper;
import com.mateuscancado.employee_attendance_history.model.Attendance;
import com.mateuscancado.employee_attendance_history.repository.AttendanceRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AttendanceServiceTest {

    @Mock
    private AttendanceRepository repository;

    @Mock
    private AttendanceMapper mapper;

    @InjectMocks
    private AttendanceService service;

    @Test
    void findById_ShouldReturnAttendanceDTO_WhenIdExists() {
        // Cenário
        Long id = 1L;
        Attendance attendance = new Attendance(id, 100L, LocalDate.now(), "Consulta", AttendanceStatus.OPEN);
        AttendanceDTO expectedDTO = new AttendanceDTO(id, 100L, attendance.getDate(), "Consulta", AttendanceStatus.OPEN);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(attendance));
        Mockito.when(mapper.toResponse(attendance)).thenReturn(expectedDTO);

        // Execução
        AttendanceDTO result = service.findById(id);

        // Verificação
        Assertions.assertThat(result).isNotNull().isEqualTo(expectedDTO);
        Mockito.verify(repository, Mockito.times(1)).findById(id);
        Mockito.verify(mapper, Mockito.times(1)).toResponse(attendance);
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException_WhenIdDoesNotExists() {
        // Cenário
        Long id = 1L;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        // Execução
        Throwable error = Assertions.catchThrowable(() -> service.findById(id));

        // Verificação
        Assertions.assertThat(error)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Atendimento não encontrado. Id: " + id);
        Mockito.verify(repository, Mockito.times(1)).findById(id);
        Mockito.verifyNoInteractions(mapper);
    }

    @Test
    void findByEmployeeId_ShouldReturnListOfAttendanceDTOs_WhenEmployeeExists() {
        // Cenário
        Long employeeId = 100L;
        Attendance att1 = new Attendance(1L, employeeId, LocalDate.now(), "Atendimento 1", AttendanceStatus.OPEN);
        Attendance att2 = new Attendance(2L, employeeId, LocalDate.now(), "Atendimento 2", AttendanceStatus.OPEN);
        AttendanceDTO dto1 = new AttendanceDTO(1L, employeeId, att1.getDate(), "Atendimento 1", AttendanceStatus.OPEN);
        AttendanceDTO dto2 = new AttendanceDTO(2L, employeeId, att2.getDate(), "Atendimento 2", AttendanceStatus.OPEN);

        Mockito.when(repository.findByEmployeeId(employeeId)).thenReturn(List.of(att1, att2));
        Mockito.when(mapper.toResponse(att1)).thenReturn(dto1);
        Mockito.when(mapper.toResponse(att2)).thenReturn(dto2);

        // Execução
        List<AttendanceDTO> result = service.findByEmployeeId(employeeId);

        // Verificação
        Assertions.assertThat(result).hasSize(2).containsExactly(dto1, dto2);
        Mockito.verify(repository, Mockito.times(1)).findByEmployeeId(employeeId);
        Mockito.verify(mapper, Mockito.times(1)).toResponse(att1);
        Mockito.verify(mapper, Mockito.times(1)).toResponse(att2);
    }

    @Test
    void findByEmployeeId_ShouldReturnEmptyList_WhenNoRecordsFound() {
        // Cenário
        Long employeeId = 100L;
        Mockito.when(repository.findByEmployeeId(employeeId)).thenReturn(List.of());

        // Execução
        List<AttendanceDTO> result = service.findByEmployeeId(employeeId);

        // Verificação
        Assertions.assertThat(result).isEmpty();
        Mockito.verify(repository, Mockito.times(1)).findByEmployeeId(employeeId);
        Mockito.verifyNoInteractions(mapper);
    }

    @Test
    void insert_ShouldReturnAttendanceDTO_WhenSuccess() {
        // Cenário
        AttendanceRequestDTO requestDTO = new AttendanceRequestDTO(
                100L,
                LocalDate.now(),
                "Atendimento",
                AttendanceStatus.PENDING_CUSTOMER_RESPONSE);

        Attendance entity = new Attendance(
                null,
                100L,
                requestDTO.date(),
                "Atendimento",
                AttendanceStatus.PENDING_CUSTOMER_RESPONSE);

        Attendance insertedEntity = new Attendance(
                1L,
                100L,
                requestDTO.date(),
                "Atendimento",
                AttendanceStatus.PENDING_CUSTOMER_RESPONSE);

        AttendanceDTO responseDTO = new AttendanceDTO(
                insertedEntity.getId(),
                insertedEntity.getEmployeeId(),
                insertedEntity.getDate(),
                "Atendimento",
                AttendanceStatus.PENDING_CUSTOMER_RESPONSE);

        Mockito.when(mapper.toEntity(requestDTO)).thenReturn(entity);
        Mockito.when(repository.insert(entity)).thenReturn(insertedEntity);
        Mockito.when(mapper.toResponse(insertedEntity)).thenReturn(responseDTO);

        // Execução
        AttendanceDTO result = service.insert(requestDTO);

        // Verificação
        Assertions.assertThat(result).isNotNull().isEqualTo(responseDTO);
        Mockito.verify(mapper, Mockito.times(1)).toEntity(requestDTO);
        Mockito.verify(repository, Mockito.times(1)).insert(entity);
        Mockito.verify(mapper, Mockito.times(1)).toResponse(insertedEntity);
    }
}
