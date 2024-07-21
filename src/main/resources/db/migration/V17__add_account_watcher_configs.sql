CREATE TABLE account_watchers
(
    id         bigserial   NOT NULL PRIMARY KEY,
    public_id  UUID        NOT NULL DEFAULT gen_random_uuid(),
    name       varchar(50) NOT NULL,
    account_id bigint REFERENCES accounts (id)
);

CREATE TABLE account_watcher_configs
(
    id                 bigserial      NOT NULL PRIMARY KEY,
    public_id          UUID           NOT NULL DEFAULT gen_random_uuid(),
    type               varchar(50)    NOT NULL,
    from_day_of_month  int,
    threshold_amount   decimal(19, 4) NOT NULL,
    threshold_currency varchar(3)     NOT NULL,
    account_watcher_id bigint REFERENCES account_watchers (id)
);


CREATE TABLE account_watcher_transaction_exclusions
(
    id                        bigserial NOT NULL PRIMARY KEY,
    public_id                 UUID      NOT NULL DEFAULT gen_random_uuid(),
    account_watcher_config_id bigint REFERENCES account_watcher_configs (id),
    financial_transaction_id  integer REFERENCES financial_transaction (id)
);

ALTER TABLE account_watcher_configs
    ADD CONSTRAINT check_nullable_fields CHECK (type = 'SUM_OF_TRANSACTIONS_IN_MONTH' AND
                                                from_day_of_month IS NOT NULL);