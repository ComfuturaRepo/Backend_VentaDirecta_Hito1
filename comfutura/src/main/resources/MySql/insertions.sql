-- =====================================================
-- INSERCIONES COMPLETAS - ADAPTADAS AL ESQUEMA ACTUAL (sin ots_trabajador)
-- Orden estricto para evitar errores de FK
-- =====================================================

-- 1. Empresas
INSERT INTO empresa (nombre) VALUES
                                 ('COMFUTURA'),
                                 ('GAB'),
                                 ('SUDCOM');

-- 2. Clientes
INSERT INTO cliente (razon_social, ruc) VALUES
                                            ('COMFUTURA', '20601234567'),
                                            ('CLARO PERÚ', '20100123456'),
                                            ('ENTEL PERÚ', '20131234567'),
                                            ('STL TELECOM', '20598765432');

-- 3. Niveles
INSERT INTO nivel (codigo, nombre, descripcion) VALUES
                                                    ('L1', 'Gerencia General', 'Gerencia General / Dirección'),
                                                    ('L2', 'Gerencia / Subgerencia', 'Gerencia funcional y subgerencias'),
                                                    ('L3', 'Jefatura', 'Jefatura / Supervisión Senior'),
                                                    ('L4', 'Coordinación', 'Coordinación / Supervisión operativa'),
                                                    ('L5', 'Operativo', 'Ejecución / Técnicos / Asistentes');

-- 4. Áreas
INSERT INTO area (nombre) VALUES
                              ('RRHH'), ('COSTOS'), ('ADMINISTRATIVA'), ('ENERGIA'), ('CW'),
                              ('COMERCIAL'), ('PEXT'), ('SAQ'), ('ENTEL'), ('SSOMA'), ('TI'),
                              ('CIERRE'), ('CONTABILIDAD'), ('LOGISTICA'), ('GERENTE GENERAL');

