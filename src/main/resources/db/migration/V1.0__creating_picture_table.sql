CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE picture (
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    path VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL
);