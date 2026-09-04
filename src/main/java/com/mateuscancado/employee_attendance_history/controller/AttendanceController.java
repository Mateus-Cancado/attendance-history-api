package com.mateuscancado.employee_attendance_history.controller;

import com.mateuscancado.employee_attendance_history.dto.AttendanceDTO;
import com.mateuscancado.employee_attendance_history.dto.AttendanceRequestDTO;
import com.mateuscancado.employee_attendance_history.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/attendances")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService service;

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AttendanceDTO>> findByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(service.findByEmployeeId(employeeId));
    }

    @PostMapping
    public ResponseEntity<AttendanceDTO> insert(@Valid @RequestBody AttendanceRequestDTO dto) {
        AttendanceDTO attendanceCreated = service.insert(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(attendanceCreated.id())
                .toUri();

        return ResponseEntity.created(uri).body(attendanceCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody AttendanceRequestDTO dto, @PathVariable Long id) {
        service.update(dto, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
