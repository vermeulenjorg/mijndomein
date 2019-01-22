-- MySQL dump 10.13  Distrib 8.0.13, for Win64 (x86_64)
--
-- Host: localhost    Database: mijndomein
-- ------------------------------------------------------
-- Server version	8.0.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `centrale`
--

DROP TABLE IF EXISTS `centrale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `centrale` (
  `centraleID` int(11) NOT NULL,
  `centraleNaam` varchar(45) NOT NULL,
  `centraleUrl` varchar(255) NOT NULL,
  `centraleMac` varchar(255) NOT NULL,
  PRIMARY KEY (`centraleID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `centrale`
--

LOCK TABLES `centrale` WRITE;
/*!40000 ALTER TABLE `centrale` DISABLE KEYS */;
INSERT INTO `centrale` VALUES (1,'CTR123','123','123'),(2,'CTR456','456','456');
/*!40000 ALTER TABLE `centrale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `device`
--

DROP TABLE IF EXISTS `device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `device` (
  `deviceID` int(11) NOT NULL AUTO_INCREMENT,
  `deviceName` varchar(45) NOT NULL,
  `deviceType` varchar(45) NOT NULL,
  `devicePort` int(11) NOT NULL,
  `deviceState` varchar(45) NOT NULL DEFAULT 'OFF',
  `AnalogState` int(11) DEFAULT '0',
  `measuredAnalogState` int(11) DEFAULT '0',
  `centraleID` int(11) DEFAULT NULL,
  PRIMARY KEY (`deviceID`),
  KEY `centraleID_idx` (`centraleID`),
  CONSTRAINT `centraleID` FOREIGN KEY (`centraleID`) REFERENCES `centrale` (`centraleid`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device`
--

LOCK TABLES `device` WRITE;
/*!40000 ALTER TABLE `device` DISABLE KEYS */;
INSERT INTO `device` VALUES (2,'Device2','DIGITAL',2,'OFF',0,0,NULL),(3,'Device3','ANALOG',3,'OFF',3,17,1),(4,'Device4','DIGITAL',4,'OFF',0,0,NULL),(5,'Device5','DIGITAL',5,'ON',0,0,NULL),(6,'Device6','DIGITAL',6,'OFF',0,0,NULL),(7,'Device7','DIGITAL',7,'OFF',0,0,NULL),(8,'Device8','DIGITAL',8,'OFF',0,0,NULL),(10,'Device10','DIGITAL',10,'OFF',0,0,NULL),(11,'Device11','DIGITAL',11,'ON',10,25,1),(12,'Device12','DIGITAL',12,'OFF',0,0,1),(13,'Device13','ANALOG',13,'OFF',3,28,1),(14,'Device14','DIGITAL',14,'OFF',0,0,1),(15,'Device15','DIGITAL',15,'OFF',0,0,2);
/*!40000 ALTER TABLE `device` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `devicegroup`
--

DROP TABLE IF EXISTS `devicegroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `devicegroup` (
  `deviceID` int(11) NOT NULL,
  `groupID` int(11) NOT NULL,
  PRIMARY KEY (`deviceID`,`groupID`),
  KEY `groupID_idx` (`groupID`),
  CONSTRAINT `deviceID` FOREIGN KEY (`deviceID`) REFERENCES `device` (`deviceID`),
  CONSTRAINT `groupID` FOREIGN KEY (`groupID`) REFERENCES `groep` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `devicegroup`
--

LOCK TABLES `devicegroup` WRITE;
/*!40000 ALTER TABLE `devicegroup` DISABLE KEYS */;
INSERT INTO `devicegroup` VALUES (2,4),(2,6),(3,6),(4,6),(3,8),(11,8),(12,8),(13,8);
/*!40000 ALTER TABLE `devicegroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `devicelogging`
--

DROP TABLE IF EXISTS `devicelogging`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `devicelogging` (
  `logID` int(11) NOT NULL,
  `deviceID` int(11) NOT NULL,
  `measure` varchar(255) NOT NULL,
  `timestamp` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`logID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `devicelogging`
--

LOCK TABLES `devicelogging` WRITE;
/*!40000 ALTER TABLE `devicelogging` DISABLE KEYS */;
/*!40000 ALTER TABLE `devicelogging` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groep`
--

DROP TABLE IF EXISTS `groep`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `groep` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `groupName` varchar(45) NOT NULL,
  `activationState` varchar(3) NOT NULL DEFAULT 'OFF',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groep`
--

LOCK TABLES `groep` WRITE;
/*!40000 ALTER TABLE `groep` DISABLE KEYS */;
INSERT INTO `groep` VALUES (2,'Groep2','ON'),(3,'Groep3','OFF'),(4,'Groep4','OFF'),(5,'Groep5','OFF'),(6,'Groep6','ON'),(7,'Groep7','OFF'),(8,'Groep8','OFF');
/*!40000 ALTER TABLE `groep` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'shy','1234'),(2,'shai','1345'),(3,'shailis','7890'),(4,'shytje','0000'),(5,'user','$2a$12$SmCuLgz2PKrKtNBU2Awjme0Skyi6OJu20uBVj8zKBC2v.S6CJcVWG');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usercentrales`
--

DROP TABLE IF EXISTS `usercentrales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `usercentrales` (
  `userID` int(11) NOT NULL,
  `centraleID` int(11) NOT NULL,
  PRIMARY KEY (`userID`,`centraleID`),
  KEY `ctID_idx` (`centraleID`),
  CONSTRAINT `ctID` FOREIGN KEY (`centraleID`) REFERENCES `centrale` (`centraleid`),
  CONSTRAINT `usID` FOREIGN KEY (`userID`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usercentrales`
--

LOCK TABLES `usercentrales` WRITE;
/*!40000 ALTER TABLE `usercentrales` DISABLE KEYS */;
INSERT INTO `usercentrales` VALUES (3,1),(4,1),(2,2);
/*!40000 ALTER TABLE `usercentrales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userdevices`
--

DROP TABLE IF EXISTS `userdevices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `userdevices` (
  `userID` int(11) NOT NULL,
  `deviceID` int(11) NOT NULL,
  PRIMARY KEY (`userID`,`deviceID`),
  KEY `deviceID_idx` (`deviceID`),
  CONSTRAINT `devID` FOREIGN KEY (`deviceID`) REFERENCES `device` (`deviceID`),
  CONSTRAINT `userID` FOREIGN KEY (`userID`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userdevices`
--

LOCK TABLES `userdevices` WRITE;
/*!40000 ALTER TABLE `userdevices` DISABLE KEYS */;
INSERT INTO `userdevices` VALUES (1,2),(1,3),(4,3),(1,4),(2,5),(3,6),(3,8),(2,10),(4,11),(4,12),(4,13),(4,14),(1,15);
/*!40000 ALTER TABLE `userdevices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usergroups`
--

DROP TABLE IF EXISTS `usergroups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `usergroups` (
  `userID` int(11) NOT NULL,
  `groupID` int(11) NOT NULL,
  PRIMARY KEY (`userID`,`groupID`),
  KEY `groupID_idx` (`groupID`),
  CONSTRAINT `gID` FOREIGN KEY (`groupID`) REFERENCES `groep` (`id`),
  CONSTRAINT `uID` FOREIGN KEY (`userID`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usergroups`
--

LOCK TABLES `usergroups` WRITE;
/*!40000 ALTER TABLE `usergroups` DISABLE KEYS */;
INSERT INTO `usergroups` VALUES (1,2),(1,3),(1,4),(2,5),(3,6),(4,8);
/*!40000 ALTER TABLE `usergroups` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-01-17 17:26:55
