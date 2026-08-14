package com.mateuscancado.employee_attendance_history.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AttendanceStatus {
    OPEN("Open"),
    IN_PROGRESS("In Progress"),
    PENDING_CUSTOMER_RESPONSE("Pending Customer Response"),
    ESCALATED_TO_TIER_2("Escalated to Tier 2"),
    RESOLVED("Resolved"),
    CANCELLED("Cancelled"),
    CLOSED("Closed");

    private final String description;

    AttendanceStatus(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    public static AttendanceStatus fromString(String text) {
        if (text == null) return null;
        for (AttendanceStatus status : AttendanceStatus.values()) {
            if (status.description.equalsIgnoreCase(text) || status.name().equalsIgnoreCase(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status não reconhecido no banco: " + text);
    }
}

