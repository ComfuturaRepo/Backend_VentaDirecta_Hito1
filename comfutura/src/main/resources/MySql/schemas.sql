-- =====================================================
-- 1. BASE DE DATOS
-- =====================================================

DROP DATABASE IF EXISTS comfutura;

CREATE DATABASE comfutura
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE comfutura;

-- =====================================================
-- 2. MAESTROS ORGANIZACIONALES
-- =====================================================

CREATE TABLE empresa (
                         id_empresa INT AUTO_INCREMENT PRIMARY KEY,
                         nombre VARCHAR(100) NOT NULL,
                         activo TINYINT(1) DEFAULT 1
);

CREATE TABLE cliente (
                         id_cliente INT AUTO_INCREMENT PRIMARY KEY,
                         razon_social VARCHAR(150) NOT NULL,
                         ruc CHAR(11) NOT NULL UNIQUE,
                         activo TINYINT(1) DEFAULT 1
);

CREATE TABLE nivel (
                       id_nivel INT AUTO_INCREMENT PRIMARY KEY,
                       codigo VARCHAR(5) NOT NULL UNIQUE,      -- L1, L2, L3...
                       nombre VARCHAR(50) NOT NULL,             -- Gerencial, Operativo
                       descripcion VARCHAR(150)
);

CREATE TABLE area (
                      id_area INT AUTO_INCREMENT PRIMARY KEY,
                      nombre VARCHAR(100) NOT NULL,
                      activo TINYINT(1) DEFAULT 1
);

CREATE TABLE cargo (
                       id_cargo INT AUTO_INCREMENT PRIMARY KEY,
                       nombre VARCHAR(100) NOT NULL,
                       id_nivel INT NOT NULL,
                       activo TINYINT(1) DEFAULT 1,

                       CONSTRAINT fk_cargo_nivel
                           FOREIGN KEY (id_nivel) REFERENCES nivel(id_nivel)
);
CREATE TABLE cliente_area (
                              id_cliente INT NOT NULL,
                              id_area INT NOT NULL,
                              activo TINYINT(1) DEFAULT 1,

                              PRIMARY KEY (id_cliente, id_area),

                              CONSTRAINT fk_ca_cliente
                                  FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),

                              CONSTRAINT fk_ca_area
                                  FOREIGN KEY (id_area) REFERENCES area(id_area)
);

-- =====================================================
-- 3. TRABAJADORES
-- =====================================================

CREATE TABLE trabajador (
                            id_trabajador INT AUTO_INCREMENT PRIMARY KEY,

                            nombres VARCHAR(100) NOT NULL,
                            apellidos VARCHAR(100) NOT NULL,
                            dni CHAR(8) NOT NULL UNIQUE,

                            celular VARCHAR(15),
                            correo_corporativo VARCHAR(150),

                            id_empresa INT NOT NULL,
                            id_area INT NOT NULL,
                            id_cargo INT NOT NULL,

                            activo TINYINT(1) DEFAULT 1,
                            fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT fk_trabajador_empresa
                                FOREIGN KEY (id_empresa) REFERENCES empresa(id_empresa),

                            CONSTRAINT fk_trabajador_area
                                FOREIGN KEY (id_area) REFERENCES area(id_area),

                            CONSTRAINT fk_trabajador_cargo
                                FOREIGN KEY (id_cargo) REFERENCES cargo(id_cargo)
);

CREATE TABLE trabajador_cliente (
                                    id_trabajador INT NOT NULL,
                                    id_cliente INT NOT NULL,

                                    PRIMARY KEY (id_trabajador, id_cliente),

                                    CONSTRAINT fk_tc_trabajador
                                        FOREIGN KEY (id_trabajador) REFERENCES trabajador(id_trabajador),

                                    CONSTRAINT fk_tc_cliente
                                        FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);

-- =====================================================
-- 4. SEGURIDAD Y ACCESOS
-- =====================================================

CREATE TABLE usuario (
                         id_usuario INT AUTO_INCREMENT PRIMARY KEY,

                         username VARCHAR(50) NOT NULL UNIQUE,
                         password VARCHAR(255) NOT NULL,

                         id_trabajador INT NOT NULL,

                         activo TINYINT(1) DEFAULT 1,
                         fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         id_nivel int not null,

                         CONSTRAINT fk_id_nivel
                             FOREIGN KEY (id_nivel) REFERENCES nivel(id_nivel),
                         CONSTRAINT fk_usuario_trabajador
                             FOREIGN KEY (id_trabajador) REFERENCES trabajador(id_trabajador)
);

