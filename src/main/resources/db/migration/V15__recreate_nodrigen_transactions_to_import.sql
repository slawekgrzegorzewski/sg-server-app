CREATE VIEW nodrigen_transactions_to_import
            (id, domain_id, conversion_rate, credit, debit, description, time_of_transaction, destination_id, source_id,
             credit_bank_account_id, debit_bank_account_id, credit_nodrigen_transaction_id,
             debit_nodrigen_transaction_id, nodrigen_transaction_id)
AS
WITH tranasactions AS (SELECT a.id,
                              a.name,
                              a.currency,
                              a.domain_id,
                              bp.institution_id,
                              nt.transaction_id,
                              nt.proprietary_bank_transaction_code,
                              nt.currency_exchange_rate,
                              nt.id                                                       AS nodrigen_transaction_id,
                              nt.currency_exchange_instructed_amount_amount,
                              nt.currency_exchange_instructed_amount_currency,
                              nt.currency_exchange_unit_currency,
                              nt.currency_exchange_source_currency,
                              nt.currency_exchange_target_currency,
                              CASE
                                  WHEN nt.transaction_amount_amount > 0::numeric THEN ba.id
                                  ELSE NULL::integer
                                  END                                                     AS credit_bank_account_id,
                              CASE
                                  WHEN nt.transaction_amount_amount < 0::numeric THEN ba.id
                                  ELSE NULL::integer
                                  END                                                     AS debit_bank_account_id,
                              CASE
                                  WHEN nt.transaction_amount_amount > 0::numeric THEN nt.transaction_amount_amount
                                  ELSE 0::numeric
                                  END                                                     AS credit,
                              CASE
                                  WHEN nt.transaction_amount_amount > 0::numeric THEN 0::numeric
                                  ELSE - nt.transaction_amount_amount
                                  END                                                     AS debit,
                              (((COALESCE(nt.remittance_information_unstructured_array,
                                          nt.remittance_information_structured_array,
                                          nt.remittance_information_unstructured,
                                          nt.remittance_information_structured)::text || ': '::text) ||
                                COALESCE(nt.debtor_name, ''::character varying)::text) || ' => '::text) ||
                              COALESCE(nt.creditor_name, ''::character varying)::text     AS description,
                              LEAST(COALESCE(nt.booking_date_time, nt.booking_date::timestamp without time zone),
                                    COALESCE(nt.value_date_time,
                                             nt.value_date::timestamp without time zone)) AS time_of_transaction,
                              CASE
                                  WHEN nt.transaction_amount_amount > 0::numeric THEN a.id::bigint
                                  ELSE NULL::bigint
                                  END                                                     AS destination_id,
                              CASE
                                  WHEN nt.transaction_amount_amount < 0::numeric THEN a.id::bigint
                                  ELSE NULL::bigint
                                  END                                                     AS source_id
                       FROM nodrigen_transaction nt
                                JOIN bank_account ba ON nt.bank_account_id = ba.id
                                JOIN bank_permission bp ON ba.bank_permission_id = bp.id
                                JOIN accounts a ON ba.id = a.bank_account_id
                       WHERE nt.credit_transaction_id IS NULL
                         AND nt.debit_transaction_id IS NULL
                         AND nt.reset_in_id IS NULL
                         AND NOT nt.ignored
                       ORDER BY a.id,
                                (LEAST(COALESCE(nt.booking_date_time, nt.booking_date::timestamp without time zone),
                                       COALESCE(nt.value_date_time, nt.value_date::timestamp without time zone))) DESC)
