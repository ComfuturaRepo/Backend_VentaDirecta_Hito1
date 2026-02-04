-- =====================================================
-- INSERCIONES PARA SISTEMA SST

-- 2. Insertar firmas ficticias
INSERT INTO firma (url) VALUES
                            ('https://storage.cloud.com/firmas/firma1.png'),
                            ('https://storage.cloud.com/firmas/firma2.png'),
                            ('https://storage.cloud.com/firmas/firma3.png'),
                            ('https://storage.cloud.com/firmas/firma4.png'),
                            ('https://storage.cloud.com/firmas/firma5.png'),
                            ('https://storage.cloud.com/firmas/firma6.png'),
                            ('https://storage.cloud.com/firmas/firma7.png'),
                            ('https://storage.cloud.com/firmas/firma8.png'),
                            ('https://storage.cloud.com/firmas/firma9.png'),
                            ('https://storage.cloud.com/firmas/firma10.png');

-- 3. Actualizar algunos trabajadores con firmas
UPDATE trabajador SET id_firma = 1 WHERE dni = '41493796'; -- WENDY FABIOLA
UPDATE trabajador SET id_firma = 2 WHERE dni = '74306365'; -- JOSE MICHAEL
UPDATE trabajador SET id_firma = 3 WHERE dni = '70616017'; -- ERICK JESUS
UPDATE trabajador SET id_firma = 4 WHERE dni = '46468388'; -- JESSICA IVETTE
UPDATE trabajador SET id_firma = 5 WHERE dni = '48010945'; -- KELLY TATIANA

-- 4. Insertar roles de trabajo
INSERT INTO rol_trabajo (nombre) VALUES
                                     ('Supervisor'),
                                     ('SOMA (Supervisor SSOMA)'),
                                     ('Responsable del Trabajo'),
                                     ('Trabajador'),
                                     ('Capacitador'),
                                     ('Inspector'),
                                     ('Brigadista'),
                                     ('Jefe de Brigada'),
                                     ('Analista de Riesgos');

-- 5. Insertar trabajos fijos
INSERT INTO trabajo (nombre) VALUES
                                 ('Instalación de Antenas en Altura'),
                                 ('Mantenimiento de Equipos Eléctricos'),
                                 ('Trabajos en Espacios Confinados'),
                                 ('Excavación para Cimentación'),
                                 ('Izaje de Equipos Pesados'),
                                 ('Soldadura en Sitio'),
                                 ('Pintura de Torres'),
                                 ('Reparación de Cableado Subterráneo'),
                                 ('Instalación de Paneles Solares'),
                                 ('Mantenimiento de Generadores');

-- 6. Insertar tareas por trabajo (Instalación de Antenas)
INSERT INTO tarea (id_trabajo, descripcion) VALUES
                                                (1, 'Revisión de permisos y documentación'),
                                                (1, 'Preparación de equipo de altura'),
                                                (1, 'Montaje de estructura base'),
                                                (1, 'Izaje de antena'),
                                                (1, 'Fijación y anclaje'),
                                                (1, 'Conexión de cableado'),
                                                (1, 'Pruebas de funcionamiento'),
                                                (1, 'Limpieza y orden del área');

-- 7. Insertar peligros comunes
INSERT INTO peligro (descripcion) VALUES
                                      ('Caída desde altura'),
                                      ('Choque eléctrico'),
                                      ('Incendio o explosión'),
                                      ('Atrapamiento'),
                                      ('Exposición a químicos'),
                                      ('Radiación no ionizante'),
                                      ('Ruido excesivo'),
                                      ('Temperaturas extremas'),
                                      ('Esfuerzo físico excesivo'),
                                      ('Movimiento de tierra');