-- 5. Cargos
INSERT INTO cargo (nombre, id_nivel) VALUES
                                         ('GERENTE GENERAL',         (SELECT id_nivel FROM nivel WHERE codigo = 'L1')),
                                         ('GERENTE COMERCIAL',       (SELECT id_nivel FROM nivel WHERE codigo = 'L1')),
                                         ('JEFA DE FINANZAS',        (SELECT id_nivel FROM nivel WHERE codigo = 'L1')),
                                         ('SUBGERENTE OPERACIONES',  (SELECT id_nivel FROM nivel WHERE codigo = 'L2')),
                                         ('JEFE TI',                 (SELECT id_nivel FROM nivel WHERE codigo = 'L2')),
                                         ('JEFE DE LOGÍSTICA',       (SELECT id_nivel FROM nivel WHERE codigo = 'L2')),
                                         ('JEFE CW - ENERGIA',       (SELECT id_nivel FROM nivel WHERE codigo = 'L2')),
                                         ('JEFE PEXT',               (SELECT id_nivel FROM nivel WHERE codigo = 'L2')),
                                         ('JEFE DE CIERRE',          (SELECT id_nivel FROM nivel WHERE codigo = 'L2')),
                                         ('SUPERVISOR CW',           (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('SUPERVISOR FIBRA',        (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('COORDINADOR TI',          (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('COORDINADOR ENERGIA',     (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('COORDINADOR PEXT',        (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('PROJECT MANAGER',         (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('TÉCNICO FIBRA ÓPTICA',    (SELECT id_nivel FROM nivel WHERE codigo = 'L5')),
                                         ('TÉCNICO ENERGÍA',         (SELECT id_nivel FROM nivel WHERE codigo = 'L5')),
                                         ('ASISTENTE DE RRHH',       (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('ANALISTA DE ENERGIA',     (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('SUPERVISORA DE OBRAS CIVILES', (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('ANALISTA PEXT',           (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('COORDINADOR DE INGENIERIA',(SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('JEFE LEGAL SAQ',          (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('ANALISTA LOGÍSTICO',      (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('COORDINADOR DE IMPLEMENTACION', (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('SUPERVISOR DE ENERGIA',   (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('ANALISTA FINANCIERO',     (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('CONTADORA',               (SELECT id_nivel FROM nivel WHERE codigo = 'L2')),
                                         ('AUXILIAR DE OFICINA',     (SELECT id_nivel FROM nivel WHERE codigo = 'L5')),
                                         ('CONSERJE',                (SELECT id_nivel FROM nivel WHERE codigo = 'L5')),
                                         ('ENCARGADO DE LIMPIEZA',   (SELECT id_nivel FROM nivel WHERE codigo = 'L5')),
                                         ('COORDINADOR TI CW',       (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
                                         ('JEFATURA RESPONSABLE',    (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('LIQUIDADOR',              (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
                                         ('EJECUTANTE',              (SELECT id_nivel FROM nivel WHERE codigo = 'L5')),
                                         ('ANALISTA CONTABLE',       (SELECT id_nivel FROM nivel WHERE codigo = 'L4'));

-- 6. Bancos
INSERT INTO banco (nombre) VALUES
                               ('BCP - Banco de Crédito del Perú'),
                               ('Interbank'),
                               ('BBVA Continental');

-- 7. Proveedores (ejemplo)
INSERT INTO proveedor (ruc, razon_social, contacto, telefono, correo, id_banco, numero_cuenta, moneda) VALUES
                                                                                                           ('20100123456', 'TECNOFIBRA SAC', 'Juan Pérez', '987-654-321', 'ventas@tecnofibra.pe', 1, '191-1234567-0-01', 'PEN'),
                                                                                                           ('20512345678', 'ELECTROREDES EIRL', 'María Gómez', '999-888-777', 'cotizaciones@electroredes.pe', 2, '0011-456789012345', 'PEN'),
                                                                                                           ('20604567890', 'INFRAESTRUCTURA ANDINA', 'Pedro López', '966-777-888', 'info@infraandina.pe', 3, '123456789012', 'PEN');

-- 8. Proyectos
INSERT INTO proyecto (nombre) VALUES
                                  ('Despliegue Fibra Metropolitana'),
                                  ('Mantenimiento Energético 2025'),
                                  ('Modernización CW 2026');

-- 9. Fases
INSERT INTO fase (nombre, orden) VALUES
                                     ('Planificación', 10),
                                     ('Ejecución', 20),
                                     ('Cierre', 30);

-- 10. Sites (códigos únicos)
INSERT INTO site (codigo_sitio, descripcion) VALUES
                                                 ('LI5761', 'NAT_ALTO_UNI'),
                                                 ('TL2411', 'NAT_HEFEI'),
                                                 ('TJ18728', 'CRUZ_DE_AVAYA'),
                                                 ('TJ2486', 'ESTADIO_VICTOR_RAUL'),
                                                 ('TJ4134', 'VICTOR_RAUL'),
                                                 ('TJ3784', 'NUEVO_TRUJILLO'),
                                                 ('TJ5236', 'CHUQUIMANCO'),
                                                 ('TJ5187', 'BLAS_PASCAL'),
                                                 ('TJ5157', 'EL_BOSQUE'),
                                                 ('TP6291', 'HUANCABAMBA');

-- 11. Regiones
INSERT INTO region (nombre) VALUES
                                ('Lima Metropolitana'),
                                ('Arequipa'),
                                ('La Libertad'),
                                ('Lambayeque');

-- 12. Jefaturas y analistas cliente
INSERT INTO jefatura_cliente_solicitante (descripcion) VALUES
                                                           ('Ing. Carla Mendoza - Claro'),
                                                           ('Sr. Roberto Díaz - Entel'),
                                                           ('Jefa Ana Torres - STL');

INSERT INTO analista_cliente_solicitante (descripcion) VALUES
                                                           ('Analista Pamela Ruiz - Claro'),
                                                           ('Analista Diego Castro - Entel'),
                                                           ('Analista Sofía Vargas - STL');

-- 13. Estados OT
INSERT INTO estado_ot (descripcion) VALUES
                                        ('ASIGNACION'),
                                        ('PRESUPUESTO ENVIADO'),
                                        ('CREACION DE OC'),
                                        ('EN_EJECUCION'),
                                        ('EN_LIQUIDACION'),
                                        ('EN_FACTURACION'),
                                        ('FINALIZADO'),
                                        ('CANCELADA');

-- 14. Trabajadores (clave: dni único)
INSERT INTO trabajador (nombres, apellidos, dni, celular, correo_corporativo, id_empresa, id_area, id_cargo) VALUES
    ('Jorge Luis', 'Espinoza Vargas', '47891234', '987654321', 'jespinoza@comfutura.pe', 1, 15, (SELECT id_cargo FROM cargo WHERE nombre = 'GERENTE GENERAL' LIMIT 1)),
    ('Carlos Enrique', 'Ramírez Salazar', '45678901', '965432189', 'cramirez@comfutura.pe', 1, 4, (SELECT id_cargo FROM cargo WHERE nombre = 'JEFE CW - ENERGIA' LIMIT 1)),
    ('Rosa Elena', 'Quispe Huamán', '32165498', '991234567', 'rquispe@comfutura.pe', 1, 11, (SELECT id_cargo FROM cargo WHERE nombre = 'JEFE DE CIERRE' LIMIT 1)),
    ('Lucía Fernanda', 'Sánchez Ortiz', '36985214', '996543210', 'lsanchez@gab.pe', 2, 7, (SELECT id_cargo FROM cargo WHERE nombre = 'COORDINADOR PEXT' LIMIT 1)),
    ('Miguel Ángel', 'Torres Gómez', '41234567', '955667788', 'mtorres@comfutura.pe', 1, 5, (SELECT id_cargo FROM cargo WHERE nombre = 'TÉCNICO ENERGÍA' LIMIT 1)),
    ('FRANKLIN', 'MERINO MONDRAGÓN', '45609714', '993546497', 'fmerino@comfutura.com', 1, 9, (SELECT id_cargo FROM cargo WHERE nombre = 'COORDINADOR TI CW' LIMIT 1)),
    ('LUIS FERNANDO', 'ÑIQUEN GOMEZ', '77684556', '921618806', 'lniquen@comfutura.com', 1, 4, (SELECT id_cargo FROM cargo WHERE nombre = 'JEFATURA RESPONSABLE' LIMIT 1)),
    ('JOSUE MANUEL RAUL', 'OTERO LOJE', '10731488', '987515878', 'jotero@comfutura.com', 1, 14, (SELECT id_cargo FROM cargo WHERE nombre = 'LIQUIDADOR' LIMIT 1)),
    ('MIGUEL ANGEL', 'SALAS CAMPOS', '32978147', '983276485', 'msalas@comfutura.com', 1, 5, (SELECT id_cargo FROM cargo WHERE nombre = 'EJECUTANTE' LIMIT 1)),
    ('ELIZABETH', 'MENDEZ NAVARRO', '06656880', '983276483', 'requerimiento@comfutura.com', 1, 13, (SELECT id_cargo FROM cargo WHERE nombre = 'ANALISTA CONTABLE' LIMIT 1));

-- 15. Usuarios (ejemplos)
INSERT INTO usuario (username, password, id_trabajador, id_nivel) VALUES
                                                                      ('fmerino', '123456', (SELECT id_trabajador FROM trabajador WHERE dni = '45609714' LIMIT 1), (SELECT id_nivel FROM nivel WHERE codigo = 'L4')),
    ('lniquen', '123456', (SELECT id_trabajador FROM trabajador WHERE dni = '77684556' LIMIT 1), (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
    ('jotero',  '123456', (SELECT id_trabajador FROM trabajador WHERE dni = '10731488' LIMIT 1), (SELECT id_nivel FROM nivel WHERE codigo = 'L3')),
    ('msalas',  '123456', (SELECT id_trabajador FROM trabajador WHERE dni = '32978147' LIMIT 1), (SELECT id_nivel FROM nivel WHERE codigo = 'L5')),
    ('emendez', '123456', (SELECT id_trabajador FROM trabajador WHERE dni = '06656880' LIMIT 1), (SELECT id_nivel FROM nivel WHERE codigo = 'L4'));

-- 16. Órdenes de Trabajo (OTS) → con id_trabajador obligatorio
INSERT INTO ots (
    ot, id_cliente, id_area, id_proyecto, id_fase, id_site, id_region,
    descripcion, fecha_apertura,
    id_jefatura_cliente_solicitante, id_analista_cliente_solicitante,
    id_coordinador_ti_cw, id_jefatura_responsable,
    id_liquidador, id_ejecutante, id_analista_contable,
    id_trabajador, id_estado_ot
) VALUES
      -- OT 1 - Claro
      (20250001,
       (SELECT id_cliente FROM cliente WHERE razon_social = 'CLARO PERÚ' LIMIT 1),
      (SELECT id_area FROM area WHERE nombre = 'ENERGIA' LIMIT 1),
      (SELECT id_proyecto FROM proyecto WHERE nombre = 'Despliegue Fibra Metropolitana' LIMIT 1),
      (SELECT id_fase FROM fase WHERE nombre = 'Ejecución' LIMIT 1),
      (SELECT id_site FROM site WHERE codigo_sitio = 'LI5761' LIMIT 1),
      (SELECT id_region FROM region WHERE nombre = 'Lima Metropolitana' LIMIT 1),
    'Backbone fibra óptica edificio corporativo Claro', '2025-02-10',
      (SELECT id FROM jefatura_cliente_solicitante WHERE descripcion = 'Ing. Carla Mendoza - Claro' LIMIT 1),
      (SELECT id FROM analista_cliente_solicitante WHERE descripcion = 'Analista Pamela Ruiz - Claro' LIMIT 1),
      (SELECT id_trabajador FROM trabajador WHERE dni = '45609714' LIMIT 1),
      (SELECT id_trabajador FROM trabajador WHERE dni = '77684556' LIMIT 1),
      (SELECT id_trabajador FROM trabajador WHERE dni = '10731488' LIMIT 1),
      (SELECT id_trabajador FROM trabajador WHERE dni = '32978147' LIMIT 1),
      (SELECT id_trabajador FROM trabajador WHERE dni = '06656880' LIMIT 1),
      (SELECT id_trabajador FROM trabajador WHERE dni = '45609714' LIMIT 1),  -- ← id_trabajador obligatorio (ej: coordinador)
      (SELECT id_estado_ot FROM estado_ot WHERE descripcion = 'ASIGNACION' LIMIT 1)),

    -- OT 2 - Claro (ejemplo 2)
    (20250002,
     (SELECT id_cliente FROM cliente WHERE razon_social = 'CLARO PERÚ' LIMIT 1),
     (SELECT id_area FROM area WHERE nombre = 'CW' LIMIT 1),
     (SELECT id_proyecto FROM proyecto WHERE nombre = 'Despliegue Fibra Metropolitana' LIMIT 1),
     (SELECT id_fase FROM fase WHERE nombre = 'Cierre' LIMIT 1),
     (SELECT id_site FROM site WHERE codigo_sitio = 'TL2411' LIMIT 1),
     (SELECT id_region FROM region WHERE nombre = 'Arequipa' LIMIT 1),
     'Reemplazo postes y tendido aéreo zona sur Claro', '2025-04-01',
     (SELECT id FROM jefatura_cliente_solicitante WHERE descripcion = 'Ing. Carla Mendoza - Claro' LIMIT 1),
     (SELECT id FROM analista_cliente_solicitante WHERE descripcion = 'Analista Pamela Ruiz - Claro' LIMIT 1),
     (SELECT id_trabajador FROM trabajador WHERE dni = '45609714' LIMIT 1),
     (SELECT id_trabajador FROM trabajador WHERE dni = '77684556' LIMIT 1),
     (SELECT id_trabajador FROM trabajador WHERE dni = '10731488' LIMIT 1),
     (SELECT id_trabajador FROM trabajador WHERE dni = '32978147' LIMIT 1),
     (SELECT id_trabajador FROM trabajador WHERE dni = '06656880' LIMIT 1),
     (SELECT id_trabajador FROM trabajador WHERE dni = '45609714' LIMIT 1),
     (SELECT id_estado_ot FROM estado_ot WHERE descripcion = 'ASIGNACION' LIMIT 1)),

    -- OT 3 - Entel
    (20250003,
     (SELECT id_cliente FROM cliente WHERE razon_social = 'ENTEL PERÚ' LIMIT 1),
     (SELECT id_area FROM area WHERE nombre = 'PEXT' LIMIT 1),
     (SELECT id_proyecto FROM proyecto WHERE nombre = 'Mantenimiento Energético 2025' LIMIT 1),
     (SELECT id_fase FROM fase WHERE nombre = 'Planificación' LIMIT 1),
     (SELECT id_site FROM site WHERE codigo_sitio = 'TJ18728' LIMIT 1),
     (SELECT id_region FROM region WHERE nombre = 'La Libertad' LIMIT 1),
     'Mantenimiento energético estación norte Entel', '2025-03-15',
     (SELECT id FROM jefatura_cliente_solicitante WHERE descripcion = 'Sr. Roberto Díaz - Entel' LIMIT 1),
     (SELECT id FROM analista_cliente_solicitante WHERE descripcion = 'Analista Diego Castro - Entel' LIMIT 1),
     (SELECT id_trabajador FROM trabajador WHERE dni = '45609714' LIMIT 1),
     (SELECT id_trabajador FROM trabajador WHERE dni = '77684556' LIMIT 1),
     (SELECT id_trabajador FROM trabajador WHERE dni = '10731488' LIMIT 1),
     (SELECT id_trabajador FROM trabajador WHERE dni = '32978147' LIMIT 1),
     (SELECT id_trabajador FROM trabajador WHERE dni = '06656880' LIMIT 1),
     (SELECT id_trabajador FROM trabajador WHERE dni = '45609714' LIMIT 1),
     (SELECT id_estado_ot FROM estado_ot WHERE descripcion = 'EN_EJECUCION' LIMIT 1));

-- 17. Estados de Orden de Compra (sin cambios)
INSERT INTO estado_oc (nombre) VALUES
                                   ('PENDIENTE'), ('APROBADA'), ('RECHAZADA'), ('ANULADA'),
                                   ('EN PROCESO'), ('ATENDIDA'), ('CERRADA');-- =====================================================
-- RELACIONES CLIENTE - ÁREA (tabla cliente_area)
-- =====================================================

-- COMFUTURA (empresa propia) → acceso a TODAS las áreas internas
INSERT IGNORE INTO cliente_area (id_cliente, id_area, activo) VALUES
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'COMFUTURA'), (SELECT id_area FROM area WHERE nombre = 'RRHH'),           1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'COMFUTURA'), (SELECT id_area FROM area WHERE nombre = 'COSTOS'),          1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'COMFUTURA'), (SELECT id_area FROM area WHERE nombre = 'ADMINISTRATIVA'),  1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'COMFUTURA'), (SELECT id_area FROM area WHERE nombre = 'ENERGIA'),         1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'COMFUTURA'), (SELECT id_area FROM area WHERE nombre = 'CW'),              1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'COMFUTURA'), (SELECT id_area FROM area WHERE nombre = 'COMERCIAL'),       1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'COMFUTURA'), (SELECT id_area FROM area WHERE nombre = 'PEXT'),            1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'COMFUTURA'), (SELECT id_area FROM area WHERE nombre = 'SAQ'),             1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'COMFUTURA'), (SELECT id_area FROM area WHERE nombre = 'SSOMA'),           1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'COMFUTURA'), (SELECT id_area FROM area WHERE nombre = 'TI'),              1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'COMFUTURA'), (SELECT id_area FROM area WHERE nombre = 'CIERRE'),          1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'COMFUTURA'), (SELECT id_area FROM area WHERE nombre = 'CONTABILIDAD'),    1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'COMFUTURA'), (SELECT id_area FROM area WHERE nombre = 'LOGISTICA'),       1);

-- CLARO PERÚ → áreas típicas de cliente externo (CW, ENERGIA, PEXT, SAQ, TI, etc.)
INSERT IGNORE INTO cliente_area (id_cliente, id_area, activo) VALUES
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'CLARO PERÚ'), (SELECT id_area FROM area WHERE nombre = 'ENERGIA'),     1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'CLARO PERÚ'), (SELECT id_area FROM area WHERE nombre = 'CW'),           1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'CLARO PERÚ'), (SELECT id_area FROM area WHERE nombre = 'PEXT'),         1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'CLARO PERÚ'), (SELECT id_area FROM area WHERE nombre = 'SAQ'),          1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'CLARO PERÚ'), (SELECT id_area FROM area WHERE nombre = 'TI'),           1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'CLARO PERÚ'), (SELECT id_area FROM area WHERE nombre = 'CIERRE'),       1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'CLARO PERÚ'), (SELECT id_area FROM area WHERE nombre = 'SSOMA'),        1);

-- ENTEL PERÚ → similar a Claro, pero con énfasis en algunas áreas específicas
INSERT IGNORE INTO cliente_area (id_cliente, id_area, activo) VALUES
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'ENTEL PERÚ'), (SELECT id_area FROM area WHERE nombre = 'ENERGIA'),     1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'ENTEL PERÚ'), (SELECT id_area FROM area WHERE nombre = 'CW'),           1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'ENTEL PERÚ'), (SELECT id_area FROM area WHERE nombre = 'PEXT'),         1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'ENTEL PERÚ'), (SELECT id_area FROM area WHERE nombre = 'SAQ'),          1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'ENTEL PERÚ'), (SELECT id_area FROM area WHERE nombre = 'TI'),           1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'ENTEL PERÚ'), (SELECT id_area FROM area WHERE nombre = 'ENTEL'),        1);  -- Área exclusiva de Entel

-- STL TELECOM → cliente más pequeño, solo algunas áreas operativas
INSERT IGNORE INTO cliente_area (id_cliente, id_area, activo) VALUES
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'STL TELECOM'), (SELECT id_area FROM area WHERE nombre = 'CW'),         1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'STL TELECOM'), (SELECT id_area FROM area WHERE nombre = 'ENERGIA'),     1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'STL TELECOM'), (SELECT id_area FROM area WHERE nombre = 'TI'),          1),
    ((SELECT id_cliente FROM cliente WHERE razon_social = 'STL TELECOM'), (SELECT id_area FROM area WHERE nombre = 'CIERRE'),      1);

INSERT INTO unidad_medida (codigo, descripcion) VALUES
                                                    ('UND',  'Unidad'),
                                                    ('M',    'Metro(s)'),
                                                    ('M2',   'Metro cuadrado'),
                                                    ('M3',   'Metro cúbico'),
                                                    ('KG',   'Kilogramo(s)'),
                                                    ('HORA', 'Hora(s)'),
                                                    ('DIA',  'Día(s)'),
                                                    ('MES',  'Mes(es)'),
                                                    ('JORN', 'Jornada(s)'),
                                                    ('PZA',  'Pieza(s)'),
                                                    ('KIT',  'Kit / Set'),
                                                    ('GLN',  'Galón');

INSERT INTO maestro_codigo (codigo, descripcion, id_unidad_medida, precio_base) VALUES
                                                                                    ('S000001', 'Cable fibra óptica monomodo 12 hilos ADSS',
                                                                                     (SELECT id_unidad_medida FROM unidad_medida WHERE codigo = 'M'),  3.80),

                                                                                    ('S000002', 'Poste de concreto 9 metros clase B',
                                                                                     (SELECT id_unidad_medida FROM unidad_medida WHERE codigo = 'UND'),  285.00),

                                                                                    ('S000003', 'Caja de derivación FTTH IP65 8 puertos',
                                                                                     (SELECT id_unidad_medida FROM unidad_medida WHERE codigo = 'UND'),  45.50),

                                                                                    ('S000004', 'Mano de obra tendido aéreo fibra óptica (incluye herrajes)',
                                                                                     (SELECT id_unidad_medida FROM unidad_medida WHERE codigo = 'M'),  8.90),

                                                                                    ('S000005', 'Mano de obra instalación poste concreto (excavación + izado)',
                                                                                     (SELECT id_unidad_medida FROM unidad_medida WHERE codigo = 'UND'),  320.00),

                                                                                    ('S000006', 'Energía temporal generador 20 KVA (incluye combustible y operador)',
                                                                                     (SELECT id_unidad_medida FROM unidad_medida WHERE codigo = 'HORA'),  65.00),

                                                                                    ('S000007', 'Revisión y mantenimiento UPS 10 KVA',
                                                                                     (SELECT id_unidad_medida FROM unidad_medida WHERE codigo = 'UND'),  450.00),

                                                                                    ('S000008', 'Kit de empalmes fibra óptica (splices + protección)',
                                                                                     (SELECT id_unidad_medida FROM unidad_medida WHERE codigo = 'KIT'),  18.75);

INSERT INTO orden_compra (
    id_estado_oc,
    id_ots,
    id_maestro,
    id_proveedor,
    cantidad,
    costo_unitario,
    observacion
) VALUES
      -- OC para OT 20250001 (Claro - backbone fibra)
      (
          (SELECT id_estado_oc FROM estado_oc WHERE nombre = 'APROBADA' LIMIT 1),
      (SELECT id_ots FROM ots WHERE ot = 20250001 LIMIT 1),
      (SELECT id_maestro FROM maestro_codigo WHERE codigo = 'S000001' LIMIT 1),
      (SELECT id_proveedor FROM proveedor WHERE ruc = '20100123456' LIMIT 1),  -- TECNOFIBRA
    850.00,
    3.80,
    'Suministro cable ADSS para backbone edificio corporativo'
    ),

    (
        (SELECT id_estado_oc FROM estado_oc WHERE nombre = 'EN PROCESO' LIMIT 1),
        (SELECT id_ots FROM ots WHERE ot = 20250001 LIMIT 1),
        (SELECT id_maestro FROM maestro_codigo WHERE codigo = 'S000004' LIMIT 1),
        (SELECT id_proveedor FROM proveedor WHERE ruc = '20100123456' LIMIT 1),
        1200.00,
        8.90,
        'Tendido aéreo tramo 1 - 2do piso'
    ),

    -- OC para OT 20250002 (Claro - postes y tendido aéreo)
    (
        (SELECT id_estado_oc FROM estado_oc WHERE nombre = 'PENDIENTE' LIMIT 1),
        (SELECT id_ots FROM ots WHERE ot = 20250002 LIMIT 1),
        (SELECT id_maestro FROM maestro_codigo WHERE codigo = 'S000002' LIMIT 1),
        (SELECT id_proveedor FROM proveedor WHERE ruc = '20512345678' LIMIT 1),  -- ELECTROREDES
        18.00,
        285.00,
        'Reemplazo postes dañados por vientos fuertes zona sur'
    ),

    -- OC para OT 20250003 (Entel - mantenimiento energético)
    (
        (SELECT id_estado_oc FROM estado_oc WHERE nombre = 'ATENDIDA' LIMIT 1),
        (SELECT id_ots FROM ots WHERE ot = 20250003 LIMIT 1),
        (SELECT id_maestro FROM maestro_codigo WHERE codigo = 'S000006' LIMIT 1),
        (SELECT id_proveedor FROM proveedor WHERE ruc = '20604567890' LIMIT 1),  -- INFRAESTRUCTURA ANDINA
        96.00,
        65.00,
        'Alquiler generador durante corte programado de 4 días'
    ),

    (
        (SELECT id_estado_oc FROM estado_oc WHERE nombre = 'CERRADA' LIMIT 1),
        (SELECT id_ots FROM ots WHERE ot = 20250003 LIMIT 1),
        (SELECT id_maestro FROM maestro_codigo WHERE codigo = 'S000007' LIMIT 1),
        (SELECT id_proveedor FROM proveedor WHERE ruc = '20100123456' LIMIT 1),
        2.00,
        450.00,
        'Mantenimiento preventivo UPS estación norte'
    );