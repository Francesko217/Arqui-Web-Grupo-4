-- data.sql: se ejecuta en CADA arranque (spring.sql.init.mode=always) despues de que
-- Hibernate crea las tablas (defer-datasource-initialization=true). Idempotente.
-- Siembra roles y ~20 registros de demo por entidad. Login demo: <correo> / demo1234.

INSERT INTO role (name_role, description_role)
SELECT 'ADMIN', 'Administrador del sistema'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE name_role = 'ADMIN');

INSERT INTO role (name_role, description_role)
SELECT 'CLIENT', 'Usuario cliente'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE name_role = 'CLIENT');

INSERT INTO category (id_category, name_category, parent_id_category) VALUES
(2001,'Electrónica',0),
(2002,'Ropa',0),
(2003,'Deportes',0),
(2004,'Libros',0),
(2005,'Hogar',0),
(2006,'Juguetes',0),
(2007,'Música',0),
(2008,'Videojuegos',0),
(2009,'Muebles',0),
(2010,'Jardín',0),
(2011,'Cocina',0),
(2012,'Belleza',0),
(2013,'Herramientas',0),
(2014,'Mascotas',0),
(2015,'Automóvil',0),
(2016,'Arte',0),
(2017,'Fotografía',0),
(2018,'Bebés',0),
(2019,'Oficina',0),
(2020,'Coleccionables',0)
ON CONFLICT (id_category) DO NOTHING;

-- 2. Usuarios (id 1001-1020, todos rol CLIENT, password: demo1234)
INSERT INTO users (id_user, email_user, password_hash_user, username_user, is_premium_user, is_verified_user, is_enabled_user, created_at_user, updated_at_user, last_login_user, id_role) VALUES
(1001,'ana.garcia@demo.com',      '$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','ana_g',      true,  true,  true,'2026-04-10','2026-06-20','2026-07-04 09:15:00',(SELECT id_role FROM role WHERE name_role='CLIENT')),
(1002,'luis.mendoza@demo.com',    '$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','luis_m',     false, true,  true,'2026-04-12','2026-06-21','2026-07-03 18:40:00',(SELECT id_role FROM role WHERE name_role='CLIENT')),
(1003,'pedro.rios@demo.com',      '$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','pedro_r',    false, true,  true,'2026-04-15','2026-06-22','2026-07-02 12:00:00',(SELECT id_role FROM role WHERE name_role='CLIENT')),
(1004,'maria.lopez@demo.com',     '$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','maria_l',    true,  true,  true,'2026-04-18','2026-06-25','2026-07-05 08:30:00',(SELECT id_role FROM role WHERE name_role='CLIENT')),
(1005,'jorge.sanchez@demo.com',   '$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','jorge_s',    false, true,  true,'2026-04-20','2026-06-26',NULL,(SELECT id_role FROM role WHERE name_role='CLIENT')),
(1006,'sofia.vargas@demo.com',    '$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','sofia_v',    false, false, true,'2026-05-01','2026-06-27',NULL,(SELECT id_role FROM role WHERE name_role='CLIENT')),
(1007,'carlos.torres@demo.com',   '$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','carlos_t',   false, true,  true,'2026-05-02','2026-06-28','2026-07-01 20:10:00',(SELECT id_role FROM role WHERE name_role='CLIENT')),
(1008,'lucia.flores@demo.com',    '$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','lucia_f',    true,  true,  true,'2026-05-03','2026-06-29','2026-07-06 10:05:00',(SELECT id_role FROM role WHERE name_role='CLIENT')),
(1009,'diego.herrera@demo.com',   '$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','diego_h',    false, true,  true,'2026-05-05','2026-06-30','2026-07-02 15:25:00',(SELECT id_role FROM role WHERE name_role='CLIENT')),
(1010,'valeria.castro@demo.com',  '$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','valeria_c',  false, true,  true,'2026-05-06','2026-07-01','2026-07-04 11:00:00',(SELECT id_role FROM role WHERE name_role='CLIENT')),
(1011,'miguel.aguirre@demo.com',  '$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','miguel_a',   false, false, true,'2026-05-08','2026-07-01',NULL,(SELECT id_role FROM role WHERE name_role='CLIENT')),
(1012,'camila.paredes@demo.com',  '$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','camila_p',   true,  true,  true,'2026-05-09','2026-07-02','2026-07-05 19:45:00',(SELECT id_role FROM role WHERE name_role='CLIENT')),
(1013,'andres.quispe@demo.com',   '$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','andres_q',   false, true,  true,'2026-05-11','2026-07-02','2026-07-03 09:30:00',(SELECT id_role FROM role WHERE name_role='CLIENT')),
(1014,'daniela.rojas@demo.com',   '$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','daniela_r',  false, true,  true,'2026-05-13','2026-07-03','2026-07-06 14:20:00',(SELECT id_role FROM role WHERE name_role='CLIENT')),
(1015,'fernando.zuniga@demo.com', '$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','fernando_z', false, true,  true,'2026-05-15','2026-07-03',NULL,(SELECT id_role FROM role WHERE name_role='CLIENT')),
(1016,'gabriela.nunez@demo.com',  '$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','gabriela_n', true,  true,  true,'2026-05-16','2026-07-04','2026-07-06 08:00:00',(SELECT id_role FROM role WHERE name_role='CLIENT')),
(1017,'ricardo.bravo@demo.com',   '$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','ricardo_b',  false, true,  true,'2026-05-18','2026-07-04','2026-07-05 17:10:00',(SELECT id_role FROM role WHERE name_role='CLIENT')),
(1018,'paola.espinoza@demo.com',  '$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','paola_e',    false, false, true,'2026-05-20','2026-07-05',NULL,(SELECT id_role FROM role WHERE name_role='CLIENT')),
(1019,'sergio.ibanez@demo.com',   '$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','sergio_i',   false, true,  true,'2026-05-22','2026-07-05','2026-07-06 21:30:00',(SELECT id_role FROM role WHERE name_role='CLIENT')),
(1020,'natalia.ojeda@demo.com',   '$2y$10$n44CFy9DsAjqJPQNpVwMF.imse3OPIecEsSLLX0/EktyUNSnwY3tW','natalia_o',  false, true,  true,'2026-05-24','2026-07-06','2026-07-06 22:05:00',(SELECT id_role FROM role WHERE name_role='CLIENT'))
ON CONFLICT (id_user) DO NOTHING;

