package com.mateuscancado.employee_attendance_history.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Employee Attendance History API")
                        .version("1.0")
                        .description("API RESTful para gerenciamento do histórico de atendimentos de funcionários.")
                        .contact(new Contact()
                                .name("Mateus Lima Cançado")
                                .email("mateuscancado.dev@gmail.com")));
    }
}
