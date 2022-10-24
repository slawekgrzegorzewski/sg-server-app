create sequence hibernate_sequence;
alter sequence hibernate_sequence owner to postgres;

create sequence nodrigen_transactions_to_import_sequence cycle;
alter sequence nodrigen_transactions_to_import_sequence owner to postgres;

create table checker_step
(
    dtype                                 varchar(31) not null,
    id                                    integer     not null primary key,
    description                           varchar(1000),
    name                                  varchar(1000),
    step_order                            integer     not null,
    attribute                             varchar(255),
    attribute_variable_name_in_expression varchar(255),
    css_query                             varchar(1000),
    operation_expression                  varchar(255),
    page_content_variable                 varchar(255),
    result_variable                       varchar(255),
    element_variable_name_in_expression   varchar(255),
    elements_variable                     varchar(255),
    filter_expression                     varchar(255),
    page_url_expression                   varchar(1000),
    variable_name                         varchar(255),
    element_index                         integer,
    email_header                          varchar(255),
    email_title                           varchar(255),
    result_variable_name                  varchar(255),
    send_email_notification               boolean,
    get_result_from_variable              varchar(255)
);
alter table checker_step owner to postgres;

create table country
(
    id integer not null primary key
);
alter table country owner to postgres;

create table country_names
(
    country_id integer not null
        constraint fk_country references country,
    names      varchar(255)
);
alter table country_names owner to postgres;

create table domain
(
    id   integer not null primary key,
    name varchar(255)
);
alter table domain owner to postgres;

create table accountant_settings
(
    id         integer not null primary key,
    is_company boolean not null,
    domain_id  integer
        constraint fk_domain references domain
);
alter table accountant_settings owner to postgres;

create table application_user
(
    id                integer               not null primary key,
    email             varchar(255),
    first_name        varchar(255),
    is_using2fa       boolean default false not null,
    last_name         varchar(255),
    login             varchar(255),
    password          varchar(255),
    secret            varchar(255),
    default_domain_id integer
        constraint fk_domain references domain
);
alter table application_user owner to postgres;

create table application_user_domain_relation
(
    access_level        varchar(255),
    application_user_id integer not null
        constraint fk_application_user references application_user,
    domain_id           integer not null
        constraint fk_domain references domain,
    primary key (application_user_id, domain_id)
);
alter table application_user_domain_relation owner to postgres;

create table application_user_roles
(
    application_user_id integer not null
        constraint fk_application_user references application_user,
    roles               varchar(255)
);
alter table application_user_roles owner to postgres;

create table bank_permission
(
    dtype             varchar(31) not null,
    id                integer     not null primary key,
    created_at        timestamp,
    given_at          timestamp,
    withdrawn_at      timestamp,
    confirmation_link varchar(255),
    institution_id    varchar(255),
    reference         text,
    requisition_id    uuid,
    ssn               text,
    domain_id         integer
        constraint fk_domain references domain
);
alter table bank_permission owner to postgres;

create table bank_account
(
    id                 integer      not null primary key,
    bic                varchar(255),
    currency           varchar(255) not null,
    external_id        varchar(255) not null,
    iban               varchar(255) not null,
    owner              varchar(255) not null,
    product            varchar(255),
    bank_permission_id integer
        constraint fk_bank_permission references bank_permission,
    domain_id          integer
        constraint fk_domain references domain
);
alter table bank_account owner to postgres;

create table account
(
    id                                      integer                     not null primary key,
    currency                                varchar(255)                not null,
    current_balance                         numeric(19, 2) default 0    not null,
    name                                    varchar(255)                not null,
    last_transaction_included_in_balance_id integer,
    domain_id                               integer
        constraint fk_domain references domain,
    visible                                 boolean        default true not null,
    bank_account_id                         integer
        constraint fk_bank_account references bank_account
);
alter table account owner to postgres;

create table billing_period
(
    id        integer      not null primary key,
    name      varchar(255) not null,
    period varchar (255) not null,
    domain_id integer
        constraint fk_domain references domain
);
alter table billing_period owner to postgres;

create table category
(
    id          integer      not null primary key,
    description varchar(255) not null,
    name        varchar(255) not null,
    domain_id   integer
        constraint fk_domain references domain
);
alter table category owner to postgres;

