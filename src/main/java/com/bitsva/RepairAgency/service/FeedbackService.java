package com.bitsva.RepairAgency.service;

import com.bitsva.RepairAgency.entity.Feedback;

public interface FeedbackService {
    public void save(Feedback feedback);

    public Feedback getById(long requestId);
}