-- 3. Perfiles (id 1101-1120, uno por usuario)
INSERT INTO profile (id_profile, first_name_profile, last_name_profile, phone_profile, bio_profile, birth_date_profile, id_user) VALUES
(1101,'Ana','García','987654321','Me gusta reciclar y darle otra vida a las cosas','1998-03-14',1001),
(1102,'Luis','Mendoza','987111222','Coleccionista de bicis y deportes','1995-07-22',1002),
(1103,'Pedro','Ríos','987333444','Gamer y lector','2000-11-02',1003),
(1104,'María','López','987555666','Amante de los libros','1997-01-30',1004),
(1105,'Jorge','Sánchez','987777888','Cocinero aficionado','1993-09-09',1005),
(1106,'Sofía','Vargas','987999000','Tenista de fin de semana','2001-05-17',1006),
(1107,'Carlos','Torres','986100200','Fanático de la tecnología','1996-02-11',1007),
(1108,'Lucía','Flores','986200300','Vendo lo que ya no uso','1999-08-25',1008),
(1109,'Diego','Herrera','986300400','Apasionado del ciclismo','1994-12-03',1009),
(1110,'Valeria','Castro','986400500','Diseñadora y manualidades','1998-06-19',1010),
(1111,'Miguel','Aguirre','986500600','Melómano de vinilos','1992-04-27',1011),
(1112,'Camila','Paredes','986600700','Amo la fotografía análoga','2000-10-08',1012),
(1113,'Andrés','Quispe','986700800','Coleccionista de figuras','1995-03-16',1013),
(1114,'Daniela','Rojas','986800900','Mamá práctica, intercambio cosas de bebé','1991-07-05',1014),
(1115,'Fernando','Zúñiga','986900100','Manitas para el hogar','1990-11-29',1015),
(1116,'Gabriela','Núñez','985100200','Lectora empedernida','1997-09-12',1016),
(1117,'Ricardo','Bravo','985200300','Deportista y minimalista','1993-01-24',1017),
(1118,'Paola','Espinoza','985300400','Me encanta la cocina','1999-05-06',1018),
(1119,'Sergio','Ibáñez','985400500','Gamer retro','1996-08-30',1019),
(1120,'Natalia','Ojeda','985500600','Artista y recicladora','2002-02-18',1020)
ON CONFLICT (id_profile) DO NOTHING;

