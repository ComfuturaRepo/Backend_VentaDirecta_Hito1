-- =====================================================
-- INSERCIONES DE DATOS INICIALES (para pruebas y desarrollo)
-- =====================================================

-- 1. Empresas
INSERT INTO empresa (nombre) VALUES
                                 ('COMFUTURA'),
                                 ('GAB'),
                                 ('SUDCOM');

-- 2. Clientes
INSERT INTO cliente (razon_social, ruc) VALUES
                                            ('COMFUTURA',       '20601234567'),
                                            ('CLARO PERÚ',      '20100123456'),
                                            ('ENTEL PERÚ',      '20131234567'),
                                            ('STL TELECOM',     '20598765432');

-- 3. Niveles
INSERT INTO nivel (codigo, nombre, descripcion) VALUES
                                                    ('L1', 'Gerencia General',      'Gerencia General / Dirección'),
                                                    ('L2', 'Gerencia / Subgerencia','Gerencia funcional y subgerencias'),
                                                    ('L3', 'Jefatura',              'Jefatura / Supervisión Senior'),
                                                    ('L4', 'Coordinación',          'Coordinación / Supervisión operativa'),
                                                    ('L5', 'Operativo',             'Ejecución / Técnicos / Asistentes');

-- 4. Áreas
INSERT INTO area (nombre) VALUES
                              ('RRHH'), ('COSTOS'), ('ADMINISTRATIVA'), ('ENERGIA'), ('CW'),
                              ('COMERCIAL'), ('PEXT'), ('SAQ'), ('ENTEL'), ('SSOMA'), ('TI'),
                              ('CIERRE'), ('CONTABILIDAD'), ('LOGISTICA'), ('GERENTE GENERAL');

-- 5. Cargos
INSERT INTO cargo (nombre, id_nivel) VALUES
                                         ('GERENTE GENERAL',           1),
                                         ('GERENTE COMERCIAL',         1),
                                         ('JEFA DE FINANZAS',          1),
                                         ('SUBGERENTE OPERACIONES',    2),
                                         ('JEFE TI',                   2),
                                         ('JEFE DE LOGÍSTICA',         2),
                                         ('JEFE CW - ENERGIA',         2),
                                         ('JEFE PEXT',                 2),
                                         ('JEFE DE CIERRE',            2),
                                         ('SUPERVISOR CW',             3),
                                         ('SUPERVISOR FIBRA',          3),
                                         ('COORDINADOR TI',            4),
                                         ('COORDINADOR ENERGIA',       4),
                                         ('COORDINADOR PEXT',          4),
                                         ('PROJECT MANAGER',           4),
                                         ('TÉCNICO FIBRA ÓPTICA',      5),
                                         ('TÉCNICO ENERGÍA',           5);

-- 6. Bancos
INSERT INTO banco (nombre) VALUES
                               ('BCP - Banco de Crédito del Perú'),
                               ('Interbank'),
                               ('BBVA Continental');

-- 7. Proveedores (ejemplos reales)
INSERT INTO proveedor (ruc, razon_social, contacto, telefono, correo, id_banco, numero_cuenta, moneda) VALUES
                                                                                                           ('20100123456', 'TECNOFIBRA SAC',          'Juan Pérez',   '987-654-321', 'ventas@tecnofibra.pe',     1, '191-1234567-0-01', 'PEN'),
                                                                                                           ('20512345678', 'ELECTROREDES EIRL',       'María Gómez',  '999-888-777', 'cotizaciones@electroredes.pe', 2, '0011-456789012345', 'PEN'),
                                                                                                           ('20604567890', 'INFRAESTRUCTURA ANDINA',  'Pedro López',  '966-777-888', 'info@infraandina.pe',      3, '123456789012', 'PEN');

-- 8. Proyectos
INSERT INTO proyecto (nombre, descripcion) VALUES
                                               ('Despliegue Fibra Metropolitana', 'Proyectos FTTH y backbone'),
                                               ('Mantenimiento Energético 2025',  'Energía y grupos para estaciones'),
                                               ('Modernización CW 2026',          'Mejora de cableado y torres');

