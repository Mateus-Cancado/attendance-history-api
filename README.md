# 📋 Employee Attendance History API

![Java 25](https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=openjdk)
![Spring Boot 3](https://img.shields.io/badge/Spring_Boot-4.1-6DB33F?style=for-the-badge&logo=springboot)
![H2 Database](https://img.shields.io/badge/H2-Database-blue?style=for-the-badge&logo=databricks)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI_3.0-85EA2D?style=for-the-badge&logo=swagger)

Microserviço RESTful responsável pelo gerenciamento completo do histórico de atendimentos vinculados a funcionários. Projeto focado em alta performance de acesso a dados com **Spring JDBC Template**, testes unitários, observabilidade e mecanismos de log (Logging).

---

## 🎯 Sobre o Projeto

Esta aplicação fornece um CRUD completo para o histórico de atendimentos de funcionários. A persistência é realizada através de queries SQL puras via `JdbcTemplate` e recuperação de IDs auto-incrementados com `GeneratedKeyHolder`.

### 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 25
- **Framework:** Spring Boot
- **Acesso a Dados:** Spring JDBC Template & GeneratedKeyHolder
- **Banco de Dados:** H2 Database
- **Testes Unitários:** JUnit 5, Mockito & AssertJ
- **Observabilidade & Documentação:** SLF4J (Logging) & Swagger
- **Produtividade:** Lombok & Maven

---

## 📁 Estrutura do Projeto

```text
com.mateuscancado.employee_attendance_history
├── controller       # Endpoints HTTP REST da aplicação
├── dto              # Records para transporte de dados (Requests, Responses e Erros)
├── enums            # Mapeamento e conversão de status (AttendanceStatus)
├── exception        # Exceções customizadas e GlobalExceptionHandler (@RestControllerAdvice)
├── mapper           # Conversão entre Entidades/Modelos e DTOs
├── model            # Objetos de domínio (Entities)
├── repository       # Operações SQL via JdbcTemplate e RowMapper
└── service          # Regras de negócio, logs de rastreabilidade e orquestração
```
---

## 🛠️ Funcionalidades e Recursos

* **CRUD Completo de Atendimentos:**
  * **Criação (`POST /attendances`):** Registra um novo atendimento utilizando `GeneratedKeyHolder` para retornar a chave primária gerada no banco.
  * **Atualização (`PUT /attendances/{id}`):** Atualiza os dados de um atendimento existente.
  * **Exclusão (`DELETE /attendances/{id}`):** Remove um registro pelo seu ID.
  * **Busca por ID (`GET /attendances/{id}`):** Consulta os detalhes de um atendimento específico.
  * **Histórico por Funcionário (`GET /attendances/employee/{employeeId}`):** Lista todo o histórico de atendimentos vinculado a um funcionário.
* **Validação e Erros Padronizados:**
  * Respostas estruturadas em formato JSON com timestamps no padrão ISO-8601 e tratamento global via `@RestControllerAdvice`.
* **Qualidade de Código e Testes:**
  * Cobertura de testes unitários automatizados nas camadas de negócio utilizando **JUnit 5**, **Mockito** e **AssertJ**.
* **Observabilidade e Documentação:**
  * Logs de execução e rastreabilidade estruturados com **SLF4J**.
  * Interface gráfica e documentação de endpoints interativa via **Swagger**.

---

## 🌐 Endpoints da API

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **`POST`** | `/attendances` | Cria um novo registro de atendimento |
| **`GET`** | `/attendances/{id}` | Busca os detalhes de um atendimento específico pelo seu ID |
| **`GET`** | `/attendances/employee/{employeeId}` | Lista o histórico de atendimentos de um funcionário |
| **`PUT`** | `/attendances/{id}` | Atualiza os dados de um atendimento existente |
| **`DELETE`** | `/attendances/{id}` | Remove um atendimento pelo ID |

---

## 🔧 Como Executar o Projeto

1. Clone o repositório:
   git clone https://github.com/Mateus-Cancado/attendance-history-api.git

2. Acesse a pasta do projeto:
   cd attendance-history-api

3. Execute a aplicação via Maven Wrapper:
   ./mvnw spring-boot:run

4. Para rodar a suíte completa de testes unitários:
   ./mvnw test

> 💡 **Massa de Dados para Testes:** Ao iniciar a aplicação, o banco de dados H2 é populado automaticamente via script SQL (`schema.sql` / `data.sql`) com dados de teste pré-cadastrados, permitindo testar as consultas e endpoints imediatamente.

---

## 📖 Documentação & H2 Console

Após iniciar a aplicação:
* **Documentação Swagger UI:** `http://localhost:8080/swagger-ui.html`
* **H2 Console:** `http://localhost:8080/h2-console`
  * **JDBC URL:** `jdbc:h2:mem:attendance-db`

---

## 👨‍💻 Autor

Desenvolvido por **Mateus Cancado**.

- GitHub: https://github.com/Mateus-Cancado
- LinkedIn: https://www.linkedin.com/in/mateus-cancado/