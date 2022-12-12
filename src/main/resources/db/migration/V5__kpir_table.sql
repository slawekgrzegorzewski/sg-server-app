create table kpir_entry
(
    id                                  bigserial      not null primary key,
    public_id                           UUID           not null default gen_random_uuid(),
    domain_id                           integer        not null
        constraint fk_domain references domain,
    entry_date                          date           not null default now(),
    entry_order                         integer        not null,
    booking_number                      varchar(1000)  not null default '',
    counterparty                        varchar(1000)  not null default '',
    counterparty_address                varchar(1000)  not null default '',
    description                         varchar(1000)  not null default '',
    provided_goods_and_services_value   numeric(19, 2) not null default 0,
    other_incomes                       numeric(19, 2) not null default 0,
    total_incomes                       numeric(19, 2) not null default 0,
    purchased_goods_and_materials_value numeric(19, 2) not null default 0,
    additional_cost_of_purchase         numeric(19, 2) not null default 0,
    remuneration_in_cash_or_in_kind     numeric(19, 2) not null default 0,
    other_expenses                      numeric(19, 2) not null default 0,
    total_expenses                      numeric(19, 2) not null default 0,
    comments                            varchar(10000) not null default ''
);
