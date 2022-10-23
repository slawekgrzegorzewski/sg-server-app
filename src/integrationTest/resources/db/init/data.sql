SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

CREATE SEQUENCE public.hibernate_sequence
    START WITH 182
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.hibernate_sequence OWNER TO postgres;

CREATE SEQUENCE public.nodrigen_transactions_to_import_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
    CYCLE;
ALTER TABLE public.nodrigen_transactions_to_import_sequence OWNER TO postgres;

SELECT pg_catalog.setval('public.hibernate_sequence', 58613, true);
SELECT pg_catalog.setval('public.nodrigen_transactions_to_import_sequence', 390552, true);

CREATE TABLE public.account (
    id integer NOT NULL,
    currency character varying(255) NOT NULL,
    current_balance numeric(19,2) DEFAULT 0 NOT NULL,
    name character varying(255) NOT NULL,
    last_transaction_included_in_balance_id integer,
    domain_id integer,
    visible boolean DEFAULT true NOT NULL,
    bank_account_id integer
);
ALTER TABLE public.account OWNER TO postgres;

CREATE TABLE public.accountant_settings (
    id integer NOT NULL,
    is_company boolean NOT NULL,
    domain_id integer
);
ALTER TABLE public.accountant_settings OWNER TO postgres;

CREATE TABLE public.application_user (
    id integer NOT NULL,
    email character varying(255),
    first_name character varying(255),
    is_using2fa boolean DEFAULT false NOT NULL,
    last_name character varying(255),
    login character varying(255),
    password character varying(255),
    secret character varying(255),
    default_domain_id integer
);
ALTER TABLE public.application_user OWNER TO postgres;

CREATE TABLE public.application_user_domain_relation (
    access_level character varying(255),
    application_user_id integer NOT NULL,
    domain_id integer NOT NULL
);
ALTER TABLE public.application_user_domain_relation OWNER TO postgres;

CREATE TABLE public.application_user_roles (
    application_user_id integer NOT NULL,
    roles character varying(255)
);
ALTER TABLE public.application_user_roles OWNER TO postgres;

CREATE TABLE public.bank_account (
    id integer NOT NULL,
    bic character varying(255),
    currency character varying(255) NOT NULL,
    external_id character varying(255) NOT NULL,
    iban character varying(255) NOT NULL,
    owner character varying(255) NOT NULL,
    product character varying(255),
    bank_permission_id integer,
    domain_id integer
);
ALTER TABLE public.bank_account OWNER TO postgres;

CREATE TABLE public.bank_permission (
    dtype character varying(31) NOT NULL,
    id integer NOT NULL,
    created_at timestamp without time zone,
    given_at timestamp without time zone,
    withdrawn_at timestamp without time zone,
    confirmation_link character varying(255),
    institution_id character varying(255),
    reference text,
    requisition_id uuid,
    ssn text,
    domain_id integer
);
ALTER TABLE public.bank_permission OWNER TO postgres;

CREATE TABLE public.billing_period (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    period character varying(255) NOT NULL,
    domain_id integer
);
ALTER TABLE public.billing_period OWNER TO postgres;

CREATE TABLE public.category (
    id integer NOT NULL,
    description character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    domain_id integer
);
ALTER TABLE public.category OWNER TO postgres;

CREATE TABLE public.checker_step (
    dtype character varying(31) NOT NULL,
    id integer NOT NULL,
    description character varying(1000),
    name character varying(1000),
    step_order integer NOT NULL,
    attribute character varying(255),
    attribute_variable_name_in_expression character varying(255),
    css_query character varying(1000),
    operation_expression character varying(255),
    page_content_variable character varying(255),
    result_variable character varying(255),
    element_variable_name_in_expression character varying(255),
    elements_variable character varying(255),
    filter_expression character varying(255),
    page_url_expression character varying(1000),
    variable_name character varying(255),
    element_index integer,
    email_header character varying(255),
    email_title character varying(255),
    result_variable_name character varying(255),
    send_email_notification boolean,
    get_result_from_variable character varying(255)
);
ALTER TABLE public.checker_step OWNER TO postgres;

CREATE TABLE public.checker_task (
    id integer NOT NULL,
    description character varying(255),
    "interval" bigint,
    name character varying(255),
    next_run timestamp without time zone,
    for_user_id integer
);
ALTER TABLE public.checker_task OWNER TO postgres;

CREATE TABLE public.checker_task_history (
    id integer NOT NULL,
    messages character varying(10000),
    result character varying(255),
    run_time timestamp without time zone,
    task_id integer
);
ALTER TABLE public.checker_task_history OWNER TO postgres;

