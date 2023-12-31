package com.bitsva.RepairAgency.feature;

import com.bitsva.RepairAgency.entity.RepairRequest;
import com.bitsva.RepairAgency.entity.User;

public enum RepairRequestPaymentStatus {
    PAID("Paid"),
    AWAITING_PAYMENT("Awaiting payment"),
    CANCELED("Canceled");

    public final String label;

    private RepairRequestPaymentStatus(String label) {
        this.label = label;
    }

    public static void main(String[] args) {
        RepairRequest repairRequest = new RepairRequest();
        repairRequest.setPaymentStatus(AWAITING_PAYMENT);

        System.out.println("repairRequest.getPaymentStatus() = " + repairRequest.getPaymentStatus().label);
    }
}
