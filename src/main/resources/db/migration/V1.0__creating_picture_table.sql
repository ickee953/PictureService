CREATE TABLE picture (
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    url VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL
);