CREATE TABLE public.checker_task_steps (
    checker_task_id integer NOT NULL,
    steps_id integer NOT NULL
);
ALTER TABLE public.checker_task_steps OWNER TO postgres;

CREATE TABLE public.client (
    id integer NOT NULL,
    name character varying(255),
    domain_id integer
);
ALTER TABLE public.client OWNER TO postgres;

CREATE TABLE public.client_payment (
    id integer NOT NULL,
    bill_of_sale boolean NOT NULL,
    bill_of_sale_as_invoice boolean NOT NULL,
    currency character varying(255) NOT NULL,
    date date NOT NULL,
    invoice boolean NOT NULL,
    price numeric(19,2) NOT NULL,
    client_id integer NOT NULL,
    domain_id integer NOT NULL,
    not_registered boolean DEFAULT false NOT NULL
);
ALTER TABLE public.client_payment OWNER TO postgres;

CREATE TABLE public.country (
    id integer NOT NULL
);
ALTER TABLE public.country OWNER TO postgres;

CREATE TABLE public.country_names (
    country_id integer NOT NULL,
    names character varying(255)
);
ALTER TABLE public.country_names OWNER TO postgres;

CREATE TABLE public.cube_record (
    id integer NOT NULL,
    cubes_type character varying(255),
    record_time timestamp without time zone,
    scramble character varying(255),
    "time" bigint,
    domain_id integer
);
ALTER TABLE public.cube_record OWNER TO postgres;

CREATE TABLE public.domain (
    id integer NOT NULL,
    name character varying(255)
);
ALTER TABLE public.domain OWNER TO postgres;

CREATE TABLE public.domain_invitation (
    application_user_id integer NOT NULL,
    domain_id integer NOT NULL
);
ALTER TABLE public.domain_invitation OWNER TO postgres;

CREATE TABLE public.expense (
    id integer NOT NULL,
    amount numeric(19,2) NOT NULL,
    currency character varying(255) NOT NULL,
    description character varying(2000) NOT NULL,
    expense_date date,
    billing_period_id integer,
    category_id integer NOT NULL
);
ALTER TABLE public.expense OWNER TO postgres;

CREATE TABLE public.financial_transaction (
    id integer NOT NULL,
    conversion_rate numeric(19,2),
    credit numeric(19,2),
    debit numeric(19,2),
    description character varying(2000),
    time_of_transaction timestamp without time zone,
    destination_id integer,
    source_id integer,
    fee numeric(19,2),
    other_fee numeric(19,2),
    fee_currency character varying(255),
    other_fee_currency character varying(255)
);
ALTER TABLE public.financial_transaction OWNER TO postgres;

CREATE TABLE public.holiday_currencies (
    id integer NOT NULL,
    euro_conversion_rate numeric(19,6),
    kuna_conversion_rate numeric(19,6),
    domain_id integer
);
ALTER TABLE public.holiday_currencies OWNER TO postgres;

CREATE TABLE public.income (
    id integer NOT NULL,
    amount numeric(19,2) NOT NULL,
    currency character varying(255) NOT NULL,
    description character varying(2000) NOT NULL,
    income_date date,
    billing_period_id integer,
    category_id integer NOT NULL
);
ALTER TABLE public.income OWNER TO postgres;

CREATE TABLE public.month_summary (
    id integer NOT NULL,
    data character varying(10000),
    billing_period_id integer
);
ALTER TABLE public.month_summary OWNER TO postgres;

CREATE TABLE public.nodrigen_access (
    id integer NOT NULL,
    access_expires bigint,
    access_expires_at timestamp without time zone,
    access_token text,
    archived_at timestamp without time zone,
    refresh_expires bigint,
    refresh_expires_at timestamp without time zone,
    refresh_token text
);
ALTER TABLE public.nodrigen_access OWNER TO postgres;

CREATE TABLE public.nodrigen_bank_account_balance (
    id integer NOT NULL,
    balance_amount_amount numeric(19,2),
    balance_amount_currency character varying(255),
    balance_type character varying(255),
    credit_limit_included boolean,
    fetch_time timestamp without time zone,
    last_change_date_time timestamp without time zone,
    last_committed_transaction character varying(255),
    reference_date date,
    bank_account_id integer
);
ALTER TABLE public.nodrigen_bank_account_balance OWNER TO postgres;