-- 4. Items (id 3001-3020). condition_item 1..3, status_item 1=activo 2=pausado
INSERT INTO item (id_item, title_item, description_item, condition_item, status_item, id_user, iducategory) VALUES
(3001,'Laptop usada','Core i5, 8GB RAM, buena para estudio',2,1,1001,2001),
(3002,'Audífonos bluetooth','Con estuche de carga, poco uso',1,1,1002,2001),
(3003,'Consola de videojuegos','Incluye dos mandos',2,1,1003,2008),
(3004,'Monitor 24 pulgadas','Full HD, sin rayones',1,1,1004,2001),
(3005,'Casaca de cuero','Talla M, color negro',2,1,1005,2002),
(3006,'Zapatillas running','Talla 42, usadas una temporada',3,1,1006,2002),
(3007,'Bicicleta montañera','Rodado 26, frenos nuevos',2,1,1007,2003),
(3008,'Pelota de fútbol','Cuero sintético, tamaño 5',2,1,1008,2003),
(3009,'Raqueta de tenis','Con funda y grip nuevo',1,1,1009,2003),
(3010,'Libro Clean Code','Buen estado, sin subrayados',1,1,1010,2004),
(3011,'Colección Harry Potter','7 tomos, tapa dura',2,1,1011,2004),
(3012,'Juego de ollas','Acero inoxidable, 5 piezas',2,1,1012,2005),
(3013,'Lámpara de escritorio','LED, brazo ajustable',1,2,1013,2005),
(3014,'Set de bloques','Más de 200 piezas',2,1,1014,2006),
(3015,'Guitarra acústica','Cuerdas nuevas, incluye funda',2,1,1015,2007),
(3016,'Cafetera espresso','Manual, poco uso',1,1,1016,2011),
(3017,'Escritorio de madera','Con cajones, buen estado',3,1,1017,2009),
(3018,'Cuadro decorativo','Óleo enmarcado, 60x40',1,2,1018,2016),
(3019,'Cámara réflex','Con lente 18-55mm',2,1,1019,2017),
(3020,'Silla ergonómica','Regulable, malla transpirable',1,1,1020,2019)
ON CONFLICT (id_item) DO NOTHING;

-- 5. Trueques (id 4001-4020). 10 ACCEPTED, 6 PENDING, 2 REJECTED, 2 CANCELLED
INSERT INTO trade (id_trade, status_trade, created_at_trade, completed_at_trade, proposer_id, receiver_id) VALUES
(4001,'ACCEPTED', '2026-06-01','2026-06-08',1001,1002),
(4002,'ACCEPTED', '2026-06-02','2026-06-09',1003,1004),
(4003,'ACCEPTED', '2026-06-03','2026-06-10',1005,1006),
(4004,'ACCEPTED', '2026-06-04','2026-06-11',1007,1008),
(4005,'ACCEPTED', '2026-06-05','2026-06-12',1009,1010),
(4006,'ACCEPTED', '2026-06-06','2026-06-13',1011,1012),
(4007,'ACCEPTED', '2026-06-07','2026-06-14',1013,1014),
(4008,'ACCEPTED', '2026-06-08','2026-06-15',1015,1016),
(4009,'ACCEPTED', '2026-06-09','2026-06-16',1017,1018),
(4010,'ACCEPTED', '2026-06-10','2026-06-17',1019,1020),
(4011,'PENDING',  '2026-06-20',NULL,1002,1003),
(4012,'PENDING',  '2026-06-21',NULL,1004,1005),
(4013,'PENDING',  '2026-06-22',NULL,1006,1007),
(4014,'PENDING',  '2026-06-23',NULL,1008,1009),
(4015,'PENDING',  '2026-06-24',NULL,1010,1011),
(4016,'PENDING',  '2026-06-25',NULL,1012,1013),
(4017,'REJECTED', '2026-06-18',NULL,1014,1015),
(4018,'REJECTED', '2026-06-19',NULL,1016,1017),
(4019,'CANCELLED','2026-06-26',NULL,1018,1019),
(4020,'CANCELLED','2026-06-27',NULL,1020,1001)
ON CONFLICT (id_trade) DO NOTHING;

