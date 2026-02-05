-- =====================================================
-- INSERCIONES PARA TABLAS SSOMA
-- =====================================================

USE comfutura;

-- 1. FIRMAS (Para trabajadores existentes)
INSERT INTO firma (id_firma, url) VALUES
                                      (1, 'https://storage.com/firmas/trabajador1.png'),
                                      (2, 'https://storage.com/firmas/trabajador2.png'),
                                      (3, 'https://storage.com/firmas/trabajador3.png'),
                                      (4, 'https://storage.com/firmas/trabajador4.png'),
                                      (5, 'https://storage.com/firmas/trabajador5.png'),
                                      (6, 'https://storage.com/firmas/trabajador6.png'),
                                      (7, 'https://storage.com/firmas/trabajador7.png'),
                                      (8, 'https://storage.com/firmas/trabajador8.png'),
                                      (9, 'https://storage.com/firmas/trabajador9.png'),
                                      (10, 'https://storage.com/firmas/trabajador10.png');

-- Actualizar trabajadores existentes con firmas (asumiendo que tienes al menos 10 trabajadores)
UPDATE trabajador SET id_firma = 1 WHERE id_trabajador = 1;
UPDATE trabajador SET id_firma = 2 WHERE id_trabajador = 2;
UPDATE trabajador SET id_firma = 3 WHERE id_trabajador = 3;
UPDATE trabajador SET id_firma = 4 WHERE id_trabajador = 4;
UPDATE trabajador SET id_firma = 5 WHERE id_trabajador = 5;
UPDATE trabajador SET id_firma = 6 WHERE id_trabajador = 6;
UPDATE trabajador SET id_firma = 7 WHERE id_trabajador = 7;
UPDATE trabajador SET id_firma = 8 WHERE id_trabajador = 8;
UPDATE trabajador SET id_firma = 9 WHERE id_trabajador = 9;
UPDATE trabajador SET id_firma = 10 WHERE id_trabajador = 10;

-- 2. ROLES DE TRABAJO
INSERT INTO rol_trabajo (id_rol, nombre) VALUES
                                             (1, 'Supervisor de Obra'),
                                             (2, 'Trabajador'),
                                             (3, 'Supervisor SST'),
                                             (4, 'Responsable del Área'),
                                             (5, 'Capacitador'),
                                             (6, 'Inspector EPP'),
                                             (7, 'Supervisor de Herramientas'),
                                             (8, 'Brigadista'),
                                             (9, 'Jefe de Brigada'),
                                             (10, 'Trabajador Autorizado PETAR');

-- 3. TRABAJOS (Tipos de trabajo fijos)
INSERT INTO trabajo (id_trabajo, nombre) VALUES
                                             (1, 'Instalación de torre de telecomunicaciones'),
                                             (2, 'Mantenimiento de equipo de radio frecuencia'),
                                             (3, 'Cableado estructurado en data center'),
                                             (4, 'Instalación de sistema de energía solar'),
                                             (5, 'Reparación de infraestructura eléctrica'),
                                             (6, 'Limpieza de paneles solares en altura'),
                                             (7, 'Montaje de racks y equipos TI'),
                                             (8, 'Tendido de fibra óptica'),
                                             (9, 'Instalación de sistema de CCTV'),
                                             (10, 'Mantenimiento preventivo de equipos');

-- 4. TAREAS (Secuencia según trabajo)
INSERT INTO tarea (id_tarea, id_trabajo, descripcion) VALUES
-- Para Instalación de torre (id_trabajo = 1)
(1, 1, 'Revisión del terreno y preparación de base'),
(2, 1, 'Montaje de estructura metálica'),
(3, 1, 'Instalación de equipos de telecomunicaciones'),
(4, 1, 'Conexión de sistemas de energía'),
(5, 1, 'Pruebas de funcionamiento y señal'),

-- Para Mantenimiento RF (id_trabajo = 2)
(6, 2, 'Revisión visual del equipo'),
(7, 2, 'Medición de niveles de señal'),
(8, 2, 'Limpieza de componentes electrónicos'),
(9, 2, 'Reemplazo de partes defectuosas'),
(10, 2, 'Calibración y ajustes finales'),

-- Para Cableado estructurado (id_trabajo = 3)
(11, 3, 'Instalación de canaletas'),
(12, 3, 'Tendido de cables UTP/Cat6'),
(13, 3, 'Conectorización en patch panel'),
(14, 3, 'Instalación de tomas de red'),
(15, 3, 'Pruebas de certificación'),

-- Para Energía solar (id_trabajo = 4)
(16, 4, 'Instalación de estructura soporte'),
(17, 4, 'Montaje de paneles solares'),
(18, 4, 'Conexión eléctrica serie/paralelo'),
(19, 4, 'Instalación de inversores'),
(20, 4, 'Pruebas de generación');

