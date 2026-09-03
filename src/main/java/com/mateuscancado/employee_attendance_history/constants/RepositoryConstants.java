package com.mateuscancado.employee_attendance_history.constants;

public class RepositoryConstants {

    public static final String QUERY_FIND_BY_ID =
            """
            SELECT * FROM tb_attendance_history WHERE id = ?
            """;

    public static final String QUERY_FIND_BY_EMPLOYEE_ID =
            """
            SELECT * FROM tb_attendance_history WHERE employee_id = ?
            """;

    public static final String QUERY_INSERT_ATTENDANCE =
            """
            INSERT INTO tb_attendance_history (employee_id, date, description, status)
            VALUES (?, ?, ?, ?)
            """;

    public static final String QUERY_DELETE_BY_ID =
            """
            DELETE FROM tb_attendance_history WHERE id = ?
            """;

    public static final String QUERY_UPDATE_BY_ID =
            """
            UPDATE tb_attendance_history
            SET employee_id = ?,
                date = ?,
                description = ?,
                status = ?
            WHERE id = ?
            """;

    private RepositoryConstants() {
        throw new IllegalStateException("Utility Class");
    }
}
