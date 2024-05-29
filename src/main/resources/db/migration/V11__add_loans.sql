CREATE TABLE repayment_day_strategy_configs
(
    id           bigserial   NOT NULL PRIMARY KEY,
    public_id    UUID        NOT NULL DEFAULT gen_random_uuid(),
    name         varchar(50) NOT NULL,
    strategy     varchar(50) NOT NULL,
    day_of_month integer UNIQUE,
    domain_id    integer REFERENCES domain (id)
);

ALTER TABLE repayment_day_strategy_configs
    ADD CONSTRAINT check_nullable_fields CHECK ((strategy = 'NTH_DAY' AND day_of_month IS NOT NULL) OR
                                                (strategy <> 'NTH_DAY' AND day_of_month IS NULL));

CREATE TABLE rate_strategy_configs
(
    id                                         bigserial   NOT NULL PRIMARY KEY,
    public_id                                  UUID        NOT NULL DEFAULT gen_random_uuid(),
    name                                       varchar(50) NOT NULL,
    strategy                                   varchar(50) NOT NULL,
    constant_rate                              numeric(10, 6),
    variable_rate_margin                       numeric(10, 6),
    becomes_variable_rate_after_n_installments integer,
    domain_id                                  integer     NOT NULL REFERENCES domain (id)
);

ALTER TABLE rate_strategy_configs
    ADD CONSTRAINT check_nullable_fields CHECK (strategy = 'CONSTANT_FOR_N_INSTALLMENTS' AND
                                                constant_rate IS NOT NULL AND variable_rate_margin IS NOT NULL AND
                                                becomes_variable_rate_after_n_installments IS NOT NULL);

CREATE TABLE loans
(
    id                               bigserial      NOT NULL PRIMARY KEY,
    public_id                        UUID           NOT NULL DEFAULT gen_random_uuid(),
    name                             varchar(50)    NOT NULL,
    payment_date                     date           NOT NULL,
    number_of_installments           int            NOT NULL,
    repayment_day_strategy_config_id bigint         NOT NULL REFERENCES repayment_day_strategy_configs (id),
    rate_strategy_config_id          bigint         NOT NULL REFERENCES rate_strategy_configs (id),
    loan_amount                      decimal(19, 4) NOT NULL,
    loan_currency                    varchar(3)     NOT NULL,
    domain_id                        integer        NOT NULL REFERENCES domain (id)
);

CREATE TABLE installments
(
    id                       bigserial      NOT NULL PRIMARY KEY,
    public_id                UUID           NOT NULL DEFAULT gen_random_uuid(),
    loan_id                  bigint         NOT NULL REFERENCES loans (id),
    paid_at                  date           NOT NULL,
    repaid_interest_amount   decimal(19, 4) NOT NULL,
    repaid_interest_currency varchar(3)     NOT NULL,
    repaid_amount            decimal(19, 4) NOT NULL,
    repaid_currency          varchar(3)     NOT NULL,
    overpayment_amount       decimal(19, 4) NOT NULL,
    overpayment_currency     varchar(3)     NOT NULL
);