-- 9. Fases
INSERT INTO fase (nombre, orden) VALUES
                                     ('Planificación', 10),
                                     ('Ejecución',     20),
                                     ('Cierre',        30);

-- 10. Sites
INSERT INTO site (nombre, direccion) VALUES
                                         ('Sede Central Lima',      'Av. Javier Prado 456, San Isidro'),
                                         ('Zona Sur AQP',           'Av. Porongoche 123, Arequipa'),
                                         ('Estación Norte Trujillo', 'Av. América Norte 789, Trujillo');

-- 11. Regiones
INSERT INTO region (nombre) VALUES
                                ('Lima Metropolitana'),
                                ('Arequipa'),
                                ('La Libertad'),
                                ('Lambayeque');

-- 12. Tablas de responsables (maestras)
INSERT INTO jefatura_cliente_solicitante (descripcion) VALUES
                                                           ('Ing. Carla Mendoza - Claro'),
                                                           ('Sr. Roberto Díaz - Entel'),
                                                           ('Jefa Ana Torres - STL');

INSERT INTO analista_cliente_solicitante (descripcion) VALUES
                                                           ('Analista Pamela Ruiz - Claro'),
                                                           ('Analista Diego Castro - Entel'),
                                                           ('Analista Sofía Vargas - STL');

INSERT INTO coordinador_ti_cw (descripcion) VALUES
                                                ('Coord. Eduardo Silva - TI/CW'),
                                                ('Coord. Miguel Ángel - Energía'),
                                                ('Coord. Patricia López - PEXT');

INSERT INTO jefatura_responsable (descripcion) VALUES
                                                   ('Jefe Jorge Espinoza - COMFUTURA'),
                                                   ('Jefa Rosa Quispe - Operaciones');

INSERT INTO liquidador (descripcion) VALUES
                                         ('Liquidador Carlos Ramírez'),
                                         ('Liquidador Lucía Fernández');

INSERT INTO ejecutante (descripcion) VALUES
                                         ('Ejecutante Técnico Fibra - Juan'),
                                         ('Ejecutante Técnico Energía - Pedro');

INSERT INTO analista_contable (descripcion) VALUES
                                                ('Analista Contable María López'),
                                                ('Analista Contable Luis Gómez');

-- 13. Maestro de códigos (servicios/materiales)
INSERT INTO unidad_medida (codigo, descripcion) VALUES
                                                    ('UND',  'Unidad'),
                                                    ('HORA', 'Hora'),
                                                    ('JUEGO','Juego (set)'),
                                                    ('ML',   'Metro Lineal');

INSERT INTO maestro_codigo (codigo, descripcion, id_unidad_medida, precio_base) VALUES
                                                                                    ('S000001', 'Instalación punto cableado CAT6',           1,   85.00),
                                                                                    ('S000002', 'Horas hombre técnico fibra óptica',         2,   95.00),
                                                                                    ('S000003', 'Empalme por fusión fibra óptica',           1,  180.00),
                                                                                    ('S000004', 'Poste galvanizado 10m',                     1, 2850.00),
                                                                                    ('S000005', 'Tendido fibra óptica aérea (por ML)',       4,   45.00);

-- 14. Trabajadores (ejemplos)
INSERT INTO trabajador (nombres, apellidos, dni, celular, correo_corporativo, id_empresa, id_area, id_cargo) VALUES
                                                                                                                 ('Jorge Luis',    'Espinoza Vargas', '47891234', '987654321', 'jespinoza@comfutura.pe',   1, 15,  1),
                                                                                                                 ('Carlos Enrique','Ramírez Salazar', '45678901', '965432189', 'cramirez@comfutura.pe',    1,  4,  7),
                                                                                                                 ('Rosa Elena',    'Quispe Huamán',   '32165498', '991234567', 'rquispe@comfutura.pe',     1, 11,  9),
                                                                                                                 ('Lucía Fernanda','Sánchez Ortiz',   '36985214', '996543210', 'lsanchez@gab.pe',          2,  7, 14),
                                                                                                                 ('Miguel Ángel',  'Torres Gómez',    '41234567', '955667788', 'mtorres@comfutura.pe',     1,  5, 16);

