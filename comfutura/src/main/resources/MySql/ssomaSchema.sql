use comfutura;
CREATE TABLE rol_trabajo (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);


-- =====================================================
-- 2. CATÁLOGOS (COMBOS DEL SISTEMA)
-- =====================================================

/*
 Tipos de trabajos fijos
 Combo "Trabajo a realizar"
*/
CREATE TABLE trabajo (
    id_trabajo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL
);

ALTER TABLE trabajo
ADD activo BOOLEAN NOT NULL DEFAULT TRUE;
use comfutura;
ALTER TABLE rol_trabajo
ADD activo BOOLEAN NOT NULL DEFAULT TRUE;
/*
 Secuencia de tareas según trabajo
*/
CREATE TABLE tarea (
    id_tarea INT AUTO_INCREMENT PRIMARY KEY,
    id_trabajo INT,
    descripcion VARCHAR(200),
    FOREIGN KEY (id_trabajo) REFERENCES trabajo(id_trabajo)
);

ALTER TABLE tarea
ADD activo BOOLEAN NOT NULL DEFAULT TRUE;
/*
 Peligros posibles
*/
CREATE TABLE peligro (
    id_peligro INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(200)
);

ALTER TABLE peligro
ADD activo BOOLEAN NOT NULL DEFAULT TRUE;
/*
 Riesgos asociados a un peligro
*/
CREATE TABLE riesgo (
    id_riesgo INT AUTO_INCREMENT PRIMARY KEY,
    id_peligro INT,
    descripcion VARCHAR(200),
    FOREIGN KEY (id_peligro) REFERENCES peligro(id_peligro)
);

ALTER TABLE riesgo
ADD activo BOOLEAN NOT NULL DEFAULT TRUE;
/*
 Medidas de control para reducir riesgos
*/
CREATE TABLE medida_control (
    id_medida INT AUTO_INCREMENT PRIMARY KEY,
    id_riesgo INT,
    descripcion VARCHAR(200),
    FOREIGN KEY (id_riesgo) REFERENCES riesgo(id_riesgo)
);
ALTER TABLE medida_control
ADD activo BOOLEAN NOT NULL DEFAULT TRUE;

-- =====================================================
-- 3. HOJA 1 → ATS (ANÁLISIS DE TRABAJO SEGURO)
-- =====================================================

/*
 Cabecera principal del ATS
*/
CREATE TABLE ats (
    id_ats INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE,
    hora TIME,
    empresa VARCHAR(150),
    lugar_trabajo VARCHAR(150),
    id_trabajo INT,
    FOREIGN KEY (id_trabajo) REFERENCES trabajo(id_trabajo)
);


/*
 Participantes del ATS
*/
CREATE TABLE ats_participante (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_ats INT,
    id_trabajador INT,
    id_rol INT,
    FOREIGN KEY (id_ats) REFERENCES ats(id_ats),
    FOREIGN KEY (id_trabajador) REFERENCES trabajador(id_trabajador),
    FOREIGN KEY (id_rol) REFERENCES rol_trabajo(id_rol)
);


/*
 Secuencia tareas + peligro + riesgo + control
*/
CREATE TABLE ats_riesgo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_ats INT,
    id_tarea INT,
    id_peligro INT,
    id_riesgo INT,
    id_medida INT,
    FOREIGN KEY (id_ats) REFERENCES ats(id_ats),
    FOREIGN KEY (id_tarea) REFERENCES tarea(id_tarea),
    FOREIGN KEY (id_peligro) REFERENCES peligro(id_peligro),
    FOREIGN KEY (id_riesgo) REFERENCES riesgo(id_riesgo),
    FOREIGN KEY (id_medida) REFERENCES medida_control(id_medida)
);


/*
 Catálogo EPP
*/
CREATE TABLE epp (
    id_epp INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100)
);

ALTER TABLE epp
ADD activo BOOLEAN NOT NULL DEFAULT TRUE;
/*
 EPP usados en el ATS
*/
CREATE TABLE ats_epp (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_ats INT,
    id_epp INT,
    FOREIGN KEY (id_ats) REFERENCES ats(id_ats),
    FOREIGN KEY (id_epp) REFERENCES epp(id_epp)
);


/*
 Tipos de trabajo de riesgo (check)
*/
CREATE TABLE tipo_riesgo_trabajo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100)
);

