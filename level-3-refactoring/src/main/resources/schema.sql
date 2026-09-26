CREATE TABLE activity (
    id       BIGINT PRIMARY KEY,
    title    VARCHAR(255) NOT NULL,
    supplier VARCHAR(255) NOT NULL
);

CREATE TABLE review (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    activity_id BIGINT        NOT NULL REFERENCES activity (id),
    author      VARCHAR(100)  NOT NULL,
    rating      INT           NOT NULL,
    comment     VARCHAR(2000),
    flagged     BOOLEAN       NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP     NOT NULL
);
