ALTER TABLE repayment_day_strategy_configs
    DROP CONSTRAINT repayment_day_strategy_configs_day_of_month_key;

CREATE UNIQUE INDEX repayment_day_strategy_configs_day_of_month_key
    ON repayment_day_strategy_configs (day_of_month, domain_id);

SELECT setval('repayment_day_strategy_configs_id_seq', (SELECT max(id) FROM repayment_day_strategy_configs) + 1, FALSE);