-- 8. Insertar riesgos asociados a peligros
INSERT INTO riesgo (id_peligro, descripcion) VALUES
                                                 (1, 'Fracturas múltiples y traumatismo craneoencefálico'),
                                                 (1, 'Muerte por impacto'),
                                                 (2, 'Paro cardíaco por electrocución'),
                                                 (2, 'Quemaduras de tercer grado'),
                                                 (3, 'Quemaduras graves y asfixia'),
                                                 (4, 'Lesiones por aplastamiento'),
                                                 (5, 'Intoxicación aguda'),
                                                 (6, 'Quemaduras en la piel'),
                                                 (7, 'Pérdida auditiva permanente'),
                                                 (8, 'Golpe de calor o hipotermia');

-- 9. Insertar medidas de control
INSERT INTO medida_control (id_riesgo, descripcion) VALUES
                                                        (1, 'Uso de arnés de cuerpo completo con doble anclaje'),
                                                        (1, 'Instalación de redes de seguridad'),
                                                        (2, 'Bloqueo y etiquetado de energías (LOTO)'),
                                                        (2, 'Uso de herramientas aisladas'),
                                                        (3, 'Mantener extintores tipo ABC accesibles'),
                                                        (3, 'Control de fuentes de ignición'),
                                                        (4, 'Sistema de comunicación por radio permanente'),
                                                        (5, 'Uso de respiradores con filtros específicos'),
                                                        (6, 'Delimitación de zona controlada'),
                                                        (7, 'Uso de protectores auditivos tipo copa'),
                                                        (8, 'Pausas activas e hidratación constante');

-- 10. Insertar EPP (Equipos de Protección Personal)
INSERT INTO epp (nombre) VALUES
                             ('Casco de seguridad'),
                             ('Chaleco reflectante'),
                             ('Zapato de seguridad'),
                             ('Guantes de cuero'),
                             ('Lentes de seguridad'),
                             ('Protectores auditivos'),
                             ('Arnés de cuerpo completo'),
                             ('Mascarilla respirador'),
                             ('Ropa ignífuga'),
                             ('Cinturón de herramientas');

-- 11. Insertar tipos de riesgo de trabajo
INSERT INTO tipo_riesgo_trabajo (nombre) VALUES
                                             ('Trabajo en altura'),
                                             ('Trabajo eléctrico'),
                                             ('Trabajo en espacios confinados'),
                                             ('Trabajo en caliente'),
                                             ('Excavación y zanjas'),
                                             ('Trabajos de izaje'),
                                             ('Manejo de sustancias peligrosas'),
                                             ('Radiaciones no ionizantes');

-- 12. Insertar herramientas manuales
INSERT INTO herramienta (nombre) VALUES
                                     ('Martillo de goma'),
                                     ('Llave Stillson 14"'),
                                     ('Destornillador plano'),
                                     ('Alicate de corte'),
                                     ('Multímetro digital'),
                                     ('Nivel láser'),
                                     ('Taladro percutor'),
                                     ('Esmeril angular'),
                                     ('Sierra circular'),
                                     ('Gatillo para silicona');

-- 13. Insertar preguntas para PETAR
INSERT INTO petar_pregunta (descripcion) VALUES
                                             ('¿Se requirió evaluación de ambiente de trabajo?'),
                                             ('¿La velocidad del aire es menor a 0.5 m/s?'),
                                             ('¿El contenido de oxígeno está entre 19.5% y 23.5%?'),
                                             ('¿El LIE (Límite Inferior de Explosividad) es menor al 10%?'),
                                             ('¿Las condiciones están dentro de los límites aceptables?'),
                                             ('¿Se realizó bloqueo y etiquetado de energías?'),
                                             ('¿Se verificó ausencia de voltaje?'),
                                             ('¿El equipo está correctamente aterrizado?'),
                                             ('¿Se dispone de extintores clase C?'),
                                             ('¿El personal cuenta con EPP adecuado?');

-- =====================================================
-- DATOS PARA HOJA 1: ATS
-- =====================================================

-- Insertar ATS
INSERT INTO ats (fecha, hora, empresa, lugar_trabajo, id_trabajo) VALUES
                                                                      ('2024-01-15', '08:30:00', 'AMERICA MOVIL PERU S.A.C.', 'Torre Teléfonica - San Isidro', 1),
                                                                      ('2024-01-20', '09:00:00', 'ENTEL PERU S.A', 'Subestación Eléctrica - La Molina', 2),
                                                                      ('2024-01-25', '10:00:00', 'COMFUTURA', 'Tanque de almacenamiento - Callao', 3);