-- 6. Items de cada trueque (id 5001-5040). side 1=proponente, 2=receptor
INSERT INTO trade_item (id_trade_item, side_trade_item, id_item, id_trade) VALUES
(5001,1,3001,4001),(5002,2,3002,4001),
(5003,1,3003,4002),(5004,2,3004,4002),
(5005,1,3005,4003),(5006,2,3006,4003),
(5007,1,3007,4004),(5008,2,3008,4004),
(5009,1,3009,4005),(5010,2,3010,4005),
(5011,1,3011,4006),(5012,2,3012,4006),
(5013,1,3013,4007),(5014,2,3014,4007),
(5015,1,3015,4008),(5016,2,3016,4008),
(5017,1,3017,4009),(5018,2,3018,4009),
(5019,1,3019,4010),(5020,2,3020,4010),
(5021,1,3002,4011),(5022,2,3003,4011),
(5023,1,3004,4012),(5024,2,3005,4012),
(5025,1,3006,4013),(5026,2,3007,4013),
(5027,1,3008,4014),(5028,2,3009,4014),
(5029,1,3010,4015),(5030,2,3011,4015),
(5031,1,3012,4016),(5032,2,3013,4016),
(5033,1,3014,4017),(5034,2,3015,4017),
(5035,1,3016,4018),(5036,2,3017,4018),
(5037,1,3018,4019),(5038,2,3019,4019),
(5039,1,3020,4020),(5040,2,3001,4020)
ON CONFLICT (id_trade_item) DO NOTHING;

-- 7. Puntos de encuentro (id 6001-6010, uno por trueque ACCEPTED). Coordenadas de Lima
INSERT INTO meetingpoint (id_meeting_point, address_meeting_point, latitude_meeting_point, longitude_meeting_point, scheduled_at_meeting_point, id_trade) VALUES
(6001,'Parque Kennedy, Miraflores',        -12.121500,-77.029700,'2026-06-08 11:00:00',4001),
(6002,'Óvalo Gutiérrez, Miraflores',       -12.099800,-77.037400,'2026-06-09 16:00:00',4002),
(6003,'Real Plaza Salaverry, Jesús María', -12.089600,-77.051300,'2026-06-10 12:30:00',4003),
(6004,'Jockey Plaza, Surco',               -12.085600,-76.974500,'2026-06-11 18:00:00',4004),
(6005,'Plaza San Miguel, San Miguel',      -12.077600,-77.083200,'2026-06-12 10:15:00',4005),
(6006,'Parque de la Amistad, Surco',       -12.135900,-76.994800,'2026-06-13 15:45:00',4006),
(6007,'Larcomar, Miraflores',              -12.132600,-77.030900,'2026-06-14 17:30:00',4007),
(6008,'Estación Central Metropolitano',    -12.056500,-77.036000,'2026-06-15 09:00:00',4008),
(6009,'Plaza Mayor de Lima, Cercado',      -12.045600,-77.030700,'2026-06-16 13:00:00',4009),
(6010,'Mall del Sur, San Juan de Miraflores',-12.159700,-76.976100,'2026-06-17 19:00:00',4010)
ON CONFLICT (id_meeting_point) DO NOTHING;