-- =====================================================
-- 5. PROVEEDORES
-- =====================================================

CREATE TABLE banco (
                       id_banco INT AUTO_INCREMENT PRIMARY KEY,
                       nombre VARCHAR(100) NOT NULL,
                       activo TINYINT(1) DEFAULT 1
);

CREATE TABLE proveedor (
                           id_proveedor INT AUTO_INCREMENT PRIMARY KEY,

                           ruc CHAR(11) NOT NULL UNIQUE,
                           razon_social VARCHAR(150) NOT NULL,

                           direccion VARCHAR(255),
                           distrito VARCHAR(100),
                           provincia VARCHAR(100),
                           departamento VARCHAR(100),

                           contacto VARCHAR(150),
                           telefono VARCHAR(20),
                           correo VARCHAR(150),

                           id_banco INT NOT NULL,
                           numero_cuenta VARCHAR(50) NOT NULL,
                           moneda VARCHAR(10) NOT NULL, -- PEN, USD

                           activo TINYINT(1) DEFAULT 1,
                           fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                           CONSTRAINT fk_proveedor_banco
                               FOREIGN KEY (id_banco) REFERENCES banco(id_banco)
);

-- =====================================================
-- 6. MAESTRO DE SERVICIOS / MATERIALES
-- =====================================================

CREATE TABLE unidad_medida (
                               id_unidad_medida INT AUTO_INCREMENT PRIMARY KEY,
                               codigo VARCHAR(10) NOT NULL UNIQUE,   -- UND, HORA, MES
                               descripcion VARCHAR(100) NOT NULL,
                               activo TINYINT(1) DEFAULT 1
);

CREATE TABLE maestro_codigo (
                                id_maestro INT AUTO_INCREMENT PRIMARY KEY,

                                codigo VARCHAR(20) NOT NULL UNIQUE,   -- S000001
                                descripcion VARCHAR(255) NOT NULL,

                                id_unidad_medida INT NOT NULL,
                                precio_base DECIMAL(10,2) NOT NULL,

                                activo TINYINT(1) DEFAULT 1,
                                fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                CONSTRAINT fk_maestro_unidad
                                    FOREIGN KEY (id_unidad_medida) REFERENCES unidad_medida(id_unidad_medida)
);

-- =====================================================
-- 7. OTS (NÚCLEO DEL SISTEMA)
-- =====================================================
CREATE TABLE proyecto (
                          id_proyecto INT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(150) NOT NULL,
                          activo TINYINT(1) DEFAULT 1
);
CREATE TABLE fase (
                      id_fase INT AUTO_INCREMENT PRIMARY KEY,
                      nombre VARCHAR(100) NOT NULL,
                      orden INT,
                      activo TINYINT(1) DEFAULT 1
);
CREATE TABLE site (
                      id_site INT AUTO_INCREMENT PRIMARY KEY,
                      codigo_sitio VARCHAR(150) NOT NULL,
                      descripcion VARCHAR(255),
                      activo TINYINT(1) DEFAULT 1
);
CREATE TABLE region (
                        id_region INT AUTO_INCREMENT PRIMARY KEY,
                        nombre VARCHAR(100) NOT NULL,
                        activo TINYINT(1) DEFAULT 1
);

CREATE TABLE estado_ot (
                           id_estado_ot INT AUTO_INCREMENT PRIMARY KEY,
                           descripcion VARCHAR(100) NOT NULL UNIQUE,
                           activo TINYINT(1) DEFAULT 1
);

-- 1. Jefatura del cliente solicitante
CREATE TABLE jefatura_cliente_solicitante (
                                              id INT AUTO_INCREMENT PRIMARY KEY,               -- ← cambia a "id"
                                              descripcion VARCHAR(150) NOT NULL,
                                              activo TINYINT(1) DEFAULT 1
);

-- 2. Analista del cliente solicitante
CREATE TABLE analista_cliente_solicitante (
                                              id INT AUTO_INCREMENT PRIMARY KEY,
                                              descripcion VARCHAR(150) NOT NULL,
                                              activo TINYINT(1) DEFAULT 1
);



