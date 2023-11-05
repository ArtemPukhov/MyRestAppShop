DROP TABLE IF EXISTS users_departments CASCADE;
DROP TABLE IF EXISTS phone_numbers CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS departments CASCADE;

CREATE TABLE IF NOT EXISTS roles
(
    role_id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS departments
(
    department_id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    department_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users
(
    user_id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_firstName VARCHAR(255) NOT NULL,
    user_lastName  VARCHAR(255) NOT NULL,
    role_id        BIGINT REFERENCES roles (role_id)
);

CREATE TABLE IF NOT EXISTS users_departments
(
    users_departments_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id              BIGINT REFERENCES users (user_id),
    department_id        BIGINT REFERENCES departments (department_id),
    CONSTRAINT unique_link UNIQUE (user_id, department_id)
);

CREATE TABLE IF NOT EXISTS phone_numbers
(
    phonenumber_id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    phonenumber_number VARCHAR(255) NOT NULL UNIQUE,
    user_id            BIGINT REFERENCES users (user_id)
);

INSERT INTO roles (role_name)
VALUES ('Manager'),        -- 1
       ('Administrator'), -- 2
       ('salesman'),     -- 3
       ('Director'),    -- 4
       ('HR'); -- 5

INSERT INTO departments (department_name)
VALUES ('Office'),       -- 1
       ('Regional office'),  -- 2
       ('Shop'), -- 3
       ('HR management'); -- 4

INSERT INTO users (user_firstName, user_lastName, role_id)
VALUES ('Petr', 'Petrov', 1),      -- 1
       ('Ivan', 'Ivanov', 2), -- 2
       ('Sergey', 'Sergeev', 3),    -- 3
       ('Maria', 'Petrova', 3),       -- 4
       ('Igor', 'Sidorov', 3),   -- 5
       ('Artem', 'Artemov', 4),     -- 6
       ('Olga', 'Orlova', 5); -- 7

INSERT INTO users_departments (user_id, department_id)
VALUES (1, 1), -- 1
       (2, 1), -- 2
       (3, 2), -- 3
       (4, 2), -- 4
       (5, 2), -- 5
       (6, 1), -- 6
       (6, 3), -- 6
       (7, 4); -- 7

INSERT INTO phone_numbers (phonenumber_number, user_id)
VALUES ('+1(234)567 8900', 1), -- 1
       ('+1(234)567 8901', 1), -- 2
       ('+1(234)567 8902', 2), -- 3
       ('+1(234)567 8903', 2), -- 4
       ('+1(234)567 8904', 3), -- 5
       ('+1(234)567 8905', 4), -- 6
       ('+1(234)567 8906', 5), -- 7
       ('+1(234)567 8907', 6), -- 8
       ('+1(234)567 8908', 7); -- 9