-- 5. PELIGROS
INSERT INTO peligro (id_peligro, descripcion) VALUES
                                                  (1, 'Trabajo en altura'),
                                                  (2, 'Riesgo eléctrico'),
                                                  (3, 'Espacios confinados'),
                                                  (4, 'Manipulación de herramientas'),
                                                  (5, 'Condiciones climáticas adversas'),
                                                  (6, 'Exposición a radiación no ionizante'),
                                                  (7, 'Carga manual de materiales'),
                                                  (8, 'Contacto con químicos'),
                                                  (9, 'Caída de objetos'),
                                                  (10, 'Incendio o explosión'),
                                                  (11, 'Atrapamiento o aprisionamiento'),
                                                  (12, 'Exposición a ruido excesivo');

-- 6. RIESGOS (asociados a peligros)
INSERT INTO riesgo (id_riesgo, id_peligro, descripcion) VALUES
-- Para Trabajo en altura (id_peligro = 1)
(1, 1, 'Caída desde altura'),
(2, 1, 'Caída de herramientas/equipos'),
(3, 1, 'Vuelco de plataforma elevadora'),

-- Riesgos eléctricos (id_peligro = 2)
(4, 2, 'Electrocución por contacto directo'),
(5, 2, 'Arco eléctrico'),
(6, 2, 'Incendio por sobrecarga'),

-- Riesgos con herramientas (id_peligro = 4)
(7, 4, 'Cortes con herramientas manuales'),
(8, 4, 'Golpes por proyección'),
(9, 4, 'Atrapamiento en partes móviles'),

-- Riesgos climáticos (id_peligro = 5)
(10, 5, 'Golpe de calor'),
(11, 5, 'Hipotermia'),
(12, 5, 'Resbalones por lluvia'),

-- Riesgos de caída de objetos (id_peligro = 9)
(13, 9, 'Golpe por caída de herramientas'),
(14, 9, 'Daño a equipos por impacto'),

-- Riesgos de ruido (id_peligro = 12)
(15, 12, 'Pérdida auditiva temporal'),
(16, 12, 'Estrés y fatiga auditiva');

-- 7. MEDIDAS DE CONTROL
INSERT INTO medida_control (id_medida, id_riesgo, descripcion) VALUES
-- Para caída desde altura (id_riesgo = 1)
(1, 1, 'Uso de arnés de cuerpo completo con línea de vida'),
(2, 1, 'Instalación de barandas de protección perimetral'),
(3, 1, 'Inspección previa de puntos de anclaje certificados'),

-- Para electrocución (id_riesgo = 4)
(4, 4, 'Bloqueo y etiquetado de energía (LOTO)'),
(5, 4, 'Uso de herramientas aisladas clase 1000V'),
(6, 4, 'Verificación de ausencia de voltaje con detector'),

-- Para cortes con herramientas (id_riesgo = 7)
(7, 7, 'Uso de guantes anticorte nivel 5'),
(8, 7, 'Mantenimiento preventivo de filos y bordes'),
(9, 7, 'Técnicas adecuadas de corte alejado del cuerpo'),

-- Para golpe de calor (id_riesgo = 10)
(10, 10, 'Pausas activas cada 2 horas en sombra'),
(11, 10, 'Hidratación constante (2 litros mínimo)'),
(12, 10, 'Uso de ropa ligera y colores claros'),

-- Para caída de objetos (id_riesgo = 13)
(13, 13, 'Uso de línea de vida para herramientas'),
(14, 13, 'Delimitación de área de trabajo con cinta'),
(15, 13, 'Inspección diaria de estado de herramientas'),

-- Para pérdida auditiva (id_riesgo = 15)
(16, 15, 'Uso de protección auditiva (tapones/orejeras)'),
(17, 15, 'Rotación de personal en áreas ruidosas'),
(18, 15, 'Medición periódica de niveles de ruido');

-- 8. EPP (Equipos de Protección Personal)
INSERT INTO epp (id_epp, nombre) VALUES
                                     (1, 'Casco de seguridad clase E'),
                                     (2, 'Lentes de protección anti-impacto'),
                                     (3, 'Protector auditivo tipo orejera'),
                                     (4, 'Tapones auditivos desechables'),
                                     (5, 'Guantes de cuero para trabajo pesado'),
                                     (6, 'Guantes dieléctricos 1000V'),
                                     (7, 'Zapatos de seguridad con punta de acero'),
                                     (8, 'Botas dieléctricas'),
                                     (9, 'Chaleco reflectante clase 2'),
                                     (10, 'Arnés de seguridad tipo cuerpo completo'),
                                     (11, 'Línea de vida retráctil'),
                                     (12, 'Respirador de media cara con filtros'),
                                     (13, 'Mascarilla N95'),
                                     (14, 'Ropa de trabajo manga larga'),
                                     (15, 'Pantalón resistente a llamas'),
                                     (16, 'Barbiquejo para casco'),
                                     (17, 'Protector facial para soldadura'),
                                     (18, 'Cinturón porta-herramientas');

