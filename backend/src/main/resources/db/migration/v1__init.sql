CREATE TABLE service_category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);
CREATE TABLE service (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL REFERENCES service_category(id),
    name VARCHAR(100) NOT NULL,
    duration_minutes INT NOT NULL CHECK (duration_minutes > 0),
    UNIQUE (category_id, name)
);
CREATE TABLE client (
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(30)  NOT NULL
);
CREATE TABLE day_schedule (
    id         BIGSERIAL primary key,
    day        DATE NOT NULL UNIQUE,
    open_time  TIME,
    close_time TIME,
    closed     BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT valid_day_schedule CHECK (
        closed = TRUE
            OR (
            open_time IS NOT NULL
                AND close_time IS NOT NULL
                AND close_time > open_time
            )
        )
);

CREATE TABLE booking (
    id         BIGSERIAL PRIMARY KEY,
    service_id BIGINT NOT NULL REFERENCES service(id),
    client_id  BIGINT NOT NULL REFERENCES client(id),
    start_at   TIMESTAMP NOT NULL,
    end_at     TIMESTAMP NOT NULL,
    CHECK (end_at > start_at),
    CONSTRAINT booking_no_overlap
    EXCLUDE USING gist (tsrange(start_at, end_at) WITH &&)
);

INSERT INTO service_category(name)  values ('Hair'),
                                           ('Manicure'),
                                           ('Pedicure');

INSERT INTO service (category_id, name, duration_minutes) VALUES
                                                 ( 1,'Haircut', 45),
                                                 ( 1,'Hair Coloring', 120),
                                                 ( 1,'Blow-Dry', 30),
                                                 (2,'Classic Manicure', 30),
                                                 (2,'Gel Manicure', 45),
                                                 (2,'Acrylic Nail Extensions', 90),
                                                 (3,'Classic Spa Pedicure', 45),
                                                 (3,'Gel Pedicure', 60),
                                                 (3,'Medical Pedicure', 60);