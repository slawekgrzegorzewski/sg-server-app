create table task_category
(
    id   bigserial     not null primary key,
    name varchar(1000) not null default ''
);

ALTER TABLE task ADD COLUMN task_category_id bigint;
ALTER TABLE task ADD CONSTRAINT fk_task_task_category FOREIGN KEY (task_category_id) REFERENCES task_category(id);