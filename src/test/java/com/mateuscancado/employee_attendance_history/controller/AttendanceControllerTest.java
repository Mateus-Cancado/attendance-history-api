package com.mateuscancado.employee_attendance_history.controller;

import com.mateuscancado.employee_attendance_history.dto.AttendanceDTO;
import com.mateuscancado.employee_attendance_history.dto.AttendanceRequestDTO;
import com.mateuscancado.employee_attendance_history.enums.AttendanceStatus;
import com.mateuscancado.employee_attendance_history.exception.ResourceNotFoundException;
import com.mateuscancado.employee_attendance_history.service.AttendanceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AttendanceController.class)
public class AttendanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AttendanceService service;

    @Test
    void findById_ShouldReturnStatus200AndDTO_WhenIdExists() throws Exception {
        // Cenário
        Long id = 1L;
        AttendanceDTO dto = new AttendanceDTO(
                id,
                100L,
                LocalDate.now(),
                "Atendimento",
                AttendanceStatus.OPEN
        );

        Mockito.when(service.findById(id)).thenReturn(dto);

        // Execução
        mockMvc.perform(get("/attendances/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.employeeId").value(100L))
                .andExpect(jsonPath("$.description").value("Atendimento"))
                .andExpect(jsonPath("$.status").value(AttendanceStatus.OPEN.getDescription()));

        // Verificação
        Mockito.verify(service, Mockito.times(1)).findById(id);
    }

    @Test
    void findById_ShouldReturnStatus404_WhenIdDoesNotExists() throws Exception {
        // Cenário
        Long id = 1L;
        Mockito.when(service.findById(id))
                .thenThrow(new ResourceNotFoundException("Atendimento não encontrado. Id: " + id));

        // Execução
        mockMvc.perform(get("/attendances/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // Verificação
        Mockito.verify(service, Mockito.times(1)).findById(id);
    }

    @Test
    void findByEmployeeId_ShouldReturnStatus200AndListOfAttendanceDTOs_WhenIdExists() throws Exception {
        // Cenário
        Long employeeId = 100L;
        AttendanceDTO dto1 = new AttendanceDTO(
                1L,
                employeeId,
                LocalDate.now(),
                "Atendimento 1",
                AttendanceStatus.OPEN
        );
        AttendanceDTO dto2 = new AttendanceDTO(
                2L,
                employeeId,
                LocalDate.now(),
                "Atendimento 2",
                AttendanceStatus.OPEN
        );

        Mockito.when(service.findByEmployeeId(employeeId)).thenReturn(List.of(dto1, dto2));

        // Execução
        mockMvc.perform(get("/attendances/employee/{employeeId}", employeeId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].employeeId").value(employeeId))
                .andExpect(jsonPath("$[0].description").value("Atendimento 1"))
                .andExpect(jsonPath("$[0].status").value(AttendanceStatus.OPEN.getDescription()))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].employeeId").value(employeeId))
                .andExpect(jsonPath("$[1].description").value("Atendimento 2"))
                .andExpect(jsonPath("$[1].status").value(AttendanceStatus.OPEN.getDescription()));

        // Verificação
        Mockito.verify(service, Mockito.times(1)).findByEmployeeId(employeeId);
    }

    @Test
    void findByEmployeeId_ShouldReturnStatus200AndEmptyList_WhenNoRecordsFound() throws Exception {
        // Cenário
        Long employeeId = 100L;
        Mockito.when(service.findByEmployeeId(employeeId)).thenReturn(List.of());

        // Execução
        mockMvc.perform(get("/attendances/employee/{employeeId}", employeeId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        // Verificação
        Mockito.verify(service, Mockito.times(1)).findByEmployeeId(employeeId);
    }

    @Test
    void insert_ShouldReturnStatus201AndAttendanceDTO_WhenSuccess() throws Exception {
        // Cenário
        AttendanceRequestDTO requestDTO = new AttendanceRequestDTO(
                100L,
                LocalDate.now(),
                "Atendimento request",
                AttendanceStatus.OPEN
        );
        AttendanceDTO responseDTO = new AttendanceDTO(
                1L,
                requestDTO.employeeId(),
                requestDTO.date(),
                requestDTO.description(),
                requestDTO.status()
        );

        Mockito.when(service.insert(requestDTO)).thenReturn(responseDTO);

        // Execução
        mockMvc.perform(post("/attendances")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.employeeId").value(100L))
                .andExpect(jsonPath("$.description").value("Atendimento request"))
                .andExpect(jsonPath("$.status").value(requestDTO.status().getDescription()));

        // Verificação
        Mockito.verify(service, Mockito.times(1)).insert(requestDTO);
    }

    @Test
    void insert_ShouldReturnStatus400_WhenInvalidDTO() throws Exception {
        // Cenário
        AttendanceRequestDTO invalidRequestDTO = new AttendanceRequestDTO(
                null,
                LocalDate.now(),
                "A".repeat(61),
                AttendanceStatus.OPEN
        );

        // Execução
        mockMvc.perform(post("/attendances")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequestDTO))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Verificação
        Mockito.verifyNoInteractions(service);
    }

    @Test
    void update_ShouldReturnStatus204_WhenIdExists() throws Exception {
        // Cenário
        Long id = 1L;
        AttendanceRequestDTO requestDTO = new AttendanceRequestDTO(
                100L,
                LocalDate.now(),
                "Atendimento atualizado",
                AttendanceStatus.ESCALATED_TO_TIER_2
        );

        Mockito.doNothing().when(service).update(requestDTO, id);

        // Execução
        mockMvc.perform(put("/attendances/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNoContent());

        // Verificação
        Mockito.verify(service, Mockito.times(1)).update(requestDTO, id);
    }

    @Test
    void update_ShouldReturnStatus404_WhenIdDoesNotExists() throws Exception {
        // Cenário
        Long id = 1L;
        AttendanceRequestDTO requestDTO = new AttendanceRequestDTO(
                100L,
                LocalDate.now(),
                "Atendimento inexistente",
                AttendanceStatus.CANCELLED
        );

        Mockito.doThrow(new ResourceNotFoundException("Falha ao atualizar: Atendimento não encontrado. ID: " + id))
                .when(service).update(requestDTO, id);

        // Execução
        mockMvc.perform(put("/attendances/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());

        // Verificação
        Mockito.verify(service, Mockito.times(1)).update(requestDTO, id);
    }

    @Test
    void update_ShouldReturnStatus400_WhenInvalidDTO() throws Exception {
        // Cenário
        Long id = 1L;
        AttendanceRequestDTO invalidRequestDTO = new AttendanceRequestDTO(
                null,
                LocalDate.now(),
                "Request inválido",
                AttendanceStatus.CANCELLED
        );

        // Execução
        mockMvc.perform(put("/attendances/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequestDTO)))
                .andExpect(status().isBadRequest());

        // Verificação
        Mockito.verifyNoInteractions(service);
    }
}
