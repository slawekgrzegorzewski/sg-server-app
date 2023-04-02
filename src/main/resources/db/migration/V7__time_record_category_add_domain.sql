alter table time_record_category
    add column domain_id integer not null
        constraint fk_domain references domain;