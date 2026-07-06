-- Datos de demo para la presentacion de TuTrade.
-- Correr DESPUES de haber levantado el backend al menos una vez (para que Hibernate cree las tablas).
-- Pensado para una BD limpia/demo. Login de los usuarios: correo + password "demo1234".

BEGIN;

-- Categorias
INSERT INTO category (id_category, name_category, parent_id_category) VALUES
(201,'Electrónica',0),
(202,'Ropa',0),
(203,'Deportes',0),
(204,'Libros',0),
(205,'Hogar',0),
(206,'Juguetes',0);

-- Usuarios (todos con rol CLIENT, password: demo1234)
INSERT INTO users (id_user, email_user, password_hash_user, username_user, is_premium_user, is_verified_user, is_enabled_user, created_at_user, updated_at_user, last_login_user, id_role) VALUES
(101,'ana@demo.com',  '$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','ana_g',  true,  true,  true,'2026-05-10','2026-06-20','2026-07-04 09:15:00',(SELECT id_role FROM role WHERE name_role='CLIENT')),
(102,'luis@demo.com', '$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','luis_m', false, true,  true,'2026-05-12','2026-06-21','2026-07-03 18:40:00',(SELECT id_role FROM role WHERE name_role='CLIENT')),
(103,'pedro@demo.com','$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','pedro_r',false, true,  true,'2026-05-15','2026-06-22','2026-07-02 12:00:00',(SELECT id_role FROM role WHERE name_role='CLIENT')),
(104,'maria@demo.com','$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','maria_l',true,  true,  true,'2026-05-18','2026-06-25','2026-07-05 08:30:00',(SELECT id_role FROM role WHERE name_role='CLIENT')),
(105,'jorge@demo.com','$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','jorge_s',false, true,  true,'2026-05-20','2026-06-26',NULL,(SELECT id_role FROM role WHERE name_role='CLIENT')),
(106,'sofia@demo.com','$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','sofia_v',false, false, true,'2026-06-01','2026-06-27',NULL,(SELECT id_role FROM role WHERE name_role='CLIENT'));

-- Items (status_item 1=activo, 2=pausado; condition_item 1..3)
INSERT INTO item (id_item, title_item, description_item, condition_item, status_item, id_user, iducategory) VALUES
(301,'Laptop usada','Core i5, 8GB RAM, buena para estudio',2,1,101,201),
(302,'Audífonos bluetooth','Con estuche de carga, poco uso',1,1,102,201),
(303,'Consola de videojuegos','Incluye dos mandos',2,1,103,201),
(304,'Monitor 24 pulgadas','Full HD, sin rayones',1,1,104,201),
(305,'Casaca de cuero','Talla M, color negro',2,1,101,202),
(306,'Zapatillas running','Talla 42, usadas una temporada',3,1,105,202),
(307,'Bicicleta montañera','Rodado 26, frenos nuevos',2,1,102,203),
(308,'Pelota de fútbol','Cuero sintético, tamaño 5',2,1,103,203),
(309,'Raqueta de tenis','Con funda y grip nuevo',1,1,106,203),
(310,'Libro Clean Code','Buen estado, sin subrayados',1,1,101,204),
(311,'Colección Harry Potter','7 tomos, tapa dura',2,1,104,204),
(312,'Juego de ollas','Acero inoxidable, 5 piezas',2,1,105,205),
(313,'Lámpara de escritorio','LED, brazo ajustable',1,2,102,205),
(314,'Set de bloques','Más de 200 piezas',2,1,103,206);

-- Trueques (varios estados para el grafico de "trueques por estado")
INSERT INTO trade (id_trade, status_trade, created_at_trade, completed_at_trade, proposer_id, receiver_id) VALUES
(401,'PENDING',  '2026-06-20',NULL,        101,102),
(402,'PENDING',  '2026-06-22',NULL,        103,104),
(403,'PENDING',  '2026-06-24',NULL,        105,101),
(404,'ACCEPTED', '2026-06-18','2026-06-25',101,103),
(405,'ACCEPTED', '2026-06-21','2026-06-28',102,105),
(406,'ACCEPTED', '2026-06-26','2026-07-01',104,101),
(407,'REJECTED', '2026-06-19',NULL,        106,102),
(408,'REJECTED', '2026-06-23',NULL,        103,106),
(409,'CANCELLED','2026-06-27',NULL,        105,104);

-- Items de cada trueque (side 1=proponente, 2=receptor)
INSERT INTO trade_item (side_trade_item, id_item, id_trade) VALUES
(1,301,401),(2,307,401),
(1,303,402),(2,304,402),
(1,306,403),(2,305,403),
(1,310,404),(2,308,404),
(1,302,405),(2,312,405),
(1,311,406),(2,301,406),
(1,309,407),(2,313,407),
(1,314,408),(2,309,408),
(1,312,409),(2,304,409);

-- Calificaciones (solo de trueques ACCEPTED; id_user = quien califica, rated_id_rating = calificado)
INSERT INTO rating (rated_id_rating, score_rating, comment_rating, created_at_rating, id_trade, id_user) VALUES
(103,5,'Todo perfecto, muy puntual','2026-06-25',404,101),
(101,4,'Buen trato, recomendado','2026-06-25',404,103),
(105,5,'Excelente intercambio','2026-06-28',405,102),
(102,5,'Muy amable y cumplido','2026-06-28',405,105),
(101,4,'Rápida coordinación','2026-07-01',406,104),
(104,5,'Producto tal cual la foto','2026-07-01',406,101);

-- Reportes de abuso (para el panel de moderacion)
INSERT INTO report (reason_report, description_report, status_report, created_at_report, reporter_id, reported_id) VALUES
('Producto no coincide','El artículo no era como en la publicación','PENDING','2026-06-29',101,106),
('No se presentó','Acordamos punto de encuentro y no llegó','PENDING','2026-06-30',102,103),
('Lenguaje ofensivo','Malos tratos en el chat','REVIEWED','2026-06-28',104,105),
('Spam','Envía publicidad repetida','RESOLVED','2026-06-26',103,106);

-- Perfiles
INSERT INTO profile (first_name_profile, last_name_profile, phone_profile, bio_profile, birth_date_profile, id_user) VALUES
('Ana','García','987654321','Me gusta reciclar y darle otra vida a las cosas','1998-03-14',101),
('Luis','Mendoza','987111222','Coleccionista de bicis y deportes','1995-07-22',102),
('Pedro','Ríos','987333444','Gamer y lector','2000-11-02',103),
('María','López','987555666','Amante de los libros','1997-01-30',104),
('Jorge','Sánchez','987777888','Cocinero aficionado','1993-09-09',105),
('Sofía','Vargas','987999000','Tenista de fin de semana','2001-05-17',106);

-- Reajusta las secuencias para que los proximos registros no choquen con los ids fijos
SELECT setval(pg_get_serial_sequence('category','id_category'), (SELECT MAX(id_category) FROM category));
SELECT setval(pg_get_serial_sequence('users','id_user'),        (SELECT MAX(id_user)     FROM users));
SELECT setval(pg_get_serial_sequence('item','id_item'),         (SELECT MAX(id_item)     FROM item));
SELECT setval(pg_get_serial_sequence('trade','id_trade'),       (SELECT MAX(id_trade)    FROM trade));

COMMIT;