ALTER TABLE tipo_riesgo_trabajo
ADD activo BOOLEAN NOT NULL DEFAULT TRUE;
/*
 Riesgos marcados en ATS
*/
CREATE TABLE ats_tipo_riesgo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_ats INT,
    id_tipo_riesgo INT,
    FOREIGN KEY (id_ats) REFERENCES ats(id_ats),
    FOREIGN KEY (id_tipo_riesgo) REFERENCES tipo_riesgo_trabajo(id)
);


-- =====================================================
-- 4. HOJA 2 → CAPACITACIÓN / CHARLA
-- =====================================================

/*
 Registro de capacitación
*/
CREATE TABLE capacitacion (
    id_capacitacion INT AUTO_INCREMENT PRIMARY KEY,
    numero_registro VARCHAR(50),
    tema VARCHAR(200),
    fecha DATE,
    hora TIME,
    id_capacitador INT,
    FOREIGN KEY (id_capacitador) REFERENCES trabajador(id_trabajador)
);


/*
 Asistentes
*/
CREATE TABLE capacitacion_asistente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_capacitacion INT,
    id_trabajador INT,
    observaciones TEXT,
    FOREIGN KEY (id_capacitacion) REFERENCES capacitacion(id_capacitacion),
    FOREIGN KEY (id_trabajador) REFERENCES trabajador(id_trabajador)
);


-- =====================================================
-- 5. HOJA 3 → INSPECCIÓN EPP
-- =====================================================

CREATE TABLE inspeccion_epp (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero_registro VARCHAR(50),
    fecha DATE,
    id_inspector INT,
    FOREIGN KEY (id_inspector) REFERENCES trabajador(id_trabajador)
);


/*
 EPP revisados por trabajador
*/
CREATE TABLE inspeccion_epp_detalle (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_inspeccion INT,
    id_trabajador INT,
    id_epp INT,
    cumple BOOLEAN,
    observacion TEXT,
    FOREIGN KEY (id_inspeccion) REFERENCES inspeccion_epp(id),
    FOREIGN KEY (id_trabajador) REFERENCES trabajador(id_trabajador),
    FOREIGN KEY (id_epp) REFERENCES epp(id_epp)
);


-- =====================================================
-- 6. HOJA 4 → INSPECCIÓN HERRAMIENTAS
-- =====================================================

CREATE TABLE herramienta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150)
);

ALTER TABLE herramienta
ADD activo BOOLEAN NOT NULL DEFAULT TRUE;
CREATE TABLE inspeccion_herramienta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero_registro VARCHAR(50),
    fecha DATE,
    id_supervisor INT,
    FOREIGN KEY (id_supervisor) REFERENCES trabajador(id_trabajador)
);

CREATE TABLE inspeccion_herramienta_detalle (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_inspeccion INT,
    id_herramienta INT,
    cumple BOOLEAN,
    foto_url VARCHAR(255),
    observacion TEXT,
    FOREIGN KEY (id_inspeccion) REFERENCES inspeccion_herramienta(id),
    FOREIGN KEY (id_herramienta) REFERENCES herramienta(id)
);


-- =====================================================
-- 7. HOJA 5 → PETAR
-- =====================================================

CREATE TABLE petar (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero_registro VARCHAR(50),
    fecha DATE
);


/*
 Preguntas configurables
*/
CREATE TABLE petar_pregunta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(200)
);

ALTER TABLE petar_pregunta
ADD activo BOOLEAN NOT NULL DEFAULT TRUE;
/*
 Respuestas SI/NO
*/
CREATE TABLE petar_respuesta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_petar INT,
    id_pregunta INT,
    respuesta BOOLEAN,
    FOREIGN KEY (id_petar) REFERENCES petar(id),
    FOREIGN KEY (id_pregunta) REFERENCES petar_pregunta(id)
);

ALTER TABLE petar_respuesta
ADD activo BOOLEAN NOT NULL DEFAULT TRUE;
/*
 Trabajadores autorizados
*/
CREATE TABLE petar_autorizado (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_petar INT,
    id_trabajador INT,
    FOREIGN KEY (id_petar) REFERENCES petar(id),
    FOREIGN KEY (id_trabajador) REFERENCES trabajador(id_trabajador)
);
-- Tablas para SSOMA (complementarias a las existentes)







