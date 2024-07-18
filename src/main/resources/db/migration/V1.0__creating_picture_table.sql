CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS picture (
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    path VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS app_user (
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    username VARCHAR(10) NOT NULL CHECK ( length(trim(username)) > 0 ) UNIQUE,
    password VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS app_authority (
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    authority VARCHAR(10) NOT NULL CHECK ( length(trim(authority)) > 0 ) UNIQUE
);

CREATE TABLE IF NOT EXISTS user_authority(
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    app_user uuid NOT NULL REFERENCES app_user (id),
    authority uuid NOT NULL REFERENCES app_authority (id),
    CONSTRAINT uk_user_authority UNIQUE (app_user, authority)
);

DO $$
DECLARE
    user_id uuid = uuid_generate_v4();
    role_id uuid = uuid_generate_v4();
BEGIN
    INSERT INTO app_authority(id, authority) VALUES(role_id, 'ROLE_MANAGER');
    INSERT INTO app_user(id, username, password) VALUES(user_id, 'user', '{noop}password');
    INSERT INTO user_authority(app_user, authority) VALUES(user_id, role_id);
END $$;
