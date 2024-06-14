package com.bitsva.RepairAgency.feature;

public enum UserRole {
    ROLE_CLIENT("Client"),
    ROLE_MANAGER("Manager"),
    ROLE_REPAIRER("Repairer"),
    ROLE_ADMIN("Admin"),
    ROLE_SUPER_ADMIN("Super Admin");

    public final String label;

    private UserRole(String label) {
        this.label = label;
    }
}