create table checker_task
(
    id          integer not null primary key,
    description varchar(255),
    interval    bigint,
    name        varchar(255),
    next_run    timestamp,
    for_user_id integer
        constraint fk_application_user references application_user
);
alter table checker_task owner to postgres;

create table checker_task_history
(
    id       integer not null primary key,
    messages varchar(10000),
    result   varchar(255),
    run_time timestamp,
    task_id  integer
        constraint fk_checker_task references checker_task
);
alter table checker_task_history owner to postgres;

create table checker_task_steps
(
    checker_task_id integer not null
        constraint fk_checker_task references checker_task,
    steps_id        integer not null
        constraint uk_checker_step unique
        constraint fk_checker_step references checker_step
);
alter table checker_task_steps owner to postgres;

create table client
(
    id        integer not null primary key,
    name      varchar(255),
    domain_id integer
        constraint fk_domain references domain
);
alter table client owner to postgres;

create table client_payment
(
    id                      integer               not null primary key,
    bill_of_sale            boolean               not null,
    bill_of_sale_as_invoice boolean               not null,
    currency                varchar(255)          not null,
    date                    date                  not null,
    invoice                 boolean               not null,
    price                   numeric(19, 2)        not null,
    client_id               integer               not null
        constraint fk_client references client,
    domain_id               integer               not null
        constraint fk_domain references domain,
    not_registered          boolean default false not null
);
alter table client_payment owner to postgres;

create table cube_record
(
    id          integer not null primary key,
    cubes_type  varchar(255),
    record_time timestamp,
    scramble    varchar(255),
    time        bigint,
    domain_id   integer
        constraint fk_domain references domain
);
alter table cube_record owner to postgres;

create table domain_invitation
(
    application_user_id integer not null
        constraint fk_application_user references application_user,
    domain_id           integer not null
        constraint fk_domain references domain,
    primary key (application_user_id, domain_id)
);
alter table domain_invitation owner to postgres;

create table expense
(
    id                integer        not null primary key,
    amount            numeric(19, 2) not null,
    currency          varchar(255)   not null,
    description       varchar(2000)  not null,
    expense_date      date,
    billing_period_id integer
        constraint fk_billing_period references billing_period,
    category_id       integer        not null
        constraint fk_category references category
);
alter table expense owner to postgres;

create table financial_transaction
(
    id                  integer not null primary key,
    conversion_rate     numeric(19, 2),
    credit              numeric(19, 2),
    debit               numeric(19, 2),
    description         varchar(2000),
    time_of_transaction timestamp,
    destination_id      integer
        constraint fk_destination_account references account,
    source_id           integer
        constraint fk_source_account references account,
    fee                 numeric(19, 2),
    other_fee           numeric(19, 2),
    fee_currency        varchar(255),
    other_fee_currency  varchar(255)
);
alter table financial_transaction owner to postgres;
alter table account
    add constraint fk_financial_transaction foreign key (last_transaction_included_in_balance_id) references financial_transaction;

create table holiday_currencies
(
    id                   integer not null primary key,
    euro_conversion_rate numeric(19, 6),
    kuna_conversion_rate numeric(19, 6),
    domain_id            integer
        constraint fk_domain references domain
);
alter table holiday_currencies owner to postgres;

create table income
(
    id                integer        not null primary key,
    amount            numeric(19, 2) not null,
    currency          varchar(255)   not null,
    description       varchar(2000)  not null,
    income_date       date,
    billing_period_id integer
        constraint fk_billing_period references billing_period,
    category_id       integer        not null
        constraint fk_category references category
);
alter table income owner to postgres;

create table month_summary
(
    id                integer not null primary key,
    data              varchar(10000),
    billing_period_id integer
        constraint fk_billing_period references billing_period
);
alter table month_summary owner to postgres;

create table nodrigen_access
(
    id                 integer not null primary key,
    access_expires     bigint,
    access_expires_at  timestamp,
    access_token       text,
    archived_at        timestamp,
    refresh_expires    bigint,
    refresh_expires_at timestamp,
    refresh_token      text
);
alter table nodrigen_access owner to postgres;

