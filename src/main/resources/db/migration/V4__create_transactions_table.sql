CREATE TABLE transactions
(
    id          UUID         NOT NULL,
    description VARCHAR(200) NOT NULL,
    type        VARCHAR(255) NOT NULL,
    category_id UUID         NOT NULL,
    account_id  UUID         NOT NULL,
    amount      INTEGER      NOT NULL,
    paid_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_transactions PRIMARY KEY (id)
);

ALTER TABLE transactions
    ADD CONSTRAINT FK_TRANSACTIONS_ON_ACCOUNT FOREIGN KEY (account_id) REFERENCES accounts (id);

ALTER TABLE transactions
    ADD CONSTRAINT FK_TRANSACTIONS_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id);