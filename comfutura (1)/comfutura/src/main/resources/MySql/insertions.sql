-- Empresas
INSERT INTO empresa (nombre) VALUES
                                 ('COMFUTURA'),
                                 ('GAB'),
                                 ('SUDCOM');


-- Clientes
INSERT INTO cliente (razon_social, ruc) VALUES
                                            ('COMFUTURA',     '20601234567'),
                                            ('CLARO PERÚ',    '20100123456'),
                                            ('ENTEL PERÚ',    '20131234567'),
                                            ('STL TELECOM',   '20598765432');


-- Niveles
INSERT INTO nivel (codigo, nombre, descripcion) VALUES
                                                    ('L1', 'Gerencia General',      'Gerencia General / Dirección'),
                                                    ('L2', 'Gerencia / Subgerencia','Gerencia funcional y subgerencias'),
                                                    ('L3', 'Jefatura',              'Jefatura / Supervisión Senior'),
                                                    ('L4', 'Coordinación',          'Coordinación / Supervisión operativa'),
                                                    ('L5', 'Operativo',             'Ejecución / Técnicos / Asistentes');


-- Áreas
INSERT INTO area (nombre) VALUES
                              ('RRHH'), ('COSTOS'), ('ADMINISTRATIVA'), ('ENERGIA'), ('CW'),
                              ('COMERCIAL'), ('PEXT'), ('SAQ'), ('ENTEL'), ('SSOMA'), ('TI'),
                              ('CIERRE'), ('CONTABILIDAD'), ('LOGISTICA'), ('GERENTE GENERAL'),
                              ('FINANZAS'), ('CW-ENERGIA'), ('LIMPIEZA');


-- Cargos
-- Cargos (todos los que proporcionaste)
INSERT INTO cargo (nombre, id_nivel) VALUES
                                         ('GERENTE', 1),
                                         ('GERENTE COMERCIAL', 1),
                                         ('JEFA DE FINANZAS', 1),
                                         ('SUB GERENTE', 2),
                                         ('JEFE TI', 2),
                                         ('JEFE DE LOGÍSTICA', 2),
                                         ('JEFE CW - JEFE DE ENERGIA', 2),
                                         ('JEFE PEXT', 2),
                                         ('JEFE DE CIERRE', 2),
                                         ('JEFE LEGAL SAQ', 2),
                                         ('JEFE PEXT PINT', 2),
                                         ('JEFE DE CIERRE Y LIQUIDACIONES', 2),
                                         ('SUPERVISOR', 3),
                                         ('SUPERVISOR TI', 3),
                                         ('SUPERVISOR CW', 3),
                                         ('SUPERVISOR DE ENERGIA', 3),
                                         ('SUPERVISOR PEXT', 3),
                                         ('SUPERVISORA DE SSOMA', 3),
                                         ('SUPERVISORA DE OBRAS CIVILES', 3),
                                         ('COORDINADOR TI', 4),
                                         ('COORDINADOR DE ENERGIA', 4),
                                         ('COORDINADOR DE CW', 4),
                                         ('COORDINADOR PEXT', 4),
                                         ('COORDINADOR LEGAL SAQ', 4),
                                         ('COORDINADOR DE IMPLEMENTACION', 4),
                                         ('COORDINADOR PLANTA INTERNA', 4),
                                         ('COORDINADOR DE INGENIERIA', 4),
                                         ('PROJECT MANAGER', 4),
                                         ('ANALISTA FINANCIERO', 4),
                                         ('ANALISTA DE ENERGIA', 4),
                                         ('ANALISTA PEXT', 4),
                                         ('ANALISTA LOGÍSTICO', 4),
                                         ('ANALISTA LEGAL SAQ', 4),
                                         ('ASISTENTE DE RRHH', 5),
                                         ('ASISTENTE DE CONTABILIDAD', 5),
                                         ('ASISTENTE LOGISTICO', 5),
                                         ('ASISTENTE LOGISTICO ENTEL', 5),
                                         ('ASISTENTE DE CONTRATACIONES PUBLICAS', 5),
                                         ('AUXILIAR DE OFICINA - DISCPACID', 5),
                                         ('AUXILIAR DE ALMACEN', 5),
                                         ('CONSERJE - DISCAPACIDAD', 5),
                                         ('CONDUCTOR', 5),
                                         ('CADISTA', 5),
                                         ('PRACTICANTE DE ENERGIA', 5),
                                         ('ENCARGADO DE LIMPIEZA', 5),
                                         ('GESTOR DE ACCESOS', 5),
                                         ('CONSULTOR EXTERNO', 5);

-- Trabajadores (ejemplos representativos)
INSERT INTO trabajador (
    nombres, apellidos, dni, celular, correo_corporativo,
    id_empresa, id_area, id_cargo, activo
) VALUES
      ('Jorge Luis',    'Espinoza Vargas',   '47891234', '987654321', 'jespinoza@comfutura.pe',   1, 15,  1, 1),
      ('Carlos Enrique','Ramírez Salazar',   '45678901', '965432189', 'cramirez@comfutura.pe',    1,  4, 12, 1),
      ('Rosa Elena',    'Quispe Huamán',     '32165498', '991234567', 'rquispe@comfutura.pe',     1, 11,  9, 1),
      ('Lucía Fernanda','Sánchez Ortiz',     '36985214', '996543210', 'lsanchez@gab.pe',          2,  7, 14, 1),
      ('José Antonio',  'Pérez Castillo',    '14725836', '992345678', 'jperez@gab.pe',            2,  4, 11, 1),
      ('Raúl',          'Herrera Soto',      '15975328', '994445556', 'rherrera@sudcom.pe',       3, 12,  8, 1),
      ('Valeria',       'Jiménez Prado',     '75315984', '995556667', 'vjimenez@sudcom.pe',       3, 14, 16, 1),
      ('Fernando',      'Rojas Guzmán',      '96385274', '991112223', 'frojas@sudcom.pe',         3,  5, 14, 1);