create table nodrigen_bank_account_balance
(
    id                         integer not null primary key,
    balance_amount_amount      numeric(19, 2),
    balance_amount_currency    varchar(255),
    balance_type               varchar(255),
    credit_limit_included      boolean,
    fetch_time                 timestamp,
    last_change_date_time      timestamp,
    last_committed_transaction varchar(255),
    reference_date             date,
    bank_account_id            integer
        constraint fk_bank_account references bank_account
);
alter table nodrigen_bank_account_balance owner to postgres;

create table nodrigen_transaction
(
    id                                                   integer               not null primary key,
    additional_information                               varchar(255),
    additional_information_structured                    varchar(255),
    balance_after_transaction_amount                     numeric(19, 2),
    balance_after_transaction_currency                   varchar(255),
    balance_after_transaction_balance_type               varchar(255),
    balance_after_transaction_credit_limit_included      boolean,
    balance_after_transaction_last_change_date_time      timestamp,
    balance_after_transaction_last_committed_transaction varchar(255),
    balance_after_transaction_reference_date             date,
    bank_transaction_code                                varchar(255),
    booking_date                                         date,
    booking_date_time                                    timestamp,
    check_id                                             varchar(255),
    creditor_account_bban                                varchar(255),
    creditor_account_iban                                varchar(255),
    creditor_agent                                       varchar(255),
    creditor_id                                          varchar(255),
    creditor_name                                        varchar(255),
    currency_exchange_rate                               numeric(19, 2),
    currency_exchange_instructed_amount_amount           numeric(19, 2),
    currency_exchange_instructed_amount_currency         varchar(255),
    currency_exchange_source_currency                    varchar(255),
    currency_exchange_target_currency                    varchar(255),
    currency_exchange_unit_currency                      varchar(255),
    debtor_account_bban                                  varchar(255),
    debtor_account_iban                                  varchar(255),
    debtor_agent                                         varchar(255),
    debtor_name                                          varchar(255),
    entry_reference                                      varchar(255),
    mandate_id                                           varchar(255),
    phase                                                varchar(255),
    proprietary_bank_transaction_code                    varchar(255),
    purpose_code                                         varchar(255),
    remittance_information_structured                    varchar(255),
    remittance_information_structured_array              varchar(255),
    remittance_information_unstructured                  varchar(255),
    remittance_information_unstructured_array            varchar(255),
    transaction_amount_amount                            numeric(19, 2),
    transaction_amount_currency                          varchar(255),
    transaction_id                                       varchar(255),
    ultimate_creditor                                    varchar(255),
    ultimate_debtor                                      varchar(255),
    value_date                                           date,
    value_date_time                                      timestamp,
    bank_account_id                                      integer
        constraint fk_bank_account references bank_account,
    import_time                                          timestamp,
    credit_transaction_id                                integer
        constraint fk_credit_transaction references financial_transaction,
    debit_transaction_id                                 integer
        constraint fk_debit_transaction references financial_transaction,
    reset_in_id                                          integer
        constraint fk_reset_in_transation references nodrigen_transaction,
    ignored                                              boolean default false not null
);
alter table nodrigen_transaction owner to postgres;

create table page_version
(
    id           integer not null primary key,
    version_time timestamp,
    task_id      integer
        constraint fk_checker_task references checker_task
);
alter table page_version owner to postgres;

create table page_version_content
(
    page_version_id integer not null
        constraint fk_page_version references page_version,
    content         varchar(200000)
);
alter table page_version_content owner to postgres;

create table page_version_elements_added
(
    page_version_id integer not null
        constraint fk_page_version references page_version,
    elements_added  varchar(200000)
);
alter table page_version_elements_added owner to postgres;

create table page_version_elements_removed
(
    page_version_id  integer not null
        constraint fk_page_version references page_version,
    elements_removed varchar(200000)
);
alter table page_version_elements_removed owner to postgres;

create table piggy_bank
(
    id             integer        not null primary key,
    balance        numeric(19, 2) not null,
    currency       varchar(255)   not null,
    description    varchar(2000)  not null,
    monthly_top_up numeric(19, 2),
    name           varchar(255)   not null,
    savings        boolean default false,
    domain_id      integer
        constraint fk_domain references domain
);
alter table piggy_bank owner to postgres;

create table save_result_step_email_ccs
(
    id       integer      not null
        constraint fk_checker_step references checker_step,
    cc_email varchar(255),
    cc_name  varchar(255) not null,
    primary key (id, cc_name)
);
alter table save_result_step_email_ccs owner to postgres;