CREATE TABLE public.nodrigen_transaction (
    id integer NOT NULL,
    additional_information character varying(255),
    additional_information_structured character varying(255),
    balance_after_transaction_amount numeric(19,2),
    balance_after_transaction_currency character varying(255),
    balance_after_transaction_balance_type character varying(255),
    balance_after_transaction_credit_limit_included boolean,
    balance_after_transaction_last_change_date_time timestamp without time zone,
    balance_after_transaction_last_committed_transaction character varying(255),
    balance_after_transaction_reference_date date,
    bank_transaction_code character varying(255),
    booking_date date,
    booking_date_time timestamp without time zone,
    check_id character varying(255),
    creditor_account_bban character varying(255),
    creditor_account_iban character varying(255),
    creditor_agent character varying(255),
    creditor_id character varying(255),
    creditor_name character varying(255),
    currency_exchange_rate numeric(19,2),
    currency_exchange_instructed_amount_amount numeric(19,2),
    currency_exchange_instructed_amount_currency character varying(255),
    currency_exchange_source_currency character varying(255),
    currency_exchange_target_currency character varying(255),
    currency_exchange_unit_currency character varying(255),
    debtor_account_bban character varying(255),
    debtor_account_iban character varying(255),
    debtor_agent character varying(255),
    debtor_name character varying(255),
    entry_reference character varying(255),
    mandate_id character varying(255),
    phase character varying(255),
    proprietary_bank_transaction_code character varying(255),
    purpose_code character varying(255),
    remittance_information_structured character varying(255),
    remittance_information_structured_array character varying(255),
    remittance_information_unstructured character varying(255),
    remittance_information_unstructured_array character varying(255),
    transaction_amount_amount numeric(19,2),
    transaction_amount_currency character varying(255),
    transaction_id character varying(255),
    ultimate_creditor character varying(255),
    ultimate_debtor character varying(255),
    value_date date,
    value_date_time timestamp without time zone,
    bank_account_id integer,
    import_time timestamp without time zone,
    credit_transaction_id integer,
    debit_transaction_id integer,
    reset_in_id integer,
    ignored boolean DEFAULT false NOT NULL
);
ALTER TABLE public.nodrigen_transaction OWNER TO postgres;

CREATE VIEW public.nodrigen_transactions_to_import AS
 WITH tranasactions AS (
         SELECT a.id,
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
                    WHEN (nt.transaction_amount_amount > (0)::numeric) THEN ba.id
                    ELSE NULL::integer
                END AS credit_bank_account_id,
                CASE
                    WHEN (nt.transaction_amount_amount < (0)::numeric) THEN ba.id
                    ELSE NULL::integer
                END AS debit_bank_account_id,
                CASE
                    WHEN (nt.transaction_amount_amount > (0)::numeric) THEN nt.transaction_amount_amount
                    ELSE (0)::numeric
                END AS credit,
                CASE
                    WHEN (nt.transaction_amount_amount > (0)::numeric) THEN (0)::numeric
                    ELSE (- nt.transaction_amount_amount)
                END AS debit,
            (((((COALESCE(nt.remittance_information_unstructured_array, nt.remittance_information_structured_array, nt.remittance_information_unstructured, nt.remittance_information_structured))::text || ': '::text) || (COALESCE(nt.debtor_name, ''::character varying))::text) || ' => '::text) || (COALESCE(nt.creditor_name, ''::character varying))::text) AS description,
            LEAST(COALESCE(nt.booking_date_time, (nt.booking_date)::timestamp without time zone), COALESCE(nt.value_date_time, (nt.value_date)::timestamp without time zone)) AS time_of_transaction,
                CASE
                    WHEN (nt.transaction_amount_amount > (0)::numeric) THEN a.id
                    ELSE NULL::integer
                END AS destination_id,
                CASE
                    WHEN (nt.transaction_amount_amount < (0)::numeric) THEN a.id
                    ELSE NULL::integer
                END AS source_id
           FROM (((public.nodrigen_transaction nt
             JOIN public.bank_account ba ON ((nt.bank_account_id = ba.id)))
             JOIN public.bank_permission bp ON ((ba.bank_permission_id = bp.id)))
             JOIN public.account a ON ((ba.id = a.bank_account_id)))
          WHERE ((nt.credit_transaction_id IS NULL) AND (nt.debit_transaction_id IS NULL) AND (nt.reset_in_id IS NULL) AND (NOT nt.ignored))
          ORDER BY a.id, LEAST(COALESCE(nt.booking_date_time, (nt.booking_date)::timestamp without time zone), COALESCE(nt.value_date_time, (nt.value_date)::timestamp without time zone)) DESC
        )
 SELECT nextval('public.nodrigen_transactions_to_import_sequence'::regclass) AS id,
    tranasactions.domain_id,
    1.0 AS conversion_rate,
    tranasactions.credit,
    tranasactions.debit,
    tranasactions.description,
    tranasactions.time_of_transaction,
    tranasactions.destination_id,
    tranasactions.source_id,
    tranasactions.credit_bank_account_id,
    tranasactions.debit_bank_account_id,
        CASE
            WHEN (tranasactions.credit > (0)::numeric) THEN tranasactions.nodrigen_transaction_id
            ELSE NULL::integer
        END AS credit_nodrigen_transaction_id,
        CASE
            WHEN (tranasactions.debit > (0)::numeric) THEN tranasactions.nodrigen_transaction_id
            ELSE NULL::integer
        END AS debit_nodrigen_transaction_id,
    tranasactions.nodrigen_transaction_id
   FROM tranasactions
  WHERE (((tranasactions.institution_id)::text <> 'REVOLUT_REVOGB21'::text) OR ((tranasactions.proprietary_bank_transaction_code)::text <> 'EXCHANGE'::text))