SELECT nextval('nodrigen_transactions_to_import_sequence'::regclass) AS id,
       tranasactions.domain_id,
       1.0                                                           AS conversion_rate,
       tranasactions.credit,
       tranasactions.debit,
       tranasactions.description,
       tranasactions.time_of_transaction,
       tranasactions.destination_id::bigint,
       tranasactions.source_id::bigint,
       tranasactions.credit_bank_account_id,
       tranasactions.debit_bank_account_id,
       CASE
           WHEN tranasactions.credit > 0::numeric THEN tranasactions.nodrigen_transaction_id
           ELSE NULL::integer
           END                                                       AS credit_nodrigen_transaction_id,
       CASE
           WHEN tranasactions.debit > 0::numeric THEN tranasactions.nodrigen_transaction_id
           ELSE NULL::integer
           END                                                       AS debit_nodrigen_transaction_id,
       tranasactions.nodrigen_transaction_id
FROM tranasactions
WHERE tranasactions.institution_id::text <> 'REVOLUT_REVOGB21'::text
   OR tranasactions.proprietary_bank_transaction_code::text <> 'EXCHANGE'::text
UNION
SELECT nextval('nodrigen_transactions_to_import_sequence'::regclass)                             AS id,
       nt_debit.domain_id,
       (nt_credit.credit / nt_debit.currency_exchange_instructed_amount_amount)::numeric(16, 12) AS conversion_rate,
       nt_credit.credit,
       nt_debit.currency_exchange_instructed_amount_amount                                       AS debit,
       nt_debit.description,
       nt_debit.time_of_transaction,
       nt_credit.destination_id::bigint,
       nt_debit.source_id::bigint,
       nt_debit.credit_bank_account_id,
       nt_debit.debit_bank_account_id,
       nt_credit.nodrigen_transaction_id                                                         AS credit_nodrigen_transaction_id,
       nt_debit.nodrigen_transaction_id                                                          AS debit_nodrigen_transaction_id,
       NULL::integer                                                                             AS nodrigen_transaction_id
FROM tranasactions nt_debit
         JOIN tranasactions nt_credit ON nt_debit.transaction_id::text = nt_credit.transaction_id::text
WHERE nt_debit.institution_id::text = 'REVOLUT_REVOGB21'::text
  AND nt_debit.proprietary_bank_transaction_code::text = 'EXCHANGE'::text
  AND nt_credit.institution_id::text = 'REVOLUT_REVOGB21'::text
  AND nt_credit.proprietary_bank_transaction_code::text = 'EXCHANGE'::text
  AND nt_credit.domain_id = nt_debit.domain_id
  AND nt_credit.debit = 0::numeric
  AND nt_debit.credit = 0::numeric
UNION
SELECT nextval('nodrigen_transactions_to_import_sequence'::regclass)        AS id,
       nt_debit.domain_id,
       1.0                                                                  AS conversion_rate,
       0.0                                                                  AS credit,
       nt_debit.debit - nt_debit.currency_exchange_instructed_amount_amount AS debit,
       'prowizja za wymianę '::text || nt_debit.nodrigen_transaction_id     AS description,
       nt_debit.time_of_transaction,
       NULL::bigint                                                         AS destination_id,
       nt_debit.source_id::bigint,
       NULL::integer                                                        AS credit_bank_account_id,
       nt_debit.credit_bank_account_id                                      AS debit_bank_account_id,
       NULL::integer                                                        AS credit_nodrigen_transaction_id,
       nt_debit.credit_bank_account_id                                      AS debit_nodrigen_transaction_id,
       NULL::integer                                                        AS nodrigen_transaction_id
FROM tranasactions nt_debit
         JOIN tranasactions nt_credit ON nt_debit.transaction_id::text = nt_credit.transaction_id::text
WHERE nt_debit.institution_id::text = 'REVOLUT_REVOGB21'::text
  AND nt_debit.proprietary_bank_transaction_code::text = 'EXCHANGE'::text
  AND nt_credit.institution_id::text = 'REVOLUT_REVOGB21'::text
  AND nt_credit.proprietary_bank_transaction_code::text = 'EXCHANGE'::text
  AND nt_credit.domain_id = nt_debit.domain_id
  AND nt_credit.debit = 0::numeric
  AND nt_debit.credit = 0::numeric
  AND (nt_debit.debit - nt_debit.currency_exchange_instructed_amount_amount) > 0::numeric;