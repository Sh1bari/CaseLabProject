CREATE OR REPLACE FUNCTION add_check_status_constraint() RETURNS VOID AS $$
DECLARE
    tbl_name TEXT;
    table_names TEXT[] := ARRAY['users', 'application', 'department', 'document', 'document_constructor_type', 'application_item'];
BEGIN
    FOREACH tbl_name IN ARRAY table_names
        LOOP
            IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints WHERE constraint_name = 'check_status' AND table_name = tbl_name) THEN
                EXECUTE 'ALTER TABLE ' || tbl_name || ' ADD CONSTRAINT check_status CHECK (record_state IN (''DELETED'', ''ACTIVE''))';
            END IF;
        END LOOP;
END
$$ LANGUAGE plpgsql;

select add_check_status_constraint();