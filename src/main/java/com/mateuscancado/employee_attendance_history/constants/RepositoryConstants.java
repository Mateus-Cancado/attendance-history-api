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

    private RepositoryConstants() {
        throw new IllegalStateException("Utility Class");
    }
}
