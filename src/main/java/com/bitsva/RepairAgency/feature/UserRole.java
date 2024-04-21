package com.bitsva.RepairAgency.feature;

public enum UserRole {
    ROLE_CLIENT("Client"),
    ROLE_MANAGER("Manager"),
    ROLE_REPAIRER("Repairer"),
    ROLE_ADMIN("Admin");

    public final String label;

    private UserRole(String label) {
        this.label = label;
    }
}
