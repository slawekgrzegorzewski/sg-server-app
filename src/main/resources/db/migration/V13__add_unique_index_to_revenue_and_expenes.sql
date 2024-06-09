CREATE UNIQUE INDEX domain_year_order_idx ON revenue_and_expense_entries (domain_id, extract(YEAR FROM entry_date), entry_order);

CREATE TABLE financial_documents
(
    id             bigserial      NOT NULL PRIMARY KEY,
    kind           varchar(50)    NOT NULL,
    public_id      UUID           NOT NULL DEFAULT gen_random_uuid(),
    name           varchar(50)    NOT NULL,
    other_party_id bigint references other_parties (id),
    amount         decimal(19, 4) NOT NULL,
    currency       varchar(3)     NOT NULL,
    vat_amount     decimal(19, 4),
    vat_currency   varchar(3),
    created_at     date           NOT NULL,
    due_to         date,
    description    varchar(1000)  NOT NULL,
    domain_id      integer REFERENCES domain (id)
);

ALTER TABLE financial_documents
    ADD CONSTRAINT non_nullable_fields CHECK (
        (
            vat_amount IS NOT NULL AND vat_currency IS NOT NULL AND due_to IS NOT NULL AND
            kind IN ('DEBIT_INVOICE', 'CREDIT_INVOICE')
            ) OR (
            vat_amount IS NULL AND vat_currency IS NULL AND due_to IS NULL AND
            kind NOT IN ('DEBIT_INVOICE', 'CREDIT_INVOICE')
            )
        );