-- 15. trabajador_cliente
INSERT INTO trabajador_cliente (id_trabajador, id_cliente) VALUES
                                                               (1,1), (1,2), (1,3),
                                                               (2,2), (2,3),
                                                               (3,3),
                                                               (4,2),
                                                               (5,2);

-- 16. Órdenes de Trabajo (OTS) - con responsables
INSERT INTO ots (
    ot, id_cliente, id_area, id_proyecto, id_fase, id_site, id_region,
    descripcion, fecha_apertura,
    id_jefatura_cliente_solicitante, id_analista_cliente_solicitante,
    id_coordinador_ti_cw, id_jefatura_responsable,
    id_liquidador, id_ejecutante, id_analista_contable
) VALUES
      (20250001, 2, 4, 1, 2, 1, 1,
       'Backbone fibra óptica edificio corporativo Claro', '2025-02-10',
       1, 1, 1, 1, 1, 1, 1),

      (20250002, 2, 5, 1, 3, 2, 2,
       'Reemplazo postes y tendido aéreo zona sur Claro', '2025-04-01',
       1, 1, 2, 2, 2, 2, 1),

      (20250003, 3, 7, 2, 1, 3, 3,
       'Mantenimiento energético estación norte Entel', '2025-03-15',
       2, 2, 3, 1, 1, 1, 2);

-- 17. ots_trabajador (asignaciones de personal)
INSERT INTO ots_trabajador (id_ots, id_trabajador, rol_en_ot) VALUES
                                                                  (1, 1, 'Responsable General'),
                                                                  (1, 2, 'Coordinador Técnico Energía'),
                                                                  (1, 5, 'Técnico Ejecutante Fibra'),
                                                                  (2, 4, 'Coordinador de Campo CW'),
                                                                  (3, 3, 'Jefa de Cierre'),
                                                                  (3, 5, 'Ejecutante Energía');

-- 18. Roles del sistema
INSERT INTO rol (nombre, descripcion) VALUES
                                          ('ADMIN',       'Administrador completo'),
                                          ('GERENCIA',    'Nivel gerencial'),
                                          ('SUPERVISOR',  'Supervisor de operaciones'),
                                          ('COORDINADOR', 'Coordinador de proyectos'),
                                          ('TECNICO',     'Técnico de campo');

-- 19. Usuarios (para login - ¡usa bcrypt/hashed en producción!)
INSERT INTO usuario (username, password, id_trabajador) VALUES
                                                            ('jespinoza',  '$2a$10$examplehashedpassword1234567890', 1),  -- admin2026 (hasheado)
                                                            ('cramirez',   '$2a$10$examplehashedpassword0987654321', 2),
                                                            ('rquispe',    '$2a$10$examplehashedpassword1122334455', 3),
                                                            ('mtorres',    '$2a$10$examplehashedpassword6677889900', 5);

-- 20. Asignación de roles a usuarios
INSERT INTO usuario_rol (id_usuario, id_rol) VALUES
                                                 (1, 1), (1, 2),  -- admin + gerencia
                                                 (2, 4),          -- coordinador
                                                 (3, 3),          -- supervisor
                                                 (4, 5);          -- técnico

-- 21. Relación cliente - área
INSERT INTO cliente_area (id_cliente, id_area) VALUES
                                                   -- COMFUTURA (propia)
                                                   (1,1), (1,2), (1,3), (1,10), (1,11), (1,12), (1,13), (1,14), (1,15),
                                                   -- CLARO PERÚ
                                                   (2,4), (2,5), (2,7), (2,9), (2,10), (2,12),
                                                   -- ENTEL PERÚ
                                                   (3,4), (3,5), (3,7), (3,9), (3,10), (3,12),
                                                   -- STL TELECOM
                                                   (4,4), (4,5), (4,7), (4,15);