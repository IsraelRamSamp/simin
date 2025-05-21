SET NAMES 'UTF8MB4';
DROP DATABASE IF EXISTS simin;
CREATE DATABASE IF NOT EXISTS simin DEFAULT CHARACTER SET UTF8MB4;
USE simin;

-- MySQL dump 10.13  Distrib 8.0.40, for macos14 (arm64)
--
-- Host: localhost    Database: simin
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `carteras`
--

DROP TABLE IF EXISTS `carteras`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carteras` (
                            `id_cartera` int NOT NULL AUTO_INCREMENT,
                            `nombre_cartera` varchar(100) NOT NULL,
                            `valor_total` decimal(15,2) DEFAULT '0.00',
                            `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `id_usuario` int NOT NULL,
                            `id_perfil_riesgo` int DEFAULT NULL,
                            PRIMARY KEY (`id_cartera`),
                            KEY `idx_nombre_cartera` (`nombre_cartera`),
                            KEY `FK1syvwbt396wee9jukqxvwc0q0` (`id_perfil_riesgo`),
                            KEY `FKov1xdlu1gj4mefsm8j39mvqkd` (`id_usuario`),
                            CONSTRAINT `carteras_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`),
                            CONSTRAINT `carteras_ibfk_2` FOREIGN KEY (`id_perfil_riesgo`) REFERENCES `perfiles_riesgo` (`id_perfil_riesgo`),
                            CONSTRAINT `FK1syvwbt396wee9jukqxvwc0q0` FOREIGN KEY (`id_perfil_riesgo`) REFERENCES `perfiles_riesgo` (`id_perfil_riesgo`),
                            CONSTRAINT `FKov1xdlu1gj4mefsm8j39mvqkd` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carteras`
--

LOCK TABLES `carteras` WRITE;
/*!40000 ALTER TABLE `carteras` DISABLE KEYS */;
INSERT INTO `carteras` VALUES (1,'Cartera Moderada Carlos',14000.00,'2025-05-01 21:04:29',1,2),(2,'Cartera Conservadora María Fernandez',101000.00,'2025-05-01 21:04:29',2,1),(3,'Israel Samperio',25000.00,'2025-05-18 12:42:12',6,3),(4,'test',1000000.00,'2025-05-18 21:09:38',7,3),(8,'test',35300.00,'2025-05-18 21:30:15',2,1),(9,'testSimin',0.00,'2025-05-18 21:55:58',7,1),(10,'test',200000.00,'2025-05-18 23:55:32',8,1);
/*!40000 ALTER TABLE `carteras` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contenido_educativo`
--

