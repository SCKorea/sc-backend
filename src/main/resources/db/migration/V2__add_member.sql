CREATE TABLE member
(
    id            BINARY(16)   NOT NULL,
    social_id     VARCHAR(255) NOT NULL,
    social_type   VARCHAR(255) NOT NULL,
    nickname      VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NULL,
    profile_image VARCHAR(255) NULL,
    CONSTRAINT pk_member PRIMARY KEY (id)
);

