DROP VIEW nodrigen_transactions_to_import;

ALTER TABLE account
    RENAME TO accounts;

ALTER TABLE accounts
    ADD COLUMN public_id UUID NOT NULL DEFAULT gen_random_uuid();

ALTER TABLE accounts
    ALTER COLUMN id TYPE bigint;

ALTER TABLE financial_transaction
    ALTER COLUMN source_id TYPE bigint;

ALTER TABLE financial_transaction
    ALTER COLUMN destination_id TYPE bigint;

ALTER TABLE financial_transaction
    DROP CONSTRAINT fk_source_account;

ALTER TABLE financial_transaction
    DROP CONSTRAINT fk_destination_account;

ALTER TABLE financial_transaction
    ADD CONSTRAINT fk_source_account
        FOREIGN KEY (source_id) REFERENCES accounts
            ON UPDATE CASCADE;

ALTER TABLE financial_transaction
    ADD CONSTRAINT fk_destination_account
        FOREIGN KEY (destination_id) REFERENCES accounts
            ON UPDATE CASCADE;

UPDATE accounts a
SET id = ((SELECT COUNT(*) FROM accounts a2 WHERE a2.id < a.id) + 1000000);

UPDATE accounts a
SET id = ((SELECT COUNT(*) FROM accounts a2 WHERE a2.id < a.id) + 1);

CREATE SEQUENCE accounts_id_seq;

ALTER TABLE accounts
    ALTER COLUMN id SET DEFAULT nextval('accounts_id_seq');

SELECT setval('accounts_id_seq', (SELECT max(id) FROM accounts) + 1, FALSE);

ALTER TABLE financial_transaction
    DROP CONSTRAINT fk_source_account;

ALTER TABLE financial_transaction
    DROP CONSTRAINT fk_destination_account;

ALTER TABLE financial_transaction
    ADD CONSTRAINT fk_source_account
        FOREIGN KEY (source_id) REFERENCES accounts;

ALTER TABLE financial_transaction
    ADD CONSTRAINT fk_destination_account
        FOREIGN KEY (destination_id) REFERENCES accounts;
