CREATE TABLE accounts
(
    id         UUID         NOT NULL,
    label      VARCHAR(100) NOT NULL,
    owner_id   UUID         NOT NULL,
    balance    INTEGER      NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_accounts PRIMARY KEY (id)
);

ALTER TABLE accounts
    ADD CONSTRAINT FK_ACCOUNTS_ON_OWNER FOREIGN KEY (owner_id) REFERENCES users (id);