-- Insertar participantes del ATS 1
INSERT INTO ats_participante (id_ats, id_trabajador, id_rol) VALUES
                                                                 (1, (SELECT id_trabajador FROM trabajador WHERE dni = '74306365'), 1), -- Supervisor
                                                                 (1, (SELECT id_trabajador FROM trabajador WHERE dni = '70616017'), 2), -- SOMA
                                                                 (1, (SELECT id_trabajador FROM trabajador WHERE dni = '46468388'), 3), -- Responsable
                                                                 (1, (SELECT id_trabajador FROM trabajador WHERE dni = '48010945'), 4); -- Trabajador

-- Insertar riesgos del ATS 1
INSERT INTO ats_riesgo (id_ats, id_tarea, id_peligro, id_riesgo, id_medida) VALUES
                                                                                (1, 1, 1, 1, 1),
                                                                                (1, 2, 1, 2, 2),
                                                                                (1, 4, 2, 3, 3),
                                                                                (1, 6, 3, 5, 5);

-- Insertar EPP usados en ATS 1
INSERT INTO ats_epp (id_ats, id_epp) VALUES
                                         (1, 1), -- Casco
                                         (1, 3), -- Zapato
                                         (1, 4), -- Guantes
                                         (1, 7); -- Arnés

-- Insertar tipos de riesgo marcados en ATS 1
INSERT INTO ats_tipo_riesgo (id_ats, id_tipo_riesgo) VALUES
                                                         (1, 1), -- Trabajo en altura
                                                         (1, 2), -- Trabajo eléctrico
                                                         (1, 6); -- Trabajos de izaje

-- =====================================================
-- DATOS PARA HOJA 2: CAPACITACIÓN
-- =====================================================

-- Insertar capacitaciones
INSERT INTO capacitacion (numero_registro, tema, fecha, hora, id_capacitador) VALUES
                                                                                  ('CAP-2024-001', 'Charla de 5 minutos - Trabajos en Altura', '2024-01-16', '07:30:00',
                                                                                   (SELECT id_trabajador FROM trabajador WHERE dni = '74306365')),
                                                                                  ('CAP-2024-002', 'Charla de 5 minutos - Bloqueo y Etiquetado', '2024-01-21', '07:45:00',
                                                                                   (SELECT id_trabajador FROM trabajador WHERE dni = '70616017')),
                                                                                  ('CAP-2024-003', 'Charla de 5 minutos - Espacios Confinados', '2024-01-26', '08:00:00',
                                                                                   (SELECT id_trabajador FROM trabajador WHERE dni = '46468388'));

-- Insertar asistentes a capacitación 1
INSERT INTO capacitacion_asistente (id_capacitacion, id_trabajador, observaciones) VALUES
                                                                                       (1, (SELECT id_trabajador FROM trabajador WHERE dni = '74306365'), 'Participación activa'),
                                                                                       (1, (SELECT id_trabajador FROM trabajador WHERE dni = '70616017'), 'Preguntas técnicas'),
                                                                                       (1, (SELECT id_trabajador FROM trabajador WHERE dni = '46468388'), 'Atención adecuada'),
                                                                                       (1, (SELECT id_trabajador FROM trabajador WHERE dni = '48010945'), 'Comprensión completa');

-- =====================================================
-- DATOS PARA HOJA 3: INSPECCIÓN EPP
-- =====================================================

-- Insertar inspecciones EPP
INSERT INTO inspeccion_epp (numero_registro, fecha, id_inspector) VALUES
                                                                      ('INS-EPP-2024-001', '2024-01-17',
                                                                       (SELECT id_trabajador FROM trabajador WHERE dni = '74306365')),
                                                                      ('INS-EPP-2024-002', '2024-01-22',
                                                                       (SELECT id_trabajador FROM trabajador WHERE dni = '70616017'));

