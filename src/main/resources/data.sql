INSERT INTO application_user (id, email, first_name, last_name, login, password, is_using2fa, secret)
VALUES (nextval('hibernate_sequence'), 'slawek.grz@gmail.com', E'Sławomir', 'Grzegorzewski', 'guest',
        '$2a$10$NdTPC0d6I29aI0ob8dxBBOPm5dBEzIjJrtFYEwFQ4zly0DwdVaHwq', true, 'NBJH44TOJV7FWSKS'),
       (nextval('hibernate_sequence'), 'slawek.grz@gmail.com', 'Sławomir', 'Grzegorzewski', 'admin',
        '$2a$10$mqExUZDgfcnyomq3shnMKeoaHf3okGbLjd1IG2vP5ONdG7A61P4dW', true, 'NBJH44TOJV7FWSKS'),
       (nextval('hibernate_sequence'), 'slawek.grz@gmail.com', E'Sławomir', 'Grzegorzewski', 'guest2',
        '$2a$10$NdTPC0d6I29aI0ob8dxBBOPm5dBEzIjJrtFYEwFQ4zly0DwdVaHwq', true, 'NBJH44TOJV7FWSKS');

INSERT INTO application_user_roles (application_user_id, roles)
VALUES (1, 'USER'),
       (2, 'ADMIN'),
       (3, 'USER');

INSERT INTO account(id, last_transaction_included_in_balance_id, currency, current_balance, name, application_user_id)
VALUES (nextval('hibernate_sequence'), null, 'PLN', 0.0, 'Konto a', 1),
       (nextval('hibernate_sequence'), null, 'PLN', 0.0, 'Konto b', 1),
       (nextval('hibernate_sequence'), null, 'EUR', 0.0, 'Konto c', 1),
       (nextval('hibernate_sequence'), null, 'EUR', 0.0, 'Konto d', 1),
       (nextval('hibernate_sequence'), null, 'USD', 0.0, 'Konto e', 1),
       (nextval('hibernate_sequence'), null, 'USD', 0.0, 'Konto f', 1),

       (nextval('hibernate_sequence'), null, 'PLN', 3100.0, 'Konto g', 2),
       (nextval('hibernate_sequence'), null, 'PLN', 50000.0, 'Konto h', 2),
       (nextval('hibernate_sequence'), null, 'EUR', 1150.0, 'Konto i', 2),
       (nextval('hibernate_sequence'), null, 'EUR', 1000.0, 'Konto j', 2),
       (nextval('hibernate_sequence'), null, 'USD', 3.5, 'Konto k', 2),
       (nextval('hibernate_sequence'), null, 'USD', 0.0, 'Konto l', 2),

       (nextval('hibernate_sequence'), null, 'PLN', 0.0, 'Konto m', 3),
       (nextval('hibernate_sequence'), null, 'PLN', 0.0, 'Konto n', 3),
       (nextval('hibernate_sequence'), null, 'EUR', 0.0, 'Konto o', 3),
       (nextval('hibernate_sequence'), null, 'EUR', 0.0, 'Konto p', 3),
       (nextval('hibernate_sequence'), null, 'USD', 0.0, 'Konto r', 3),
       (nextval('hibernate_sequence'), null, 'USD', 0.0, 'Konto s', 3);

INSERT INTO public.financial_transaction (id, credit, debit, description, time_of_transaction, application_user_id,
                                          destination_id, source_id)
VALUES (nextval('hibernate_sequence'), 200.00, 0.00, 'Savings', '2020-08-25 14:07:38.731184', 2, 12, null),
       (nextval('hibernate_sequence'), 0.00, 50.00, 'For a road ticket', '2020-08-25 14:08:00.475559', 2, null, 12),
       (nextval('hibernate_sequence'), 2000.00, 0.00, 'Holiday''s work', '2020-08-25 14:08:17.393654', 2, 13, null),
       (nextval('hibernate_sequence'), 1000.00, 1000.00, 'On holidays', '2020-08-25 14:10:51.545252', 2, 12, 13),
       (nextval('hibernate_sequence'), 4500.00, 0.00, 'Wypłata', '2020-08-25 14:11:48.044368', 2, 10, null),
       (nextval('hibernate_sequence'), 0.00, 1400.00, 'Mieszkanie', '2020-08-25 14:12:03.476252', 2, null, 10),
       (nextval('hibernate_sequence'), 50000.00, 0.00, 'Oszczędności', '2020-08-25 14:12:14.534483', 2, 11, null),
       (nextval('hibernate_sequence'), 3.50, 0.00, 'Lol', '2020-08-25 14:12:36.740926', 2, 14, null);

INSERT INTO category(id, name, description, application_user_id)
VALUES(nextval('hibernate_sequence'), 'Jedzenie', 'Wydatki na żywność', 2);
