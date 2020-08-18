INSERT INTO application_user (id, email, first_name, last_name, login, password, is_using2fa, secret) VALUES
(1, 'slawek.grz@gmail.com', E'Sławomir', 'Grzegorzewski', 'guest', '$2a$10$NdTPC0d6I29aI0ob8dxBBOPm5dBEzIjJrtFYEwFQ4zly0DwdVaHwq', true, 'NBJH44TOJV7FWSKS'),
(2, 'slawek.grz@gmail.com', 'Sławomir', 'Grzegorzewski', 'admin', '$2a$10$mqExUZDgfcnyomq3shnMKeoaHf3okGbLjd1IG2vP5ONdG7A61P4dW', true, 'NBJH44TOJV7FWSKS'),
(3, 'slawek.grz@gmail.com', E'Sławomir', 'Grzegorzewski', 'guest2', '$2a$10$NdTPC0d6I29aI0ob8dxBBOPm5dBEzIjJrtFYEwFQ4zly0DwdVaHwq', true, 'NBJH44TOJV7FWSKS');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
INSERT INTO application_user_roles (application_user_id, roles) VALUES (1, 'USER'), (2, 'ADMIN'), (3, 'USER');
INSERT INTO account(id, last_transaction_included_in_balance_id, currency, current_balance, name, application_user_id) VALUES
(4, null, 'PLN', 100.0, 'Konto a', 1),
(5, null, 'PLN', 4560, 'Konto b', 1),
(6, null, 'EUR', 3000, 'Konto c', 1),
(7, null, 'EUR', 20, 'Konto d', 1),
(8, null, 'USD', 80, 'Konto e', 1),
(9, null, 'USD', 10, 'Konto f', 1),

(10, null, 'PLN', 237.7, 'Konto g', 2),
(11, null, 'PLN', 98, 'Konto h', 2),
(12, null, 'EUR', 4376, 'Konto i', 2),
(13, null, 'EUR', 94.9, 'Konto j', 2),
(14, null, 'USD', 2354, 'Konto k', 2),
(15, null, 'USD', 8787.98, 'Konto l', 2),

(16, null, 'PLN', 312.43, 'Konto m', 3),
(17, null, 'PLN', 87, 'Konto n', 3),
(18, null, 'EUR', 432.4, 'Konto o', 3),
(19, null, 'EUR', 87, 'Konto p', 3),
(20, null, 'USD', 9874.34, 'Konto r', 3),
(21, null, 'USD', 34.45, 'Konto s', 3);


SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
