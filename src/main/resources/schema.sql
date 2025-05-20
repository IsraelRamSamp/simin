SET NAMES 'UTF8MB4';
DROP DATABASE IF EXISTS simin;
CREATE DATABASE IF NOT EXISTS simin DEFAULT CHARACTER SET UTF8MB4;
USE simin;

-- Tipos de usuario
CREATE TABLE tipos_usuario (
                               id_tipo_usuario INT AUTO_INCREMENT PRIMARY KEY,
                               descripcion VARCHAR(50) NOT NULL UNIQUE
);

-- Roles
CREATE TABLE roles (
                       id_rol INT AUTO_INCREMENT PRIMARY KEY,
                       nombre VARCHAR(50) NOT NULL UNIQUE
);

-- Usuarios
CREATE TABLE usuarios (
                          id_usuario INT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(50) NOT NULL,
                          apellido_paterno VARCHAR(50) NOT NULL,
                          apellido_materno VARCHAR(50),
                          correo VARCHAR(100) NOT NULL UNIQUE,
                          contrasena VARCHAR(255) NOT NULL,
                          fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          id_tipo_usuario INT NOT NULL,
                          id_rol INT NOT NULL,
                          FOREIGN KEY (id_tipo_usuario) REFERENCES tipos_usuario(id_tipo_usuario),
                          FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
);

-- Perfiles de Riesgo
CREATE TABLE perfiles_riesgo (
                                 id_perfil_riesgo INT AUTO_INCREMENT PRIMARY KEY,
                                 descripcion VARCHAR(50) NOT NULL UNIQUE
);

-- Tipos de instrumento
CREATE TABLE tipos_instrumento (
                                   id_tipo_instrumento INT AUTO_INCREMENT PRIMARY KEY,
                                   descripcion VARCHAR(50) NOT NULL UNIQUE
);

-- Instrumentos
CREATE TABLE instrumentos (
                              id_instrumento INT AUTO_INCREMENT PRIMARY KEY,
                              nombre VARCHAR(100) NOT NULL UNIQUE,
                              id_tipo_instrumento INT NOT NULL,
                              plazo_meses INT DEFAULT 0 CHECK (plazo_meses BETWEEN 0 AND 12),
                              plazo_anos INT DEFAULT 0 CHECK (plazo_anos >= 0),
                              tasa_interes DECIMAL(5, 2) NOT NULL CHECK (tasa_interes > 0),
                              valor_nominal DECIMAL(15, 2) NOT NULL CHECK (valor_nominal > 0),
                              frecuencia_pagos ENUM('Mensual','Trimestral','Semestral','Anual') NOT NULL DEFAULT 'Anual',
                              tipo_rendimiento ENUM('Fijo','Variable') NOT NULL DEFAULT 'Fijo',
                              descripcion_adicional VARCHAR(250),
                              FOREIGN KEY (id_tipo_instrumento) REFERENCES tipos_instrumento (id_tipo_instrumento)
);

-- Carteras
CREATE TABLE carteras (
                          id_cartera INT AUTO_INCREMENT PRIMARY KEY,
                          nombre_cartera VARCHAR(100) NOT NULL,
                          valor_total DECIMAL(15, 2) DEFAULT 0,
                          fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          id_usuario INT NOT NULL,
                          id_perfil_riesgo INT,
                          FOREIGN KEY (id_usuario) REFERENCES usuarios (id_usuario),
                          FOREIGN KEY (id_perfil_riesgo) REFERENCES perfiles_riesgo(id_perfil_riesgo)
);

-- Instrumentos por Cartera
CREATE TABLE instrumentos_cartera (
                                      id_instrumentos_cartera INT AUTO_INCREMENT PRIMARY KEY,
                                      id_cartera INT NOT NULL,
                                      id_instrumento INT NOT NULL,
                                      cantidad INT NOT NULL CHECK (cantidad > 0),
                                      FOREIGN KEY (id_cartera) REFERENCES carteras (id_cartera),
                                      FOREIGN KEY (id_instrumento) REFERENCES instrumentos (id_instrumento)
);

-- Historial de Rendimientos
CREATE TABLE historial_rendimientos_cartera (
                                                id_historial INT AUTO_INCREMENT PRIMARY KEY,
                                                id_cartera INT NOT NULL,
                                                valor_total DECIMAL(15, 2) NOT NULL,
                                                rendimiento_obtenido DECIMAL(10, 2),
                                                fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                FOREIGN KEY (id_cartera) REFERENCES carteras(id_cartera)
);

-- Simulaciones
CREATE TABLE simulaciones (
                              id_simulacion INT AUTO_INCREMENT PRIMARY KEY,
                              id_usuario INT NOT NULL,
                              id_instrumento INT NOT NULL,
                              monto_invertido DECIMAL(15, 2) NOT NULL CHECK (monto_invertido > 0),
                              fecha_simulacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              fecha_finalizacion DATETIME NOT NULL,
                              FOREIGN KEY (id_usuario) REFERENCES usuarios (id_usuario),
                              FOREIGN KEY (id_instrumento) REFERENCES instrumentos (id_instrumento)
);

-- Historial de Simulaciones
CREATE TABLE historial_simulaciones (
                                        id_historial INT AUTO_INCREMENT PRIMARY KEY,
                                        id_simulacion INT NOT NULL,
                                        descripcion VARCHAR(250),
                                        fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        FOREIGN KEY (id_simulacion) REFERENCES simulaciones (id_simulacion)
);

-- Tipos de Reporte
CREATE TABLE tipos_reporte (
                               id_tipo_reporte INT AUTO_INCREMENT PRIMARY KEY,
                               descripcion VARCHAR(50) NOT NULL UNIQUE
);

-- Reportes
CREATE TABLE reportes (
                          id_reporte INT AUTO_INCREMENT PRIMARY KEY,
                          id_cartera INT NOT NULL,
                          id_tipo_reporte INT NOT NULL,
                          fecha_reporte DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (id_cartera) REFERENCES carteras (id_cartera),
                          FOREIGN KEY (id_tipo_reporte) REFERENCES tipos_reporte (id_tipo_reporte)
);

-- Contenido Educativo
CREATE TABLE contenido_educativo (
                                     id_contenido INT AUTO_INCREMENT PRIMARY KEY,
                                     titulo VARCHAR(100) NOT NULL,
                                     descripcion TEXT NOT NULL,
                                     fecha_publicacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE detalle_simulacion (
                                    id_detalle_simulacion INT AUTO_INCREMENT PRIMARY KEY,
                                    id_simulacion INT NOT NULL,
                                    nombre_instrumento VARCHAR(100) NOT NULL,
                                    tasa_bruta DECIMAL(5,2) NOT NULL,
                                    titulos INT NOT NULL,
                                    inversion DECIMAL(15,2) NOT NULL,
                                    interes DECIMAL(15,2) NOT NULL,
                                    FOREIGN KEY (id_simulacion) REFERENCES simulaciones(id_simulacion) ON DELETE CASCADE
);

-- Índices
CREATE INDEX idx_nombre_usuario ON usuarios(nombre);
CREATE INDEX idx_nombre_cartera ON carteras(nombre_cartera);
CREATE INDEX idx_fecha_simulacion ON simulaciones(fecha_simulacion);
CREATE INDEX idx_historial_rendimiento_fecha ON historial_rendimientos_cartera(fecha_registro);
CREATE INDEX idx_simulacion_tipo ON detalle_simulacion (id_simulacion, tipo_instrumento);
