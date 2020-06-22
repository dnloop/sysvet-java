-- MariaDB dump 10.17  Distrib 10.4.13-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: sysvet
-- ------------------------------------------------------
-- Server version	10.4.13-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cuentas_corrientes`
--

DROP TABLE IF EXISTS `cuentas_corrientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cuentas_corrientes` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
  `monto` decimal(8,4) NOT NULL,
  `fecha` date NOT NULL,
  `deleted` tinyint(1) NOT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `propietarios_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `cuentas_corrientes_propietarios_id_foreign` (`propietarios_id`),
  CONSTRAINT `cuentas_corrientes_propietarios_id_foreign` FOREIGN KEY (`propietarios_id`) REFERENCES `propietarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `desparasitaciones`
--

DROP TABLE IF EXISTS `desparasitaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `desparasitaciones` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `fecha` date NOT NULL,
  `tratamiento` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fechaProxima` date NOT NULL,
  `tipo` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `deleted` tinyint(1) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  `pacientes_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `desparasitaciones_pacientes_id_foreign` (`pacientes_id`),
  CONSTRAINT `desparasitaciones_pacientes_id_foreign` FOREIGN KEY (`pacientes_id`) REFERENCES `pacientes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `entrega`
--

DROP TABLE IF EXISTS `entrega`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entrega` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `monto` decimal(8,4) NOT NULL,
  `fecha` date NOT NULL,
  `deleted` tinyint(1) NOT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `propietario_id` int(10) unsigned NOT NULL,
  `tipo` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pendiente` decimal(8,4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `entrega_propietario_id_foreign` (`propietario_id`) USING BTREE,
  CONSTRAINT `entrega_propietarios_id_foreign` FOREIGN KEY (`propietario_id`) REFERENCES `propietarios` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `examen_general`
--

DROP TABLE IF EXISTS `examen_general`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `examen_general` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `fecha` date NOT NULL,
  `pesoCorporal` int(11) NOT NULL,
  `tempCorporal` int(11) NOT NULL,
  `deshidratacion` int(11) NOT NULL,
  `frecResp` int(11) NOT NULL,
  `amplitud` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tipo` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ritmo` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `frecCardio` int(11) NOT NULL,
  `pulso` int(11) NOT NULL,
  `tllc` int(11) NOT NULL,
  `bucal` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `escleral` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `palperal` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sexual` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `submandibular` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `preescapular` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `precrural` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `inguinal` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `popliteo` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `otros` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pacientes_id` int(10) unsigned NOT NULL,
  `deleted` tinyint(1) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  `vulvar` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pacientes_id` (`pacientes_id`),
  CONSTRAINT `examen_general_ibfk_1` FOREIGN KEY (`pacientes_id`) REFERENCES `pacientes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fichas_clinicas`
--

DROP TABLE IF EXISTS `fichas_clinicas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fichas_clinicas` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `fecha` date NOT NULL,
  `motivoConsulta` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `anamnesis` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `medicacion` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `estadoNutricion` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `estadoSanitario` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `aspectoGeneral` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `deterDiagComp` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `derivaciones` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pronostico` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `diagnostico` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `exploracion` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `evolucion` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pacientes_id` int(10) unsigned NOT NULL,
  `deleted` tinyint(1) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fichas_clinicas_pacientes_id_foreign` (`pacientes_id`),
  CONSTRAINT `fichas_clinicas_pacientes_id_foreign` FOREIGN KEY (`pacientes_id`) REFERENCES `pacientes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `historia_clinica`
--

DROP TABLE IF EXISTS `historia_clinica`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historia_clinica` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `descripcionEvento` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `fechaInicio` date NOT NULL,
  `fechaResolucion` date DEFAULT NULL,
  `resultado` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `secuelas` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `consideraciones` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `comentarios` text COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fichas_clinicas_id` int(10) unsigned NOT NULL,
  `deleted` tinyint(1) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `historia_clinica_fichas_clinicas_id_foreign` (`fichas_clinicas_id`),
  CONSTRAINT `historia_clinica_fichas_clinicas_id_foreign` FOREIGN KEY (`fichas_clinicas_id`) REFERENCES `fichas_clinicas` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `internaciones`
--

DROP TABLE IF EXISTS `internaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `internaciones` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `fechaIngreso` date NOT NULL,
  `fechaAlta` date DEFAULT NULL,
  `pacientes_id` int(10) unsigned NOT NULL,
  `deleted` tinyint(1) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pacientes_id` (`pacientes_id`),
  CONSTRAINT `internaciones_ibfk_1` FOREIGN KEY (`pacientes_id`) REFERENCES `pacientes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `localidades`
--

DROP TABLE IF EXISTS `localidades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `localidades` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `cod_postal` int(5) DEFAULT NULL,
  `nombre` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `provincia_id` int(10) unsigned NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `provincia_id` (`provincia_id`),
  CONSTRAINT `localidades_ibfk_1` FOREIGN KEY (`provincia_id`) REFERENCES `provincias` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22166 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pacientes`
--

DROP TABLE IF EXISTS `pacientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pacientes` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `especie` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `raza` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sexo` char(1) COLLATE utf8mb4_unicode_ci NOT NULL,
  `temperamento` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `pelaje` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fechaNacimiento` date DEFAULT NULL,
  `foto` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `propietarios_id` int(10) unsigned DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pacientes_propietarios_id_foreign` (`propietarios_id`),
  CONSTRAINT `pacientes_propietarios_id_foreign` FOREIGN KEY (`propietarios_id`) REFERENCES `propietarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `propietarios`
--

DROP TABLE IF EXISTS `propietarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `propietarios` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellido` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `domicilio` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `telCel` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `telFijo` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mail` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `localidades_id` int(10) unsigned DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `propietarios_localidades_id_foreign` (`localidades_id`),
  CONSTRAINT `propietarios_localidades_id_foreign` FOREIGN KEY (`localidades_id`) REFERENCES `localidades` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `provincias`
--

DROP TABLE IF EXISTS `provincias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `provincias` (
  `id` int(10) unsigned NOT NULL,
  `nombre` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT 0,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tratamientos`
--

DROP TABLE IF EXISTS `tratamientos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tratamientos` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `tratamiento` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha` date DEFAULT NULL,
  `hora` time DEFAULT NULL,
  `deleted` tinyint(1) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  `fichas_clinicas_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fichas_clinicas_id` (`fichas_clinicas_id`),
  CONSTRAINT `tratamientos_ibfk_1` FOREIGN KEY (`fichas_clinicas_id`) REFERENCES `fichas_clinicas` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vacunas`
--

DROP TABLE IF EXISTS `vacunas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vacunas` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `fecha` date NOT NULL,
  `descripcion` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `deleted` tinyint(1) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  `pacientes_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `vacunas_pacientes_id_foreign` (`pacientes_id`),
  CONSTRAINT `vacunas_pacientes_id_foreign` FOREIGN KEY (`pacientes_id`) REFERENCES `pacientes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'sysvet'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-06-22 11:54:54
