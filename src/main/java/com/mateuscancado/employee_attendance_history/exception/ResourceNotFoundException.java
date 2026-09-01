package com.mateuscancado.employee_attendance_history.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("Atendimento não foi encontrado pelo ID requisitado");
    }

    public ResourceNotFoundException(String message) { super(message); }
}
