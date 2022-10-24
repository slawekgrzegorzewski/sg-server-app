create sequence intellectual_property_id_sequence;
alter sequence intellectual_property_id_sequence owner to postgres;

create table intellectual_property
(
    id          integer not null primary key,
    description varchar(10000),
    end_date    date,
    start_date  date,
    domain_id   integer
        constraint fk_domain references domain
);

alter table intellectual_property
    owner to postgres;

create sequence intellectual_property_task_id_sequence;
alter sequence intellectual_property_task_id_sequence owner to postgres;

create table task
(
    id                       integer not null primary key,
    co_authors               varchar(200),
    description              varchar(10000),
    intellectual_property_id integer
        constraint fk_intellectual_property references intellectual_property
);
alter table task
    owner to postgres;

create table task_attachments
(
    task_id     integer not null
        constraint fk_task references task,
    attachments varchar(255)
);
alter table task_attachments
    owner to postgres;

create sequence time_record_id_sequence;
alter sequence time_record_id_sequence owner to postgres;

create table time_record
(
    id              integer not null primary key,
    date            date,
    description     varchar(10000),
    number_of_hours integer not null,
    domain_id       integer
        constraint fk_domain references domain,
    task_id         integer
        constraint fk_task references task
);
alter table time_record
    owner to postgres;