-- 8. Chats (id 7001-7020, uno por trueque). id_user=proponente, user_b_id_chat=receptor
INSERT INTO chat (id_chat, user_b_id_chat, created_at_chat, id_user, id_trade) VALUES
(7001,1002,'2026-06-01',1001,4001),
(7002,1004,'2026-06-02',1003,4002),
(7003,1006,'2026-06-03',1005,4003),
(7004,1008,'2026-06-04',1007,4004),
(7005,1010,'2026-06-05',1009,4005),
(7006,1012,'2026-06-06',1011,4006),
(7007,1014,'2026-06-07',1013,4007),
(7008,1016,'2026-06-08',1015,4008),
(7009,1018,'2026-06-09',1017,4009),
(7010,1020,'2026-06-10',1019,4010),
(7011,1003,'2026-06-20',1002,4011),
(7012,1005,'2026-06-21',1004,4012),
(7013,1007,'2026-06-22',1006,4013),
(7014,1009,'2026-06-23',1008,4014),
(7015,1011,'2026-06-24',1010,4015),
(7016,1013,'2026-06-25',1012,4016),
(7017,1015,'2026-06-18',1014,4017),
(7018,1017,'2026-06-19',1016,4018),
(7019,1019,'2026-06-26',1018,4019),
(7020,1001,'2026-06-27',1020,4020)
ON CONFLICT (id_chat) DO NOTHING;

-- 9. Mensajes (id 8001-8040, 2 por chat). status SENT / READ
INSERT INTO message (id_message, content_message, status_message, sent_at_message, id_user, id_chat) VALUES
(8001,'Hola, me interesa tu artículo, ¿seguimos?','READ','2026-06-01 10:00:00',1001,7001),
(8002,'Claro, coordinemos el punto de encuentro','SENT','2026-06-01 10:05:00',1002,7001),
(8003,'¿Sigue disponible el cambio?','READ','2026-06-02 09:30:00',1003,7002),
(8004,'Sí, todo listo de mi lado','SENT','2026-06-02 09:40:00',1004,7002),
(8005,'Podemos vernos el finde','READ','2026-06-03 14:00:00',1005,7003),
(8006,'Perfecto, te confirmo la hora','SENT','2026-06-03 14:10:00',1006,7003),
(8007,'Gracias por aceptar el trueque','READ','2026-06-04 17:00:00',1007,7004),
(8008,'A ti, quedó excelente','SENT','2026-06-04 17:20:00',1008,7004),
(8009,'¿Traes el artículo en su caja?','READ','2026-06-05 11:00:00',1009,7005),
(8010,'Sí, con todos sus accesorios','SENT','2026-06-05 11:15:00',1010,7005),
(8011,'Nos vemos en el punto acordado','READ','2026-06-06 15:00:00',1011,7006),
(8012,'De acuerdo, llego puntual','SENT','2026-06-06 15:05:00',1012,7006),
(8013,'Todo conforme, muchas gracias','READ','2026-06-07 16:00:00',1013,7007),
(8014,'Igualmente, un gusto','SENT','2026-06-07 16:30:00',1014,7007),
(8015,'¿Qué te parece el intercambio?','READ','2026-06-08 09:00:00',1015,7008),
(8016,'Me parece justo, hecho','SENT','2026-06-08 09:10:00',1016,7008),
(8017,'Confirmo el punto de encuentro','READ','2026-06-09 12:00:00',1017,7009),
(8018,'Perfecto, ahí estaré','SENT','2026-06-09 12:20:00',1018,7009),
(8019,'Llevo el artículo revisado','READ','2026-06-10 18:00:00',1019,7010),
(8020,'Genial, gracias por avisar','SENT','2026-06-10 18:15:00',1020,7010),
(8021,'Hola, ¿te interesa mi propuesta?','SENT','2026-06-20 10:00:00',1002,7011),
(8022,'Estoy revisando, te respondo','SENT','2026-06-20 10:30:00',1003,7011),
(8023,'¿Aceptas el cambio?','SENT','2026-06-21 11:00:00',1004,7012),
(8024,'Déjame pensarlo','SENT','2026-06-21 11:20:00',1005,7012),
(8025,'Buenas, propongo intercambio','SENT','2026-06-22 09:00:00',1006,7013),
(8026,'Interesante, lo veo','SENT','2026-06-22 09:30:00',1007,7013),
(8027,'¿Coordinamos?','SENT','2026-06-23 14:00:00',1008,7014),
(8028,'Aún no decido','SENT','2026-06-23 14:15:00',1009,7014),
(8029,'Te mando mi propuesta','SENT','2026-06-24 16:00:00',1010,7015),
(8030,'Gracias, la reviso','SENT','2026-06-24 16:10:00',1011,7015),
(8031,'¿Qué opinas del trueque?','SENT','2026-06-25 10:00:00',1012,7016),
(8032,'Lo estoy evaluando','SENT','2026-06-25 10:25:00',1013,7016),
(8033,'Propongo este cambio','READ','2026-06-18 09:00:00',1014,7017),
(8034,'No me convence, gracias','READ','2026-06-18 09:30:00',1015,7017),
(8035,'¿Te interesa?','READ','2026-06-19 11:00:00',1016,7018),
(8036,'Prefiero no esta vez','READ','2026-06-19 11:20:00',1017,7018),
(8037,'Mejor cancelamos el trueque','READ','2026-06-26 15:00:00',1018,7019),
(8038,'De acuerdo, sin problema','READ','2026-06-26 15:10:00',1019,7019),
(8039,'Voy a cancelar, surgió un imprevisto','READ','2026-06-27 12:00:00',1020,7020),
(8040,'Entendido, será otra vez','READ','2026-06-27 12:15:00',1001,7020)
ON CONFLICT (id_message) DO NOTHING;

