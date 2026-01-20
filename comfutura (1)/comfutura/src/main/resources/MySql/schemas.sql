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

                         CONSTRAINT fk_usuario_trabajador
                             FOREIGN KEY (id_trabajador) REFERENCES trabajador(id_trabajador)
);

CREATE TABLE rol (
                     id_rol INT AUTO_INCREMENT PRIMARY KEY,
                     nombre VARCHAR(50) NOT NULL UNIQUE,
                     descripcion VARCHAR(150)
);

CREATE TABLE usuario_rol (
                             id_usuario INT NOT NULL,
                             id_rol INT NOT NULL,

                             PRIMARY KEY (id_usuario, id_rol),

                             CONSTRAINT fk_ur_usuario
                                 FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),

                             CONSTRAINT fk_ur_rol
                                 FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
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
                          descripcion VARCHAR(255),
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
                      nombre VARCHAR(150) NOT NULL,
                      direccion VARCHAR(255),
                      activo TINYINT(1) DEFAULT 1
);
CREATE TABLE region (
                        id_region INT AUTO_INCREMENT PRIMARY KEY,
                        nombre VARCHAR(100) NOT NULL,
                        activo TINYINT(1) DEFAULT 1
);

CREATE TABLE ots (
                     id_ots INT AUTO_INCREMENT PRIMARY KEY,

                     ot BIGINT NOT NULL UNIQUE,          -- Número de OT
                     ceco VARCHAR(20) NOT NULL,          -- Centro de costos

                     id_ots_anterior INT NULL,              -- OT anterior
                     id_cliente INT NOT NULL,
                     id_area INT NOT NULL,
                     id_proyecto INT NOT NULL,
                     id_fase INT NOT NULL,
                     id_site INT NOT NULL,
                     id_region INT NOT NULL,

                     descripcion TEXT,
                     fecha_apertura DATE NOT NULL,
                     dias_asignados INT DEFAULT 0,       -- Días asignados a la fecha

                     activo TINYINT(1) DEFAULT 1,
                     fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                     CONSTRAINT fk_ots_padre
                         FOREIGN KEY (id_ots_anterior) REFERENCES ots(id_ots),

                     CONSTRAINT fk_ots_cliente
                         FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),

                     CONSTRAINT fk_ots_area
                         FOREIGN KEY (id_area) REFERENCES area(id_area),

                     CONSTRAINT fk_ots_proyecto
                         FOREIGN KEY (id_proyecto) REFERENCES proyecto(id_proyecto),

                     CONSTRAINT fk_ots_fase
                         FOREIGN KEY (id_fase) REFERENCES fase(id_fase),

                     CONSTRAINT fk_ots_site
                         FOREIGN KEY (id_site) REFERENCES site(id_site),

                     CONSTRAINT fk_ots_region
                         FOREIGN KEY (id_region) REFERENCES region(id_region)
);


-- =====================================================
-- 8. RELACIONES OTS
-- =====================================================

-- Trabajadores asignados a la OT (roles dinámicos)
CREATE TABLE ots_trabajador (
                                id_ots INT NOT NULL,
                                id_trabajador INT NOT NULL,
                                rol_en_ot VARCHAR(50) NOT NULL,
                                fecha_asignacion datetime DEFAULT current_timestamp,
                                activo TINYINT(1) DEFAULT 1,

                                PRIMARY KEY (id_ots, id_trabajador, rol_en_ot),

                                CONSTRAINT fk_ot_trab_ots
                                    FOREIGN KEY (id_ots) REFERENCES ots(id_ots),

                                CONSTRAINT fk_ot_trab_trabajador
                                    FOREIGN KEY (id_trabajador) REFERENCES trabajador(id_trabajador)
);

-- =========================
-- ESTADOS DE ORDEN DE COMPRA
-- =========================
CREATE TABLE estado_oc (
                           id_estado_oc INT AUTO_INCREMENT PRIMARY KEY,
                           nombre VARCHAR(50) NOT NULL UNIQUE
);
CREATE TABLE orden_compra (
                              id_oc INT AUTO_INCREMENT PRIMARY KEY,

                              id_estado_oc INT NOT NULL,
                              id_ots INT NOT NULL,
                              id_maestro INT NOT NULL,
                              id_proveedor INT NOT NULL,

                              cantidad DECIMAL(10,2) NOT NULL,
                              costo_unitario DECIMAL(10,2) NOT NULL,

                              fecha_oc TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              observacion VARCHAR(255),

                              CONSTRAINT fk_oc_estado
                                  FOREIGN KEY (id_estado_oc) REFERENCES estado_oc(id_estado_oc),

                              CONSTRAINT fk_oc_ots
                                  FOREIGN KEY (id_ots) REFERENCES ots(id_ots),

                              CONSTRAINT fk_oc_maestro
                                  FOREIGN KEY (id_maestro) REFERENCES maestro_codigo(id_maestro),

                              CONSTRAINT fk_oc_proveedor
                                  FOREIGN KEY (id_proveedor) REFERENCES proveedor(id_proveedor)
);



