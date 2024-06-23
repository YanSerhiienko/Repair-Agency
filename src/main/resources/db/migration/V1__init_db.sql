CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone VARCHAR(255),
    balance BIGINT,
    rating FLOAT(23),
    role ENUM ('ROLE_ADMIN','ROLE_CLIENT','ROLE_MANAGER','ROLE_REPAIRER','ROLE_SUPER_ADMIN'),
    password VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT uc_users UNIQUE (email, phone)
);

CREATE TABLE requests (
    id BIGINT NOT NULL AUTO_INCREMENT,
    client_id BIGINT,
    repairer_id BIGINT,
    creation_date VARCHAR(255),
    description VARCHAR(10000),
    cost BIGINT,
    deposited_to_pay BIGINT,
    completion_status ENUM ('COMPLETED','IN_PROGRESS','NOT_STARTED'),
    payment_status ENUM ('AWAITING_PAYMENT','CANCELED','PAID'),
    is_has_feedback BIT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (client_id) REFERENCES users (id),
    FOREIGN KEY (repairer_id) REFERENCES users (id)
);

CREATE TABLE feedbacks (
    id BIGINT NOT NULL AUTO_INCREMENT,
    feedback_date VARCHAR(255),
    rating BIGINT,
    feedback_text VARCHAR(10000),
    request_id BIGINT,
    client_id BIGINT,
    repairer_id BIGINT,
    PRIMARY KEY (id)
);
