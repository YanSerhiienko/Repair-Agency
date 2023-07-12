CREATE TABLE request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    creation_date DATE,
    description VARCHAR(100),
    repairer VARCHAR(100),
    cost BIGINT,
    completion_status VARCHAR(100),
    payment_status VARCHAR(100)
);

CREATE TABLE feedback (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    feedback_date DATE,
    rating SMALLINT,
    description VARCHAR(100),
    request_id BIGINT,
    client_id VARCHAR(100),
    repairer_id VARCHAR(100)
);
