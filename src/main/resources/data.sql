INSERT INTO roles (nombre) VALUES ('ADMIN'), ('INVERSIONISTA'), ('ANALISTA');
INSERT INTO tipos_usuario (descripcion) VALUES ('Administrador'), ('Inversionista'), ('Analista');
INSERT INTO usuarios (nombre, apellido_paterno, apellido_materno, correo, contrasena, id_tipo_usuario, id_rol) VALUES
                                                                                                                   ('Carlos', 'López', 'Ramírez', 'carlos.lopez@simin.com', '{bcrypt}$2a$10$V7HaxkFPXybT1Xsda/fe5uxlXR9Ii7b5x354FR41ZgNyuTey0Mvp2', 1, 1),
                                                                                                                   ('María', 'Fernández', 'Gómez', 'maria.fernandez@simin.com', '{bcrypt}$2a$12$6URn/alsplso2DLBTrVaXOfULgsSdOisEs5v8PBWA5JPv1hmq3H3.', 2, 2),
                                                                                                                   ('Jorge', 'Martínez', 'Sánchez', 'jorge.martinez@simin.com', '{bcrypt}$2a$12$hlFvyWnnBR.8oMtY1S/Q9.FBwK9zhXM1Gqaz0QdcIkCssypmbfoRC', 3, 3);

INSERT INTO perfiles_riesgo (descripcion) VALUES ('Conservador'), ('Moderado'), ('Agresivo');
INSERT INTO tipos_instrumento (descripcion) VALUES ('BONO'), ('CETE'), ('REPORTO');
INSERT INTO instrumentos (nombre, id_tipo_instrumento, plazo_meses, plazo_anos, tasa_interes, valor_nominal, frecuencia_pagos, tipo_rendimiento, descripcion_adicional) VALUES
                                                                                                                                                                            ('Bono Gubernamental 5 años', 1, 0, 5, 7.50, 1000.00, 'Semestral', 'Fijo', 'Bono emitido por el gobierno con pagos semestrales.'),
                                                                                                                                                                            ('CETE 12 meses', 2, 12, 0, 6.00, 10.00, 'Anual', 'Fijo', 'Certificado de la tesorería a 12 meses.'),
                                                                                                                                                                            ('Reporto Corporativo XYZ', 3, 6, 0, 4.00, 5000.00, 'Trimestral', 'Variable', 'Reporto a 6 meses con pagos trimestrales.');

INSERT INTO carteras (nombre_cartera, valor_total, id_usuario, id_perfil_riesgo) VALUES
                                                                                     ('Cartera Moderada Carlos', 50000, 1, 2),
                                                                                     ('Cartera Conservadora María', 30000, 2, 1);

INSERT INTO instrumentos_cartera (id_cartera, id_instrumento, cantidad) VALUES
                                                                            (1, 1, 20),
                                                                            (1, 2, 100),
                                                                            (2, 2, 50),
                                                                            (2, 3, 5);

INSERT INTO historial_simulaciones (id_simulacion, descripcion) VALUES
                                                                    (1, 'Simulación inicial del Bono a 5 años'),
                                                                    (2, 'Simulación inicial del Reporto Corporativo XYZ');

INSERT INTO tipos_reporte (descripcion) VALUES ('Rendimiento Proyectado'), ('Valorización Total'), ('Exposición al Riesgo');
INSERT INTO reportes (id_cartera, id_tipo_reporte) VALUES
                                                       (1, 1),
                                                       (2, 2);

INSERT INTO contenido_educativo (titulo, descripcion) VALUES
                                                          ('¿Qué son los Bonos?', 'Información detallada sobre los bonos gubernamentales y cómo invertir.'),
                                                          ('Invirtiendo en CETES', 'Guía práctica sobre los CETES y sus ventajas.'),
                                                          ('Entendiendo los Reportos', 'Todo lo que necesitas saber sobre el funcionamiento de los reportos.');