CREATE TABLE ots (
                     id_ots INT AUTO_INCREMENT PRIMARY KEY,
                     ot INT NOT NULL UNIQUE,
                     id_ots_anterior INT NULL,

                     id_cliente  INT NOT NULL,
                     id_area     INT NOT NULL,
                     id_proyecto INT NOT NULL,
                     id_fase     INT NOT NULL,
                     id_site     INT NOT NULL,
                     id_region   INT NOT NULL,

                     descripcion TEXT,
                     fecha_apertura DATE NOT NULL,
                     id_jefatura_cliente_solicitante   INT DEFAULT NULL,
                     id_analista_cliente_solicitante   INT DEFAULT NULL,
    -- 🔥 ROLES (TODOS SON TRABAJADORES)
                     id_coordinador_ti_cw    INT DEFAULT NULL,
                     id_jefatura_responsable INT DEFAULT NULL,
                     id_liquidador           INT DEFAULT NULL,
                     id_ejecutante           INT DEFAULT NULL,
                     id_analista_contable    INT DEFAULT NULL,

                     dias_asignados INT DEFAULT 0,
                     id_trabajador int not null,

                     id_estado_ot INT not null,
                     activo TINYINT(1) DEFAULT 1,
                     fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Relaciones normales
                     CONSTRAINT fk_ots_cliente
                         FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
                     CONSTRAINT fk_ots_jefatura_cliente
                         FOREIGN KEY (id_jefatura_cliente_solicitante) REFERENCES jefatura_cliente_solicitante(id),
                     CONSTRAINT fk_ots_id_trabajador
                         FOREIGN KEY (id_trabajador) REFERENCES trabajador(id_trabajador),

                     CONSTRAINT fk_ots_analista_cliente
                         FOREIGN KEY (id_analista_cliente_solicitante) REFERENCES analista_cliente_solicitante(id),
                     CONSTRAINT fk_ots_area
                         FOREIGN KEY (id_area) REFERENCES area(id_area),

                     CONSTRAINT fk_ots_proyecto
                         FOREIGN KEY (id_proyecto) REFERENCES proyecto(id_proyecto),

                     CONSTRAINT fk_ots_fase
                         FOREIGN KEY (id_fase) REFERENCES fase(id_fase),

                     CONSTRAINT fk_ots_site
                         FOREIGN KEY (id_site) REFERENCES site(id_site),

                     CONSTRAINT fk_ots_region
                         FOREIGN KEY (id_region) REFERENCES region(id_region),
                     CONSTRAINT fk_ot_estado
                         FOREIGN KEY (id_estado_ot) REFERENCES estado_ot(id_estado_ot),
    -- 🔥 FKs A TRABAJADOR (NO A CARGOS)
                     CONSTRAINT fk_ots_coord
                         FOREIGN KEY (id_coordinador_ti_cw) REFERENCES trabajador(id_trabajador),

                     CONSTRAINT fk_ots_jef_resp
                         FOREIGN KEY (id_jefatura_responsable) REFERENCES trabajador(id_trabajador),

                     CONSTRAINT fk_ots_liq
                         FOREIGN KEY (id_liquidador) REFERENCES trabajador(id_trabajador),

                     CONSTRAINT fk_ots_ejec
                         FOREIGN KEY (id_ejecutante) REFERENCES trabajador(id_trabajador),

                     CONSTRAINT fk_ots_anal_cont
                         FOREIGN KEY (id_analista_contable) REFERENCES trabajador(id_trabajador)
);



-- ======================================
-- TABLA: ESTADOS DE ORDEN DE COMPRA
-- ======================================
CREATE TABLE estado_oc (
                           id_estado_oc INT AUTO_INCREMENT PRIMARY KEY,
                           nombre VARCHAR(50) NOT NULL UNIQUE
);

-- ======================================
-- TABLA: ORDEN DE COMPRA (CABECERA)
-- ======================================
CREATE TABLE IF NOT EXISTS orden_compra (
                                            id_oc INT AUTO_INCREMENT PRIMARY KEY,

                                            id_estado_oc INT NOT NULL,
                                            id_ots INT NOT NULL,
                                            id_proveedor INT NOT NULL,

                                            forma_pago VARCHAR(50),

    subtotal DECIMAL(12,2),
    igv_porcentaje DECIMAL(5,2),
    igv_total DECIMAL(12,2),
    total DECIMAL(12,2),

    fecha_oc DATETIME,
    observacion TEXT,

    CONSTRAINT fk_oc_estado
    FOREIGN KEY (id_estado_oc)
    REFERENCES estado_oc(id_estado_oc)
    );