-- 9. TIPOS DE RIESGO DE TRABAJO (para checkboxes)
INSERT INTO tipo_riesgo_trabajo (id, nombre) VALUES
                                                 (1, 'Trabajo en altura'),
                                                 (2, 'Trabajo eléctrico'),
                                                 (3, 'Trabajo en espacios confinados'),
                                                 (4, 'Trabajo en caliente'),
                                                 (5, 'Excavación y zanjas'),
                                                 (6, 'Trabajos de izaje'),
                                                 (7, 'Radiación no ionizante'),
                                                 (8, 'Manipulación de químicos'),
                                                 (9, 'Trabajo con herramientas peligrosas'),
                                                 (10, 'Trabajo en atmósferas explosivas');

-- 10. HERRAMIENTAS
INSERT INTO herramienta (id, nombre) VALUES
                                         (1, 'Taladro percutor 1/2"'),
                                         (2, 'Martillo demoledor'),
                                         (3, 'Destornillador eléctrico inalámbrico'),
                                         (4, 'Llave ajustable 10"'),
                                         (5, 'Alicate de electricista aislado'),
                                         (6, 'Crimpadora RJ45/RJ11'),
                                         (7, 'Probador de cableado Fluke'),
                                         (8, 'Medidor de voltaje True RMS'),
                                         (9, 'Sierra circular 7-1/4"'),
                                         (10, 'Nivel láser 360°'),
                                         (11, 'Multímetro digital Fluke 87V'),
                                         (12, 'Escalera extensible fibra de vidrio'),
                                         (13, 'Andamio tubular'),
                                         (14, 'Torre de iluminación'),
                                         (15, 'Generador eléctrico 5KW'),
                                         (16, 'Soldadora inversora');

-- =====================================================
-- INSERCIONES EN TABLAS PRINCIPALES SSOMA
-- =====================================================

-- 11. ATS (Análisis de Trabajo Seguro) - 5 registros
INSERT INTO ats (id_ats, fecha, hora, empresa, lugar_trabajo, id_trabajo) VALUES
                                                                              (1, '2024-03-15', '08:30:00', 'COMFUTURA SAC', 'Torre Celular - San Isidro Lote 123', 1),
                                                                              (2, '2024-03-16', '09:00:00', 'COMFUTURA SAC', 'Centro Comercial - Miraflores Nivel 3', 3),
                                                                              (3, '2024-03-17', '07:45:00', 'COMFUTURA SAC', 'Planta Solar - Lurín Módulo A', 4),
                                                                              (4, '2024-03-18', '10:15:00', 'COMFUTURA SAC', 'Data Center - La Molina Sala 2B', 3),
                                                                              (5, '2024-03-19', '08:00:00', 'COMFUTURA SAC', 'Edificio Corporativo - San Borja Piso 12', 2);

-- 12. PARTICIPANTES ATS
INSERT INTO ats_participante (id, id_ats, id_trabajador, id_rol) VALUES
-- Para ATS 1 (Torre Celular)
(1, 1, 1, 1),  -- Supervisor de Obra
(2, 1, 2, 2),  -- Trabajador
(3, 1, 3, 2),  -- Trabajador
(4, 1, 4, 3),  -- Supervisor SST

-- Para ATS 2 (Cableado Centro Comercial)
(5, 2, 1, 1),
(6, 2, 5, 2),
(7, 2, 6, 2),
(8, 2, 7, 4),  -- Responsable del Área

-- Para ATS 3 (Planta Solar)
(9, 3, 1, 1),
(10, 3, 8, 2),
(11, 3, 9, 2),
(12, 3, 4, 3),

-- Para ATS 4 (Data Center)
(13, 4, 2, 1),
(14, 4, 10, 2),
(15, 4, 5, 2),
(16, 4, 7, 4),

-- Para ATS 5 (Edificio Corporativo)
(17, 5, 3, 1),
(18, 5, 6, 2),
(19, 5, 8, 2),
(20, 5, 4, 3);

-- 13. RIESGOS IDENTIFICADOS EN ATS
INSERT INTO ats_riesgo (id, id_ats, id_tarea, id_peligro, id_riesgo, id_medida) VALUES
-- Para ATS 1 (Instalación torre)
(1, 1, 2, 1, 1, 1),   -- Montaje estructura → Caída altura → Arnés
(2, 1, 2, 9, 13, 13), -- Montaje → Caída objetos → Línea vida
(3, 1, 4, 2, 4, 4),   -- Conexión energía → Electrocución → LOTO
(4, 1, 3, 6, 15, 16), -- Instalación equipos → Ruido → Protección auditiva

