CREATE TABLE categories
(
    id          UUID         NOT NULL,
    label       VARCHAR(50)  NOT NULL,
    description VARCHAR(200) NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);