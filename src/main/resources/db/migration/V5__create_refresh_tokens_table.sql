CREATE TABLE refresh_tokens
(
    id                     UUID         NOT NULL,
    token_hash             VARCHAR(64)  NOT NULL,
    user_id                UUID         NOT NULL,
    family_id              UUID         NOT NULL,
    created_at             TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    expires_at             TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    revoked_at             TIMESTAMP WITHOUT TIME ZONE,
    replaced_by_token_hash VARCHAR(64),
    CONSTRAINT pk_refresh_tokens PRIMARY KEY (id),
    CONSTRAINT uk_refresh_tokens_token_hash UNIQUE (token_hash),
    CONSTRAINT fk_refresh_tokens_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_refresh_tokens_family_id ON refresh_tokens (family_id);
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens (user_id);