-- Para ATS 2 (Cableado)
(5, 2, 11, 4, 7, 7),   -- Instalación canaletas → Cortes → Guantes
(6, 2, 12, 2, 4, 5),   -- Tendido cables → Electrocución → Herramientas aisladas
(7, 2, 15, 2, 6, 6),   -- Pruebas certificación → Incendio → Verificación voltaje

-- Para ATS 3 (Energía solar)
(8, 3, 16, 1, 1, 1),   -- Instalación estructura → Caída altura → Arnés
(9, 3, 17, 5, 10, 10), -- Montaje paneles → Golpe calor → Pausas activas
(10, 3, 18, 2, 4, 4),  -- Conexión eléctrica → Electrocución → LOTO

-- Para ATS 4 (Data Center)
(11, 4, 11, 3, 11, 17), -- Instalación canaletas → Atrapamiento → Rotación personal
(12, 4, 13, 4, 7, 7),   -- Conectorización → Cortes → Guantes
(13, 4, 14, 2, 4, 5),   -- Instalación tomas → Electrocución → Herramientas aisladas

-- Para ATS 5 (Mantenimiento)
(14, 5, 6, 2, 4, 4),    -- Revisión visual → Electrocución → LOTO
(15, 5, 8, 8, 16, 12),  -- Limpieza componentes → Químicos → Mascarilla
(16, 5, 10, 4, 8, 9);   -- Calibración → Proyección → Técnicas adecuadas

-- 14. EPP REQUERIDOS POR ATS
INSERT INTO ats_epp (id, id_ats, id_epp) VALUES
-- Para ATS 1 (trabajo en altura y eléctrico)
(1, 1, 1),   -- Casco
(2, 1, 2),   -- Lentes
(3, 1, 7),   -- Zapatos seguridad
(4, 1, 9),   -- Chaleco
(5, 1, 10),  -- Arnés
(6, 1, 11),  -- Línea vida
(7, 1, 14),  -- Ropa manga larga
(8, 1, 16),  -- Barbiquejo

-- Para ATS 2 (cableado estructurado)
(9, 2, 1),
(10, 2, 2),
(11, 2, 5),   -- Guantes cuero
(12, 2, 7),
(13, 2, 9),
(14, 2, 14),

-- Para ATS 3 (energía solar)
(15, 3, 1),
(16, 3, 2),
(17, 3, 6),   -- Guantes dieléctricos
(18, 3, 8),   -- Botas dieléctricas
(19, 3, 10),
(20, 3, 11),
(21, 3, 14),

-- Para ATS 4 (data center)
(22, 4, 1),
(23, 4, 2),
(24, 4, 5),
(25, 4, 7),
(26, 4, 9),

-- Para ATS 5 (mantenimiento)
(27, 5, 1),
(28, 5, 2),
(29, 5, 4),   -- Tapones auditivos
(30, 5, 7),
(31, 5, 13),  -- Mascarilla N95
(32, 5, 14);

-- 15. TIPOS DE RIESGO MARCADOS EN ATS
INSERT INTO ats_tipo_riesgo (id, id_ats, id_tipo_riesgo) VALUES
-- ATS 1 tiene trabajo en altura, eléctrico e izaje
(1, 1, 1),  -- Trabajo en altura
(2, 1, 2),  -- Trabajo eléctrico
(3, 1, 6),  -- Trabajos de izaje

-- ATS 2 tiene trabajo eléctrico
(4, 2, 2),  -- Trabajo eléctrico
(5, 2, 9),  -- Herramientas peligrosas

-- ATS 3 tiene trabajo en altura, eléctrico y radiación
(6, 3, 1),  -- Trabajo en altura
(7, 3, 2),  -- Trabajo eléctrico
(8, 3, 7),  -- Radiación no ionizante

-- ATS 4 tiene espacios confinados y eléctrico
(9, 4, 2),  -- Trabajo eléctrico
(10, 4, 3), -- Espacios confinados

-- ATS 5 tiene químicos y herramientas
(11, 5, 8), -- Manipulación de químicos
(12, 5, 9); -- Herramientas peligrosas

-- =====================================================
-- CAPACITACIONES / CHARLAS
-- =====================================================

