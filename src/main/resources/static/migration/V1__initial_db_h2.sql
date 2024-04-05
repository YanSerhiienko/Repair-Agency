CREATE TABLE users (
    id IDENTITY PRIMARY KEY,
    email VARCHAR(100),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone VARCHAR(100),
    balance BIGINT,
    role VARCHAR(100)
);

CREATE TABLE requests (
    client_id BIGINT,
    id IDENTITY PRIMARY KEY,
    description VARCHAR(100),
    repairer VARCHAR(100),
    cost BIGINT,
    completion_status VARCHAR(100),
    payment_status VARCHAR(100),
    FOREIGN KEY (client_id) REFERENCES users(id)
);

INSERT INTO users (email, first_name, last_name, phone, balance, role)
VALUES ('optimus@prime.com', 'Optimus', 'Prime', '09999999999', 0, 'ROLE_CLIENT'),
('pain@prime.com', 'Max', 'Pain', '09911111111', 0, 'ROLE_CLIENT');

INSERT INTO requests (client_id, description, repairer, cost, completion_status, payment_status)
VALUES (1, 'des', 'none', 0, 'NOT_STARTED', 'AWAITING_PAYMENT'),
(2, 'des2', 'none', 0, 'NOT_STARTED', 'AWAITING_PAYMENT');