-- 10. Calificaciones (id 9001-9020, 2 por trueque ACCEPTED). id_user=quien califica, rated_id_rating=calificado
INSERT INTO rating (id_rating, rated_id_rating, score_rating, comment_rating, created_at_rating, id_trade, id_user) VALUES
(9001,1002,5,'Todo perfecto, muy puntual','2026-06-08',4001,1001),
(9002,1001,4,'Buen trato, recomendado','2026-06-08',4001,1002),
(9003,1004,5,'Excelente intercambio','2026-06-09',4002,1003),
(9004,1003,5,'Muy amable y cumplido','2026-06-09',4002,1004),
(9005,1006,4,'Rápida coordinación','2026-06-10',4003,1005),
(9006,1005,4,'Producto tal cual la foto','2026-06-10',4003,1006),
(9007,1008,5,'Gran experiencia','2026-06-11',4004,1007),
(9008,1007,5,'Todo en orden','2026-06-11',4004,1008),
(9009,1010,4,'Cumplió con lo acordado','2026-06-12',4005,1009),
(9010,1009,5,'Muy recomendable','2026-06-12',4005,1010),
(9011,1012,5,'Puntual y honesto','2026-06-13',4006,1011),
(9012,1011,4,'Buen intercambio','2026-06-13',4006,1012),
(9013,1014,5,'Sin quejas, excelente','2026-06-14',4007,1013),
(9014,1013,5,'Repetiría el trueque','2026-06-14',4007,1014),
(9015,1016,3,'Se demoró un poco','2026-06-15',4008,1015),
(9016,1015,4,'Todo bien al final','2026-06-15',4008,1016),
(9017,1018,5,'Muy correcto','2026-06-16',4009,1017),
(9018,1017,4,'Buena comunicación','2026-06-16',4009,1018),
(9019,1020,5,'Intercambio impecable','2026-06-17',4010,1019),
(9020,1019,5,'Todo perfecto','2026-06-17',4010,1020)
ON CONFLICT (id_rating) DO NOTHING;

