ALTER TABLE users
    ADD CONSTRAINT check_status CHECK (record_state IN ('DELETED', 'ACTIVE'));

