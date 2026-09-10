package com.mateuscancado.employee_attendance_history.controller;

import com.mateuscancado.employee_attendance_history.dto.AttendanceDTO;
import com.mateuscancado.employee_attendance_history.dto.AttendanceRequestDTO;
import com.mateuscancado.employee_attendance_history.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Attendances", description = "Endpoints para gerenciamento do histórico de atendimentos")
public class AttendanceController {

    private final AttendanceService service;

    @Operation(summary = "Busca um atendimento por ID", description = "Retorna os dados detalhados de um atendimento específico cadastrado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atendimento localizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Atendimento não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AttendanceDTO> findById(
            @Parameter(description = "ID do atendimento a ser pesquisado", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Lista atendimentos de um funcionário", description = "Retorna uma lista com todo o histórico de atendimentos vinculados ao ID do funcionário.")
    @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso")
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AttendanceDTO>> findByEmployeeId(
            @Parameter(description = "ID do funcionário a ser pesquisado", example = "1")
            @PathVariable Long employeeId) {
        return ResponseEntity.ok(service.findByEmployeeId(employeeId));
    }

    @Operation(summary = "Cadastra um novo atendimento", description = "Registra um novo atendimento no sistema e retorna o recurso criado com o cabeçalho Location.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Atendimento criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos ou incorretos"),
            @ApiResponse(responseCode = "409", description = "Erro de ID duplicado. Já existe um registro com as mesmas credenciais no banco de dados."),
            @ApiResponse(responseCode = "500", description = "Falha ao recuperar o ID gerado para o atendimento.")
    })
    @PostMapping
    public ResponseEntity<AttendanceDTO> insert(@Valid @RequestBody AttendanceRequestDTO dto) {
        AttendanceDTO attendanceCreated = service.insert(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(attendanceCreated.id())
                .toUri();

        return ResponseEntity.created(uri).body(attendanceCreated);
    }

    @Operation(summary = "Atualiza um atendimento existente", description = "Substitui as informações de um atendimento previamente cadastrado com base no ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Atendimento atualizado com sucesso (Sem corpo de resposta)"),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos"),
            @ApiResponse(responseCode = "404", description = "Atendimento não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @Valid
            @RequestBody AttendanceRequestDTO dto,
            @Parameter(description = "ID do atendimento a ser atualizado", example = "1")
            @PathVariable Long id) {
        service.update(dto, id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove um atendimento", description = "Deleta permanentemente o registro de atendimento correspondente ao ID informado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Atendimento removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Atendimento não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do atendimento a ser deletado", example = "1")
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
