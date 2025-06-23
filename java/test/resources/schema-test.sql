-- This file is used to create the database schema for testing purposes.
CREATE TABLE IF NOT EXISTS TIME_DEPOSITS
(
    ID        INT PRIMARY KEY,
    PLAN_TYPE VARCHAR(8)       NOT NULL,
    DAYS      INT              NOT NULL,
    BALANCE   DOUBLE PRECISION NOT NULL
);

CREATE TABLE IF NOT EXISTS WITHDRAWALS
(
    ID              INT PRIMARY KEY,
    AMOUNT          DOUBLE PRECISION NOT NULL,
    DATE            DATE             NOT NULL,
    TIME_DEPOSIT_ID INT              NOT NULL,
    CONSTRAINT FK_TIME_DEPOSIT
        FOREIGN KEY (TIME_DEPOSIT_ID)
            REFERENCES TIME_DEPOSITS (ID)
);