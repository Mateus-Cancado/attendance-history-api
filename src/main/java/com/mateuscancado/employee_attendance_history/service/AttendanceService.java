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
    public AttendanceDTO findById(Long id) {
        log.debug("Fetching attendance by ID: {}", id);

        Attendance attendance = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atendimento não encontrado. Id: " + id));

        log.info("Attendance retrieved succesfully. ID: {}", id);
        return mapper.toResponse(attendance);
    }

    // Consulta e gera uma lista com todos os atendimentos realizados por um Id de funcionário.
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
}
