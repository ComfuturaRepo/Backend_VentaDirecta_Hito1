-- =========================
-- FORMULARIO MAESTRO SSOMA (HOJA 1)
-- =========================
CREATE TABLE ssoma_formulario (
    id_ssoma INT AUTO_INCREMENT PRIMARY KEY,
    id_ots INT NOT NULL,
    empresa_id INT,
    trabajo_id INT,
    latitud DECIMAL(10,7),
    longitud DECIMAL(10,7),
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    hora_inicio TIME,
    hora_fin TIME,
    hora_inicio_trabajo TIME,
    hora_fin_trabajo TIME,
    supervisor_trabajo VARCHAR(150),
    supervisor_sst VARCHAR(150),
    responsable_area VARCHAR(150),
    FOREIGN KEY (id_ots) REFERENCES ots(id_ots),
    FOREIGN KEY (empresa_id) REFERENCES empresa(id_empresa),
    FOREIGN KEY (trabajo_id) REFERENCES maestro_servicio(id_maestro_servicio)
);

-- =========================
-- SECUENCIA DE TAREA (HOJA 1)
-- =========================
CREATE TABLE ssoma_secuencia_tarea (
    id_secuencia INT AUTO_INCREMENT PRIMARY KEY,
    id_ssoma INT NOT NULL,
    secuencia_tarea VARCHAR(255),
    peligro VARCHAR(255),
    riesgo VARCHAR(255),
    consecuencias VARCHAR(255),
    medidas_control VARCHAR(255),
    orden INT,
    FOREIGN KEY (id_ssoma) REFERENCES ssoma_formulario(id_ssoma)
);

-- =========================
-- CHECKLIST SEGURIDAD (HOJA 1 - 8 items)
-- =========================
CREATE TABLE ssoma_checklist_seguridad (
    id_checklist INT AUTO_INCREMENT PRIMARY KEY,
    id_ssoma INT NOT NULL,
    item_nombre VARCHAR(100),
    usado BOOLEAN DEFAULT FALSE,
    observaciones TEXT,
    FOREIGN KEY (id_ssoma) REFERENCES ssoma_formulario(id_ssoma)
);

CREATE TABLE ssoma_checklist_fotos (
    id_foto INT AUTO_INCREMENT PRIMARY KEY,
    id_checklist INT NOT NULL,
    foto_url TEXT,
    FOREIGN KEY (id_checklist) REFERENCES ssoma_checklist_seguridad(id_checklist)
);

-- =========================
-- PARTICIPANTES + FOTO + FIRMA
-- =========================
CREATE TABLE ssoma_participantes (
    id_participante INT AUTO_INCREMENT PRIMARY KEY,
    id_ssoma INT NOT NULL,
    id_trabajador INT,
    nombre VARCHAR(150),
    cargo VARCHAR(100),
    FOREIGN KEY (id_ssoma) REFERENCES ssoma_formulario(id_ssoma),
    FOREIGN KEY (id_trabajador) REFERENCES trabajador(id_trabajador)
);

CREATE TABLE ssoma_participante_firma (
    id_firma INT AUTO_INCREMENT PRIMARY KEY,
    id_participante INT NOT NULL,
    firma_url TEXT,
    fecha_firma DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_participante) REFERENCES ssoma_participantes(id_participante)
);

CREATE TABLE ssoma_participante_fotos (
    id_foto INT AUTO_INCREMENT PRIMARY KEY,
    id_participante INT NOT NULL,
    foto_url TEXT,
    tipo_foto ENUM('FRONTAL', 'CREDENCIAL') DEFAULT 'FRONTAL',
    FOREIGN KEY (id_participante) REFERENCES ssoma_participantes(id_participante)
);

-- =========================
-- EPP USADOS + FOTO
-- =========================
CREATE TABLE ssoma_epp_checks (
    id_epp INT AUTO_INCREMENT PRIMARY KEY,
    id_ssoma INT NOT NULL,
    epp_nombre VARCHAR(100),
    usado BOOLEAN,
    FOREIGN KEY (id_ssoma) REFERENCES ssoma_formulario(id_ssoma)
);

CREATE TABLE ssoma_epp_fotos (
    id_foto INT AUTO_INCREMENT PRIMARY KEY,
    id_epp INT NOT NULL,
    foto_url TEXT,
    FOREIGN KEY (id_epp) REFERENCES ssoma_epp_checks(id_epp)
);

-- =========================
-- CHARLA 5 MINUTOS (HOJA 2)
-- =========================
CREATE TABLE ssoma_tema_charla (
    id_tema INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    duracion_minutos INT,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE ssoma_charla (
    id_charla INT AUTO_INCREMENT PRIMARY KEY,
    id_ssoma INT NOT NULL,
    id_tema INT,
    fecha_charla DATETIME DEFAULT CURRENT_TIMESTAMP,
    duracion_horas DECIMAL(4,2),
    capacitador_id INT,
    FOREIGN KEY (id_ssoma) REFERENCES ssoma_formulario(id_ssoma),
    FOREIGN KEY (id_tema) REFERENCES ssoma_tema_charla(id_tema),
    FOREIGN KEY (capacitador_id) REFERENCES trabajador(id_trabajador)
);

CREATE TABLE ssoma_charla_video (
    id_video INT AUTO_INCREMENT PRIMARY KEY,
    id_charla INT NOT NULL,
    video_url TEXT,
    duracion_segundos INT,
    fecha_subida DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_charla) REFERENCES ssoma_charla(id_charla)
);