-- 16. CAPACITACIONES
INSERT INTO capacitacion (id_capacitacion, numero_registro, tema, fecha, hora, id_capacitador) VALUES
                                                                                                   (1, 'CAP-2024-001', 'Seguridad en trabajo en altura', '2024-03-10', '10:00:00', 4),
                                                                                                   (2, 'CAP-2024-002', 'Procedimientos eléctricos seguros (LOTO)', '2024-03-12', '14:30:00', 7),
                                                                                                   (3, 'CAP-2024-003', 'Manejo seguro de herramientas manuales', '2024-03-14', '08:00:00', 1),
                                                                                                   (4, 'CAP-2024-004', 'Uso correcto de EPP en espacios confinados', '2024-03-16', '11:00:00', 4),
                                                                                                   (5, 'CAP-2024-005', 'Procedimientos de emergencia y primeros auxilios', '2024-03-18', '15:30:00', 7);

-- 17. ASISTENTES A CAPACITACIONES
INSERT INTO capacitacion_asistente (id, id_capacitacion, id_trabajador, observaciones) VALUES
-- Capacitación 1 (Altura)
(1, 1, 1, 'Participación activa, experiencia previa'),
(2, 1, 2, 'Asistió puntual, preguntas técnicas'),
(3, 1, 3, 'Necesita práctica adicional en anclajes'),
(4, 1, 5, 'Certificado de altura vigente'),
(5, 1, 8, 'Buena comprensión de procedimientos'),

-- Capacitación 2 (Eléctrica)
(6, 2, 2, 'Experiencia en eléctrica industrial'),
(7, 2, 6, 'Necesita refuerzo en procedimientos LOTO'),
(8, 2, 7, 'Capacitación completa, aprobado'),
(9, 2, 9, 'Interés en especialización eléctrica'),
(10, 2, 10, 'Primera capacitación en eléctrica'),

-- Capacitación 3 (Herramientas)
(11, 3, 3, 'Demostró buena técnica en práctica'),
(12, 3, 4, 'Supervisor, necesita recertificación'),
(13, 3, 5, 'Experiencia comprobada en mantenimiento'),
(14, 3, 8, 'Aprendizaje rápido, buena actitud'),
(15, 3, 9, 'Necesita mejorar técnica de corte'),

-- Capacitación 4 (EPP espacios confinados)
(16, 4, 1, 'Supervisor, necesita actualización'),
(17, 4, 6, 'Trabaja frecuentemente en espacios reducidos'),
(18, 4, 7, 'Responsable de área, capacitación completa'),
(19, 4, 10, 'Nuevo en espacios confinados'),

-- Capacitación 5 (Emergencias)
(20, 5, 1, 'Líder de brigada'),
(21, 5, 2, 'Brigadista, necesita recertificación RCP'),
(22, 5, 4, 'Supervisor SST, capacitador'),
(23, 5, 5, 'Primera capacitación en primeros auxilios'),
(24, 5, 8, 'Interés en ser brigadista');

-- =====================================================
-- INSPECCIONES EPP
-- =====================================================

-- 18. INSPECCIONES EPP
INSERT INTO inspeccion_epp (id, numero_registro, fecha, id_inspector) VALUES
                                                                          (1, 'INSP-EPP-001', '2024-03-15', 4),
                                                                          (2, 'INSP-EPP-002', '2024-03-16', 7),
                                                                          (3, 'INSP-EPP-003', '2024-03-17', 4),
                                                                          (4, 'INSP-EPP-004', '2024-03-18', 1),
                                                                          (5, 'INSP-EPP-005', '2024-03-19', 7);

-- 19. DETALLE INSPECCIÓN EPP
INSERT INTO inspeccion_epp_detalle (id, id_inspeccion, id_trabajador, id_epp, cumple, observacion) VALUES
-- Inspección 1
(1, 1, 1, 1, TRUE, 'Casco en buen estado, barbiquejo presente'),
(2, 1, 1, 2, TRUE, 'Lentes sin rayas, protección UV adecuada'),
(3, 1, 1, 7, TRUE, 'Zapatos con punta de acero, suela antideslizante'),
(4, 1, 2, 1, TRUE, 'Casco nuevo, etiqueta de inspección vigente'),
(5, 1, 2, 5, FALSE, 'Guantes desgastados en palma, reemplazar'),
(6, 1, 2, 7, TRUE, 'Zapatos en buen estado'),

-- Inspección 2
(7, 2, 3, 1, FALSE, 'Casco sin etiqueta de inspección, retirar de servicio'),
(8, 2, 3, 6, TRUE, 'Guantes dieléctricos certificados'),
(9, 2, 3, 8, TRUE, 'Botas dieléctricas en buen estado'),
(10, 2, 5, 1, TRUE, 'Casco con barbiquejo ajustado'),
(11, 2, 5, 2, TRUE, 'Lentes de protección completa'),
(12, 2, 5, 14, TRUE, 'Ropa manga larga adecuada'),

