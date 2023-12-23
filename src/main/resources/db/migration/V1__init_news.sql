CREATE TABLE content
(
    sequence BIGINT AUTO_INCREMENT NOT NULL,
    news_id  BINARY(16)            NOT NULL,
    language VARCHAR(255)          NOT NULL,
    content  TEXT                  NOT NULL,
    title    VARCHAR(255)          NOT NULL,
    excerpt  VARCHAR(512)          NULL,
    CONSTRAINT pk_content PRIMARY KEY (sequence)
);

CREATE TABLE news
(
    id                BINARY(16)   NOT NULL,
    news_type         VARCHAR(255) NOT NULL,
    origin_id         BIGINT       NOT NULL,
    origin_url        VARCHAR(255) NOT NULL,
    published_at      DATETIME     NOT NULL,
    support_languages VARCHAR(255) NOT NULL,
    title             VARCHAR(255) NOT NULL,
    excerpt           VARCHAR(512) NULL,
    CONSTRAINT pk_news PRIMARY KEY (id)
);

ALTER TABLE content
    ADD CONSTRAINT FK_CONTENT_ON_NEWS FOREIGN KEY (news_id) REFERENCES news (id);