DROP TABLE IF EXISTS `contenido_educativo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contenido_educativo` (
                                       `id_contenido` int NOT NULL AUTO_INCREMENT,
                                       `fecha_publicacion` datetime(6) NOT NULL,
                                       `titulo` varchar(100) NOT NULL,
                                       `descripcion` text NOT NULL,
                                       PRIMARY KEY (`id_contenido`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contenido_educativo`
--

LOCK TABLES `contenido_educativo` WRITE;
/*!40000 ALTER TABLE `contenido_educativo` DISABLE KEYS */;
/*!40000 ALTER TABLE `contenido_educativo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalle_simulacion`
--

DROP TABLE IF EXISTS `detalle_simulacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle_simulacion` (
                                      `cantidad_titulos` int DEFAULT NULL,
                                      `es_principal` bit(1) DEFAULT NULL,
                                      `id_detalle` int NOT NULL AUTO_INCREMENT,
                                      `id_simulacion` int NOT NULL,
                                      `interes_generado` decimal(15,2) DEFAULT NULL,
                                      `inversion_asignada` decimal(15,2) DEFAULT NULL,
                                      `isr_calculado` decimal(15,2) DEFAULT NULL,
                                      `remanente` decimal(15,2) DEFAULT NULL,
                                      `tasa_bruta` decimal(10,4) DEFAULT NULL,
                                      `nombre_instrumento` varchar(255) DEFAULT NULL,
                                      `tipo_instrumento` varchar(255) DEFAULT NULL,
                                      `monto_invertido` decimal(38,2) DEFAULT NULL,
                                      `plazo` varchar(50) DEFAULT NULL,
                                      `inversion_bonddia` decimal(18,4) DEFAULT NULL,
                                      `interes_bonddia` decimal(18,4) DEFAULT NULL,
                                      `titulos_bonddia` int DEFAULT NULL,
                                      `valor_final` decimal(18,4) DEFAULT NULL,
                                      PRIMARY KEY (`id_detalle`),
                                      KEY `idx_simulacion_tipo` (`id_simulacion`,`tipo_instrumento`),
                                      CONSTRAINT `FKn1pin8onlshicmeggcyw4jk1q` FOREIGN KEY (`id_simulacion`) REFERENCES `simulaciones` (`id_simulacion`)
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_simulacion`
--

LOCK TABLES `detalle_simulacion` WRITE;
/*!40000 ALTER TABLE `detalle_simulacion` DISABLE KEYS */;
INSERT INTO `detalle_simulacion` VALUES (10,NULL,5,17,282.30,1000.00,NULL,0.00,9.4100,'BONO M 3 AÑOS','BONO',NULL,NULL,NULL,NULL,NULL,NULL),(100,NULL,11,20,2823.00,10000.00,NULL,0.00,9.4100,'BONO M 3 AÑOS','BONO',NULL,NULL,NULL,NULL,NULL,NULL),(108,NULL,12,20,90.47,996.53,NULL,1.27,8.4000,'CETES 28','CETE',NULL,NULL,NULL,NULL,NULL,NULL),(108,NULL,13,20,88.86,997.90,NULL,2.10,8.2500,'CETES 182','CETE',NULL,NULL,NULL,NULL,NULL,NULL),(11,NULL,14,20,474.42,9309.63,NULL,0.31,5.1100,'UDIBONO 3 AÑOS','UDIBONOS',NULL,NULL,NULL,NULL,NULL,NULL),(10,NULL,15,21,462.00,998.00,NULL,2.00,9.2400,'BONDES 5 AÑOS','BONDES',NULL,NULL,NULL,NULL,NULL,NULL),(100,NULL,16,22,6.44,993.60,0.37,2.00,8.4000,'CETES 28','CETE',1000.00,NULL,NULL,NULL,NULL,NULL),(460,NULL,17,23,2.44,998.75,0.14,1.25,8.9300,'BONDDIA','BONDDIA',1000.00,NULL,NULL,NULL,NULL,NULL),(10,NULL,18,23,462.00,998.00,26.45,2.00,9.2400,'BONDES 5 AÑOS','BONDES',1000.00,NULL,NULL,NULL,NULL,NULL),(100,NULL,19,23,2823.00,10000.00,40.93,0.00,9.4100,'BONO M 3 AÑOS','BONO',10000.00,NULL,NULL,NULL,NULL,NULL),(100,NULL,20,23,6.44,993.60,0.37,2.00,8.4000,'CETES 28','CETE',1000.00,NULL,NULL,NULL,NULL,NULL),(1,NULL,21,23,129.74,846.33,7.43,1.77,5.1100,'UDIBONO 3 AÑOS','UDIBONOS',1000.00,NULL,NULL,NULL,NULL,NULL),(460,NULL,33,27,2.44,998.75,0.14,1.25,8.9300,'BONDDIA','BONDDIA',1000.00,'10 días',0.0000,0.0000,0,1002.3000),(10,NULL,34,27,462.00,998.00,26.45,2.00,9.2400,'BONDES 5 AÑOS','BONDES',1000.00,'5 años',0.0000,0.0000,0,1435.5500),(100,NULL,35,27,2823.00,10000.00,40.93,0.00,9.4100,'BONO M 3 AÑOS','BONO',10000.00,'3 años',0.0000,0.0000,0,12782.0700),(100,NULL,36,27,6.44,993.60,0.37,2.00,8.4000,'CETES 28','CETE',1000.00,'28 días',4.4000,0.0300,2,1006.1000),(1,NULL,37,27,129.74,846.33,7.43,1.77,5.1100,'UDIBONO 3 AÑOS','UDIBONOS',1000.00,'3 años',151.9000,1.0400,70,1123.3500),(10,NULL,38,28,462.00,998.00,26.45,2.00,9.2400,'BONDES 5 AÑOS','BONDES',1000.00,'5 años',0.0000,0.0000,0,1435.5500),(100,NULL,39,29,18820.00,10000.00,272.89,0.00,9.4100,'BONO M 20 AÑOS','BONO',10000.00,'20 años',0.0000,0.0000,0,28547.1100),(1,NULL,40,30,129.74,846.33,7.43,1.77,5.1100,'UDIBONO 3 AÑOS','UDIBONOS',1000.00,'3 años',151.9000,1.0400,70,1123.3500),(460,NULL,41,31,2.44,998.75,0.14,1.25,8.9300,'BONDDIA','BONDDIA',1000.00,'10 días',0.0000,0.0000,460,1002.3000),(100,NULL,42,32,6.44,993.60,0.37,2.00,8.4000,'CETES 28','CETE',1000.00,'28 días',4.4000,0.0300,2,1006.1000),(460,NULL,43,33,2.44,998.75,0.14,1.25,8.9300,'BONDDIA','BONDDIA',1000.00,'10 días',0.0000,0.0000,460,1002.3000),(10,NULL,44,33,462.00,998.00,26.45,2.00,9.2400,'BONDES 5 AÑOS','BONDES',1000.00,'5 años',0.0000,0.0000,0,1435.5500),(100,NULL,45,33,2823.00,10000.00,40.93,0.00,9.4100,'BONO M 3 AÑOS','BONO',10000.00,'3 años',0.0000,0.0000,0,12782.0700),(100,NULL,46,33,6.44,993.60,0.37,2.00,8.4000,'CETES 28','CETE',1000.00,'28 días',4.4000,0.0300,2,1006.1000),(1,NULL,47,33,129.74,846.33,7.43,1.77,5.1100,'UDIBONO 3 AÑOS','UDIBONOS',1000.00,'3 años',151.9000,1.0400,70,1123.3500),(10,NULL,49,35,462.00,998.00,26.45,2.00,9.2400,'BONDES 5 AÑOS','BONDES',1000.00,'5 años',0.0000,0.0000,0,1435.5500),(4605,NULL,50,36,892.86,9998.38,51.11,1.62,8.9300,'BONDDIA','BONDDIA',10000.00,'1 año',0.0000,0.0000,4605,10841.7500),(150,NULL,51,36,42345.00,15000.00,614.00,0.00,9.4100,'BONO M 30 AÑOS','BONO',15000.00,'30 años',0.0000,0.0000,0,56731.0000),(10,NULL,52,37,941.00,1000.00,13.64,0.00,9.4100,'BONO M 10 AÑOS','BONO',1000.00,'10 años',0.0000,0.0000,0,1927.3600),(10,NULL,53,38,282.30,1000.00,4.09,0.00,9.4100,'BONO M 3 AÑOS','BONO',1000.00,'3 años',0.0000,0.0000,0,1278.2100),(104,NULL,54,39,42.78,998.91,2.45,1.09,8.2500,'CETES 182','CETE',1000.00,'6 meses',0.0000,0.0000,0,1040.3300),(100,NULL,55,40,4705.00,10000.00,68.22,0.00,9.4100,'BONO M 5 AÑOS','BONO',10000.00,'5 años',0.0000,0.0000,0,14636.7800),(10,NULL,56,41,1882.00,1000.00,27.29,0.00,9.4100,'BONO M 20 AÑOS','BONO',1000.00,'20 años',0.0000,0.0000,0,2854.7100),(1000,NULL,57,42,28230.00,100000.00,409.34,0.00,9.4100,'BONO M 3 AÑOS','BONO',100000.00,'3 años',0.0000,0.0000,0,127820.6600),(10837,NULL,58,43,9070.57,99999.50,519.21,0.50,8.3700,'CETES 365','CETE',100000.00,'1 año',0.0000,0.0000,0,108551.3600),(104,NULL,59,44,42.78,998.91,2.45,1.09,8.2500,'CETES 182','CETE',1000.00,'6 meses',0.0000,0.0000,0,1040.3300),(100,NULL,60,45,2823.00,10000.00,40.93,0.00,9.4100,'BONO M 3 AÑOS','BONO',10000.00,'3 años',0.0000,0.0000,0,12782.0700),(10,NULL,61,46,282.30,1000.00,4.09,0.00,9.4100,'BONO M 3 AÑOS','BONO',1000.00,'3 años',0.0000,0.0000,0,1278.2100),(10000,NULL,62,47,2823000.00,1000000.00,40933.50,0.00,9.4100,'BONO M 30 AÑOS','BONO',1000000.00,'30 años',0.0000,0.0000,0,3782066.5000),(10,NULL,63,48,282.30,1000.00,4.09,0.00,9.4100,'BONO M 3 AÑOS','BONO',1000.00,'3 años',0.0000,0.0000,0,1278.2100),(104,NULL,64,49,42.78,998.91,2.45,1.09,8.2500,'CETES 182','CETE',1000.00,'6 meses',0.0000,0.0000,0,1040.3300),(100,NULL,65,50,6.44,993.60,0.37,2.00,8.4000,'CETES 28','CETE',1000.00,'28 días',4.4000,0.0300,2,1006.1000),(10,NULL,66,51,470.50,1000.00,6.82,0.00,9.4100,'BONO M 5 AÑOS','BONO',1000.00,'5 años',0.0000,0.0000,0,1463.6800),(10,NULL,67,52,470.50,1000.00,6.82,0.00,9.4100,'BONO M 5 AÑOS','BONO',1000.00,'5 años',0.0000,0.0000,0,1463.6800),(460,NULL,69,54,2.44,998.75,0.14,1.25,8.9300,'BONDDIA','BONDDIA',1000.00,'10 días',0.0000,0.0000,460,1002.3000),(10,NULL,70,54,462.00,998.00,26.45,2.00,9.2400,'BONDES 5 AÑOS','BONDES',1000.00,'5 años',0.0000,0.0000,0,1435.5500),(100,NULL,71,54,2823.00,10000.00,40.93,0.00,9.4100,'BONO M 3 AÑOS','BONO',10000.00,'3 años',0.0000,0.0000,0,12782.0700),(100,NULL,72,54,6.44,993.60,0.37,2.00,8.4000,'CETES 28','CETE',1000.00,'28 días',4.4000,0.0300,2,1006.1000),(1,NULL,73,54,129.74,846.33,7.43,1.77,5.1100,'UDIBONO 3 AÑOS','UDIBONOS',1000.00,'3 años',151.9000,1.0400,70,1123.3500),(1000,NULL,74,55,28230.00,100000.00,409.34,0.00,9.4100,'BONO M 3 AÑOS','BONO',100000.00,'3 años',0.0000,0.0000,0,127820.6600),(1000,NULL,75,56,28230.00,100000.00,409.34,0.00,9.4100,'BONO M 3 AÑOS','BONO',100000.00,'3 años',0.0000,0.0000,0,127820.6600),(100,NULL,76,57,9410.00,10000.00,136.45,0.00,9.4100,'BONO M 10 AÑOS','BONO',10000.00,'10 años',0.0000,0.0000,0,19273.5500),(18,NULL,77,57,7784.54,15233.94,445.59,0.96,5.1100,'UDIBONO 10 AÑOS','UDIBONOS',15300.00,'10 años',65.1000,0.4500,30,22639.4000),(100644,NULL,78,58,6485.33,999998.78,371.23,1.22,8.4000,'CETES 28','CETE',1000000.00,'28 días',0.0000,0.0000,0,1006114.1000),(100,NULL,79,59,6.44,993.60,0.37,2.00,8.4000,'CETES 28','CETE',1000.00,'28 días',4.4000,0.0300,2,1006.1000),(10064,NULL,81,61,648.51,99995.90,37.12,1.90,8.4000,'CETES 28','CETE',100000.00,'28 días',2.2000,0.0200,1,100611.4100),(100,NULL,82,62,2823.00,10000.00,40.93,0.00,9.4100,'BONO M 3 AÑOS','BONO',10000.00,'3 años',0.0000,0.0000,0,12782.0700),(100,NULL,83,63,6.44,993.60,0.37,2.00,8.4000,'CETES 28','CETE',1000.00,'28 días',4.4000,0.0300,2,1006.1000),(1000,NULL,84,64,28230.00,100000.00,409.34,0.00,9.4100,'BONO M 3 AÑOS','BONO',100000.00,'3 años',0.0000,0.0000,0,127820.6600),(10064,NULL,85,64,648.51,99995.90,37.12,1.90,8.4000,'CETES 28','CETE',100000.00,'28 días',2.2000,0.0200,1,100611.4100);
/*!40000 ALTER TABLE `detalle_simulacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalles_simulacion`
--

DROP TABLE IF EXISTS `detalles_simulacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalles_simulacion` (
                                       `id_detalle` int NOT NULL AUTO_INCREMENT,
                                       `instrumento` varchar(255) NOT NULL,
                                       `interes` decimal(15,2) NOT NULL,
                                       `inversion` decimal(15,2) NOT NULL,
                                       `remanente` decimal(15,2) DEFAULT NULL,
                                       `tasa_bruta` decimal(6,2) NOT NULL,
                                       `titulos` int NOT NULL,
                                       `id_simulacion` int NOT NULL,
                                       PRIMARY KEY (`id_detalle`),
                                       KEY `FKtme64xni3guea0dn8uy2j4gst` (`id_simulacion`),
                                       CONSTRAINT `FKtme64xni3guea0dn8uy2j4gst` FOREIGN KEY (`id_simulacion`) REFERENCES `simulaciones` (`id_simulacion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalles_simulacion`
--

LOCK TABLES `detalles_simulacion` WRITE;
/*!40000 ALTER TABLE `detalles_simulacion` DISABLE KEYS */;
/*!40000 ALTER TABLE `detalles_simulacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historial_rendimientos_cartera`
--

DROP TABLE IF EXISTS `historial_rendimientos_cartera`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `historial_rendimientos_cartera` (
                                                  `id_cartera` int NOT NULL,
                                                  `id_historial` int NOT NULL AUTO_INCREMENT,
                                                  `rendimiento_obtenido` decimal(10,2) DEFAULT NULL,
                                                  `valor_total` decimal(15,2) NOT NULL,
                                                  `fecha_registro` datetime(6) NOT NULL,
                                                  PRIMARY KEY (`id_historial`),
                                                  KEY `FKoesckfwk00511dxds9yqe5u2s` (`id_cartera`),
                                                  CONSTRAINT `FKoesckfwk00511dxds9yqe5u2s` FOREIGN KEY (`id_cartera`) REFERENCES `carteras` (`id_cartera`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historial_rendimientos_cartera`
--

LOCK TABLES `historial_rendimientos_cartera` WRITE;
/*!40000 ALTER TABLE `historial_rendimientos_cartera` DISABLE KEYS */;
/*!40000 ALTER TABLE `historial_rendimientos_cartera` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historial_simulaciones`
--

DROP TABLE IF EXISTS `historial_simulaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `historial_simulaciones` (
                                          `id_historial` int NOT NULL AUTO_INCREMENT,
                                          `id_simulacion` int NOT NULL,
                                          `fecha_registro` datetime(6) NOT NULL,
                                          `descripcion` varchar(250) DEFAULT NULL,
                                          PRIMARY KEY (`id_historial`),
                                          KEY `FKijcngksmgqv7jpartmombapja` (`id_simulacion`),
                                          CONSTRAINT `FKijcngksmgqv7jpartmombapja` FOREIGN KEY (`id_simulacion`) REFERENCES `simulaciones` (`id_simulacion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historial_simulaciones`
--

LOCK TABLES `historial_simulaciones` WRITE;
/*!40000 ALTER TABLE `historial_simulaciones` DISABLE KEYS */;
/*!40000 ALTER TABLE `historial_simulaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instrumentos`
--

DROP TABLE IF EXISTS `instrumentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `instrumentos` (
                                `id_instrumento` int NOT NULL AUTO_INCREMENT,
                                `nombre` varchar(100) NOT NULL,
                                `id_tipo_instrumento` int NOT NULL,
                                `plazo_meses` int DEFAULT '0',
                                `plazo_anos` int DEFAULT '0',
                                `tasa_interes` decimal(5,2) NOT NULL,
                                `valor_nominal` decimal(15,4) NOT NULL,
                                `frecuencia_pagos` enum('Diario','Mensual','Trimestral','Semestral','Anual') NOT NULL DEFAULT 'Anual',
                                `tipo_rendimiento` enum('Fijo','Variable') NOT NULL DEFAULT 'Fijo',
                                `descripcion_adicional` varchar(250) DEFAULT NULL,
                                `precio_mercado` decimal(15,4) DEFAULT NULL,
                                `dias_plazo` int DEFAULT NULL,
                                `fecha_emision` datetime(6) DEFAULT NULL,
                                `fecha_vencimiento` datetime(6) DEFAULT NULL,
                                `tasa_cupon` decimal(10,4) DEFAULT NULL,
                                PRIMARY KEY (`id_instrumento`),
                                UNIQUE KEY `nombre` (`nombre`),
                                UNIQUE KEY `UKgrgemme3muirjcwmgbok4a776` (`nombre`),
                                KEY `FKedd2n4frr1vcbddsk0rom0xkc` (`id_tipo_instrumento`),
                                CONSTRAINT `FKedd2n4frr1vcbddsk0rom0xkc` FOREIGN KEY (`id_tipo_instrumento`) REFERENCES `tipos_instrumento` (`id_tipo_instrumento`),
                                CONSTRAINT `instrumentos_ibfk_1` FOREIGN KEY (`id_tipo_instrumento`) REFERENCES `tipos_instrumento` (`id_tipo_instrumento`),
                                CONSTRAINT `instrumentos_chk_1` CHECK ((`plazo_meses` between 0 and 12)),
                                CONSTRAINT `instrumentos_chk_2` CHECK ((`plazo_anos` >= 0)),
                                CONSTRAINT `instrumentos_chk_3` CHECK ((`tasa_interes` > 0)),
                                CONSTRAINT `instrumentos_chk_4` CHECK ((`valor_nominal` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instrumentos`
--

LOCK TABLES `instrumentos` WRITE;
/*!40000 ALTER TABLE `instrumentos` DISABLE KEYS */;
INSERT INTO `instrumentos` VALUES (5,'CETES 28',2,1,0,8.40,10.0000,'Anual','Fijo','Plazo fijo de 28 días',9.9351,28,NULL,NULL,NULL),(6,'CETES 91',2,3,0,8.35,10.0000,'Anual','Fijo','Plazo fijo de 91 días',9.7979,91,NULL,NULL,NULL),(7,'CETES 182',2,6,0,8.25,10.0000,'Anual','Fijo','Plazo fijo de 182 días',9.5965,182,NULL,NULL,NULL),(8,'CETES 365',2,12,0,8.37,10.0000,'Anual','Fijo','Plazo fijo de 1 año',9.2374,365,NULL,NULL,NULL),(9,'BONDDIA',6,0,0,8.93,10.0000,'Anual','Variable','Rendimiento diario compuesto',2.1700,NULL,NULL,NULL,NULL),(10,'BONO M 3 AÑOS',1,0,3,9.41,100.0000,'Semestral','Fijo','Pago de intereses semestrales, plazo de 3 años',100.0000,1095,'2025-05-17 10:54:00.000000','2028-05-17 10:54:00.000000',9.4100),(11,'BONO M 5 AÑOS',1,0,5,8.42,100.0000,'Semestral','Fijo','Pago de intereses semestrales, plazo de 5 años',100.0000,1825,'2025-05-17 11:13:00.000000','2030-05-17 11:14:00.000000',9.4100),(12,'BONO M 10 AÑOS',1,0,10,8.42,100.0000,'Semestral','Fijo','Pago de intereses anuales, plazo de 10 años',100.0000,3650,'2025-05-17 11:17:00.000000','2035-05-17 11:17:00.000000',9.4100),(13,'BONO M 20 AÑOS',1,0,20,8.42,100.0000,'Semestral','Fijo','Pago de intereses anuales, plazo de 20 años',100.0000,7300,'2025-05-17 11:18:00.000000','2045-05-17 11:18:00.000000',9.4100),(14,'BONO M 30 AÑOS',1,0,30,8.42,100.0000,'Semestral','Fijo','Pago de intereses anuales, plazo de 30 años',100.0000,10950,'2025-05-17 11:19:00.000000','2055-05-17 11:19:00.000000',9.4100),(15,'BONDES 5 AÑOS',4,0,5,9.24,100.0000,'Trimestral','Variable','Bonos de desarrollo con tasa revisable.',99.8000,1825,'2024-05-17 19:28:00.000000','2029-05-17 19:28:00.000000',9.2400),(16,'UDIBONO 3 AÑOS',5,0,3,5.09,846.3300,'Anual','Fijo','Rinde con base en inflación, plazo de 3 años',846.3300,1095,'2025-05-17 12:20:00.000000','2028-05-17 12:20:00.000000',5.1100),(17,'UDIBONO 10 AÑOS',5,0,10,5.09,846.3300,'Anual','Fijo','Protegido contra inflación, plazo de 10 años',846.3300,3650,'2025-05-17 18:30:00.000000','2035-05-17 18:30:00.000000',5.1100),(18,'UDIBONO 30 AÑOS',5,0,30,5.09,846.3300,'Anual','Fijo','Instrumento de largo plazo con tasa real fija',846.3300,10950,'2025-05-17 18:44:00.000000','2055-05-17 18:44:00.000000',5.1100),(19,'REPORTO SIMPLE',3,0,0,9.00,10.0000,'Mensual','Fijo','Reporto a plazo libre',10.0000,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `instrumentos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instrumentos_cartera`
--

DROP TABLE IF EXISTS `instrumentos_cartera`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `instrumentos_cartera` (
                                        `cantidad` int NOT NULL,
                                        `id_cartera` int NOT NULL,
                                        `id_instrumento` int NOT NULL,
                                        `id_instrumentos_cartera` int NOT NULL AUTO_INCREMENT,
                                        `monto_invertido` decimal(12,2) NOT NULL DEFAULT '0.00',
                                        `plazo_dias_bonddia` int DEFAULT NULL,
                                        PRIMARY KEY (`id_instrumentos_cartera`),
                                        KEY `FKav9f2oj9nknwt68m42w8svjjy` (`id_cartera`),
                                        KEY `FK7ytnneo10oi49un1ahwthln72` (`id_instrumento`),
                                        CONSTRAINT `FK7ytnneo10oi49un1ahwthln72` FOREIGN KEY (`id_instrumento`) REFERENCES `instrumentos` (`id_instrumento`),
                                        CONSTRAINT `FKav9f2oj9nknwt68m42w8svjjy` FOREIGN KEY (`id_cartera`) REFERENCES `carteras` (`id_cartera`),
                                        CONSTRAINT `instrumentos_cartera_chk_1` CHECK ((`cantidad` >= 1))
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instrumentos_cartera`
--

LOCK TABLES `instrumentos_cartera` WRITE;
/*!40000 ALTER TABLE `instrumentos_cartera` DISABLE KEYS */;
INSERT INTO `instrumentos_cartera` VALUES (100,1,5,7,1000.00,NULL),(1000,2,10,9,100000.00,NULL),(100,1,10,10,10000.00,NULL),(1,1,16,11,1000.00,NULL),(460,1,9,12,1000.00,10),(10,1,15,13,1000.00,NULL),(150,3,14,14,15000.00,NULL),(4608,3,9,15,10000.00,365),(100,2,5,17,1000.00,NULL),(100,8,12,18,10000.00,NULL),(18,8,17,19,15300.00,NULL),(4608,8,9,20,10000.00,28),(100653,4,5,21,1000000.00,NULL),(10065,10,5,22,100000.00,NULL),(1000,10,10,23,100000.00,NULL);
/*!40000 ALTER TABLE `instrumentos_cartera` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `perfiles_riesgo`
--

DROP TABLE IF EXISTS `perfiles_riesgo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `perfiles_riesgo` (
                                   `id_perfil_riesgo` int NOT NULL AUTO_INCREMENT,
                                   `descripcion` varchar(50) NOT NULL,
                                   PRIMARY KEY (`id_perfil_riesgo`),
                                   UNIQUE KEY `descripcion` (`descripcion`),
                                   UNIQUE KEY `UK33qg5orwjhrqu53s6pxn5yldh` (`descripcion`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `perfiles_riesgo`
--

LOCK TABLES `perfiles_riesgo` WRITE;
/*!40000 ALTER TABLE `perfiles_riesgo` DISABLE KEYS */;
INSERT INTO `perfiles_riesgo` VALUES (3,'Agresivo'),(1,'Conservador'),(2,'Moderado');
/*!40000 ALTER TABLE `perfiles_riesgo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reportes`
--

DROP TABLE IF EXISTS `reportes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reportes` (
                            `id_cartera` int DEFAULT NULL,
                            `id_reporte` int NOT NULL AUTO_INCREMENT,
                            `id_tipo_reporte` int NOT NULL,
                            `fecha_reporte` datetime(6) NOT NULL,
                            `correo_usuario` varchar(255) NOT NULL,
                            `id_simulacion` int NOT NULL,
                            `nombre_usuario` varchar(255) NOT NULL,
                            `tipo_origen` varchar(255) NOT NULL,
                            `compartido_analista` bit(1) DEFAULT NULL,
                            PRIMARY KEY (`id_reporte`),
                            KEY `FKgagc93h2ia7145ye5p0q7qc9a` (`id_cartera`),
                            KEY `FKqso8nfps64yqi6s2eh82jcy37` (`id_tipo_reporte`),
                            CONSTRAINT `FKgagc93h2ia7145ye5p0q7qc9a` FOREIGN KEY (`id_cartera`) REFERENCES `carteras` (`id_cartera`),
                            CONSTRAINT `FKqso8nfps64yqi6s2eh82jcy37` FOREIGN KEY (`id_tipo_reporte`) REFERENCES `tipos_reporte` (`id_tipo_reporte`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reportes`
--

LOCK TABLES `reportes` WRITE;
/*!40000 ALTER TABLE `reportes` DISABLE KEYS */;
INSERT INTO `reportes` VALUES (NULL,2,1,'2025-05-18 12:12:00.922650','carlos.lopez@simin.com',29,'Carlos López','INSTRUMENTO',NULL),(NULL,3,1,'2025-05-18 18:46:51.116468','carlos.lopez@simin.com',29,'Carlos López','INSTRUMENTO',NULL),(NULL,4,1,'2025-05-18 18:54:51.765956','carlos.lopez@simin.com',29,'Carlos López','INSTRUMENTO',NULL),(NULL,6,1,'2025-05-18 19:13:44.702144','carlos.lopez@simin.com',28,'Carlos López','INSTRUMENTO',NULL),(2,7,1,'2025-05-18 20:03:39.146126','maria.fernandez@simin.com',56,'María Fernández','CARTERA',NULL),(8,8,1,'2025-05-18 21:31:39.013984','maria.fernandez@simin.com',57,'María Fernández','CARTERA',NULL),(4,9,1,'2025-05-18 21:39:54.370881','samperi@simin.com',58,'israel samperio','CARTERA',NULL),(10,11,1,'2025-05-18 23:57:57.578683','zay@simin.com',61,'Zay Dominguez','CARTERA',NULL),(NULL,12,1,'2025-05-18 23:58:46.134675','zay@simin.com',62,'Zay Dominguez','INSTRUMENTO',NULL),(NULL,13,1,'2025-05-21 13:18:02.443927','carlos.lopez@simin.com',17,'Carlos López','INSTRUMENTO',NULL),(NULL,14,1,'2025-05-21 14:18:12.772090','carlos.lopez@simin.com',63,'Carlos López','INSTRUMENTO',NULL),(10,15,1,'2025-05-21 14:19:46.402317','zay@simin.com',64,'Zay Dominguez','CARTERA',NULL);
/*!40000 ALTER TABLE `reportes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
                         `id_rol` int NOT NULL AUTO_INCREMENT,
                         `nombre` varchar(255) NOT NULL,
                         PRIMARY KEY (`id_rol`),
                         UNIQUE KEY `nombre` (`nombre`),
                         UNIQUE KEY `UKldv0v52e0udsh2h1rs0r0gw1n` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ADMIN'),(3,'ANALISTA'),(2,'INVERSIONISTA');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `simulaciones`
--

DROP TABLE IF EXISTS `simulaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `simulaciones` (
                                `id_simulacion` int NOT NULL AUTO_INCREMENT,
                                `id_usuario` int NOT NULL,
                                `id_instrumento` int DEFAULT NULL,
                                `monto_invertido` decimal(15,2) NOT NULL,
                                `fecha_simulacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `fecha_finalizacion` datetime NOT NULL,
                                `id_cartera` int DEFAULT NULL,
                                `interes_bruto` decimal(15,2) DEFAULT NULL,
                                `isr` decimal(15,2) DEFAULT NULL,
                                `valor_final` decimal(15,2) DEFAULT NULL,
                                `plazo` varchar(50) DEFAULT NULL,
                                `compartida_analista` bit(1) DEFAULT NULL,
                                PRIMARY KEY (`id_simulacion`),
                                KEY `idx_fecha_simulacion` (`fecha_simulacion`),
                                KEY `FK1xijjbexgp7t8al07fwtl0bnp` (`id_cartera`),
                                KEY `FKa1haadk4d8lbxs50knet74ce2` (`id_instrumento`),
                                KEY `FKndoxllahqpg8i8pphq9q7lcj9` (`id_usuario`),
                                CONSTRAINT `FK1xijjbexgp7t8al07fwtl0bnp` FOREIGN KEY (`id_cartera`) REFERENCES `carteras` (`id_cartera`),
                                CONSTRAINT `FKa1haadk4d8lbxs50knet74ce2` FOREIGN KEY (`id_instrumento`) REFERENCES `instrumentos` (`id_instrumento`),
                                CONSTRAINT `FKndoxllahqpg8i8pphq9q7lcj9` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`),
                                CONSTRAINT `simulaciones_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`),
                                CONSTRAINT `simulaciones_ibfk_2` FOREIGN KEY (`id_instrumento`) REFERENCES `instrumentos` (`id_instrumento`),
                                CONSTRAINT `simulaciones_chk_1` CHECK ((`monto_invertido` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `simulaciones`
--

LOCK TABLES `simulaciones` WRITE;
/*!40000 ALTER TABLE `simulaciones` DISABLE KEYS */;
INSERT INTO `simulaciones` VALUES (17,1,10,1000.00,'2025-05-17 21:22:03','2028-05-16 21:21:00',NULL,282.30,4.09,1278.21,'3 año(s)',NULL),(20,1,NULL,22000.00,'2025-05-17 21:45:09','2026-05-16 21:44:00',1,3481.68,78.37,25403.31,'',NULL),(21,1,15,1000.00,'2025-05-18 00:07:12','2030-05-17 00:07:00',NULL,462.00,26.45,1435.55,'5 año(s)',NULL),(22,1,5,1000.00,'2025-05-18 00:58:33','2025-06-15 00:58:00',NULL,6.47,0.37,1006.10,'28 día(s)',NULL),(23,1,NULL,14000.00,'2025-05-18 01:00:17','2030-05-17 01:00:17',1,3424.69,75.32,17349.37,'Hasta 1825 días (plazo más largo)',NULL),(27,1,NULL,14000.00,'2025-05-18 01:54:05','2030-05-17 01:54:05',1,3424.69,75.32,17349.37,'Hasta 1825 días (plazo más largo)',NULL),(28,1,15,1000.00,'2025-05-18 02:09:22','2030-05-17 02:09:00',NULL,462.00,26.45,1435.55,'5 año(s)',NULL),(29,1,13,10000.00,'2025-05-18 02:20:18','2045-05-13 02:19:00',NULL,18820.00,272.89,28547.11,'20 año(s)',NULL),(30,1,16,1000.00,'2025-05-18 02:20:54','2028-05-17 02:20:00',NULL,130.78,7.43,1123.35,'3 año(s)',NULL),(31,1,9,1000.00,'2025-05-18 02:24:36','2025-05-28 02:24:36',NULL,2.44,0.14,1002.30,'10 día(s)',NULL),(32,1,5,1000.00,'2025-05-18 02:28:01','2025-06-15 02:27:00',NULL,6.47,0.37,1006.10,'28 día(s)',NULL),(33,1,NULL,14000.00,'2025-05-18 11:49:10','2030-05-17 11:49:10',1,3424.69,75.32,17349.37,'Hasta 1825 días (plazo más largo)',NULL),(35,1,15,1000.00,'2025-05-18 12:40:32','2030-05-17 12:40:00',NULL,462.00,26.45,1435.55,'5 año(s)',NULL),(36,6,NULL,25000.00,'2025-05-18 12:54:26','2055-05-11 12:54:26',3,43237.86,665.11,67572.75,'Hasta 10950 días (plazo más largo)',NULL),(37,1,12,1000.00,'2025-05-18 15:37:39','2035-05-16 15:37:00',NULL,941.00,13.64,1927.36,'10 año(s)',_binary '\0'),(38,1,10,1000.00,'2025-05-18 15:51:58','2028-05-17 15:44:00',NULL,282.30,4.09,1278.21,'3 año(s)',_binary '\0'),(39,1,7,1000.00,'2025-05-18 15:52:55','2025-11-16 15:52:00',NULL,42.78,2.45,1040.33,'6 mes(es)',_binary '\0'),(40,1,11,10000.00,'2025-05-18 15:56:12','2030-05-17 15:56:00',NULL,4705.00,68.22,14636.78,'5 año(s)',_binary ''),(41,1,13,1000.00,'2025-05-18 15:56:45','2045-05-13 15:56:00',NULL,1882.00,27.29,2854.71,'20 año(s)',NULL),(42,1,10,100000.00,'2025-05-18 15:58:28','2028-05-17 15:58:00',NULL,28230.00,409.34,127820.66,'3 año(s)',_binary '\0'),(43,1,8,100000.00,'2025-05-18 15:59:00','2026-05-18 15:58:00',NULL,9070.57,519.21,108551.36,'1 año(s)',_binary '\0'),(44,1,7,1000.00,'2025-05-18 16:01:59','2025-11-16 16:01:00',NULL,42.78,2.45,1040.33,'6 mes(es)',_binary '\0'),(45,1,10,10000.00,'2025-05-18 16:03:52','2028-05-17 16:03:00',NULL,2823.00,40.93,12782.07,'3 año(s)',_binary ''),(46,1,10,1000.00,'2025-05-18 16:04:11','2028-05-17 16:04:00',NULL,282.30,4.09,1278.21,'3 año(s)',NULL),(47,1,14,1000000.00,'2025-05-18 16:05:34','2055-05-11 16:05:00',NULL,2823000.00,40933.50,3782066.50,'30 año(s)',_binary '\0'),(48,1,10,1000.00,'2025-05-18 16:08:13','2028-05-17 16:07:00',NULL,282.30,4.09,1278.21,'3 año(s)',_binary '\0'),(49,1,7,1000.00,'2025-05-18 16:10:08','2025-11-16 16:10:00',NULL,42.78,2.45,1040.33,'6 mes(es)',NULL),(50,1,5,1000.00,'2025-05-18 16:10:47','2025-06-15 16:10:00',NULL,6.47,0.37,1006.10,'28 día(s)',_binary '\0'),(51,1,11,1000.00,'2025-05-18 16:13:38','2030-05-17 16:13:00',NULL,470.50,6.82,1463.68,'5 año(s)',NULL),(52,1,11,1000.00,'2025-05-18 16:15:31','2030-05-17 16:15:00',NULL,470.50,6.82,1463.68,'5 año(s)',_binary '\0'),(54,1,NULL,14000.00,'2025-05-18 16:22:29','2030-05-17 16:22:29',1,3424.69,75.32,17349.37,'Hasta 1825 días (plazo más largo)',_binary '\0'),(55,2,NULL,100000.00,'2025-05-18 16:23:00','2028-05-17 16:23:00',2,28230.00,409.34,127820.66,'Hasta 1095 días (plazo más largo)',_binary '\0'),(56,2,NULL,100000.00,'2025-05-18 16:23:13','2028-05-17 16:23:13',2,28230.00,409.34,127820.66,'Hasta 1095 días (plazo más largo)',_binary ''),(57,2,NULL,25300.00,'2025-05-18 21:31:08','2035-05-16 21:31:08',8,17194.99,582.04,41912.95,'Hasta 3650 días (plazo más largo)',_binary ''),(58,7,NULL,1000000.00,'2025-05-18 21:33:36','2025-06-15 21:33:36',4,6485.33,371.23,1006114.10,'Hasta 28 días (plazo más largo)',_binary '\0'),(59,2,5,1000.00,'2025-05-18 22:20:13','2025-06-15 22:19:00',NULL,6.47,0.37,1006.10,'28 día(s)',_binary ''),(61,8,NULL,100000.00,'2025-05-18 23:57:33','2025-06-15 23:57:33',10,648.53,37.12,100611.41,'Hasta 28 días (plazo más largo)',_binary ''),(62,8,10,10000.00,'2025-05-18 23:58:32','2028-05-17 23:58:00',NULL,2823.00,40.93,12782.07,'3 año(s)',_binary '\0'),(63,1,5,1000.00,'2025-05-21 14:17:56','2025-06-18 14:17:00',NULL,6.47,0.37,1006.10,'28 día(s)',_binary ''),(64,8,NULL,200000.00,'2025-05-21 14:19:22','2028-05-20 14:19:22',10,28878.53,446.46,228432.07,'Hasta 1095 días (plazo más largo)',_binary '\0');
/*!40000 ALTER TABLE `simulaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipos_instrumento`
--

DROP TABLE IF EXISTS `tipos_instrumento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipos_instrumento` (
                                     `id_tipo_instrumento` int NOT NULL AUTO_INCREMENT,
                                     `descripcion` varchar(50) NOT NULL,
                                     PRIMARY KEY (`id_tipo_instrumento`),
                                     UNIQUE KEY `descripcion` (`descripcion`),
                                     UNIQUE KEY `UKbcchnkwysgishjrbysxnc2tpb` (`descripcion`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipos_instrumento`
--

LOCK TABLES `tipos_instrumento` WRITE;
/*!40000 ALTER TABLE `tipos_instrumento` DISABLE KEYS */;
INSERT INTO `tipos_instrumento` VALUES (6,'BONDDIA'),(4,'BONDES'),(1,'BONO'),(2,'CETE'),(3,'REPORTO'),(5,'UDIBONOS');
/*!40000 ALTER TABLE `tipos_instrumento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipos_reporte`
--

DROP TABLE IF EXISTS `tipos_reporte`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipos_reporte` (
                                 `id_tipo_reporte` int NOT NULL AUTO_INCREMENT,
                                 `descripcion` varchar(50) NOT NULL,
                                 PRIMARY KEY (`id_tipo_reporte`),
                                 UNIQUE KEY `UK3uk9oi7pe4q62ael4igl7eq4q` (`descripcion`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipos_reporte`
--

LOCK TABLES `tipos_reporte` WRITE;
/*!40000 ALTER TABLE `tipos_reporte` DISABLE KEYS */;
INSERT INTO `tipos_reporte` VALUES (1,'Simulación PDF');
/*!40000 ALTER TABLE `tipos_reporte` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipos_usuario`
--

DROP TABLE IF EXISTS `tipos_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipos_usuario` (
                                 `id_tipo_usuario` int NOT NULL AUTO_INCREMENT,
                                 `descripcion` varchar(50) NOT NULL,
                                 PRIMARY KEY (`id_tipo_usuario`),
                                 UNIQUE KEY `descripcion` (`descripcion`),
                                 UNIQUE KEY `UKb7t23838fkyqmjstwuev67l1v` (`descripcion`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipos_usuario`
--

LOCK TABLES `tipos_usuario` WRITE;
/*!40000 ALTER TABLE `tipos_usuario` DISABLE KEYS */;
INSERT INTO `tipos_usuario` VALUES (1,'Administrador'),(3,'Analista'),(2,'Inversionista');
/*!40000 ALTER TABLE `tipos_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
                            `id_usuario` int NOT NULL AUTO_INCREMENT,
                            `nombre` varchar(50) NOT NULL,
                            `apellido_paterno` varchar(50) NOT NULL,
                            `apellido_materno` varchar(50) DEFAULT NULL,
                            `correo` varchar(100) NOT NULL,
                            `contrasena` varchar(255) NOT NULL,
                            `fecha_registro` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `id_tipo_usuario` int NOT NULL,
                            `id_rol` int NOT NULL,
                            PRIMARY KEY (`id_usuario`),
                            UNIQUE KEY `correo` (`correo`),
                            UNIQUE KEY `UKcdmw5hxlfj78uf4997i3qyyw5` (`correo`),
                            KEY `idx_nombre_usuario` (`nombre`),
                            KEY `FK3kl77pehgupicftwfreqnjkll` (`id_rol`),
                            KEY `FKd651q4e8cc8rlxpdfb3xduq2w` (`id_tipo_usuario`),
                            CONSTRAINT `FK3kl77pehgupicftwfreqnjkll` FOREIGN KEY (`id_rol`) REFERENCES `roles` (`id_rol`),
                            CONSTRAINT `FKd651q4e8cc8rlxpdfb3xduq2w` FOREIGN KEY (`id_tipo_usuario`) REFERENCES `tipos_usuario` (`id_tipo_usuario`),
                            CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`id_tipo_usuario`) REFERENCES `tipos_usuario` (`id_tipo_usuario`),
                            CONSTRAINT `usuarios_ibfk_2` FOREIGN KEY (`id_rol`) REFERENCES `roles` (`id_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Carlos','López','Ramírez','carlos.lopez@simin.com','{bcrypt}$2a$10$V7HaxkFPXybT1Xsda/fe5uxlXR9Ii7b5x354FR41ZgNyuTey0Mvp2','2025-05-01 21:04:29',1,1),(2,'María','Fernández','Gómez','maria.fernandez@simin.com','{bcrypt}$2a$10$QuQD/2fOUxkjkUTcPYde7uUUTpWjfQlNRlMPTDFxMrtEfQ7qyQ7AG','2025-05-01 21:04:29',2,2),(3,'Jorge','Martínez','Ramirez','jorge.martinez@simin.com','{bcrypt}$2a$10$fz1XCLmlVHuaBKWLytsxb.xOCl9ibgaquHARDd/.Gi/mStvMAw1Fu','2025-05-01 21:04:29',3,3),(6,'Israel','Ramirez','Samperio','israelsamperio@simin.com','{bcrypt}$2a$10$xWEV5P0.0G2P6Ux2ep6Hd.8Uii7xESA.6Od.GZIb9yTQP1VM7pBiO','2025-05-18 12:39:24',1,1),(7,'israel','samperio','ramirez','samperi@simin.com','{bcrypt}$2a$10$pexUJtb5vVZGp.MNnobHdOWE7svJEzx2.RbRM9mTrsMysOS8Z1w4.','2025-05-18 16:38:31',3,3),(8,'Zay','Dominguez','','zay@simin.com','{bcrypt}$2a$10$U4n2iSm7y8O0KHVgcNlckOWYMH54mk8LTMriEuJCWbsGaS0Q3gC0a','2025-05-18 23:50:14',2,2);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-21 15:00:03