create table save_result_step_email_tos
(
    id       integer      not null
        constraint fk_checker_step references checker_step,
    to_email varchar(255),
    to_name  varchar(255) not null,
    primary key (id, to_name)
);
alter table save_result_step_email_tos owner to postgres;

create table service
(
    id        integer not null primary key,
    name      varchar(255),
    domain_id integer
        constraint fk_domain references domain
);
alter table service owner to postgres;

create table performed_service
(
    id         integer        not null primary key,
    currency   varchar(255)   not null,
    date       date           not null,
    price      numeric(19, 2) not null,
    client_id  integer        not null
        constraint fk_client references client,
    domain_id  integer        not null
        constraint fk_domain references domain,
    service_id integer        not null
        constraint fk_service references service
);
alter table performed_service owner to postgres;

create table performed_service_payment
(
    id                   integer        not null primary key,
    price                numeric(19, 2) not null,
    client_payment_id    integer        not null
        constraint fk_client_payment references client_payment,
    performed_service_id integer        not null
        constraint fk_performed_service references performed_service
);
alter table performed_service_payment owner to postgres;

create table syr
(
    dtype                      varchar(31)  not null,
    id                         integer      not null primary key,
    year                       varchar(255) not null,
    average                    integer,
    average_auxiliary_pioneers integer,
    average_bible_studies      integer,
    average_pioneers           integer,
    average_previous_year      integer,
    baptized                   integer,
    memorial_attendance        integer,
    number_of_congregations    integer,
    peak                       integer,
    percent_increase           integer,
    population                 integer,
    ratio1publisher_to         integer,
    total_hours                integer,
    number_of_countries        integer,
    country_id                 integer
        constraint fk_country references country
);
alter table syr owner to postgres;

create view nodrigen_transactions_to_import
            (id, domain_id, conversion_rate, credit, debit, description, time_of_transaction, destination_id, source_id,
             credit_bank_account_id, debit_bank_account_id, credit_nodrigen_transaction_id,
             debit_nodrigen_transaction_id, nodrigen_transaction_id)
