ALTER TABLE client
    RENAME TO other_parties;

ALTER TABLE other_parties
    ADD COLUMN kind VARCHAR(50);

ALTER TABLE other_parties
    ADD COLUMN public_id UUID NOT NULL DEFAULT gen_random_uuid();

UPDATE other_parties
SET kind = 'CLIENT';

ALTER TABLE other_parties
    ALTER COLUMN kind SET NOT NULL;

ALTER TABLE other_parties
    ALTER COLUMN id TYPE bigint;

CREATE SEQUENCE other_parties_id_seq;

ALTER TABLE other_parties
    ALTER COLUMN id SET DEFAULT nextval('other_parties_id_seq');

ALTER TABLE client_payment
    ALTER COLUMN client_id TYPE bigint;

ALTER TABLE performed_service
    ALTER COLUMN client_id TYPE bigint;

ALTER TABLE performed_service
    DROP CONSTRAINT fk_client;

ALTER TABLE performed_service
    ADD CONSTRAINT fk_client
        FOREIGN KEY (client_id) REFERENCES other_parties
            ON UPDATE CASCADE;

ALTER TABLE client_payment
    DROP CONSTRAINT fk_client;

ALTER TABLE client_payment
    ADD CONSTRAINT fk_client
        FOREIGN KEY (client_id) REFERENCES other_parties
            ON UPDATE CASCADE;

UPDATE other_parties op
SET id = (SELECT COUNT(*) FROM other_parties op2 WHERE op2.id < op.id + 1);

ALTER TABLE performed_service
    DROP CONSTRAINT fk_client;

ALTER TABLE performed_service
    ADD CONSTRAINT fk_client
        FOREIGN KEY (client_id) REFERENCES other_parties;

ALTER TABLE client_payment
    DROP CONSTRAINT fk_client;

ALTER TABLE client_payment
    ADD CONSTRAINT fk_client
        FOREIGN KEY (client_id) REFERENCES other_parties;

ALTER SEQUENCE other_parties_id_seq OWNED BY other_parties.id;

SELECT setval('other_parties_id_seq', (SELECT max(id) FROM other_parties) + 1, FALSE);