INSERT INTO application_user (id, email, first_name, last_name, login, password, is_using2fa, secret)
VALUES (1, 'slawek.grz@gmail.com', E'Sławomir', 'Grzegorzewski', 'guest',
        '$2a$10$NdTPC0d6I29aI0ob8dxBBOPm5dBEzIjJrtFYEwFQ4zly0DwdVaHwq', true, 'NBJH44TOJV7FWSKS'),
       (2, 'slawek.grz@gmail.com', 'Sławomir', 'Grzegorzewski', 'admin',
        '$2a$10$mqExUZDgfcnyomq3shnMKeoaHf3okGbLjd1IG2vP5ONdG7A61P4dW', true, 'NBJH44TOJV7FWSKS'),
       (3, 'slawek.grz@gmail.com', E'Sławomir', 'Grzegorzewski', 'guest2',
        '$2a$10$NdTPC0d6I29aI0ob8dxBBOPm5dBEzIjJrtFYEwFQ4zly0DwdVaHwq', true, 'NBJH44TOJV7FWSKS');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
INSERT INTO application_user_roles (application_user_id, roles)
VALUES (1, 'USER'),
       (2, 'ADMIN'),
       (3, 'USER');
INSERT INTO account(id, last_transaction_included_in_balance_id, currency, current_balance, name, application_user_id)
VALUES (4, null, 'PLN', 0.0, 'Konto a', 1),
       (5, null, 'PLN', 0.0, 'Konto b', 1),
       (6, null, 'EUR', 0.0, 'Konto c', 1),
       (7, null, 'EUR', 0.0, 'Konto d', 1),
       (8, null, 'USD', 0.0, 'Konto e', 1),
       (9, null, 'USD', 0.0, 'Konto f', 1),

       (10, null, 'PLN', 0.0, 'Konto g', 2),
       (11, null, 'PLN', 0.0, 'Konto h', 2),
       (12, null, 'EUR', 0.0, 'Konto i', 2),
       (13, null, 'EUR', 0.0, 'Konto j', 2),
       (14, null, 'USD', 0.0, 'Konto k', 2),
       (15, null, 'USD', 0.0, 'Konto l', 2),

       (16, null, 'PLN', 0.0, 'Konto m', 3),
       (17, null, 'PLN', 0.0, 'Konto n', 3),
       (18, null, 'EUR', 0.0, 'Konto o', 3),
       (19, null, 'EUR', 0.0, 'Konto p', 3),
       (20, null, 'USD', 0.0, 'Konto r', 3),
       (21, null, 'USD', 0.0, 'Konto s', 3);


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

-- INSERT INTO financial_transaction (id, credit, debit, description, time_of_transaction, application_user_id,
--                                    destination_id, source_id)
-- VALUES (22, 0.00, 0.00, 'a', now() + (1 ||' minutes')::interval, 2, 4, 20),
--        (23, 0.00, 0.00, 'b', now() + (2 ||' minutes')::interval, 1, 11, 20),
--        (24, 0.00, 0.00, 'c', now() + (3 ||' minutes')::interval, 3, 4, 11),
--        (25, 0.00, 0.00, 'd', now() + (4 ||' minutes')::interval, 2, 11, 20),
--        (26, 0.00, 0.00, 'e', now() + (5 ||' minutes')::interval, 2, 4, 11),
--        (27, 0.00, 0.00, 'f', now() + (6 ||' minutes')::interval, 2, 11, 12),
--        (28, 0.00, 0.00, 'g', now() + (7 ||' minutes')::interval, 1, 4, 5);

SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');
SELECT nextval('hibernate_sequence');