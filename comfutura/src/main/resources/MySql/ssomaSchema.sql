USE comfutura;

-- =====================================================
-- 1. TABLAS BASE PARA SSOMA
-- =====================================================

CREATE TABLE rol_trabajo (
                             id_rol INT AUTO_INCREMENT PRIMARY KEY,
                             nombre VARCHAR(50) NOT NULL,
                             activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- =====================================================
-- 2. CATÁLOGOS PARA SSOMA
-- =====================================================

-- Tabla: TRABAJO (Combo "Trabajo a realizar")
CREATE TABLE trabajo (
                         id_trabajo INT AUTO_INCREMENT PRIMARY KEY,
                         nombre VARCHAR(150) NOT NULL,
                         activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Tabla: TAREA (Secuencia de tareas según trabajo)
CREATE TABLE tarea (
                       id_tarea INT AUTO_INCREMENT PRIMARY KEY,
                       id_trabajo INT,
                       descripcion VARCHAR(200),
                       activo BOOLEAN NOT NULL DEFAULT TRUE,
                       FOREIGN KEY (id_trabajo) REFERENCES trabajo(id_trabajo)
);

-- Tabla: PELIGRO (Peligros posibles)
CREATE TABLE peligro (
                         id_peligro INT AUTO_INCREMENT PRIMARY KEY,
                         descripcion VARCHAR(200),
                         activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Tabla: RIESGO (Riesgos asociados a un peligro)
CREATE TABLE riesgo (
                        id_riesgo INT AUTO_INCREMENT PRIMARY KEY,
                        id_peligro INT,
                        descripcion VARCHAR(200),
                        activo BOOLEAN NOT NULL DEFAULT TRUE,
                        FOREIGN KEY (id_peligro) REFERENCES peligro(id_peligro)
);

-- Tabla: MEDIDA_CONTROL (Medidas de control para reducir riesgos)
CREATE TABLE medida_control (
                                id_medida INT AUTO_INCREMENT PRIMARY KEY,
                                id_riesgo INT,
                                descripcion VARCHAR(200),
                                activo BOOLEAN NOT NULL DEFAULT TRUE,
                                FOREIGN KEY (id_riesgo) REFERENCES riesgo(id_riesgo)
);

-- Tabla: EPP (Catálogo EPP)
CREATE TABLE epp (
                     id_epp INT AUTO_INCREMENT PRIMARY KEY,
                     nombre VARCHAR(100),
                     activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Tabla: TIPO_RIESGO_TRABAJO (Tipos de trabajo de riesgo)
CREATE TABLE tipo_riesgo_trabajo (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     nombre VARCHAR(100),
                                     activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Tabla: HERRAMIENTA (Catálogo de herramientas)
CREATE TABLE herramienta (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             nombre VARCHAR(150),
                             activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- =====================================================
-- HOJA 1: ANÁLISIS DE TRABAJO SEGURO (ATS)
-- =====================================================

-- Tabla principal ATS
CREATE TABLE ats (
                     id_ats INT AUTO_INCREMENT PRIMARY KEY,
    -- Campos automáticos
                     fecha DATE DEFAULT (CURRENT_DATE),
                     hora TIME DEFAULT (CURRENT_TIME),
                     numero_registro VARCHAR(20) UNIQUE,

    -- Información básica
                     empresa VARCHAR(150),
                     lugar_trabajo VARCHAR(150),
                     coordenadas VARCHAR(100), -- Coordenadas GPS

    -- Relaciones con catálogos
                     id_trabajo INT,

    -- Personas (comunes a todas las hojas)
                     id_supervisor_trabajo INT,      -- Supervisor del trabajo
                     id_responsable_lugar INT,       -- Responsable del lugar del trabajo
                     id_supervisor_sst INT,          -- Supervisor SST/SSOMA

    -- Vinculación con OT
                     id_ots INT,

    -- Fechas de auditoría
                     fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                     fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Constraints
                     FOREIGN KEY (id_trabajo) REFERENCES trabajo(id_trabajo),
                     FOREIGN KEY (id_supervisor_trabajo) REFERENCES trabajador(id_trabajador),
                     FOREIGN KEY (id_responsable_lugar) REFERENCES trabajador(id_trabajador),
                     FOREIGN KEY (id_supervisor_sst) REFERENCES trabajador(id_trabajador),
                     FOREIGN KEY (id_ots) REFERENCES ots(id_ots)
);

-- Participantes del ATS
CREATE TABLE ats_participante (
                                  id INT AUTO_INCREMENT PRIMARY KEY,
                                  id_ats INT,
                                  id_trabajador INT,
                                  id_rol INT,
                                  fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                  FOREIGN KEY (id_ats) REFERENCES ats(id_ats),
                                  FOREIGN KEY (id_trabajador) REFERENCES trabajador(id_trabajador),
                                  FOREIGN KEY (id_rol) REFERENCES rol_trabajo(id_rol)
);

-- Secuencia: Tarea -> Peligro -> Riesgo -> Medida Control
CREATE TABLE ats_riesgo (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            id_ats INT,
                            id_tarea INT,
                            id_peligro INT,
                            id_riesgo INT,
                            id_medida INT,
                            observacion TEXT,
                            fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                            FOREIGN KEY (id_ats) REFERENCES ats(id_ats),
                            FOREIGN KEY (id_tarea) REFERENCES tarea(id_tarea),
                            FOREIGN KEY (id_peligro) REFERENCES peligro(id_peligro),
                            FOREIGN KEY (id_riesgo) REFERENCES riesgo(id_riesgo),
                            FOREIGN KEY (id_medida) REFERENCES medida_control(id_medida)
);

-- EPP usados en el ATS
CREATE TABLE ats_epp (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         id_ats INT,
                         id_epp INT,
                         cantidad INT DEFAULT 1,
                         fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                         FOREIGN KEY (id_ats) REFERENCES ats(id_ats),
                         FOREIGN KEY (id_epp) REFERENCES epp(id_epp)
);

-- Tipos de riesgo marcados en ATS
CREATE TABLE ats_tipo_riesgo (
                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                 id_ats INT,
                                 id_tipo_riesgo INT,
                                 fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                 FOREIGN KEY (id_ats) REFERENCES ats(id_ats),
                                 FOREIGN KEY (id_tipo_riesgo) REFERENCES tipo_riesgo_trabajo(id)
);

-- =====================================================
-- HOJA 2: CAPACITACIÓN / CHARLA
-- =====================================================

CREATE TABLE capacitacion (
                              id_capacitacion INT AUTO_INCREMENT PRIMARY KEY,
    -- Campos automáticos
                              fecha DATE DEFAULT (CURRENT_DATE),
                              hora TIME DEFAULT (CURRENT_TIME),
                              numero_registro VARCHAR(20) UNIQUE,

    -- Información de la capacitación
                              tema VARCHAR(200),
                              tipo_charla VARCHAR(50) DEFAULT 'CHARLA_5_MINUTOS', -- Siempre charla de 5 minutos
                              duracion_minutos INT,

    -- Personas (comunes)
                              id_capacitador INT,
                              id_supervisor_trabajo INT,
                              id_responsable_lugar INT,
                              id_supervisor_sst INT,

    -- Vinculación con OT y ATS
                              id_ots INT,
                              id_ats INT, -- Relación con ATS para obtener participantes

    -- Auditoría
                              fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Constraints
                              FOREIGN KEY (id_capacitador) REFERENCES trabajador(id_trabajador),
                              FOREIGN KEY (id_supervisor_trabajo) REFERENCES trabajador(id_trabajador),
                              FOREIGN KEY (id_responsable_lugar) REFERENCES trabajador(id_trabajador),
                              FOREIGN KEY (id_supervisor_sst) REFERENCES trabajador(id_trabajador),
                              FOREIGN KEY (id_ots) REFERENCES ots(id_ots),
                              FOREIGN KEY (id_ats) REFERENCES ats(id_ats)
);

-- Asistentes a la capacitación
CREATE TABLE capacitacion_asistente (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        id_capacitacion INT,
                                        id_trabajador INT,
                                        asistio BOOLEAN DEFAULT TRUE,
                                        observaciones TEXT,
                                        fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                        FOREIGN KEY (id_capacitacion) REFERENCES capacitacion(id_capacitacion),
                                        FOREIGN KEY (id_trabajador) REFERENCES trabajador(id_trabajador)
);

-- =====================================================
-- HOJA 3: INSPECCIÓN EPP
-- =====================================================

CREATE TABLE inspeccion_epp (
                                id INT AUTO_INCREMENT PRIMARY KEY,
    -- Campos automáticos
                                fecha DATE DEFAULT (CURRENT_DATE),
                                numero_registro VARCHAR(20) UNIQUE,

    -- Información de inspección
                                tipo_inspeccion VARCHAR(50) DEFAULT 'PLANIFICADA', -- Siempre planificada
                                area_trabajo VARCHAR(150),

    -- Personas
                                id_inspector INT,
                                id_supervisor_trabajo INT,
                                id_responsable_lugar INT,
                                id_supervisor_sst INT,

    -- Vinculación con OT
                                id_ots INT,

    -- Auditoría
                                fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Constraints
                                FOREIGN KEY (id_inspector) REFERENCES trabajador(id_trabajador),
                                FOREIGN KEY (id_supervisor_trabajo) REFERENCES trabajador(id_trabajador),
                                FOREIGN KEY (id_responsable_lugar) REFERENCES trabajador(id_trabajador),
                                FOREIGN KEY (id_supervisor_sst) REFERENCES trabajador(id_trabajador),
                                FOREIGN KEY (id_ots) REFERENCES ots(id_ots)
);

-- Detalle de inspección EPP por trabajador
CREATE TABLE inspeccion_epp_detalle (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        id_inspeccion INT,
                                        id_trabajador INT,
                                        id_epp INT,
                                        cumple BOOLEAN,
                                        accion_correctiva TEXT, -- Campo escrito para acción correctiva
                                        observacion TEXT,
                                        fecha_inspeccion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                        FOREIGN KEY (id_inspeccion) REFERENCES inspeccion_epp(id),
                                        FOREIGN KEY (id_trabajador) REFERENCES trabajador(id_trabajador),
                                        FOREIGN KEY (id_epp) REFERENCES epp(id_epp)
);

-- =====================================================
-- HOJA 4: INSPECCIÓN HERRAMIENTAS
-- =====================================================

-- Tabla para preguntas de inspección de herramientas
CREATE TABLE pregunta_herramienta (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      pregunta VARCHAR(500) NOT NULL,
                                      orden INT DEFAULT 0,
                                      activo BOOLEAN DEFAULT TRUE,
                                      fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE inspeccion_herramienta (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
    -- Campos automáticos
                                        fecha DATE DEFAULT (CURRENT_DATE),
                                        numero_registro VARCHAR(20) UNIQUE,

    -- Información del cliente/proyecto
                                        id_cliente INT,
                                        id_proyecto INT,
                                        ubicacion_sede VARCHAR(255),

    -- Personas
                                        id_supervisor INT, -- Supervisor operativo
                                        id_supervisor_trabajo INT,
                                        id_responsable_lugar INT,
                                        id_supervisor_sst INT,

    -- Vinculación con OT
                                        id_ots INT,

    -- Auditoría
                                        fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Constraints
                                        FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
                                        FOREIGN KEY (id_proyecto) REFERENCES proyecto(id_proyecto),
                                        FOREIGN KEY (id_supervisor) REFERENCES trabajador(id_trabajador),
                                        FOREIGN KEY (id_supervisor_trabajo) REFERENCES trabajador(id_trabajador),
                                        FOREIGN KEY (id_responsable_lugar) REFERENCES trabajador(id_trabajador),
                                        FOREIGN KEY (id_supervisor_sst) REFERENCES trabajador(id_trabajador),
                                        FOREIGN KEY (id_ots) REFERENCES ots(id_ots)
);

-- Detalle de herramientas inspeccionadas
CREATE TABLE inspeccion_herramienta_detalle (
                                                id INT AUTO_INCREMENT PRIMARY KEY,
                                                id_inspeccion INT,
                                                id_herramienta INT,
                                                observacion TEXT,
                                                fecha_inspeccion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                                FOREIGN KEY (id_inspeccion) REFERENCES inspeccion_herramienta(id),
                                                FOREIGN KEY (id_herramienta) REFERENCES herramienta(id)
);

-- Respuestas a preguntas por herramienta
CREATE TABLE inspeccion_herramienta_respuesta (
                                                  id INT AUTO_INCREMENT PRIMARY KEY,
                                                  id_inspeccion_detalle INT,
                                                  id_pregunta INT,
                                                  respuesta BOOLEAN,
                                                  foto_url VARCHAR(255),
                                                  observacion TEXT,
                                                  fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                                  FOREIGN KEY (id_inspeccion_detalle) REFERENCES inspeccion_herramienta_detalle(id),
                                                  FOREIGN KEY (id_pregunta) REFERENCES pregunta_herramienta(id)
);

-- =====================================================
-- HOJA 5: PETAR (Permisos para trabajos de alto riesgo)
-- =====================================================

CREATE TABLE petar (
                       id INT AUTO_INCREMENT PRIMARY KEY,
    -- Campos automáticos
                       fecha DATE DEFAULT (CURRENT_DATE),
                       numero_registro VARCHAR(20) UNIQUE,

    -- Evaluación de ambiente de trabajo
                       requiere_evaluacion_ambiente BOOLEAN DEFAULT FALSE,
                       apertura_linea_equipos BOOLEAN DEFAULT FALSE,

    -- Personas
                       id_supervisor_trabajo INT,
                       id_responsable_lugar INT,
                       id_supervisor_sst INT,
                       id_brigadista INT, -- Jefe de brigadista
                       id_responsable_trabajo INT, -- Responsable del trabajo

    -- Vinculación con OT
                       id_ots INT,

    -- Información adicional
                       hora_inicio TIME,
                       recursos_necesarios TEXT,
                       procedimiento TEXT,

    -- Auditoría
                       fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Constraints
                       FOREIGN KEY (id_supervisor_trabajo) REFERENCES trabajador(id_trabajador),
                       FOREIGN KEY (id_responsable_lugar) REFERENCES trabajador(id_trabajador),
                       FOREIGN KEY (id_supervisor_sst) REFERENCES trabajador(id_trabajador),
                       FOREIGN KEY (id_brigadista) REFERENCES trabajador(id_trabajador),
                       FOREIGN KEY (id_responsable_trabajo) REFERENCES trabajador(id_trabajador),
                       FOREIGN KEY (id_ots) REFERENCES ots(id_ots)
);

-- Tabla para evaluación de ambiente (PETAR)
CREATE TABLE petar_evaluacion_ambiente (
                                           id INT AUTO_INCREMENT PRIMARY KEY,
                                           id_petar INT,
                                           numero_evaluacion INT, -- 1ra, 2da, 3ra, etc.
                                           tipo_ventilacion ENUM('NATURAL', 'FORZADA'),
                                           velocidad_aire DECIMAL(5,2),
                                           contenido_oxigeno DECIMAL(5,2),
                                           limite_explosividad DECIMAL(5,2),
                                           menor_tlb BOOLEAN,
                                           observaciones TEXT,
                                           fecha_evaluacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                           FOREIGN KEY (id_petar) REFERENCES petar(id)
);

-- Tabla para preguntas de apertura en línea (PETAR)
CREATE TABLE petar_pregunta_apertura (
                                         id INT AUTO_INCREMENT PRIMARY KEY,
                                         pregunta VARCHAR(500) NOT NULL,
                                         orden INT DEFAULT 0,
                                         activo BOOLEAN DEFAULT TRUE,
                                         fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Respuestas de apertura en línea (PETAR)
CREATE TABLE petar_respuesta_apertura (
                                          id INT AUTO_INCREMENT PRIMARY KEY,
                                          id_petar INT,
                                          id_pregunta INT,
                                          respuesta BOOLEAN,
                                          observacion TEXT,
                                          fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                          FOREIGN KEY (id_petar) REFERENCES petar(id),
                                          FOREIGN KEY (id_pregunta) REFERENCES petar_pregunta_apertura(id)
);

-- Documentación contrastada (PETAR)
CREATE TABLE petar_documentacion (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     id_petar INT,
                                     pregunta VARCHAR(500),
                                     respuesta BOOLEAN,
                                     observacion TEXT,
                                     fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                     FOREIGN KEY (id_petar) REFERENCES petar(id)
);

-- Preguntas configurables PETAR
CREATE TABLE petar_pregunta (
                                id INT AUTO_INCREMENT PRIMARY KEY,
                                descripcion VARCHAR(200),
                                tipo ENUM('GENERAL', 'ESPECIFICA') DEFAULT 'GENERAL',
                                activo BOOLEAN NOT NULL DEFAULT TRUE,
                                fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Respuestas preguntas generales PETAR
CREATE TABLE petar_respuesta (
                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                 id_petar INT,
                                 id_pregunta INT,
                                 respuesta BOOLEAN,
                                 observacion TEXT,
                                 activo BOOLEAN NOT NULL DEFAULT TRUE,
                                 fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                 FOREIGN KEY (id_petar) REFERENCES petar(id),
                                 FOREIGN KEY (id_pregunta) REFERENCES petar_pregunta(id)
);

-- Trabajadores autorizados PETAR
CREATE TABLE petar_autorizado (
                                  id INT AUTO_INCREMENT PRIMARY KEY,
                                  id_petar INT,
                                  id_trabajador INT,
                                  conformidad_requerida BOOLEAN DEFAULT FALSE,
                                  fecha_autorizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                  FOREIGN KEY (id_petar) REFERENCES petar(id),
                                  FOREIGN KEY (id_trabajador) REFERENCES trabajador(id_trabajador)
);



-- =====================================================
-- ÍNDICES PARA MEJOR PERFORMANCE
-- =====================================================

-- Índices para ATS
CREATE INDEX idx_ats_fecha ON ats(fecha);
CREATE INDEX idx_ats_ots ON ats(id_ots);
CREATE INDEX idx_ats_numero_registro ON ats(numero_registro);

-- Índices para Capacitación
CREATE INDEX idx_capacitacion_fecha ON capacitacion(fecha);
CREATE INDEX idx_capacitacion_ots ON capacitacion(id_ots);
CREATE INDEX idx_capacitacion_numero_registro ON capacitacion(numero_registro);

-- Índices para Inspección EPP
CREATE INDEX idx_inspeccion_epp_fecha ON inspeccion_epp(fecha);
CREATE INDEX idx_inspeccion_epp_ots ON inspeccion_epp(id_ots);
CREATE INDEX idx_inspeccion_epp_numero_registro ON inspeccion_epp(numero_registro);

-- Índices para Inspección Herramienta
CREATE INDEX idx_inspeccion_herramienta_fecha ON inspeccion_herramienta(fecha);
CREATE INDEX idx_inspeccion_herramienta_ots ON inspeccion_herramienta(id_ots);
CREATE INDEX idx_inspeccion_herramienta_numero_registro ON inspeccion_herramienta(numero_registro);

-- Índices para PETAR
CREATE INDEX idx_petar_fecha ON petar(fecha);
CREATE INDEX idx_petar_ots ON petar(id_ots);
CREATE INDEX idx_petar_numero_registro ON petar(numero_registro);