as
WITH tranasactions AS (SELECT a.id,
                              a.name,
                              a.currency,
                              a.domain_id,
                              bp.institution_id,
                              nt.transaction_id,
                              nt.proprietary_bank_transaction_code,
                              nt.currency_exchange_rate,
                              nt.id AS nodrigen_transaction_id,
                              nt.currency_exchange_instructed_amount_amount,
                              nt.currency_exchange_instructed_amount_currency,
                              nt.currency_exchange_unit_currency,
                              nt.currency_exchange_source_currency,
                              nt.currency_exchange_target_currency,
                              CASE
                                  WHEN nt.transaction_amount_amount > 0::
        numeric THEN ba.id
        ELSE NULL :: integer
END
AS credit_bank_account_id,
                              CASE
                                  WHEN nt.transaction_amount_amount < 0::numeric THEN ba.id
                                  ELSE NULL::integer
END
AS debit_bank_account_id,
                              CASE
                                  WHEN nt.transaction_amount_amount > 0::numeric THEN nt.transaction_amount_amount
                                  ELSE 0::numeric
END
AS credit,
                              CASE
                                  WHEN nt.transaction_amount_amount > 0::numeric THEN 0::numeric
                                  ELSE - nt.transaction_amount_amount
END
AS debit,
                              (((COALESCE(nt.remittance_information_unstructured_array,
                                          nt.remittance_information_structured_array,
                                          nt.remittance_information_unstructured,
                                          nt.remittance_information_structured)::text || ': '::text) ||
                                COALESCE(nt.debtor_name, ''::character varying)::text) || ' => '::text) ||
                              COALESCE(nt.creditor_name, ''::character varying)::text                         AS description,
                              LEAST(COALESCE(nt.booking_date_time, nt.booking_date::timestamp without time zone),
                                    COALESCE(nt.value_date_time,
                                             nt.value_date::timestamp without time zone))                     AS time_of_transaction,
                              CASE
                                  WHEN nt.transaction_amount_amount > 0::numeric THEN a.id
                                  ELSE NULL::integer
END
AS destination_id,
                              CASE
                                  WHEN nt.transaction_amount_amount < 0::numeric THEN a.id
                                  ELSE NULL::integer
END
AS source_id
                       FROM nodrigen_transaction nt
                                JOIN bank_account ba ON nt.bank_account_id = ba.id
                                JOIN bank_permission bp ON ba.bank_permission_id = bp.id
                                JOIN account a ON ba.id = a.bank_account_id
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
       tranasactions.destination_id,
       tranasactions.source_id,
       tranasactions.credit_bank_account_id,
       tranasactions.debit_bank_account_id,
       CASE
           WHEN tranasactions.credit > 0::numeric THEN tranasactions.nodrigen_transaction_id
           ELSE NULL::integer
END
AS credit_nodrigen_transaction_id,
       CASE
           WHEN tranasactions.debit > 0::numeric THEN tranasactions.nodrigen_transaction_id
           ELSE NULL::integer
END
AS debit_nodrigen_transaction_id,
       tranasactions.nodrigen_transaction_id
FROM tranasactions
WHERE tranasactions.institution_id::text <> 'REVOLUT_REVOGB21'::text
   OR tranasactions.proprietary_bank_transaction_code::text <> 'EXCHANGE'::text
UNION
SELECT nextval('nodrigen_transactions_to_import_sequence'::regclass) AS id,
       nt_debit.domain_id,
       (nt_credit.credit / nt_debit.currency_exchange_instructed_amount_amount)::numeric(16, 12) AS conversion_rate, nt_credit.credit,
       nt_debit.currency_exchange_instructed_amount_amount           AS debit,
       nt_debit.description,
       nt_debit.time_of_transaction,
       nt_credit.destination_id,
       nt_debit.source_id,
       nt_debit.credit_bank_account_id,
       nt_debit.debit_bank_account_id,
       nt_credit.nodrigen_transaction_id                             AS credit_nodrigen_transaction_id,
       nt_debit.nodrigen_transaction_id                              AS debit_nodrigen_transaction_id,
       NULL::integer                                                                             AS nodrigen_transaction_id
FROM tranasactions nt_debit
         JOIN tranasactions nt_credit ON nt_debit.transaction_id::text = nt_credit.transaction_id::text
WHERE nt_debit.institution_id::text = 'REVOLUT_REVOGB21'::text
  AND nt_debit.proprietary_bank_transaction_code::text = 'EXCHANGE'::text
  AND nt_credit.institution_id::text = 'REVOLUT_REVOGB21'::text
  AND nt_credit.proprietary_bank_transaction_code::text = 'EXCHANGE'::text
  AND nt_credit.domain_id = nt_debit.domain_id
  AND nt_credit.debit = 0:: numeric
  AND nt_debit.credit = 0:: numeric
UNION
SELECT nextval('nodrigen_transactions_to_import_sequence'::regclass)        AS id,
       nt_debit.domain_id,
       1.0                                                                  AS conversion_rate,
       0.0                                                                  AS credit,
       nt_debit.debit - nt_debit.currency_exchange_instructed_amount_amount AS debit,
       'prowizja za wymianę '::text || nt_debit.nodrigen_transaction_id     AS description, nt_debit.time_of_transaction,
       NULL::integer                                                        AS destination_id, nt_debit.source_id,
       NULL::integer                                                        AS credit_bank_account_id, nt_debit.credit_bank_account_id AS debit_bank_account_id,
       NULL::integer                                                        AS credit_nodrigen_transaction_id, nt_debit.credit_bank_account_id AS debit_nodrigen_transaction_id,
       NULL::integer                                                        AS nodrigen_transaction_id
FROM tranasactions nt_debit
         JOIN tranasactions nt_credit ON nt_debit.transaction_id::text = nt_credit.transaction_id::text
WHERE nt_debit.institution_id::text = 'REVOLUT_REVOGB21'::text
  AND nt_debit.proprietary_bank_transaction_code::text = 'EXCHANGE'::text
  AND nt_credit.institution_id::text = 'REVOLUT_REVOGB21'::text
  AND nt_credit.proprietary_bank_transaction_code::text = 'EXCHANGE'::text
  AND nt_credit.domain_id = nt_debit.domain_id
  AND nt_credit.debit = 0:: numeric
  AND nt_debit.credit = 0:: numeric
  AND (nt_debit.debit - nt_debit.currency_exchange_instructed_amount_amount)
    > 0:: numeric;

alter table nodrigen_transactions_to_import
    owner to postgres;