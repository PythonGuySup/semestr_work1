CREATE TABLE IF NOT EXISTS TranslationLogs
(
    id              SERIAL PRIMARY KEY,
    ip_address      VARCHAR(50) NOT NULL,
    original_text   TEXT NOT NULL,
    translated_text TEXT NOT NULL ,
    created_at      TIMESTAMP NOT NULL
);