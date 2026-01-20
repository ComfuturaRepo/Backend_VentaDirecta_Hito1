-- 1. Empresas
INSERT INTO empresa (nombre) VALUES
                                 ('COMFUTURA'),
                                 ('GAB'),
                                 ('SUDCOM');

-- 2. Clientes
INSERT INTO cliente (razon_social, ruc) VALUES
                                            ('COMFUTURA',     '20601234567'),
                                            ('CLARO PERÚ',    '20100123456'),
                                            ('ENTEL PERÚ',    '20131234567'),
                                            ('STL TELECOM',   '20598765432');

-- 3. Niveles
INSERT INTO nivel (codigo, nombre, descripcion) VALUES
                                                    ('L1', 'Gerencia General',      'Gerencia General / Dirección'),
                                                    ('L2', 'Gerencia / Subgerencia','Gerencia funcional y subgerencias'),
                                                    ('L3', 'Jefatura',              'Jefatura / Supervisión Senior'),
                                                    ('L4', 'Coordinación',          'Coordinación / Supervisión operativa'),
                                                    ('L5', 'Operativo',             'Ejecución / Técnicos / Asistentes');

-- 4. Áreas (usadas en los ejemplos)
INSERT INTO area (nombre) VALUES
                              ('RRHH'), ('COSTOS'), ('ADMINISTRATIVA'), ('ENERGIA'), ('CW'),
                              ('COMERCIAL'), ('PEXT'), ('SAQ'), ('ENTEL'), ('SSOMA'), ('TI'),
                              ('CIERRE'), ('CONTABILIDAD'), ('LOGISTICA'), ('GERENTE GENERAL');

-- 5. Cargos (solo algunos para no extender demasiado, puedes agregar el resto)
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
                                         ('SUPERVISOR', 3),
                                         ('SUPERVISOR CW', 3),
                                         ('COORDINADOR TI', 4),
                                         ('COORDINADOR DE ENERGIA', 4),
                                         ('COORDINADOR PEXT', 4),
                                         ('PROJECT MANAGER', 4),
                                         ('TECNICO DE FIBRA', 5);

-- 6. Bancos
INSERT INTO banco (nombre) VALUES
                               ('BCP - Banco de Crédito del Perú'),
                               ('Interbank'),
                               ('BBVA Continental');

-- 7. Proyectos (necesarios para ots)
INSERT INTO proyecto (nombre, descripcion) VALUES
                                               ('Despliegue Fibra Metropolitana', 'Proyectos FTTH y backbone'),
                                               ('Mantenimiento Energético 2025',  'Energía y grupos para estaciones');

-- 8. Fases
INSERT INTO fase (nombre, orden) VALUES
                                     ('Planificación', 10),
                                     ('Ejecución',     20),
                                     ('Cierre',        30);

-- 9. Sites
INSERT INTO site (nombre, direccion) VALUES
                                         ('Sede Central Lima', 'Av. Javier Prado 456, San Isidro'),
                                         ('Zona Sur AQP',      'Av. Porongoche 123, Arequipa');

-- 10. Regiones
INSERT INTO region (nombre) VALUES
                                ('Lima Metropolitana'),
                                ('Arequipa'),
                                ('Norte'),
                                ('Sur');

-- 11. Maestro de códigos
INSERT INTO unidad_medida (codigo, descripcion) VALUES
                                                    ('UND',  'Unidad'),
                                                    ('HORA', 'Hora'),
                                                    ('JUEGO','Juego (set)');

INSERT INTO maestro_codigo (codigo, descripcion, id_unidad_medida, precio_base) VALUES
                                                                                    ('S000001', 'Instalación punto cableado CAT6',         1,  85.00),
                                                                                    ('S000002', 'Horas hombre técnico fibra óptica',       2,  95.00),
                                                                                    ('S000003', 'Empalme por fusión fibra óptica',         1, 180.00),
                                                                                    ('S000004', 'Poste galvanizado 10m',                   1,2850.00);

