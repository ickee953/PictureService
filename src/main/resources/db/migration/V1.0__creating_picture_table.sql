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

--init
--insert into app_authority(id, authority) values('b3562a5d-e6ac-40dd-8eb7-aa0e06bc2a7a', 'ROLE_MANAGER');
--insert into app_user(id, username, password) values('e24a2790-d727-48be-a9a4-a4596ae802f', 'user', '{noop}password');
--insert into user_authority(app_user, authority) values('e24a2790-d727-48be-a9a4-a4596ae802f3', 'b3562a5d-e6ac-40dd-8eb7-aa0e06bc2a7a');