-- Inspección 3
(13, 3, 6, 1, TRUE, 'Casco sin daños visibles'),
(14, 3, 6, 4, TRUE, 'Tapones auditivos nuevos'),
(15, 3, 6, 7, TRUE, 'Zapatos antideslizantes'),
(16, 3, 8, 1, TRUE, NULL),
(17, 3, 8, 2, FALSE, 'Lentes rayados, cambiar por nuevos'),
(18, 3, 8, 5, TRUE, 'Guantes en buen estado'),

-- Inspección 4
(19, 4, 9, 1, TRUE, 'Casco con adhesivo reflectante'),
(20, 4, 9, 2, TRUE, NULL),
(21, 4, 9, 7, TRUE, 'Zapatos limpios y en buen estado'),
(22, 4, 10, 1, TRUE, 'Casco ajustado correctamente'),
(23, 4, 10, 2, TRUE, 'Lentes protección lateral'),
(24, 4, 10, 14, FALSE, 'Ropa manga corta, no cumple normativa'),

-- Inspección 5
(25, 5, 4, 1, TRUE, 'Casco de supervisor, en buen estado'),
(26, 5, 4, 2, TRUE, NULL),
(27, 5, 4, 9, TRUE, 'Chaleco reflectante visible'),
(28, 5, 7, 1, TRUE, NULL),
(29, 5, 7, 2, TRUE, NULL),
(30, 5, 7, 5, TRUE, 'Guantes certificados para trabajo pesado');

-- =====================================================
-- INSPECCIONES HERRAMIENTAS
-- =====================================================

-- 20. INSPECCIONES HERRAMIENTAS
INSERT INTO inspeccion_herramienta (id, numero_registro, fecha, id_supervisor) VALUES
                                                                                   (1, 'INSP-HERR-001', '2024-03-15', 1),
                                                                                   (2, 'INSP-HERR-002', '2024-03-16', 4),
                                                                                   (3, 'INSP-HERR-003', '2024-03-17', 7),
                                                                                   (4, 'INSP-HERR-004', '2024-03-18', 1),
                                                                                   (5, 'INSP-HERR-005', '2024-03-19', 4);

-- 21. DETALLE INSPECCIÓN HERRAMIENTAS
INSERT INTO inspeccion_herramienta_detalle (id, id_inspeccion, id_herramienta, cumple, foto_url, observacion) VALUES
-- Inspección 1
(1, 1, 1, TRUE, 'https://storage.com/herramientas/taladro-001.jpg', 'Taladro con cinta de inspección marzo 2024'),
(2, 1, 3, TRUE, 'https://storage.com/herramientas/destornillador-001.jpg', 'Batería al 100%, cargador funcionando'),
(3, 1, 5, TRUE, NULL, 'Alicates aislados, certificación vigente'),
(4, 1, 8, FALSE, 'https://storage.com/herramientas/medidor-001.jpg', 'Pantalla dañada, enviar a calibración'),

-- Inspección 2
(5, 2, 2, TRUE, NULL, 'Martillo demoledor, cabezal en buen estado'),
(6, 2, 6, TRUE, 'https://storage.com/herramientas/crimpadora-001.jpg', 'Crimpadora calibrada, cabezales completos'),
(7, 2, 7, TRUE, NULL, 'Probador Fluke, certificado de calibración vigente'),
(8, 2, 9, TRUE, 'https://storage.com/herramientas/sierra-001.jpg', 'Sierra con hoja nueva, protección funcionando'),

-- Inspección 3
(9, 3, 4, TRUE, NULL, 'Llave ajustable, mecanismo suave'),
(10, 3, 10, TRUE, 'https://storage.com/herramientas/nivel-001.jpg', 'Nivel láser calibrado, batería nueva'),
(11, 3, 11, TRUE, NULL, 'Multímetro Fluke 87V, certificado vigente'),
(12, 3, 12, FALSE, 'https://storage.com/herramientas/escalera-001.jpg', 'Escalera con peldaño flojo, RETIRAR DE SERVICIO'),

-- Inspección 4
(13, 4, 13, TRUE, 'https://storage.com/herramientas/andamio-001.jpg', 'Andamio certificado, barandas instaladas'),
(14, 4, 14, TRUE, NULL, 'Torre de iluminación, sistema eléctrico OK'),
(15, 4, 15, TRUE, 'https://storage.com/herramientas/generador-001.jpg', 'Generador, mantenimiento reciente realizado'),
(16, 4, 16, TRUE, NULL, 'Soldadora, cables en buen estado'),

-- Inspección 5
(17, 5, 1, TRUE, NULL, 'Segundo taladro, inspección mensual OK'),
(18, 5, 3, FALSE, 'https://storage.com/herramientas/destornillador-002.jpg', 'Destornillador con batería defectuosa'),
(19, 5, 5, TRUE, NULL, 'Segundo par de alicates, aislación intacta'),
(20, 5, 7, TRUE, 'https://storage.com/herramientas/probador-002.jpg', 'Probador secundario, funcionando correctamente');

