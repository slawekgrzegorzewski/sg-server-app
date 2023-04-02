create table time_record_category
(
    id   bigserial     not null primary key,
    name varchar(1000) not null default ''
);

ALTER TABLE time_record ADD COLUMN time_record_category_id bigint;
ALTER TABLE time_record ADD CONSTRAINT fk_time_record_time_record_category FOREIGN KEY (time_record_category_id) REFERENCES time_record_category(id);