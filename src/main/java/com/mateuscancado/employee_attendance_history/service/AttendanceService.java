package com.mateuscancado.employee_attendance_history.service;

import com.mateuscancado.employee_attendance_history.dto.AttendanceDTO;
import com.mateuscancado.employee_attendance_history.exception.ResourceNotFoundException;
import com.mateuscancado.employee_attendance_history.mapper.AttendanceMapper;
import com.mateuscancado.employee_attendance_history.model.Attendance;
import com.mateuscancado.employee_attendance_history.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceService {

    private final AttendanceRepository repository;
    private final AttendanceMapper mapper;

    // Consulta o atendimento pelo id.
    public AttendanceDTO findById(Long id) {
        log.info("Start-findById");
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atendimento não encontrado. Id: " + id)));
    }

    // Consulta e gera uma lista com todos os atendimentos realizados por um Id de funcionário.
    public List<AttendanceDTO> findByEmployeeId(Long employeeId) {
        List<Attendance> list = repository.findByEmployeeId(employeeId);

        return list.stream()
                .map(mapper :: toResponse)
                .toList();
    }
}