-- 12. Proveedores (ejemplo mínimo)
INSERT INTO proveedor (ruc, razon_social, contacto, telefono, correo, id_banco, numero_cuenta, moneda) VALUES
                                                                                                           ('20100123456', 'TECNOFIBRA SAC',          'Juan Pérez',   '987-654-321', 'ventas@tecnofibra.pe',     1, '191-1234567-0-01', 'PEN'),
                                                                                                           ('20512345678', 'ELECTROREDES EIRL',       'María Gómez',  '999-888-777', 'cotizaciones@electroredes.pe', 2, '0011-456789012345', 'PEN');

-- 13. Trabajadores (ajustado a IDs reales creados arriba)
INSERT INTO trabajador (nombres, apellidos, dni, celular, correo_corporativo, id_empresa, id_area, id_cargo) VALUES
                                                                                                                 ('Jorge Luis',    'Espinoza Vargas', '47891234', '987654321', 'jespinoza@comfutura.pe',   1, 15,  1),
                                                                                                                 ('Carlos Enrique','Ramírez Salazar', '45678901', '965432189', 'cramirez@comfutura.pe',    1,  4,  8),
                                                                                                                 ('Rosa Elena',    'Quispe Huamán',   '32165498', '991234567', 'rquispe@comfutura.pe',     1, 11,  9),
                                                                                                                 ('Lucía Fernanda','Sánchez Ortiz',   '36985214', '996543210', 'lsanchez@gab.pe',          2,  7, 14);

-- 14. trabajador_cliente
INSERT INTO trabajador_cliente (id_trabajador, id_cliente) VALUES
                                                               (1,1), (1,2), (1,3),
                                                               (2,2), (2,3),
                                                               (3,3),
                                                               (4,2);

-- 15. Órdenes de Trabajo (OTS) → versión compatible con campos nuevos
INSERT INTO ots (
    ot, ceco,
    id_cliente, id_area, id_proyecto, id_fase, id_site, id_region,
    descripcion, fecha_apertura
) VALUES
      (20250001, 'CC-ENER-2025',
       2,  4, 1, 2, 1, 1,
       'Backbone fibra óptica edificio corporativo', '2025-02-10'),

      (20250002, 'CC-CLARO-025',
       2,  5, 1, 3, 2, 2,
       'Reemplazo postes y tendido aéreo zona sur',   '2025-04-01');

-- 16. ots_trabajador
INSERT INTO ots_trabajador (id_ots, id_trabajador, rol_en_ot) VALUES
                                                                  (1, 1, 'Responsable General'),
                                                                  (1, 2, 'Coordinador Técnico'),
                                                                  (1, 3, 'Supervisor Técnico'),
                                                                  (2, 4, 'Coordinador de Campo');

-- 17. ots_detalle
INSERT INTO ots_detalle (id_ots, id_maestro, id_proveedor, cantidad, precio_unitario) VALUES
                                                                                          (1, 1, 1, 320.00,  85.00),
                                                                                          (1, 2, 1, 180.00,  95.00),
                                                                                          (1, 3, 1,  45.00, 180.00),
                                                                                          (2, 4, 1,  18.00, 2850.00);

-- 18. Roles
INSERT INTO rol (nombre, descripcion) VALUES
                                          ('ADMIN',       'Administrador completo'),
                                          ('GERENCIA',    'Nivel gerencial'),
                                          ('SUPERVISOR',  'Supervisor de operaciones'),
                                          ('COORDINADOR', 'Coordinador de proyectos');

-- 19. Usuarios (solo pruebas - nunca usar passwords en texto plano en producción)
INSERT INTO usuario (username, password, id_trabajador) VALUES
                                                            ('jespinoza', 'admin2026', 1),
                                                            ('cramirez',  'energia26', 2),
                                                            ('rquispe',   'ti2026',    3);

-- 20. usuario_rol
INSERT INTO usuario_rol (id_usuario, id_rol) VALUES
                                                 (1, 1), (1, 2),
                                                 (2, 3),
                                                 (3, 3);