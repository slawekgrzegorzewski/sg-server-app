SELECT nextval('hibernate_sequence');
INSERT INTO application_user (id, email, first_name, last_name, login, password, is_using2fa, secret) VALUES (1, 'slawek.grz@gmail.com', E'Sławomir', 'Grzegorzewski', 'guest', '$2a$10$NdTPC0d6I29aI0ob8dxBBOPm5dBEzIjJrtFYEwFQ4zly0DwdVaHwq', true, 'NBJH44TOJV7FWSKS');
SELECT nextval('hibernate_sequence');
INSERT INTO application_user (id, email, first_name, last_name, login, password, is_using2fa, secret) VALUES (2, 'slawek.grz@gmail.com', 'Sławomir', 'Grzegorzewski', 'admin', '$2a$10$mqExUZDgfcnyomq3shnMKeoaHf3okGbLjd1IG2vP5ONdG7A61P4dW', true, 'NBJH44TOJV7FWSKS');
INSERT INTO application_user_roles (application_user_id, roles) VALUES (1, 'USER');
INSERT INTO application_user_roles (application_user_id, roles) VALUES (2, 'ADMIN');