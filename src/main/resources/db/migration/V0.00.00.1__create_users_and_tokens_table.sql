CREATE TABLE users (
    id SERIAL PRIMARY KEY NOT NULL,

    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,

    created_at TIMESTAMP,
    created_by VARCHAR(50),
    updated_at TIMESTAMP,
    updated_by VARCHAR(50)
);

CREATE TABLE tokens (
	id SERIAL PRIMARY KEY NOT NULL,

    user_id INTEGER NOT NULL,
	token VARCHAR(255) NOT NULL,

	created_at TIMESTAMP,
    created_by VARCHAR(50),
    updated_at TIMESTAMP,
    updated_by VARCHAR(50),

    CONSTRAINT unique_tokens_user UNIQUE (user_id, token)
);