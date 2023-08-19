package com.bitsva.RepairAgency.feature;

public enum RepairRequestCompletionStatus {
    NOT_STARTED("Not started"),
    IN_PROGRESS("In progress"),
    COMPLETED ("Completed");

    public final String label;

    private RepairRequestCompletionStatus(String label) {
        this.label = label;
    }
}