UNION
 SELECT nextval('public.nodrigen_transactions_to_import_sequence'::regclass) AS id,
    nt_debit.domain_id,
    ((nt_credit.credit / nt_debit.currency_exchange_instructed_amount_amount))::numeric(16,12) AS conversion_rate,
    nt_credit.credit,
    nt_debit.currency_exchange_instructed_amount_amount AS debit,
    nt_debit.description,
    nt_debit.time_of_transaction,
    nt_credit.destination_id,
    nt_debit.source_id,
    nt_debit.credit_bank_account_id,
    nt_debit.debit_bank_account_id,
    nt_credit.nodrigen_transaction_id AS credit_nodrigen_transaction_id,
    nt_debit.nodrigen_transaction_id AS debit_nodrigen_transaction_id,
    NULL::integer AS nodrigen_transaction_id
   FROM (tranasactions nt_debit
     JOIN tranasactions nt_credit ON (((nt_debit.transaction_id)::text = (nt_credit.transaction_id)::text)))
  WHERE (((nt_debit.institution_id)::text = 'REVOLUT_REVOGB21'::text) AND ((nt_debit.proprietary_bank_transaction_code)::text = 'EXCHANGE'::text) AND ((nt_credit.institution_id)::text = 'REVOLUT_REVOGB21'::text) AND ((nt_credit.proprietary_bank_transaction_code)::text = 'EXCHANGE'::text) AND (nt_credit.domain_id = nt_debit.domain_id) AND (nt_credit.debit = (0)::numeric) AND (nt_debit.credit = (0)::numeric))
UNION
 SELECT nextval('public.nodrigen_transactions_to_import_sequence'::regclass) AS id,
    nt_debit.domain_id,
    1.0 AS conversion_rate,
    0.0 AS credit,
    (nt_debit.debit - nt_debit.currency_exchange_instructed_amount_amount) AS debit,
    ('prowizja za wymianę '::text || nt_debit.nodrigen_transaction_id) AS description,
    nt_debit.time_of_transaction,
    NULL::integer AS destination_id,
    nt_debit.source_id,
    NULL::integer AS credit_bank_account_id,
    nt_debit.credit_bank_account_id AS debit_bank_account_id,
    NULL::integer AS credit_nodrigen_transaction_id,
    nt_debit.credit_bank_account_id AS debit_nodrigen_transaction_id,
    NULL::integer AS nodrigen_transaction_id
   FROM (tranasactions nt_debit
     JOIN tranasactions nt_credit ON (((nt_debit.transaction_id)::text = (nt_credit.transaction_id)::text)))
  WHERE (((nt_debit.institution_id)::text = 'REVOLUT_REVOGB21'::text) AND ((nt_debit.proprietary_bank_transaction_code)::text = 'EXCHANGE'::text) AND ((nt_credit.institution_id)::text = 'REVOLUT_REVOGB21'::text) AND ((nt_credit.proprietary_bank_transaction_code)::text = 'EXCHANGE'::text) AND (nt_credit.domain_id = nt_debit.domain_id) AND (nt_credit.debit = (0)::numeric) AND (nt_debit.credit = (0)::numeric) AND ((nt_debit.debit - nt_debit.currency_exchange_instructed_amount_amount) > (0)::numeric));
