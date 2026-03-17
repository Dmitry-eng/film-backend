INSERT INTO public.role (id, name, is_deleted, create_date_time, update_datetime)
VALUES (1, 'ADMIN', false, '2026-03-11 21:18:20.866092', '2026-03-11 21:18:20.866092')
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.account_entity (
    id, email, name, encode_password, session_id, is_blocked, is_deleted,
    role_id, create_date_time, update_datetime
) VALUES (
    1, 'test@test.com', 'Test User', '$2a$10$tOZEZ9XwW7xtOXPZ2sxXr.Gvwh05MWvBfceIg74PchRxQXhtu8/Q2',
    '3213231', false, false, 1, '2026-03-11 21:18:26.263327', '2026-03-11 21:18:26.263327'
)
ON CONFLICT (id) DO NOTHING;