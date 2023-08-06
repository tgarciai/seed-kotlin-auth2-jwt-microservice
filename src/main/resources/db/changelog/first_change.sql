CREATE TABLE IF NOT EXISTS SEED_USER (
                                         id         uuid PRIMARY KEY,
                                         name       TEXT,
                                         email      TEXT,
                                         password   TEXT,
                                         created_at TIMESTAMP,
                                         updated_at TIMESTAMP
);
