CREATE DATABASE  IF NOT EXISTS `gestion_sueldos` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `gestion_sueldos`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: gestion_sueldos
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `alicuota`
--

DROP TABLE IF EXISTS `alicuota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alicuota` (
  `id_alicuota` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(100) NOT NULL,
  `porcentaje` decimal(5,2) NOT NULL,
  `id_trabajador` int DEFAULT NULL,
  PRIMARY KEY (`id_alicuota`),
  KEY `fk_trabajador` (`id_trabajador`),
  CONSTRAINT `fk_trabajador` FOREIGN KEY (`id_trabajador`) REFERENCES `trabajador` (`id_trabajador`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alicuota`
--

LOCK TABLES `alicuota` WRITE;
/*!40000 ALTER TABLE `alicuota` DISABLE KEYS */;
/*!40000 ALTER TABLE `alicuota` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recibo`
--

DROP TABLE IF EXISTS `recibo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recibo` (
  `id_recibo` int NOT NULL AUTO_INCREMENT,
  `id_trabajador` int NOT NULL,
  `fecha` date NOT NULL,
  `sueldo_neto` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id_recibo`),
  KEY `id_trabajador` (`id_trabajador`),
  CONSTRAINT `recibo_ibfk_1` FOREIGN KEY (`id_trabajador`) REFERENCES `trabajador` (`id_trabajador`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recibo`
--

LOCK TABLES `recibo` WRITE;
/*!40000 ALTER TABLE `recibo` DISABLE KEYS */;
/*!40000 ALTER TABLE `recibo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trabajador`
--

DROP TABLE IF EXISTS `trabajador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trabajador` (
  `id_trabajador` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `apellido` varchar(50) NOT NULL,
  `dni` varchar(10) NOT NULL,
  `sueldo_bruto` decimal(10,2) NOT NULL,
  `email` varchar(50) DEFAULT NULL,
  `telefono` varchar(15) DEFAULT NULL,
  `id_usuario` int DEFAULT NULL,
  PRIMARY KEY (`id_trabajador`),
  KEY `fk_usuario` (`id_usuario`),
  CONSTRAINT `fk_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trabajador`
--

LOCK TABLES `trabajador` WRITE;
/*!40000 ALTER TABLE `trabajador` DISABLE KEYS */;
INSERT INTO `trabajador` VALUES (31,'Juan','Brossard','29694425',50000.00,NULL,NULL,NULL),(32,'Mariela','Bordon','27447099',300000.00,'mariela@hotmail.com','1565464',NULL),(33,'xxxx','aaaaaaa','23423442',320000.00,'usuario@gmail.com','235345',NULL),(34,'thyrthrtsdh','fdgdfgdf','23567865',32000.00,'Usuario1@hotmail.com','123456',NULL),(35,'fdghfgh','fghfgh','345345',345345.00,'usuario8@hotmail.com','4353452',NULL),(42,'NombreEjemplo','ApellidoEjemplo','12345678',50000.00,'email@example.com','1234567890',2),(49,'Trabajador','Ocho','88888888',888.00,'888@hotmail.com','8888888888',2),(50,'Trabajador','Trece','131313131',131311.00,'313131@hotmail.com','1313131313',6),(51,'Trabajador','Catorce','1414141414',141414.00,'1414@hotmail.com','1414141414',12);
/*!40000 ALTER TABLE `trabajador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `apellido` varchar(50) NOT NULL,
  `dni` varchar(10) NOT NULL,
  `matricula` varchar(20) DEFAULT NULL,
  `email` varchar(50) NOT NULL,
  `contrasena` varchar(100) NOT NULL,
  `rol` enum('admin','usuario') NOT NULL,
  PRIMARY KEY (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (2,'María','González','23456789',NULL,'maria.gonzalez@example.com','hashed_password_2','usuario'),(6,'Marie','Bordon','202325556','1321321','marie@gmail.com','12345','usuario'),(8,'Noah','Brossard','54687798','0002','noah@hotmail.com','12345','usuario'),(9,'Juan','Pérez','12345678','AB123','juan.perez@example.com','hashed_password_1','admin'),(10,'Antuan','Brossard','54253525','00005','antuan@hotmail.com','123456','admin'),(12,'Juan','Brossard','29694424','65641','manuel@hotmai.com','231564','admin'),(13,'Usuario','Siete','77777777','7777777','7777@hotmail.com','7777','usuario'),(14,'Usuario','Tres','33333333','3333','3333@hotmail.com','3333','usuario');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario_trabajador`
--

DROP TABLE IF EXISTS `usuario_trabajador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario_trabajador` (
  `id_usuario_trabajador` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `id_trabajador` int NOT NULL,
  PRIMARY KEY (`id_usuario_trabajador`),
  KEY `id_usuario` (`id_usuario`),
  KEY `id_trabajador` (`id_trabajador`),
  CONSTRAINT `usuario_trabajador_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `usuario_trabajador_ibfk_2` FOREIGN KEY (`id_trabajador`) REFERENCES `trabajador` (`id_trabajador`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_trabajador`
--

LOCK TABLES `usuario_trabajador` WRITE;
/*!40000 ALTER TABLE `usuario_trabajador` DISABLE KEYS */;
/*!40000 ALTER TABLE `usuario_trabajador` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-14 13:47:20
