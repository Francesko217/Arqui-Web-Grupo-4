INSERT INTO role (name_role, description_role)
SELECT 'ADMIN', 'Administrador del sistema'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE name_role = 'ADMIN');

INSERT INTO role (name_role, description_role)
SELECT 'CLIENT', 'Usuario cliente'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE name_role = 'CLIENT');
