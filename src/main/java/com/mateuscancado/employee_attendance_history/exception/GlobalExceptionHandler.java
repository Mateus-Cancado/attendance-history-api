package com.mateuscancado.employee_attendance_history.exception;

import com.mateuscancado.employee_attendance_history.dto.StandardErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Exceção personalizada de recurso não encontrado.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardErrorDTO> handleResourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardErrorDTO err = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                "Resource Not Found",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    // Exceção personalizada de argumentos inválidos (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardErrorDTO> handleValidationExceptions(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        StandardErrorDTO err = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                "Validation Error",
                errorMessage,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    // Exceção default de recurso não encontrado (NoResourceFoundException)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<StandardErrorDTO> handleNoResourceFound(NoResourceFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardErrorDTO err = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                "Resource not Found",
                "O recurso ou rota solicitada não foi encontrada.",
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    // Exceção default de PK duplicada (DuplicateKeyException)
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<StandardErrorDTO> handleDuplicatedKey(DuplicateKeyException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;

        StandardErrorDTO err = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                "Unique index or primary key violation",
                "O registro informado já está cadastrado no sistema",
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    // Exceção default de erro de integridade de dados (DataIntegrityViolationException)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardErrorDTO> handleDataIntegrityViolation(DataIntegrityViolationException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        StandardErrorDTO err = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                "Data Integrity Violation",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }
}