-- trabajador_cliente
INSERT INTO trabajador_cliente (id_trabajador, id_cliente) VALUES
                                                               (1,1), (1,2), (1,3), (1,4),
                                                               (2,2), (2,3),
                                                               (3,3),
                                                               (4,4),
                                                               (5,2),
                                                               (6,4),
                                                               (7,2), (7,4),
                                                               (8,2);


-- Bancos
INSERT INTO banco (nombre) VALUES
                               ('BCP - Banco de Crédito del Perú'),
                               ('Interbank'),
                               ('BBVA Continental'),
                               ('Scotiabank Perú'),
                               ('BanBif'),
                               ('Banco de la Nación'),
                               ('Banco Falabella');


-- Proveedores
INSERT INTO proveedor (
    ruc, razon_social, direccion, distrito, provincia, departamento,
    contacto, telefono, correo, id_banco, numero_cuenta, moneda, activo
) VALUES
      ('20100123456', 'TECNOFIBRA SAC',           'Av. La Marina 2565', 'San Miguel', 'Lima', 'Lima', 'Juan Pérez',   '987-654-321', 'ventas@tecnofibra.pe',     1, '191-1234567-0-01', 'PEN', 1),
      ('20512345678', 'ELECTROREDES EIRL',        'Calle Los Faisanes 145', 'Surco',   'Lima', 'Lima', 'María Gómez',  '999-888-777', 'cotizaciones@electroredes.pe', 3, '0011-456789012345', 'PEN', 1),
      ('20198765432', 'SERV. ENERGÉTICOS DEL SUR', 'Av. Arequipa 3456', 'Miraflores', 'Lima', 'Lima', 'Luis Fernández','944-332-110', 'proyectos@senersur.pe',    1, '191-9876543-0-99', 'USD', 1);


-- Unidades de medida
INSERT INTO unidad_medida (codigo, descripcion) VALUES
                                                    ('UND', 'Unidad'),
                                                    ('JUEGO', 'Juego (set)'),
                                                    ('HORA', 'Hora'),
                                                    ('DIA', 'Día'),
                                                    ('MES', 'Mes'),
                                                    ('M2', 'Metro cuadrado'),
                                                    ('M3', 'Metro cúbico'),
                                                    ('KG', 'Kilogramo'),
                                                    ('GLB', 'Globo / Global'),
                                                    ('SERV', 'Servicio (concepto general)');


-- Maestro de códigos / servicios
INSERT INTO maestro_codigo (codigo, descripcion, id_unidad_medida, precio_base, activo) VALUES
                                                                                            ('S000001', 'Instalación punto cableado CAT6',        1,   85.00, 1),
                                                                                            ('S000002', 'Horas hombre técnico fibra óptica',      2,   95.00, 1),
                                                                                            ('S000003', 'Empalme por fusión fibra óptica',        1,  180.00, 1),
                                                                                            ('S000004', 'Poste galvanizado 10m',                  1, 2850.00, 1);


-- Órdenes de trabajo (OTS)
INSERT INTO ots (
    ot, ceco, id_cliente, id_area, descripcion, fecha_apertura, activo
) VALUES
      (20250001, 'CC-ENER-2025', 2,  4, 'Backbone fibra óptica edificio corporativo', '2025-02-10', 1),
      (20250002, 'CC-CLARO-025', 2,  5, 'Reemplazo postes y tendido aéreo zona sur',   '2025-04-01', 1);


-- ots_trabajador
INSERT INTO ots_trabajador (id_ots, id_trabajador, rol_en_ot) VALUES
                                                                  (1, 1, 'Responsable General'),
                                                                  (1, 2, 'Coordinador Técnico'),
                                                                  (1, 3, 'Supervisor Técnico'),
                                                                  (2, 5, 'Coordinador de Campo'),
                                                                  (2, 6, 'Jefe de Cierre');


-- ots_detalle
INSERT INTO ots_detalle (id_ots, id_maestro, id_proveedor, cantidad, precio_unitario) VALUES
                                                                                          (1, 1, 1, 320.00,  85.00),   -- puntos cableado
                                                                                          (1, 2, 1, 180.00,  95.00),   -- horas fibra
                                                                                          (1, 3, 1,  45.00, 180.00),   -- empalmes
                                                                                          (2, 4, 1,  18.00, 2850.00);  -- postes


-- Roles (seguridad)
INSERT INTO rol (nombre, descripcion) VALUES
                                          ('ADMIN',       'Administrador completo del sistema'),
                                          ('GERENCIA',    'Acceso nivel gerencial'),
                                          ('SUPERVISOR',  'Supervisor de operaciones'),
                                          ('COORDINADOR', 'Coordinador de proyectos'),
                                          ('TECNICO',     'Personal de campo');


-- Usuarios (contraseñas en plano → SOLO pruebas!)
INSERT INTO usuario (username, password, id_trabajador, activo) VALUES
                                                                    ('jespinoza',   'admin2026',  1, 1),
                                                                    ('cramirez',    'energia26',  2, 1),
                                                                    ('rquispe',     'ti2026',     3, 1),
                                                                    ('lsanchez',    'pext2026',   4, 1);


-- usuario_rol
INSERT INTO usuario_rol (id_usuario, id_rol) VALUES
                                                 (1, 1),  -- ADMIN
                                                 (1, 2),  -- GERENCIA
                                                 (2, 3),  -- SUPERVISOR
                                                 (3, 3),  -- SUPERVISOR
                                                 (4, 4);  -- COORDINADOR