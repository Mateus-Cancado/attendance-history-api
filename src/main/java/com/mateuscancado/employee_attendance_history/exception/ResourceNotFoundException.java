package com.mateuscancado.employee_attendance_history.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) { super(message); }

    public ResourceNotFoundException() {
        super("Atendimento não foi encontrado pelo ID requisitado");
    }
}
