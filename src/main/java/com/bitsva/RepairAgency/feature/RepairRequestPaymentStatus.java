package com.bitsva.RepairAgency.feature;

public enum RepairRequestPaymentStatus {
    PAID("Paid"),
    AWAITING_PAYMENT("Awaiting payment"),
    CANCELED("Canceled");

    public final String label;

    private RepairRequestPaymentStatus(String label) {
        this.label = label;
    }
}
