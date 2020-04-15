-- MySQL dump 10.16  Distrib 10.1.38-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: restaurante_local
-- ------------------------------------------------------
-- Server version	10.1.38-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `shared_preferences`
--

DROP TABLE IF EXISTS `shared_preferences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shared_preferences` (
  `funCodigo` int(11) NOT NULL,
  `funNome` text,
  `funEmail` text NOT NULL,
  `funSenha` text NOT NULL,
  `funCargo` text,
  PRIMARY KEY (`funCodigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shared_preferences`
--

LOCK TABLES `shared_preferences` WRITE;
/*!40000 ALTER TABLE `shared_preferences` DISABLE KEYS */;
INSERT INTO `shared_preferences` VALUES (1,'Daniel','danielantonio23599@gmai.com','galodoido',NULL);
/*!40000 ALTER TABLE `shared_preferences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shared_preferences_empresa`
--

DROP TABLE IF EXISTS `shared_preferences_empresa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shared_preferences_empresa` (
  `speCodigo` int(11) NOT NULL,
  `speEmail` text NOT NULL,
  `speSenha` text NOT NULL,
  `speLogo` longblob,
  `speFantazia` text NOT NULL,
  PRIMARY KEY (`speCodigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shared_preferences_empresa`
--

LOCK TABLES `shared_preferences_empresa` WRITE;
/*!40000 ALTER TABLE `shared_preferences_empresa` DISABLE KEYS */;
INSERT INTO `shared_preferences_empresa` VALUES (2,'danielantonio23599@gmai.com','galodoido',NULL,'SASistema e Informatica');
/*!40000 ALTER TABLE `shared_preferences_empresa` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-04-10 21:36:24