ALTER TABLE public.nodrigen_transactions_to_import OWNER TO postgres;

CREATE TABLE public.page_version (
    id integer NOT NULL,
    version_time timestamp without time zone,
    task_id integer
);
ALTER TABLE public.page_version OWNER TO postgres;

CREATE TABLE public.page_version_content (
    page_version_id integer NOT NULL,
    content character varying(200000)
);
ALTER TABLE public.page_version_content OWNER TO postgres;

CREATE TABLE public.page_version_elements_added (
    page_version_id integer NOT NULL,
    elements_added character varying(200000)
);
ALTER TABLE public.page_version_elements_added OWNER TO postgres;

CREATE TABLE public.page_version_elements_removed (
    page_version_id integer NOT NULL,
    elements_removed character varying(200000)
);
ALTER TABLE public.page_version_elements_removed OWNER TO postgres;

CREATE TABLE public.performed_service (
    id integer NOT NULL,
    currency character varying(255) NOT NULL,
    date date NOT NULL,
    price numeric(19,2) NOT NULL,
    client_id integer NOT NULL,
    domain_id integer NOT NULL,
    service_id integer NOT NULL
);
ALTER TABLE public.performed_service OWNER TO postgres;

CREATE TABLE public.performed_service_payment (
    id integer NOT NULL,
    price numeric(19,2) NOT NULL,
    client_payment_id integer NOT NULL,
    performed_service_id integer NOT NULL
);
ALTER TABLE public.performed_service_payment OWNER TO postgres;

CREATE TABLE public.piggy_bank (
    id integer NOT NULL,
    balance numeric(19,2) NOT NULL,
    currency character varying(255) NOT NULL,
    description character varying(2000) NOT NULL,
    monthly_top_up numeric(19,2),
    name character varying(255) NOT NULL,
    savings boolean DEFAULT false,
    domain_id integer
);
ALTER TABLE public.piggy_bank OWNER TO postgres;

CREATE TABLE public.save_result_step_email_ccs (
    id integer NOT NULL,
    cc_email character varying(255),
    cc_name character varying(255) NOT NULL
);
ALTER TABLE public.save_result_step_email_ccs OWNER TO postgres;

CREATE TABLE public.save_result_step_email_tos (
    id integer NOT NULL,
    to_email character varying(255),
    to_name character varying(255) NOT NULL
);
ALTER TABLE public.save_result_step_email_tos OWNER TO postgres;

CREATE TABLE public.service (
    id integer NOT NULL,
    name character varying(255),
    domain_id integer
);
ALTER TABLE public.service OWNER TO postgres;

CREATE TABLE public.syr (
    dtype character varying(31) NOT NULL,
    id integer NOT NULL,
    year character varying(255) NOT NULL,
    average integer,
    average_auxiliary_pioneers integer,
    average_bible_studies integer,
    average_pioneers integer,
    average_previous_year integer,
    baptized integer,
    memorial_attendance integer,
    number_of_congregations integer,
    peak integer,
    percent_increase integer,
    population integer,
    ratio1publisher_to integer,
    total_hours integer,
    number_of_countries integer,
    country_id integer
);
ALTER TABLE public.syr OWNER TO postgres;

INSERT INTO public.domain (id, name) VALUES (1, 'Test domain');

INSERT INTO public.accountant_settings (id, is_company, domain_id) VALUES (4021, false, 1);

INSERT INTO public.application_user (id, email, first_name, is_using2fa, last_name, login, password, secret, default_domain_id)
    VALUES (1, NULL, NULL, true, NULL, 'slag', '$2a$11$dEjUdcJg39/ecqIWdKG8Ou4nJnna8M8F62Uk5FAWhFxX5kwzkWJMu', 'LRWWUMSQGJFWCUKQ', 1);

INSERT INTO public.application_user_domain_relation (access_level, application_user_id, domain_id) VALUES ('ADMIN', 1, 1);

INSERT INTO public.application_user_roles (application_user_id, roles) VALUES (1, 'ACCOUNTANT_ADMIN');
INSERT INTO public.application_user_roles (application_user_id, roles) VALUES (1, 'ACCOUNTANT_USER');
INSERT INTO public.application_user_roles (application_user_id, roles) VALUES (1, 'CHECKER_ADMIN');
INSERT INTO public.application_user_roles (application_user_id, roles) VALUES (1, 'CHECKER_USER');
INSERT INTO public.application_user_roles (application_user_id, roles) VALUES (1, 'SYR_USER');
INSERT INTO public.application_user_roles (application_user_id, roles) VALUES (1, 'SYR_ADMIN');
INSERT INTO public.application_user_roles (application_user_id, roles) VALUES (1, 'CUBES');
INSERT INTO public.application_user_roles (application_user_id, roles) VALUES (1, 'IPR');

