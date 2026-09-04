package com.mateuscancado.employee_attendance_history.service;

import com.mateuscancado.employee_attendance_history.dto.AttendanceDTO;
import com.mateuscancado.employee_attendance_history.dto.AttendanceRequestDTO;
import com.mateuscancado.employee_attendance_history.exception.ResourceNotFoundException;
import com.mateuscancado.employee_attendance_history.mapper.AttendanceMapper;
import com.mateuscancado.employee_attendance_history.model.Attendance;
import com.mateuscancado.employee_attendance_history.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceService {

    private final AttendanceRepository repository;
    private final AttendanceMapper mapper;

    // Consulta o atendimento pelo id.
    @Transactional(readOnly = true)
    public AttendanceDTO findById(Long id) {
        log.debug("Fetching attendance by ID: {}", id);

        Attendance attendance = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atendimento não encontrado. Id: " + id));

        log.info("Attendance retrieved successfully. ID: {}", id);
        return mapper.toResponse(attendance);
    }

    // Consulta e gera uma lista com todos os atendimentos realizados por um Id de funcionário.
    @Transactional(readOnly = true)
    public List<AttendanceDTO> findByEmployeeId(Long employeeId) {
        log.debug("Fetching attendances for employee ID: {}", employeeId);

        List<Attendance> list = repository.findByEmployeeId(employeeId);

        log.info("Attendance retrieved successfully. Employee ID: {}, Record found: {}", employeeId, list.size());
        return list.stream()
                .map(mapper :: toResponse)
                .toList();
    }

    // Criar um atendimento e fazer a permanência no Banco de Dados.
    @Transactional
    public AttendanceDTO insert(AttendanceRequestDTO dto) {
        log.debug("Processing new attendance insertion");

        Attendance attendance = mapper.toEntity(dto);
        Attendance insertedAttendance = repository.insert(attendance);

        log.info("Attendance created successfully. Generated ID: {}", insertedAttendance.getId());
        return mapper.toResponse(insertedAttendance);
    }

    @Transactional
    public void update(AttendanceRequestDTO dto, Long id) {
        log.debug("Processing update method");
        log.debug("Checking if attendance ID: {} exists", id);

        boolean exists = repository.existsById(id);
        if (!exists) {
            throw new ResourceNotFoundException("Falha ao atualizar: Atendimento não encontrado. ID: " + id);
        }
        Attendance newAttendanceContent = mapper.toEntity(dto);

        repository.updateById(newAttendanceContent, id);
        log.info("Attendance updated successfully. Attendance ID: {}", id);
    }

    @Transactional
    public void delete(Long id) {
        log.debug("Processing delete method");
        boolean exists = repository.existsById(id);
        if (!exists) {
            throw new ResourceNotFoundException("Falha ao deletar: Atendimento não encontrado. ID: " + id);
        }
        repository.deleteById(id);
        log.info("Attendance deleted successfully. Attendance ID: {}", id);
    }
}
