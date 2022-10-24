INSERT INTO public.domain (id, name) VALUES (1, 'Test domain');

INSERT INTO public.accountant_settings (id, is_company, domain_id) VALUES (4021, false, 1);

INSERT INTO public.application_user (id, email, first_name, is_using2fa, last_name, login, password, secret, default_domain_id)
    VALUES (1, NULL, NULL, true, NULL, 'slawek', '', '', 1);

INSERT INTO public.application_user_domain_relation (access_level, application_user_id, domain_id) VALUES ('ADMIN', 1, 1);

INSERT INTO public.application_user_roles (application_user_id, roles) VALUES (1, 'ACCOUNTANT_ADMIN');
INSERT INTO public.application_user_roles (application_user_id, roles) VALUES (1, 'ACCOUNTANT_USER');
INSERT INTO public.application_user_roles (application_user_id, roles) VALUES (1, 'CHECKER_ADMIN');
INSERT INTO public.application_user_roles (application_user_id, roles) VALUES (1, 'CHECKER_USER');
INSERT INTO public.application_user_roles (application_user_id, roles) VALUES (1, 'SYR_USER');
INSERT INTO public.application_user_roles (application_user_id, roles) VALUES (1, 'SYR_ADMIN');
INSERT INTO public.application_user_roles (application_user_id, roles) VALUES (1, 'CUBES');
INSERT INTO public.application_user_roles (application_user_id, roles) VALUES (1, 'IPR');