-- 11. Reportes de abuso (id 9101-9120). status PENDING / REVIEWED / RESOLVED
INSERT INTO report (id_report, reason_report, description_report, status_report, created_at_report, reporter_id, reported_id) VALUES
(9101,'Producto no coincide','El artículo no era como en la publicación','PENDING','2026-06-29',1001,1006),
(9102,'No se presentó','Acordamos punto de encuentro y no llegó','PENDING','2026-06-30',1002,1003),
(9103,'Lenguaje ofensivo','Malos tratos en el chat','REVIEWED','2026-06-28',1004,1005),
(9104,'Spam','Envía publicidad repetida','RESOLVED','2026-06-26',1003,1006),
(9105,'Producto dañado','Llegó con detalles no mencionados','PENDING','2026-06-27',1005,1007),
(9106,'Intento de estafa','Pidió pago adicional fuera de la app','REVIEWED','2026-06-25',1007,1008),
(9107,'Perfil falso','Datos que no coinciden con la persona','PENDING','2026-06-24',1008,1009),
(9108,'No respondió','Dejó de contestar tras aceptar','RESOLVED','2026-06-23',1009,1010),
(9109,'Contenido inapropiado','Publicación con imágenes indebidas','REVIEWED','2026-06-22',1010,1011),
(9110,'Acoso','Mensajes insistentes fuera de lugar','PENDING','2026-06-21',1011,1012),
(9111,'Producto no coincide','Descripción exagerada','PENDING','2026-06-20',1012,1013),
(9112,'No se presentó','No llegó al punto acordado','REVIEWED','2026-06-19',1013,1014),
(9113,'Lenguaje ofensivo','Insultos en la conversación','RESOLVED','2026-06-18',1014,1015),
(9114,'Spam','Comparte enlaces sospechosos','PENDING','2026-06-17',1015,1016),
(9115,'Intento de estafa','Solicitó datos bancarios','REVIEWED','2026-06-16',1016,1017),
(9116,'Perfil falso','Cuenta duplicada','PENDING','2026-06-15',1017,1018),
(9117,'Producto dañado','No funcionaba al recibirlo','RESOLVED','2026-06-14',1018,1019),
(9118,'Acoso','Contacto reiterado no deseado','PENDING','2026-06-13',1019,1020),
(9119,'Contenido inapropiado','Texto ofensivo en la publicación','REVIEWED','2026-06-12',1020,1001),
(9120,'No respondió','Ignoró la coordinación','PENDING','2026-06-11',1006,1002)
ON CONFLICT (id_report) DO NOTHING;

-- Reajusta las secuencias para que los proximos registros no choquen con los ids fijos
SELECT setval(pg_get_serial_sequence('category','id_category'),       (SELECT MAX(id_category)     FROM category));
SELECT setval(pg_get_serial_sequence('users','id_user'),              (SELECT MAX(id_user)         FROM users));
SELECT setval(pg_get_serial_sequence('profile','id_profile'),         (SELECT MAX(id_profile)      FROM profile));
SELECT setval(pg_get_serial_sequence('item','id_item'),               (SELECT MAX(id_item)         FROM item));
SELECT setval(pg_get_serial_sequence('trade','id_trade'),             (SELECT MAX(id_trade)        FROM trade));
SELECT setval(pg_get_serial_sequence('trade_item','id_trade_item'),   (SELECT MAX(id_trade_item)   FROM trade_item));
SELECT setval(pg_get_serial_sequence('meetingpoint','id_meeting_point'),(SELECT MAX(id_meeting_point) FROM meetingpoint));
SELECT setval(pg_get_serial_sequence('chat','id_chat'),               (SELECT MAX(id_chat)         FROM chat));
SELECT setval(pg_get_serial_sequence('message','id_message'),         (SELECT MAX(id_message)      FROM message));
SELECT setval(pg_get_serial_sequence('rating','id_rating'),           (SELECT MAX(id_rating)       FROM rating));
SELECT setval(pg_get_serial_sequence('report','id_report'),           (SELECT MAX(id_report)       FROM report));