-- ======================================
-- TABLA: DETALLE DE ORDEN DE COMPRA
-- ======================================
CREATE TABLE IF NOT EXISTS oc_detalle (
                                          id_oc_detalle INT AUTO_INCREMENT PRIMARY KEY,

                                          id_oc INT NOT NULL,
                                          id_maestro INT NOT NULL,

                                          cantidad DECIMAL(10,2) NOT NULL,
    precio_unitario DECIMAL(12,2) NOT NULL,

    subtotal DECIMAL(12,2),
    igv DECIMAL(12,2),
    total DECIMAL(12,2),

    CONSTRAINT fk_oc_detalle_oc
    FOREIGN KEY (id_oc)
    REFERENCES orden_compra(id_oc)
    );

CREATE TABLE maestro_partida (
                                 id_maestro_partida INT AUTO_INCREMENT PRIMARY KEY,
                                 codigo VARCHAR(20) NOT NULL UNIQUE,
                                 descripcion VARCHAR(255) NOT NULL,
                                 activo TINYINT(1) DEFAULT 1,
                                 CONSTRAINT uk_maestro_partida_codigo UNIQUE (codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_maestro_partida_codigo ON maestro_partida(codigo);

-- Maestro Servicios
CREATE TABLE maestro_servicio (
                                  id_maestro_servicio INT AUTO_INCREMENT PRIMARY KEY,
                                  codigo VARCHAR(20) NOT NULL UNIQUE,
                                  descripcion VARCHAR(255) NOT NULL,
                                  id_unidad_medida INT NULL,
                                  costo_base DECIMAL(12,2) DEFAULT 0,
                                  activo TINYINT(1) DEFAULT 1,
                                  CONSTRAINT fk_maestro_servicio_um FOREIGN KEY (id_unidad_medida)
                                      REFERENCES unidad_medida(id_unidad_medida),
                                  CONSTRAINT uk_maestro_servicio_codigo UNIQUE (codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_maestro_servicio_codigo ON maestro_servicio(codigo);

-- Maestro Materiales
CREATE TABLE maestro_material (
                                  id_maestro_material INT AUTO_INCREMENT PRIMARY KEY,
                                  codigo VARCHAR(20) NOT NULL UNIQUE,
                                  descripcion VARCHAR(255) NOT NULL,
                                  id_unidad_medida INT NULL,
                                  costo_base DECIMAL(12,2) DEFAULT 0,
                                  activo TINYINT(1) DEFAULT 1,
                                  CONSTRAINT fk_maestro_material_um FOREIGN KEY (id_unidad_medida)
                                      REFERENCES unidad_medida(id_unidad_medida),
                                  CONSTRAINT uk_maestro_material_codigo UNIQUE (codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_maestro_material_codigo ON maestro_material(codigo);

-- =====================================================
-- 1. TABLA CRONOGRAMA_OT
-- Propósito: Partidas/fases del proyecto por OT
-- =====================================================

CREATE TABLE cronograma_ot (
                               id_cronograma INT AUTO_INCREMENT PRIMARY KEY,
                               id_ots INT NOT NULL,
                               id_maestro_partida INT NOT NULL,
                               duracion_dias DECIMAL(10,2) DEFAULT 0,
                               fecha_inicio DATE,
                               fecha_fin DATE,
                               activo TINYINT(1) DEFAULT 1,
                               CONSTRAINT fk_cronograma_ots FOREIGN KEY (id_ots)
                                   REFERENCES ots(id_ots) ON DELETE CASCADE,
                               CONSTRAINT fk_cronograma_maestro_partida FOREIGN KEY (id_maestro_partida)
                                   REFERENCES maestro_partida(id_maestro_partida)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_cronograma_ots ON cronograma_ot(id_ots);
CREATE INDEX idx_cronograma_maestro ON cronograma_ot(id_maestro_partida);

-- =====================================================
-- 2. TABLA RESUMEN_OT
-- Propósito: Resumen de costos por tipo (flexible)
-- =====================================================

CREATE TABLE resumen_ot (
                            id_resumen INT AUTO_INCREMENT PRIMARY KEY,
                            id_ots INT NOT NULL,
                            tipo_gasto VARCHAR(50) NOT NULL,
                            monto DECIMAL(12,2) DEFAULT 0,
                            activo TINYINT(1) DEFAULT 1,
                            CONSTRAINT fk_resumen_ots FOREIGN KEY (id_ots)
                                REFERENCES ots(id_ots) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_resumen_ots ON resumen_ot(id_ots);

-- =====================================================
-- 3. TABLA MATERIAL_OT
-- Propósito: Materiales/productos requeridos por OT
-- =====================================================

CREATE TABLE material_ot (
                             id_material_ot INT AUTO_INCREMENT PRIMARY KEY,
                             id_ots INT NOT NULL,
                             id_maestro_material INT NULL,
                             id_unidad_medida INT NULL,
                             cantidad DECIMAL(12,2) DEFAULT 1,
                             costo_unitario DECIMAL(12,2) DEFAULT 0,
                             total DECIMAL(12,2) DEFAULT 0,
                             moneda VARCHAR(10) DEFAULT 'PEN',
                             activo TINYINT(1) DEFAULT 1,
                             CONSTRAINT fk_material_ots FOREIGN KEY (id_ots)
                                 REFERENCES ots(id_ots) ON DELETE CASCADE,
                             CONSTRAINT fk_material_maestro FOREIGN KEY (id_maestro_material)
                                 REFERENCES maestro_material(id_maestro_material),
                             CONSTRAINT fk_material_um FOREIGN KEY (id_unidad_medida)
                                 REFERENCES unidad_medida(id_unidad_medida)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_material_ots ON material_ot(id_ots);
CREATE INDEX idx_material_maestro ON material_ot(id_maestro_material);
CREATE INDEX idx_material_um ON material_ot(id_unidad_medida);

-- =====================================================
-- 4. TABLA CONTRATISTA_OT
-- Propósito: Servicios de terceros/contratistas por OT
-- =====================================================

CREATE TABLE contratista_ot (
                                id_contratista_ot INT AUTO_INCREMENT PRIMARY KEY,
                                id_ots INT NOT NULL,
                                id_maestro_servicio INT NULL,
                                id_unidad_medida INT NULL,
                                id_proveedor INT NULL,
                                cantidad DECIMAL(12,2) DEFAULT 1,
                                costo_unitario DECIMAL(12,2) DEFAULT 0,
                                total DECIMAL(12,2) DEFAULT 0,
                                activo TINYINT(1) DEFAULT 1,
                                CONSTRAINT fk_contratista_ots FOREIGN KEY (id_ots)
                                    REFERENCES ots(id_ots) ON DELETE CASCADE,
                                CONSTRAINT fk_contratista_maestro FOREIGN KEY (id_maestro_servicio)
                                    REFERENCES maestro_servicio(id_maestro_servicio),
                                CONSTRAINT fk_contratista_um FOREIGN KEY (id_unidad_medida)
                                    REFERENCES unidad_medida(id_unidad_medida),
                                CONSTRAINT fk_contratista_prov FOREIGN KEY (id_proveedor)
                                    REFERENCES proveedor(id_proveedor)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_contratista_ots ON contratista_ot(id_ots);
CREATE INDEX idx_contratista_maestro ON contratista_ot(id_maestro_servicio);
CREATE INDEX idx_contratista_prov ON contratista_ot(id_proveedor);

-- =====================================================
-- 5. TABLA GASTO_LOGISTICO_OT
-- Propósito: Gastos de transporte/logística por OT
-- =====================================================

CREATE TABLE gasto_logistico_ot (
                                    id_gasto_log INT AUTO_INCREMENT PRIMARY KEY,
                                    id_ots INT NOT NULL,
                                    concepto VARCHAR(150) NOT NULL,
                                    id_unidad_medida INT NULL,
                                    cantidad DECIMAL(12,2) DEFAULT 1,
                                    precio DECIMAL(12,2) DEFAULT 0,
                                    total DECIMAL(12,2) DEFAULT 0,
                                    id_proveedor INT NULL,
                                    activo TINYINT(1) DEFAULT 1,
                                    CONSTRAINT fk_gl_ots FOREIGN KEY (id_ots)
                                        REFERENCES ots(id_ots) ON DELETE CASCADE,
                                    CONSTRAINT fk_gl_um FOREIGN KEY (id_unidad_medida)
                                        REFERENCES unidad_medida(id_unidad_medida),
                                    CONSTRAINT fk_gl_proveedor FOREIGN KEY (id_proveedor)
                                        REFERENCES proveedor(id_proveedor)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_gl_ots ON gasto_logistico_ot(id_ots);
CREATE INDEX idx_gl_prov ON gasto_logistico_ot(id_proveedor);
CREATE INDEX idx_gl_um ON gasto_logistico_ot(id_unidad_medida);

-- =====================================================
-- 6. TABLA VIATICO_OT
-- Propósito: Viáticos, pasajes, cajas (unificado)
-- =====================================================

CREATE TABLE viatico_ot (
                            id_viatico INT AUTO_INCREMENT PRIMARY KEY,
                            id_ots INT NOT NULL,
                            tipo ENUM('MOVILIDAD_CAJA','PASAJE','CAJA_CHICA','VIATICO') NOT NULL,
                            concepto VARCHAR(150),
                            id_unidad_medida INT NULL,
                            cantidad DECIMAL(12,2) DEFAULT 1,
                            precio DECIMAL(12,2) DEFAULT 0,
                            costo_dia DECIMAL(12,2) DEFAULT 0,
                            cant_dias DECIMAL(12,2) DEFAULT 0,
                            total DECIMAL(12,2) DEFAULT 0,
                            id_trabajador INT NULL,
                            id_proveedor INT NULL,
                            fecha DATE,
                            tipo_doc VARCHAR(20),
                            ruc_dni VARCHAR(20),
                            id_banco INT NULL,
                            moneda VARCHAR(10) DEFAULT 'PEN',
                            cuenta VARCHAR(50),
                            cci VARCHAR(50),
                            activo TINYINT(1) DEFAULT 1,
                            CONSTRAINT fk_viatico_ots FOREIGN KEY (id_ots)
                                REFERENCES ots(id_ots) ON DELETE CASCADE,
                            CONSTRAINT fk_viatico_trab FOREIGN KEY (id_trabajador)
                                REFERENCES trabajador(id_trabajador),
                            CONSTRAINT fk_viatico_prov FOREIGN KEY (id_proveedor)
                                REFERENCES proveedor(id_proveedor),
                            CONSTRAINT fk_viatico_um FOREIGN KEY (id_unidad_medida)
                                REFERENCES unidad_medida(id_unidad_medida),
                            CONSTRAINT fk_viatico_banco FOREIGN KEY (id_banco)
                                REFERENCES banco(id_banco)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_viatico_ots ON viatico_ot(id_ots);
CREATE INDEX idx_viatico_tipo ON viatico_ot(tipo);
CREATE INDEX idx_viatico_trab ON viatico_ot(id_trabajador);

-- =====================================================
-- 7. TABLA PLANILLA_TRABAJO_OT
-- Propósito: Planilla de trabajo/mano de obra por OT
-- =====================================================

CREATE TABLE planilla_trabajo_ot (
                                     id_planilla INT AUTO_INCREMENT PRIMARY KEY,
                                     id_ots INT NOT NULL,
                                     id_trabajador INT NULL,
                                     id_cargo INT NULL,
                                     fecha DATE,
                                     costo_dia DECIMAL(12,2) DEFAULT 0,
                                     cant_dias DECIMAL(12,2) DEFAULT 0,
                                     total DECIMAL(12,2) DEFAULT 0,
                                     tipo_doc VARCHAR(20),
                                     ruc_dni VARCHAR(20),
                                     id_banco INT NULL,
                                     moneda VARCHAR(10) DEFAULT 'PEN',
                                     cuenta VARCHAR(50),
                                     cci VARCHAR(50),
                                     activo TINYINT(1) DEFAULT 1,
                                     CONSTRAINT fk_planilla_ots FOREIGN KEY (id_ots)
                                         REFERENCES ots(id_ots) ON DELETE CASCADE,
                                     CONSTRAINT fk_planilla_trab FOREIGN KEY (id_trabajador)
                                         REFERENCES trabajador(id_trabajador),
                                     CONSTRAINT fk_planilla_cargo FOREIGN KEY (id_cargo)
                                         REFERENCES cargo(id_cargo),
                                     CONSTRAINT fk_planilla_banco FOREIGN KEY (id_banco)
                                         REFERENCES banco(id_banco)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_planilla_ots ON planilla_trabajo_ot(id_ots);
CREATE INDEX idx_planilla_trab ON planilla_trabajo_ot(id_trabajador);
CREATE INDEX idx_planilla_cargo ON planilla_trabajo_ot(id_cargo);
