package com.bitsva.RepairAgency.feature;

import com.bitsva.RepairAgency.entity.RepairRequest;
import com.bitsva.RepairAgency.entity.User;

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
