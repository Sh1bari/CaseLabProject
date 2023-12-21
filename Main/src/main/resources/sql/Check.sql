ALTER TABLE "department" ADD CONSTRAINT record_state_check CHECK (record_state IN ('DELETED', 'ACTIVE'));
ALTER TABLE "users" ADD CONSTRAINT record_state_check CHECK (record_state IN ('DELETED', 'ACTIVE'));
ALTER TABLE "document_constructor_type" ADD CONSTRAINT record_state_check CHECK (record_state IN ('DELETED', 'ACTIVE'));
ALTER TABLE "application" ADD CONSTRAINT record_state_check CHECK (record_state IN ('DELETED', 'ACTIVE'));
ALTER TABLE "application_item" ADD CONSTRAINT record_state_check CHECK (record_state IN ('DELETED', 'ACTIVE'));
ALTER TABLE "document" ADD CONSTRAINT record_state_check CHECK (record_state IN ('DELETED', 'ACTIVE'));

ALTER TABLE "application" ADD CONSTRAINT application_status_check CHECK (application_status IN ('WAITING_FOR_ANSWER', 'ACCEPTED', 'DENIED', 'DRAW', 'NOT_ENOUGH_VOTES'));

ALTER TABLE "application_item" ADD CONSTRAINT application_item_status_check CHECK (status IN ('ACCEPTED', 'DENIED', 'PENDING'));