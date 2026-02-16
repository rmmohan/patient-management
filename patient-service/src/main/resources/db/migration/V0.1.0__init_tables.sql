CREATE TABLE IF NOT EXISTS patient
(
    id              UUID PRIMARY KEY,
    name            VARCHAR(100)        NOT NULL,
    email           VARCHAR(100) UNIQUE NOT NULL,
    address         VARCHAR(500)        NOT NULL,
    date_of_birth   DATE                NOT NULL,
    registered_date DATE                NOT NULL
);