ALTER TABLE ONLY public.account ADD CONSTRAINT account_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.accountant_settings ADD CONSTRAINT accountant_settings_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.application_user_domain_relation ADD CONSTRAINT application_user_domain_relation_pkey PRIMARY KEY (application_user_id, domain_id);
ALTER TABLE ONLY public.application_user ADD CONSTRAINT application_user_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.bank_account ADD CONSTRAINT bank_account_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.bank_permission ADD CONSTRAINT bank_permission_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.billing_period ADD CONSTRAINT billing_period_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.category ADD CONSTRAINT category_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.checker_step ADD CONSTRAINT checker_step_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.checker_task_history ADD CONSTRAINT checker_task_history_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.checker_task ADD CONSTRAINT checker_task_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.client_payment ADD CONSTRAINT client_payment_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.client ADD CONSTRAINT client_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.country ADD CONSTRAINT country_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.cube_record ADD CONSTRAINT cube_record_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.domain_invitation ADD CONSTRAINT domain_invitation_pkey PRIMARY KEY (application_user_id, domain_id);
ALTER TABLE ONLY public.domain ADD CONSTRAINT domain_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.expense ADD CONSTRAINT expense_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.financial_transaction ADD CONSTRAINT financial_transaction_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.holiday_currencies ADD CONSTRAINT holiday_currencies_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.income ADD CONSTRAINT income_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.month_summary ADD CONSTRAINT month_summary_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.nodrigen_access ADD CONSTRAINT nodrigen_access_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.nodrigen_bank_account_balance ADD CONSTRAINT nodrigen_bank_account_balance_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.nodrigen_transaction ADD CONSTRAINT nodrigen_transaction_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.page_version ADD CONSTRAINT page_version_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.performed_service_payment ADD CONSTRAINT performed_service_payment_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.performed_service ADD CONSTRAINT performed_service_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.piggy_bank ADD CONSTRAINT piggy_bank_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.save_result_step_email_ccs ADD CONSTRAINT save_result_step_email_ccs_pkey PRIMARY KEY (id, cc_name);
ALTER TABLE ONLY public.save_result_step_email_tos ADD CONSTRAINT save_result_step_email_tos_pkey PRIMARY KEY (id, to_name);
ALTER TABLE ONLY public.service ADD CONSTRAINT service_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.syr ADD CONSTRAINT syr_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.checker_task_steps ADD CONSTRAINT uk_7q5bgtqsco1vt3aso3h7matk1 UNIQUE (steps_id);
ALTER TABLE ONLY public.nodrigen_transaction ADD CONSTRAINT fk1pe8sbpm7crybdqvmfmy5fn6h FOREIGN KEY (reset_in_id) REFERENCES public.nodrigen_transaction(id);
ALTER TABLE ONLY public.bank_account ADD CONSTRAINT fk1q9r1njydqnf2w768cxedblxa FOREIGN KEY (bank_permission_id) REFERENCES public.bank_permission(id);
ALTER TABLE ONLY public.checker_task ADD CONSTRAINT fk1wntwhme0o9xrj008g6bq49x5 FOREIGN KEY (for_user_id) REFERENCES public.application_user(id);
ALTER TABLE ONLY public.checker_task_history ADD CONSTRAINT fk25ppus0ixljdcpfl0yin1vatk FOREIGN KEY (task_id) REFERENCES public.checker_task(id);
ALTER TABLE ONLY public.performed_service_payment ADD CONSTRAINT fk29s6judviu89dxhbehuj2yapc FOREIGN KEY (client_payment_id) REFERENCES public.client_payment(id);
ALTER TABLE ONLY public.save_result_step_email_tos ADD CONSTRAINT fk2wol8ks8e3emm96buwu67uc6v FOREIGN KEY (id) REFERENCES public.checker_step(id);
ALTER TABLE ONLY public.performed_service_payment ADD CONSTRAINT fk33dt3gj1au0vcri13qcqqmg5h FOREIGN KEY (performed_service_id) REFERENCES public.performed_service(id);
ALTER TABLE ONLY public.income ADD CONSTRAINT fk41cabgmjxhgcbeeo08po7o1h4 FOREIGN KEY (category_id) REFERENCES public.category(id);
ALTER TABLE ONLY public.performed_service ADD CONSTRAINT fk4gdiwy3i1pixe35k2jixwnffu FOREIGN KEY (domain_id) REFERENCES public.domain(id);
ALTER TABLE ONLY public.page_version ADD CONSTRAINT fk67ksfr14v97yp7e3rv446wbko FOREIGN KEY (task_id) REFERENCES public.checker_task(id);
ALTER TABLE ONLY public.page_version_elements_added ADD CONSTRAINT fk77yj76tbbgo3ywjmtgbyy1aw3 FOREIGN KEY (page_version_id) REFERENCES public.page_version(id);
ALTER TABLE ONLY public.application_user ADD CONSTRAINT fk7ngnopg7kr4p0lqrie36byfq8 FOREIGN KEY (default_domain_id) REFERENCES public.domain(id);
ALTER TABLE ONLY public.application_user_domain_relation ADD CONSTRAINT fk85wkpgv3xb7dorgp2s06makrm FOREIGN KEY (domain_id) REFERENCES public.domain(id);
ALTER TABLE ONLY public.application_user_roles ADD CONSTRAINT fk9hwva08h4u671cqxpexx1dx7i FOREIGN KEY (application_user_id) REFERENCES public.application_user(id);
ALTER TABLE ONLY public.account ADD CONSTRAINT fkacrf19p8otsuo5e2q64ok1c4p FOREIGN KEY (last_transaction_included_in_balance_id) REFERENCES public.financial_transaction(id);
ALTER TABLE ONLY public.domain_invitation ADD CONSTRAINT fkc7cydfelo8sxt60xbf3f3udjk FOREIGN KEY (application_user_id) REFERENCES public.application_user(id);
ALTER TABLE ONLY public.performed_service ADD CONSTRAINT fkcfquuqeu34s9oo5s482lpelco FOREIGN KEY (service_id) REFERENCES public.service(id);
ALTER TABLE ONLY public.client ADD CONSTRAINT fkclc61ej5u409l3h8gqelqsj33 FOREIGN KEY (domain_id) REFERENCES public.domain(id);ALTER TABLE ONLY public.billing_period ADD CONSTRAINT fkcw4sfk2uuxuid0pxf0vjidybf FOREIGN KEY (domain_id) REFERENCES public.domain(id);
ALTER TABLE ONLY public.syr ADD CONSTRAINT fkf6cbt3nnwll9s8jydaqclwnwv FOREIGN KEY (country_id) REFERENCES public.country(id);ALTER TABLE ONLY public.client_payment ADD CONSTRAINT fkflno7r2uunhjyjew4ovb6jqu5 FOREIGN KEY (client_id) REFERENCES public.client(id);
ALTER TABLE ONLY public.nodrigen_bank_account_balance ADD CONSTRAINT fkfuuqffminycl3ojnc1mk0dj5h FOREIGN KEY (bank_account_id) REFERENCES public.bank_account(id);ALTER TABLE ONLY public.cube_record ADD CONSTRAINT fkg1595gvfqkpnqcnlc2fsa5s4o FOREIGN KEY (domain_id) REFERENCES public.domain(id);
ALTER TABLE ONLY public.domain_invitation ADD CONSTRAINT fkgda8f8uo69u32ao7plsnuhfqg FOREIGN KEY (domain_id) REFERENCES public.domain(id);
ALTER TABLE ONLY public.checker_task_steps ADD CONSTRAINT fkgr2kcn6xnp1maq4ocl4sc5h8g FOREIGN KEY (checker_task_id) REFERENCES public.checker_task(id);
ALTER TABLE ONLY public.accountant_settings ADD CONSTRAINT fkhod25qlto4g97h2n90ebpsosu FOREIGN KEY (domain_id) REFERENCES public.domain(id);
ALTER TABLE ONLY public.nodrigen_transaction ADD CONSTRAINT fki4lhmct0u01jm8vwev8dnibw3 FOREIGN KEY (debit_transaction_id) REFERENCES public.financial_transaction(id);
ALTER TABLE ONLY public.page_version_elements_removed ADD CONSTRAINT fkie9o0364mmgvb4ip4d6ogaod4 FOREIGN KEY (page_version_id) REFERENCES public.page_version(id);
ALTER TABLE ONLY public.client_payment ADD CONSTRAINT fkj4m4smholepu02yxh4y8jdokn FOREIGN KEY (domain_id) REFERENCES public.domain(id);
ALTER TABLE ONLY public.performed_service ADD CONSTRAINT fkkf1sbbbj3xvar33855fasaswg FOREIGN KEY (client_id) REFERENCES public.client(id);
ALTER TABLE ONLY public.service ADD CONSTRAINT fkkkssml48w521ncsm4alfsnnur FOREIGN KEY (domain_id) REFERENCES public.domain(id);
ALTER TABLE ONLY public.bank_account ADD CONSTRAINT fkkl5s0jstikog427o1k6a7avbt FOREIGN KEY (domain_id) REFERENCES public.domain(id);
ALTER TABLE ONLY public.nodrigen_transaction ADD CONSTRAINT fkl2p16b6ig9hf9rektnsupb8up FOREIGN KEY (credit_transaction_id) REFERENCES public.financial_transaction(id);
ALTER TABLE ONLY public.piggy_bank ADD CONSTRAINT fkl5940qn78dv0dvhxix1imx37h FOREIGN KEY (domain_id) REFERENCES public.domain(id);
ALTER TABLE ONLY public.month_summary ADD CONSTRAINT fkm3eg27tdiygoimnaobsbrbhwu FOREIGN KEY (billing_period_id) REFERENCES public.billing_period(id);
ALTER TABLE ONLY public.income ADD CONSTRAINT fkm48ays11nvebfxcrf59vg6ovf FOREIGN KEY (billing_period_id) REFERENCES public.billing_period(id);
ALTER TABLE ONLY public.category ADD CONSTRAINT fkm8kqjvj9rkgqkodggplaki3n2 FOREIGN KEY (domain_id) REFERENCES public.domain(id);
ALTER TABLE ONLY public.expense ADD CONSTRAINT fkmvjm59reb5i075vu38bwcaqj6 FOREIGN KEY (category_id) REFERENCES public.category(id);
ALTER TABLE ONLY public.bank_permission ADD CONSTRAINT fkn7ullt3c84ndnrou8a4l0vmjb FOREIGN KEY (domain_id) REFERENCES public.domain(id);
ALTER TABLE ONLY public.country_names ADD CONSTRAINT fknbr8beurtp5qsm823msvkpsw4 FOREIGN KEY (country_id) REFERENCES public.country(id);
ALTER TABLE ONLY public.financial_transaction ADD CONSTRAINT fknxt96yfdqhi0pf9cj3igaaga FOREIGN KEY (destination_id) REFERENCES public.account(id);
ALTER TABLE ONLY public.account ADD CONSTRAINT fkoxdtjwjuekbmw2kaclsgpbxnb FOREIGN KEY (bank_account_id) REFERENCES public.bank_account(id);
ALTER TABLE ONLY public.application_user_domain_relation ADD CONSTRAINT fkoygx1tflnxcsg5py1awdftbu4 FOREIGN KEY (application_user_id) REFERENCES public.application_user(id);
ALTER TABLE ONLY public.nodrigen_transaction ADD CONSTRAINT fkp0bppx0mbbqpsyr1y6onswyeu FOREIGN KEY (bank_account_id) REFERENCES public.bank_account(id);
ALTER TABLE ONLY public.save_result_step_email_ccs ADD CONSTRAINT fkpsi7qwll5r3pu0iqbbc2593op FOREIGN KEY (id) REFERENCES public.checker_step(id);
ALTER TABLE ONLY public.account ADD CONSTRAINT fkpy61tgfe9h2a1r5pn6i6fy5j5 FOREIGN KEY (domain_id) REFERENCES public.domain(id);
ALTER TABLE ONLY public.financial_transaction ADD CONSTRAINT fkqs8esnsig9op4m18exed9uexe FOREIGN KEY (source_id) REFERENCES public.account(id);
ALTER TABLE ONLY public.expense ADD CONSTRAINT fkqw9uluakw4eii8c8tot7ywg3j FOREIGN KEY (billing_period_id) REFERENCES public.billing_period(id);
ALTER TABLE ONLY public.holiday_currencies ADD CONSTRAINT fkr4knvpkce66abhmhlsut8p5ft FOREIGN KEY (domain_id) REFERENCES public.domain(id);
ALTER TABLE ONLY public.page_version_content ADD CONSTRAINT fkso1eoq8tt33opfohwyw4j0ybn FOREIGN KEY (page_version_id) REFERENCES public.page_version(id);
ALTER TABLE ONLY public.checker_task_steps ADD CONSTRAINT fkte46nlog0696tcwhhddlpw707 FOREIGN KEY (steps_id) REFERENCES public.checker_step(id);