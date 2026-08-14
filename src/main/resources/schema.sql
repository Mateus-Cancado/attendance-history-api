CREATE TABLE IF NOT EXISTS tb_attendance_history (
    id BIGSERIAL PRIMARY KEY,
    employee_id INT NOT NULL,
    date DATE NOT NULL,
    description VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL
);