-- Insertar detalles de inspección EPP 1
INSERT INTO inspeccion_epp_detalle (id_inspeccion, id_trabajador, id_epp, cumple, observacion) VALUES
                                                                                                   (1, (SELECT id_trabajador FROM trabajador WHERE dni = '70616017'), 1, TRUE, 'Casco en buen estado'),
                                                                                                   (1, (SELECT id_trabajador FROM trabajador WHERE dni = '70616017'), 3, TRUE, 'Zapatos certificados'),
                                                                                                   (1, (SELECT id_trabajador FROM trabajador WHERE dni = '70616017'), 4, FALSE, 'Guantes desgastados, cambiar'),
                                                                                                   (1, (SELECT id_trabajador FROM trabajador WHERE dni = '46468388'), 1, TRUE, 'Casco correcto'),
                                                                                                   (1, (SELECT id_trabajador FROM trabajador WHERE dni = '46468388'), 5, TRUE, 'Lentes sin rayaduras');

-- =====================================================
-- DATOS PARA HOJA 4: INSPECCIÓN HERRAMIENTAS
-- =====================================================

-- Insertar inspecciones de herramientas
INSERT INTO inspeccion_herramienta (numero_registro, fecha, id_supervisor) VALUES
                                                                               ('INS-HERR-2024-001', '2024-01-18',
                                                                                (SELECT id_trabajador FROM trabajador WHERE dni = '74306365')),
                                                                               ('INS-HERR-2024-002', '2024-01-23',
                                                                                (SELECT id_trabajador FROM trabajador WHERE dni = '70616017'));

-- Insertar detalles de inspección herramientas 1
INSERT INTO inspeccion_herramienta_detalle (id_inspeccion, id_herramienta, cumple, foto_url, observacion) VALUES
                                                                                                              (1, 1, TRUE, 'https://storage.cloud.com/herramientas/martillo1.jpg', 'Martillo en buen estado'),
                                                                                                              (1, 4, TRUE, 'https://storage.cloud.com/herramientas/alicate1.jpg', 'Alicate funcionando'),
                                                                                                              (1, 5, FALSE, 'https://storage.cloud.com/herramientas/multimetro1.jpg', 'Batería baja, calibrar'),
                                                                                                              (1, 7, TRUE, 'https://storage.cloud.com/herramientas/taladro1.jpg', 'Taladro operativo');

-- =====================================================
-- DATOS PARA HOJA 5: PETAR
-- =====================================================

-- Insertar PETAR
INSERT INTO petar (numero_registro, fecha) VALUES
                                               ('PETAR-2024-001', '2024-01-19'),
                                               ('PETAR-2024-002', '2024-01-24');

-- Insertar respuestas PETAR 1
INSERT INTO petar_respuesta (id_petar, id_pregunta, respuesta) VALUES
                                                                   (1, 1, TRUE),   -- Evaluación requerida: Sí
                                                                   (1, 2, TRUE),   -- Velocidad aire OK: Sí
                                                                   (1, 3, TRUE),   -- Oxígeno OK: Sí
                                                                   (1, 4, TRUE),   -- LIE OK: Sí
                                                                   (1, 5, TRUE),   -- Límites aceptables: Sí
                                                                   (1, 6, TRUE),   -- Bloqueo realizado: Sí
                                                                   (1, 7, TRUE),   -- Ausencia de voltaje: Sí
                                                                   (1, 8, TRUE),   -- Aterrizado correcto: Sí
                                                                   (1, 9, TRUE),   -- Extintores disponibles: Sí
                                                                   (1, 10, TRUE);  -- EPP adecuado: Sí

-- Insertar trabajadores autorizados PETAR 1
INSERT INTO petar_autorizado (id_petar, id_trabajador) VALUES
                                                           (1, (SELECT id_trabajador FROM trabajador WHERE dni = '74306365')),
                                                           (1, (SELECT id_trabajador FROM trabajador WHERE dni = '70616017')),
                                                           (1, (SELECT id_trabajador FROM trabajador WHERE dni = '46468388'));

