CREATE TABLE translate_progression
(
    id                  BINARY(36)   NOT NULL,
    news_id             BINARY(36)   NOT NULL,
    source_language     VARCHAR(255) NOT NULL,
    target_language     VARCHAR(255) NOT NULL,
    translation_status  VARCHAR(255) NOT NULL,
    translator_provider VARCHAR(255) NOT NULL,
    message             VARCHAR(255) NULL,
    CONSTRAINT pk_translate_progression PRIMARY KEY (id)
);

ALTER TABLE translate_progression
    ADD CONSTRAINT UNIQUE_NEWS_ID_AND_TARGET_LANGUAGE UNIQUE (news_id, target_language);