-- =========================
-- INSPECCION POR TRABAJADOR (HOJA 3)
-- =========================
CREATE TABLE ssoma_inspeccion_trabajador (
    id_inspeccion INT AUTO_INCREMENT PRIMARY KEY,
    id_ssoma INT NOT NULL,
    tipo_inspeccion ENUM('PLANIFICADA', 'NO_PLANIFICADA') DEFAULT 'PLANIFICADA',
    trabajador_id INT,
    trabajador_nombre VARCHAR(150),
    casco BOOLEAN,
    lentes BOOLEAN,
    orejeras BOOLEAN,
    tapones BOOLEAN,
    guantes BOOLEAN,
    botas BOOLEAN,
    arnes BOOLEAN,
    chaleco BOOLEAN,
    mascarilla BOOLEAN,
    gafas BOOLEAN,
    otros TEXT,
    accion_correctiva TEXT,
    seguimiento TEXT,
    responsable_id INT,
    FOREIGN KEY (id_ssoma) REFERENCES ssoma_formulario(id_ssoma),
    FOREIGN KEY (trabajador_id) REFERENCES trabajador(id_trabajador),
    FOREIGN KEY (responsable_id) REFERENCES trabajador(id_trabajador)
);

-- =========================
-- HERRAMIENTAS MANUALES
-- =========================
CREATE TABLE ssoma_herramienta_maestra (
    id_herramienta_maestra INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE ssoma_herramienta_inspeccion (
    id_herramienta INT AUTO_INCREMENT PRIMARY KEY,
    id_ssoma INT NOT NULL,
    id_herramienta_maestra INT,
    herramienta_nombre VARCHAR(150),
    p1 BOOLEAN, -- ¿Está en buen estado?
    p2 BOOLEAN, -- ¿Tiene filo adecuado?
    p3 BOOLEAN, -- ¿Mango en buen estado?
    p4 BOOLEAN, -- ¿Sin grietas?
    p5 BOOLEAN, -- ¿Limpia?
    p6 BOOLEAN, -- ¿Sin óxido?
    p7 BOOLEAN, -- ¿Guardada correctamente?
    p8 BOOLEAN, -- ¿Identificada?
    observaciones TEXT,
    FOREIGN KEY (id_ssoma) REFERENCES ssoma_formulario(id_ssoma),
    FOREIGN KEY (id_herramienta_maestra) REFERENCES ssoma_herramienta_maestra(id_herramienta_maestra)
);

CREATE TABLE ssoma_herramienta_fotos (
    id_foto INT AUTO_INCREMENT PRIMARY KEY,
    id_herramienta INT NOT NULL,
    foto_url TEXT,
    FOREIGN KEY (id_herramienta) REFERENCES ssoma_herramienta_inspeccion(id_herramienta)
);

-- =========================
-- PETAR (ALTO RIESGO)
-- =========================
CREATE TABLE ssoma_petar (
    id_petar INT AUTO_INCREMENT PRIMARY KEY,
    id_ssoma INT NOT NULL,
    energia_peligrosa BOOLEAN,
    trabajo_altura BOOLEAN,
    izaje BOOLEAN,
    excavacion BOOLEAN,
    espacios_confinados BOOLEAN,
    trabajo_caliente BOOLEAN,
    otros BOOLEAN,
    otros_descripcion TEXT,
    velocidad_aire VARCHAR(50),
    contenido_oxigeno VARCHAR(50),
    hora_inicio_petar TIME,
    hora_fin_petar TIME,
    FOREIGN KEY (id_ssoma) REFERENCES ssoma_formulario(id_ssoma)
);

CREATE TABLE ssoma_petar_pregunta_maestra (
    id_pregunta INT AUTO_INCREMENT PRIMARY KEY,
    categoria VARCHAR(100),
    pregunta VARCHAR(255) NOT NULL,
    orden INT,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE ssoma_petar_respuestas (
    id_respuesta INT AUTO_INCREMENT PRIMARY KEY,
    id_petar INT NOT NULL,
    id_pregunta INT,
    respuesta BOOLEAN,
    observaciones TEXT,
    FOREIGN KEY (id_petar) REFERENCES ssoma_petar(id_petar),
    FOREIGN KEY (id_pregunta) REFERENCES ssoma_petar_pregunta_maestra(id_pregunta)
);

-- =========================
-- EQUIPOS PROTECCIÓN LEGALES (PETAR)
-- =========================
CREATE TABLE ssoma_equipo_proteccion (
    id_equipo INT AUTO_INCREMENT PRIMARY KEY,
    id_petar INT NOT NULL,
    equipo_nombre VARCHAR(100),
    usado BOOLEAN,
    FOREIGN KEY (id_petar) REFERENCES ssoma_petar(id_petar)
);