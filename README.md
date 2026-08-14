# 📋 Attendance History API - Microserviço de Histórico de Atendimentos

![Java 21](https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1-6DB33F?style=for-the-badge&logo=springboot)
![H2 Database](https://img.shields.io/badge/H2-Database-blue?style=for-the-badge&logo=databricks)

Microserviço RESTful responsável pelo gerenciamento e consulta do histórico de atendimentos e chamados vinculados a funcionários.

---

## 🎯 Sobre o Projeto

Este microserviço foi desenvolvido aplicando conceitos de arquitetura em camadas, utilizando **Spring Boot** e **Spring JDBC Template**. Ele gerencia os registros de atendimento, podendo buscar por Id de funcionário, todo o seu histórico de atendimento.

### 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 25
- **Framework:** Spring Boot
- **Banco de Dados:** H2 Database
- **Mapeamento:** RowMapper & Custom Mappers
- **Produtividade:** Lombok
- **Gerenciador de Dependências:** Maven

---

## 📁 Estrutura do Projeto

```text
com.mateuscancado.employee_attendance_history
├── controller       # Endpoints HTTP da aplicação
├── dto              # Objetos de transferência de dados e tratamento de erro (Records)
├── enums            # Mapeamento de status de atendimento (AttendanceStatus)
├── exception        # Exceções customizadas e GlobalExceptionHandler (@RestControllerAdvice)
├── mapper           # Conversão entre Entidades/Modelos e DTOs
├── model            # Objetos de domínio (POJOs)
├── repository       # Acesso a dados via JdbcTemplate e Mapeamento de linhas (RowMapper)
└── service          # Regras de negócio e orquestração
```

---

## 🛠️ Funcionalidades e Recursos

* **Listar Todos os Atendimentos (`GET /attendances`):**
    * Retorna o histórico completo de atendimentos cadastrados.
* **Busca por ID de Atendimento (`GET /attendances/{id}`):**
    * Retorna os detalhes de um atendimento específico.
    * Lança exceção de negócio padronizada caso o ID não seja localizado (`404 Not Found`).
* **Busca por ID do Funcionário (`GET /attendances/employee/{employeeId}`):**
    * Retorna uma lista com todo o histórico de atendimentos pertencentes a um funcionário específico.
* **Tratamento de Enums:**
    * Mapeamento customizado no enum `AttendanceStatus` para conversão dos valores vindos do banco de dados.
* **Tratamento Global de Exceções (`GlobalExceptionHandler`):**
    * Respostas de erro padronizadas utilizando o `StandardErrorDTO` com payload limpo e timestamps ISO-8601.

---

## 🔧 Como Executar o Projeto

1. Clone o repositório:
```bash
git clone https://github.com/Mateus-Cancado/attendance-history-api.git
```
2. Acesse a pasta do projeto:
```bash
cd attendance-history-api
```
3. Execute a aplicação via Maven:
   ./mvnw spring-boot:run

> 💡 O banco de dados **H2 em memória** será inicializado na porta **8082** e populado automaticamente a partir dos scripts SQL do projeto.

>http://localhost:8082/h2-console
>H2 db name: attendance-db
---

## 🌐 Endpoints da API

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| **`GET`** | `/attendances` | Lista todos os atendimentos |
| **`GET`** | `/attendances/{id}` | Busca um atendimento específico pelo seu ID |
| **`GET`** | `/attendances/employee/{employeeId}` | Lista o histórico de atendimentos de um funcionário |

---

## 👨‍💻 Autor

Desenvolvido por **Mateus Cancado**.

- GitHub: [github.com/Mateus-Cancado](https://github.com/Mateus-Cancado)
- LinkedIn: [linkedin.com/in/mateus-cancado](https://www.linkedin.com/in/mateus-cancado/)