-- =====================================================
-- PETAR (Permisos de Trabajo de Alto Riesgo)
-- =====================================================

-- 22. REGISTROS PETAR
INSERT INTO petar (id, numero_registro, fecha) VALUES
                                                   (1, 'PETAR-2024-001', '2024-03-15'),
                                                   (2, 'PETAR-2024-002', '2024-03-16'),
                                                   (3, 'PETAR-2024-003', '2024-03-17'),
                                                   (4, 'PETAR-2024-004', '2024-03-18'),
                                                   (5, 'PETAR-2024-005', '2024-03-19');

-- 23. PREGUNTAS PETAR (configurables)
INSERT INTO petar_pregunta (id, descripcion) VALUES
                                                 (1, '¿Se requiere evaluación de ambiente de trabajo previa?'),
                                                 (2, '¿Ventilación natural suficiente (> 0.5 m/s)?'),
                                                 (3, '¿Ventilación forzada instalada y funcionando?'),
                                                 (4, '¿Velocidad del aire mayor a 0.5 m/s?'),
                                                 (5, '¿Contenido de oxígeno entre 19.5% y 23.5%?'),
                                                 (6, '¿Límite inferior de explosividad (LIE) menor al 10%?'),
                                                 (7, '¿Se realizó bloqueo y etiquetado de energía (LOTO)?'),
                                                 (8, '¿Se verificó ausencia de voltaje en todos los circuitos?'),
                                                 (9, '¿Se cuenta con permiso de apertura en línea firmado?'),
                                                 (10, '¿Personal capacitado en procedimientos de emergencia?'),
                                                 (11, '¿Equipos de rescate disponibles y verificados?'),
                                                 (12, '¿Comunicación establecida con supervisión?'),
                                                 (13, '¿EPP específico verificado y disponible?'),
                                                 (14, '¿Procedimiento de trabajo aprobado por SST?'),
                                                 (15, '¿Señalización de área de trabajo instalada?'),
                                                 (16, '¿Ruta de evacuación despejada e identificada?');

-- 24. RESPUESTAS PETAR
INSERT INTO petar_respuesta (id, id_petar, id_pregunta, respuesta) VALUES
-- PETAR 1 (Trabajo en altura en torre)
(1, 1, 1, TRUE),
(2, 1, 2, TRUE),
(3, 1, 3, FALSE),
(4, 1, 4, FALSE),
(5, 1, 5, TRUE),
(6, 1, 10, TRUE),
(7, 1, 11, TRUE),
(8, 1, 12, TRUE),
(9, 1, 13, TRUE),
(10, 1, 14, TRUE),
(11, 1, 15, TRUE),
(12, 1, 16, TRUE),

-- PETAR 2 (Trabajo eléctrico en data center)
(13, 2, 1, FALSE),
(14, 2, 7, TRUE),
(15, 2, 8, TRUE),
(16, 2, 9, TRUE),
(17, 2, 10, TRUE),
(18, 2, 12, TRUE),
(19, 2, 13, TRUE),
(20, 2, 14, TRUE),
(21, 2, 15, TRUE),
(22, 2, 16, TRUE),

-- PETAR 3 (Espacio confinado en tanque)
(23, 3, 1, TRUE),
(24, 3, 2, FALSE),
(25, 3, 3, TRUE),
(26, 3, 4, TRUE),
(27, 3, 5, TRUE),
(28, 3, 6, TRUE),
(29, 3, 10, TRUE),
(30, 3, 11, TRUE),
(31, 3, 12, TRUE),
(32, 3, 13, TRUE),
(33, 3, 14, TRUE),
(34, 3, 15, TRUE),
(35, 3, 16, TRUE),

-- PETAR 4 (Trabajo en caliente soldadura)
(36, 4, 1, TRUE),
(37, 4, 2, TRUE),
(38, 4, 10, TRUE),
(39, 4, 11, TRUE),
(40, 4, 12, TRUE),
(41, 4, 13, TRUE),
(42, 4, 14, TRUE),
(43, 4, 15, TRUE),
(44, 4, 16, TRUE),

-- PETAR 5 (Izaje de equipos pesados)
(45, 5, 1, TRUE),
(46, 5, 10, TRUE),
(47, 5, 11, TRUE),
(48, 5, 12, TRUE),
(49, 5, 13, TRUE),
(50, 5, 14, TRUE),
(51, 5, 15, TRUE),
(52, 5, 16, TRUE);

