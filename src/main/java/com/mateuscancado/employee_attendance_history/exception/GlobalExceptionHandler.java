package com.mateuscancado.employee_attendance_history.exception;

import com.mateuscancado.employee_attendance_history.dto.StandardErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
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

        String error = "Resource Not Found";
        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardErrorDTO err = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                error,
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

        String error = "Resource not Found";
        String message = "O recurso ou rota solicitada não foi encontrada.";
        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardErrorDTO err = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                error,
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    // Exceção default de PK duplicada (DuplicateKeyException)
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<StandardErrorDTO> handleDuplicateKey(DuplicateKeyException e, HttpServletRequest request) {

        String error = "Unique index or primary key violation";
        String message = "ID duplicado. Já existe um registro com as mesmas credenciais no banco de dados.";
        HttpStatus status = HttpStatus.CONFLICT;

        StandardErrorDTO err = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                error,
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    // Exceção default de erro de integridade de dados (DataIntegrityViolationException)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardErrorDTO> handleDataIntegrityViolation(DataIntegrityViolationException e, HttpServletRequest request) {

        String error = "Data Integrity Violation";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        StandardErrorDTO err = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                error,
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    // Exceção default de request não suportado (HttpRequestMethodNotSupportedException)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<StandardErrorDTO> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {

        String error = "Request Method Not Supported";
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;

        StandardErrorDTO err = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                error,
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }

    // Exceção default de request body inválido (HttpMessageNotReadableException)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardErrorDTO> handleHttpMessageNotReadable(
            HttpMessageNotReadableException e,
            HttpServletRequest request) {

        String error = "Corpo da requisição inválido ou malformatado";
        String message = "Verifique a sintaxe do JSON e os valores de enums/campos informados.";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardErrorDTO err = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                error,
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }
}