-- =====================================================
-- DATOS ADICIONALES PARA PRUEBAS
-- =====================================================

-- Insertar más tareas para otros trabajos
INSERT INTO tarea (id_trabajo, descripcion) VALUES
                                                (2, 'Desenergización del equipo'),
                                                (2, 'Verificación de ausencia de voltaje'),
                                                (2, 'Limpieza de componentes'),
                                                (2, 'Reemplazo de piezas'),
                                                (2, 'Pruebas de aislamiento'),
                                                (3, 'Medición de atmósfera'),
                                                (3, 'Ventilación del espacio'),
                                                (3, 'Instalación de barandas'),
                                                (3, 'Comunicación constante'),
                                                (3, 'Monitoreo continuo');

-- Insertar más riesgos y medidas
INSERT INTO riesgo (id_peligro, descripcion) VALUES
                                                 (9, 'Lesiones músculo-esqueléticas'),
                                                 (10, 'Derrumbe y asfixia');

INSERT INTO medida_control (id_riesgo, descripcion) VALUES
                                                        (11, 'Rotación de tareas y pausas activas'),
                                                        (12, 'Sistema de apuntalamiento y taludes');

-- Insertar más datos de prueba para EPP
INSERT INTO epp (nombre) VALUES
                             ('Overol antiestático'),
                             ('Botas de jebe'),
                             ('Careta para soldar'),
                             ('Manga ignífuga'),
                             ('Monitor de gas');

-- Insertar más herramientas
INSERT INTO herramienta (nombre) VALUES
                                     ('Cinta métrica 8m'),
                                     ('Pinza amperimétrica'),
                                     ('Termómetro infrarrojo'),
                                     ('Detector de gas'),
                                     ('Nivel de burbuja');

-- Insertar más preguntas PETAR
INSERT INTO petar_pregunta (descripcion) VALUES
                                             ('¿Se realizó inspección visual del área?'),
                                             ('¿Existen fuentes de ignición cercanas?'),
                                             ('¿El personal está certificado para la tarea?'),
                                             ('¿Se cuenta con plan de rescate?'),
                                             ('¿Hay señalización adecuada en el área?');

-- =====================================================
-- VERIFICACIÓN DE DATOS INSERTADOS
-- =====================================================

SELECT '=== RESUMEN DE DATOS INSERTADOS ===' as info;

SELECT 'Firmas:' as tabla, COUNT(*) as cantidad FROM firma
UNION ALL
SELECT 'Roles:' as tabla, COUNT(*) as cantidad FROM rol_trabajo
UNION ALL
SELECT 'Trabajos:' as tabla, COUNT(*) as cantidad FROM trabajo
UNION ALL
SELECT 'Tareas:' as tabla, COUNT(*) as cantidad FROM tarea
UNION ALL
SELECT 'Peligros:' as tabla, COUNT(*) as cantidad FROM peligro
UNION ALL
SELECT 'Riesgos:' as tabla, COUNT(*) as cantidad FROM riesgo
UNION ALL
SELECT 'Medidas:' as tabla, COUNT(*) as cantidad FROM medida_control
UNION ALL
SELECT 'EPP:' as tabla, COUNT(*) as cantidad FROM epp
UNION ALL
SELECT 'Herramientas:' as tabla, COUNT(*) as cantidad FROM herramienta
UNION ALL
SELECT 'ATS:' as tabla, COUNT(*) as cantidad FROM ats
UNION ALL
SELECT 'Capacitaciones:' as tabla, COUNT(*) as cantidad FROM capacitacion
UNION ALL
SELECT 'Inspecciones EPP:' as tabla, COUNT(*) as cantidad FROM inspeccion_epp
UNION ALL
SELECT 'Inspecciones Herramientas:' as tabla, COUNT(*) as cantidad FROM inspeccion_herramienta
UNION ALL
SELECT 'PETAR:' as tabla, COUNT(*) as cantidad FROM petar;