-- 25. TRABAJADORES AUTORIZADOS PETAR
INSERT INTO petar_autorizado (id, id_petar, id_trabajador) VALUES
-- PETAR 1 autoriza a 3 trabajadores
(1, 1, 1),
(2, 1, 2),
(3, 1, 3),

-- PETAR 2 autoriza a 2 trabajadores
(4, 2, 5),
(5, 2, 6),

-- PETAR 3 autoriza a 3 trabajadores
(6, 3, 7),
(7, 3, 8),
(8, 3, 9),

-- PETAR 4 autoriza a 2 trabajadores
(9, 4, 3),
(10, 4, 10),

-- PETAR 5 autoriza a 4 trabajadores
(11, 5, 1),
(12, 5, 4),
(13, 5, 5),
(14, 5, 7);

-- =====================================================
-- RESUMEN DE INSERCIONES
-- =====================================================
/*
RESUMEN DE DATOS INSERTADOS:

1. firma: 10 registros (actualizadas en trabajadores)
2. rol_trabajo: 10 roles
3. trabajo: 10 tipos de trabajo
4. tarea: 20 tareas
5. peligro: 12 peligros
6. riesgo: 16 riesgos
7. medida_control: 18 medidas
8. epp: 18 equipos
9. tipo_riesgo_trabajo: 10 tipos
10. herramienta: 16 herramientas

TABLAS PRINCIPALES:
11. ats: 5 registros
12. ats_participante: 20 participantes
13. ats_riesgo: 16 riesgos identificados
14. ats_epp: 32 EPP requeridos
15. ats_tipo_riesgo: 12 tipos marcados
16. capacitacion: 5 charlas
17. capacitacion_asistente: 24 asistentes
18. inspeccion_epp: 5 inspecciones
19. inspeccion_epp_detalle: 30 detalles
20. inspeccion_herramienta: 5 inspecciones
21. inspeccion_herramienta_detalle: 20 detalles
22. petar: 5 permisos
23. petar_pregunta: 16 preguntas
24. petar_respuesta: 52 respuestas
25. petar_autorizado: 14 autorizaciones

TOTAL: 366 registros para SSOMA
*/

-- Verificar conteos
SELECT
    'firma' as tabla, COUNT(*) as cantidad FROM firma
UNION ALL SELECT 'rol_trabajo', COUNT(*) FROM rol_trabajo
UNION ALL SELECT 'trabajo', COUNT(*) FROM trabajo
UNION ALL SELECT 'tarea', COUNT(*) FROM tarea
UNION ALL SELECT 'peligro', COUNT(*) FROM peligro
UNION ALL SELECT 'riesgo', COUNT(*) FROM riesgo
UNION ALL SELECT 'medida_control', COUNT(*) FROM medida_control
UNION ALL SELECT 'epp', COUNT(*) FROM epp
UNION ALL SELECT 'tipo_riesgo_trabajo', COUNT(*) FROM tipo_riesgo_trabajo
UNION ALL SELECT 'herramienta', COUNT(*) FROM herramienta
UNION ALL SELECT 'ats', COUNT(*) FROM ats
UNION ALL SELECT 'ats_participante', COUNT(*) FROM ats_participante
UNION ALL SELECT 'ats_riesgo', COUNT(*) FROM ats_riesgo
UNION ALL SELECT 'ats_epp', COUNT(*) FROM ats_epp
UNION ALL SELECT 'ats_tipo_riesgo', COUNT(*) FROM ats_tipo_riesgo
UNION ALL SELECT 'capacitacion', COUNT(*) FROM capacitacion
UNION ALL SELECT 'capacitacion_asistente', COUNT(*) FROM capacitacion_asistente
UNION ALL SELECT 'inspeccion_epp', COUNT(*) FROM inspeccion_epp
UNION ALL SELECT 'inspeccion_epp_detalle', COUNT(*) FROM inspeccion_epp_detalle
UNION ALL SELECT 'inspeccion_herramienta', COUNT(*) FROM inspeccion_herramienta
UNION ALL SELECT 'inspeccion_herramienta_detalle', COUNT(*) FROM inspeccion_herramienta_detalle
UNION ALL SELECT 'petar', COUNT(*) FROM petar
UNION ALL SELECT 'petar_pregunta', COUNT(*) FROM petar_pregunta
UNION ALL SELECT 'petar_respuesta', COUNT(*) FROM petar_respuesta
UNION ALL SELECT 'petar_autorizado', COUNT(*) FROM petar_autorizado;

-- Verificar que los trabajadores tengan firma
SELECT t.id_trabajador, t.nombres, t.apellidos, f.url as firma_url
FROM trabajador t
         LEFT JOIN firma f ON t.id_firma = f.id_firma
WHERE t.id_trabajador <= 10
ORDER BY t.id_trabajador;