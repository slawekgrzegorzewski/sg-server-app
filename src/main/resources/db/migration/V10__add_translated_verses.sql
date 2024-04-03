create table translated_verses
(
    id               bigserial                not null primary key,
    translation_date timestamp with time zone not null,
    book             varchar(50)              not null,
    chapter          integer                  not null,
    verse            integer                  not null
);
