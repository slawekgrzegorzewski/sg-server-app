CREATE SEQUENCE nodrigen_transactions_to_import_sequence CYCLE;

CREATE OR REPLACE VIEW nodrigen_transactions_to_import (id)
AS
with tranasactions as (select a.id,
                              a.name,
                              a.currency,
                              a.domain_id,
                              bp.institution_id,
                              nt.transaction_id,
                              nt.proprietary_bank_transaction_code,
                              currency_exchange_rate,
                              nt.id                                                    as nodrigen_transaction_id,
                              currency_exchange_instructed_amount_amount,
                              currency_exchange_instructed_amount_currency,
                              currency_exchange_unit_currency,
                              currency_exchange_source_currency,
                              currency_exchange_target_currency,
                              (case when transaction_amount_amount > 0 then ba.id end) as credit_bank_account_id,
                              (case when transaction_amount_amount < 0 then ba.id end) as debit_bank_account_id,
                              (case
                                   when transaction_amount_amount > 0 then transaction_amount_amount
                                   else 0 end)                                         as credit,
                              (case
                                   when transaction_amount_amount > 0 then 0
                                   else -transaction_amount_amount end)                as debit,
                              coalesce(remittance_information_unstructured_array,
                                       remittance_information_structured_array,
                                       remittance_information_unstructured,
                                       remittance_information_structured)
                                  || ': '
                                  || coalesce(debtor_name, '')
                                  || ' => '
                                  || coalesce(creditor_name, '')                       as description,
                              least(coalesce(booking_date_time, booking_date),
                                    coalesce(value_date_time, value_date))::timestamp  as time_of_transaction,
                              (case when transaction_amount_amount > 0 then a.id end)  as destination_id,
                              (case when transaction_amount_amount < 0 then a.id end)  as source_id
                       from nodrigen_transaction nt
                                join bank_account ba on nt.bank_account_id = ba.id
                                join bank_permission bp on ba.bank_permission_id = bp.id
                                join account a on ba.id = a.bank_account_id
                       where nt.credit_transaction_id is null
                         and nt.debit_transaction_id is null
                         and reset_in_id is null
                         and not ignored
                       order by id, time_of_transaction desc)
    (select nextval('nodrigen_transactions_to_import_sequence')   as id,
            domain_id,
            1.0                                                   as conversion_rate,
            credit,
            debit,
            description,
            time_of_transaction,
            destination_id,
            source_id,
            credit_bank_account_id,
            debit_bank_account_id,
            case when credit > 0 then nodrigen_transaction_id end as credit_nodrigen_transaction_id,
            case when debit > 0 then nodrigen_transaction_id end  as debit_nodrigen_transaction_id
     from tranasactions
     where (institution_id <> 'REVOLUT_REVOGB21' or proprietary_bank_transaction_code <> 'EXCHANGE'))
union
(select nextval('nodrigen_transactions_to_import_sequence')                                       as id,
        nt_debit.domain_id                                                                        as domain_id,
        (nt_credit.credit / nt_debit.currency_exchange_instructed_amount_amount)::numeric(16, 12) as conversion_rate,
        nt_credit.credit                                                                          as credit,
        nt_debit.currency_exchange_instructed_amount_amount                                       as debit,
        nt_debit.description                                                                      as description,
        nt_debit.time_of_transaction                                                              as time_of_transaction,
        nt_credit.destination_id                                                                  as destination_id,
        nt_debit.source_id                                                                        as source_id,
        nt_debit.credit_bank_account_id                                                           as credit_bank_account_id,
        nt_debit.debit_bank_account_id                                                            as debit_bank_account_id,
        nt_credit.nodrigen_transaction_id                                                         as credit_nodrigen_transaction_id,
        nt_debit.nodrigen_transaction_id                                                          as debit_nodrigen_transaction_id
 from tranasactions nt_debit
          join tranasactions nt_credit on nt_debit.transaction_id = nt_credit.transaction_id
 where nt_debit.institution_id = 'REVOLUT_REVOGB21'
   and nt_debit.proprietary_bank_transaction_code = 'EXCHANGE'
   and nt_credit.institution_id = 'REVOLUT_REVOGB21'
   and nt_credit.proprietary_bank_transaction_code = 'EXCHANGE'
   and nt_credit.domain_id = nt_debit.domain_id
   and nt_credit.debit = 0
   and nt_debit.credit = 0)
union
(select nextval('nodrigen_transactions_to_import_sequence')                  as id,
        nt_debit.domain_id                                                   as domain_id,
        1.0                                                                  as conversion_rate,
        0.0                                                                  as credit,
        nt_debit.debit - nt_debit.currency_exchange_instructed_amount_amount as debit,
        'prowizja za wymianę ' || nt_debit.nodrigen_transaction_id           as description,
        nt_debit.time_of_transaction                                         as time_of_transaction,
        null                                                                 as destination_id,
        nt_debit.source_id                                                   as source_id,
        null                                                                 as credit_bank_account_id,
        nt_debit.credit_bank_account_id                                      as debit_bank_account_id,
        null                                                                 as credit_nodrigen_transaction_id,
        nt_debit.credit_bank_account_id                                      as debit_nodrigen_transaction_i
 from tranasactions nt_debit
          join tranasactions nt_credit on nt_debit.transaction_id = nt_credit.transaction_id
 where nt_debit.institution_id = 'REVOLUT_REVOGB21'
   and nt_debit.proprietary_bank_transaction_code = 'EXCHANGE'
   and nt_credit.institution_id = 'REVOLUT_REVOGB21'
   and nt_credit.proprietary_bank_transaction_code = 'EXCHANGE'
   and nt_credit.domain_id = nt_debit.domain_id
   and nt_credit.debit = 0
   and nt_debit.credit = 0
   and nt_debit.debit - nt_debit.currency_exchange_instructed_amount_amount > 0);