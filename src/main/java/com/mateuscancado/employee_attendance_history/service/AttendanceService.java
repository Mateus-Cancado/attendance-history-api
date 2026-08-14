package com.mateuscancado.employee_attendance_history.service;

import com.mateuscancado.employee_attendance_history.dto.AttendanceDTO;
import com.mateuscancado.employee_attendance_history.exception.ResourceNotFoundException;
import com.mateuscancado.employee_attendance_history.mapper.AttendanceMapper;
import com.mateuscancado.employee_attendance_history.model.Attendance;
import com.mateuscancado.employee_attendance_history.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository repository;
    private final AttendanceMapper mapper;

    public List<AttendanceDTO> findAll() {
        List<Attendance> list = repository.findAll();

        return list.stream()
                .map(mapper :: toResponse)
                .toList();
    }

    public AttendanceDTO findById(Long id) {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atendimento não encontrado. Id: " + id)));
    }

    public List<AttendanceDTO> findByEmployeeId(Long employeeId) {
        List<Attendance> list = repository.findByEmployeeId(employeeId);

        return list.stream()
                .map(mapper :: toResponse)
                .toList();
    }
}
