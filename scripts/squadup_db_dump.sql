-- MySQL dump 10.13  Distrib 8.0.31, for macos13.0 (arm64)
--
-- Host: localhost    Database: squadup_db
-- ------------------------------------------------------
-- Server version	8.0.31

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
-- Table structure for table `comrades`
--

DROP TABLE IF EXISTS `comrades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comrades` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date_comraded` datetime(6) NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `comrade_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK42xo47bq8x6pqqypkvg8kc2uv` (`user_id`),
  KEY `FKx8xdx9nvys8nhc591ni4pwg4` (`comrade_id`),
  CONSTRAINT `FK42xo47bq8x6pqqypkvg8kc2uv` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKx8xdx9nvys8nhc591ni4pwg4` FOREIGN KEY (`comrade_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comrades`
--

LOCK TABLES `comrades` WRITE;
/*!40000 ALTER TABLE `comrades` DISABLE KEYS */;
/*!40000 ALTER TABLE `comrades` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `game_genre`
--

DROP TABLE IF EXISTS `game_genre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game_genre` (
  `game_id` bigint NOT NULL,
  `genre_id` bigint NOT NULL,
  PRIMARY KEY (`game_id`,`genre_id`),
  KEY `FKou7qtr2d8jvpemnniel386hgx` (`genre_id`),
  CONSTRAINT `FKj4fg0k0h6r6n8df60att283nj` FOREIGN KEY (`game_id`) REFERENCES `games` (`id`),
  CONSTRAINT `FKou7qtr2d8jvpemnniel386hgx` FOREIGN KEY (`genre_id`) REFERENCES `genres` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game_genre`
--

LOCK TABLES `game_genre` WRITE;
/*!40000 ALTER TABLE `game_genre` DISABLE KEYS */;
INSERT INTO `game_genre` VALUES (1,1),(2,1),(3,1),(4,1),(17,1),(18,1),(19,1),(20,1),(21,1),(22,1),(23,1),(24,1),(25,1),(26,1),(30,1),(31,1),(32,1),(33,1),(34,1),(35,1),(36,1),(37,1),(38,1),(40,1),(41,1),(1,2),(3,2),(4,2),(5,2),(12,2),(39,2),(3,3),(4,3),(5,3),(6,3),(13,3),(5,4),(6,4),(7,4),(8,4),(14,4),(5,5),(6,5),(7,5),(27,5),(28,5),(29,5),(7,6),(8,6),(9,6),(10,6),(11,6),(13,6),(14,6),(16,6),(24,6),(27,6),(28,6),(29,6),(35,6),(42,6),(11,7),(12,8),(13,9),(29,9),(14,10),(27,11),(28,11),(29,11),(39,12);
/*!40000 ALTER TABLE `game_genre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `game_platform`
--

DROP TABLE IF EXISTS `game_platform`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game_platform` (
  `game_id` bigint NOT NULL,
  `platform_id` bigint NOT NULL,
  PRIMARY KEY (`game_id`,`platform_id`),
  KEY `FKrbhqd4lmddt2jmgdjy3kirt38` (`platform_id`),
  CONSTRAINT `FK4safgr5f71s67seckgpbttc1f` FOREIGN KEY (`game_id`) REFERENCES `games` (`id`),
  CONSTRAINT `FKrbhqd4lmddt2jmgdjy3kirt38` FOREIGN KEY (`platform_id`) REFERENCES `platforms` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `game_platform`
--

LOCK TABLES `game_platform` WRITE;
/*!40000 ALTER TABLE `game_platform` DISABLE KEYS */;
INSERT INTO `game_platform` VALUES (1,1),(2,1),(3,1),(4,1),(5,1),(7,1),(8,1),(9,1),(11,1),(12,1),(13,1),(14,1),(16,1),(17,1),(18,1),(20,1),(21,1),(24,1),(25,1),(26,1),(27,1),(28,1),(29,1),(31,1),(32,1),(33,1),(34,1),(37,1),(38,1),(39,1),(40,1),(42,1),(1,2),(2,2),(3,2),(4,2),(5,2),(6,2),(9,2),(12,2),(13,2),(28,2),(29,2),(30,2),(31,2),(33,2),(38,2),(40,2),(42,2),(1,3),(2,3),(3,3),(4,3),(5,3),(6,3),(9,3),(12,3),(13,3),(17,3),(18,3),(19,3),(20,3),(21,3),(22,3),(24,3),(25,3),(26,3),(28,3),(30,3),(31,3),(33,3),(34,3),(38,3),(40,3),(42,3),(1,4),(2,4),(5,4),(6,4),(9,4),(12,4),(13,4),(27,4),(28,4),(29,4),(40,4),(42,4);
/*!40000 ALTER TABLE `game_platform` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `games`
--

DROP TABLE IF EXISTS `games`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `games` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `artwork` varchar(255) DEFAULT NULL,
  `igdb_id` bigint DEFAULT NULL,
  `title` varchar(100) NOT NULL,
  `year` int DEFAULT NULL,
  `rating_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfegs7w80427alnau7ecfd6qbf` (`rating_id`),
  CONSTRAINT `FKfegs7w80427alnau7ecfd6qbf` FOREIGN KEY (`rating_id`) REFERENCES `ratings` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `games`
--

LOCK TABLES `games` WRITE;
/*!40000 ALTER TABLE `games` DISABLE KEYS */;
INSERT INTO `games` VALUES (1,'https://images.igdb.com/igdb/image/upload/t_cover_big/co1rcb.jpg',8173,'Overwatch',2016,4),(2,'https://images.igdb.com/igdb/image/upload/t_cover_big/co5tkm.jpg',125174,'Overwatch 2',2022,4),(3,'https://images.igdb.com/igdb/image/upload/t_cover_big/co22sz.jpg',21229,'Overwatch: Game of the Year Edition',2017,4),(4,'https://images.igdb.com/igdb/image/upload/t_cover_big/d4uhamjy9fpsyanqefxg.jpg',28061,'Overwatch: Collector\'s Edition',2016,4),(5,'https://images.igdb.com/igdb/image/upload/t_cover_big/xrpmydnu9rpxvxfjkiu7.jpg',17000,'Stardew Valley',2016,3),(6,'https://images.igdb.com/igdb/image/upload/t_cover_big/co27vj.jpg',42895,'Stardew Valley: Collector\'s Edition',2017,3),(7,'https://images.igdb.com/igdb/image/upload/t_cover_big/co27sn.jpg',129496,'Minecraft China',2017,7),(8,'https://images.igdb.com/igdb/image/upload/t_cover_big/co49x5.jpg',121,'Minecraft',2011,3),(9,'https://images.igdb.com/igdb/image/upload/t_cover_big/co1vi7.jpg',8339,'Minecraft: Story Mode',2015,3),(10,'https://images.igdb.com/igdb/image/upload/t_cover_big/co1mro.jpg',118711,'Minecraft Earth',2019,3),(11,'https://images.igdb.com/igdb/image/upload/t_cover_big/co2u3m.jpg',144104,'Floppy Minecraft',2020,7),(12,'https://images.igdb.com/igdb/image/upload/t_cover_big/co4v2s.jpg',204621,'Minecraft: Legends',2023,3),(13,'https://images.igdb.com/igdb/image/upload/t_cover_big/co233r.jpg',110474,'Minecraft Dungeons',2020,3),(14,'https://images.igdb.com/igdb/image/upload/t_cover_big/co3het.jpg',144111,'Minecraft Lovehunt',2021,7),(15,'https://images.igdb.com/igdb/image/upload/t_cover_big/co3h17.jpg',106175,'Minecraft for Gear VR',2016,7),(16,'https://images.igdb.com/igdb/image/upload/t_cover_big/co1ur5.jpg',28407,'Minecraft: Education Edition',2016,7),(17,'https://images.igdb.com/igdb/image/upload/t_cover_big/co2r2r.jpg',740,'Halo: Combat Evolved',2003,5),(18,'https://images.igdb.com/igdb/image/upload/t_cover_big/co1xhb.jpg',2640,'Halo: Combat Evolved Anniversary',2011,5),(19,'https://images.igdb.com/igdb/image/upload/t_cover_big/co3ly9.jpg',45148,'Halo 4: Limited Edition',2012,5),(20,'https://images.igdb.com/igdb/image/upload/t_cover_big/co1xhh.jpg',989,'Halo 3: ODST',2009,5),(21,'https://images.igdb.com/igdb/image/upload/t_cover_big/co1xhf.jpg',991,'Halo 4',2012,5),(22,'https://images.igdb.com/igdb/image/upload/t_cover_big/co2s5s.jpg',43955,'Halo 3: Legendary Edition',2007,5),(23,'https://images.igdb.com/igdb/image/upload/t_cover_big/co2smo.jpg',77343,'Halo 4: King of the Hill Fueled by Mountain Dew',2012,7),(24,'https://images.igdb.com/igdb/image/upload/t_cover_big/co2dto.jpg',103281,'Halo Infinite',2021,4),(25,'https://images.igdb.com/igdb/image/upload/t_cover_big/co1xhc.jpg',987,'Halo 3',2007,5),(26,'https://images.igdb.com/igdb/image/upload/t_cover_big/co1x77.jpg',986,'Halo 2',2004,5),(27,'https://images.igdb.com/igdb/image/upload/t_cover_big/co1rgi.jpg',14593,'Hollow Knight',2017,3),(28,'https://images.igdb.com/igdb/image/upload/t_cover_big/co3vtl.jpg',115289,'Hollow Knight: Silksong',0,7),(29,'https://images.igdb.com/igdb/image/upload/t_cover_big/co3dk6.jpg',116151,'Hollow Knight: Collector\'s Edition',2019,3),(30,'https://images.igdb.com/igdb/image/upload/t_cover_big/co1wku.jpg',949,'Call of Duty 3',2006,4),(31,'https://images.igdb.com/igdb/image/upload/t_cover_big/co2n19.jpg',621,'Call of Duty',2003,4),(32,'https://images.igdb.com/igdb/image/upload/t_cover_big/co3p0l.jpg',80555,'Duty Calls',2011,7),(33,'https://images.igdb.com/igdb/image/upload/t_cover_big/co1rdj.jpg',28204,'Call of Duty: WWII',2017,5),(34,'https://images.igdb.com/igdb/image/upload/t_cover_big/co3acf.jpg',622,'Call of Duty 2',2006,4),(35,'https://images.igdb.com/igdb/image/upload/t_cover_big/co4a7d.jpg',89115,'Call of Duty: Zombies HD',2010,7),(36,'https://images.igdb.com/igdb/image/upload/t_cover_big/co3b6a.jpg',3761,'Call of Duty: Finest Hour',2004,4),(37,'https://images.igdb.com/igdb/image/upload/t_cover_big/co2adn.jpg',77289,'Call of Duty Online',2015,7),(38,'https://images.igdb.com/igdb/image/upload/t_cover_big/co1wkl.jpg',545,'Call of Duty: Black Ops',2010,5),(39,'https://images.igdb.com/igdb/image/upload/t_cover_big/co2ado.jpg',60777,'Call of Duty: Heroes',2014,4),(40,'https://images.igdb.com/igdb/image/upload/t_cover_big/co1wzo.jpg',114795,'Apex Legends',2019,4),(41,'https://images.igdb.com/igdb/image/upload/t_cover_big/co4roc.jpg',118210,'Apex Legends Mobile',2022,4),(42,'https://images.igdb.com/igdb/image/upload/t_cover_big/co2leq.jpg',140895,'Apex Legends: Champions Edition',2020,4);
/*!40000 ALTER TABLE `games` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `genres`
--

DROP TABLE IF EXISTS `genres`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `genres` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `genres`
--

LOCK TABLES `genres` WRITE;
/*!40000 ALTER TABLE `genres` DISABLE KEYS */;
INSERT INTO `genres` VALUES (1,'Shooter'),(2,'Strategy'),(3,'Role-playing (RPG)'),(4,'Simulator'),(5,'Indie'),(6,'Adventure'),(7,'Fighting'),(8,'Arcade'),(9,'Hack and slash/Beat \'em up'),(10,'Visual Novel'),(11,'Platform'),(12,'Real Time Strategy (RTS)');
/*!40000 ALTER TABLE `genres` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (1);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `languages`
--

DROP TABLE IF EXISTS `languages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `languages` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `language` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `languages`
--

LOCK TABLES `languages` WRITE;
/*!40000 ALTER TABLE `languages` DISABLE KEYS */;
INSERT INTO `languages` VALUES (1,'English'),(2,'Spanish'),(3,'Chinese'),(4,'Japanese'),(5,'Korean'),(6,'German'),(7,'French'),(8,'Italian'),(9,'Portuguese');
/*!40000 ALTER TABLE `languages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `locations`
--

DROP TABLE IF EXISTS `locations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `locations` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `timezone` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `locations`
--

LOCK TABLES `locations` WRITE;
/*!40000 ALTER TABLE `locations` DISABLE KEYS */;
INSERT INTO `locations` VALUES (1,'Unspecified Time Zone'),(2,'Hawaii-Aleutian Time Zone'),(3,'Alaskan Time Zone'),(4,'Pacific Time Zone'),(5,'Mountain Time Zone'),(6,'Mountain Arizona Time Zone'),(7,'Central Time Zone'),(8,'Eastern Time Zone'),(9,'Atlantic Time Zone');
/*!40000 ALTER TABLE `locations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `platform_mapping`
--

DROP TABLE IF EXISTS `platform_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `platform_mapping` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `igdb_id` bigint NOT NULL,
  `platform_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfo7lsb7ki2vt0qlmsr3yy3ssn` (`platform_id`),
  CONSTRAINT `FKfo7lsb7ki2vt0qlmsr3yy3ssn` FOREIGN KEY (`platform_id`) REFERENCES `platforms` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `platform_mapping`
--

LOCK TABLES `platform_mapping` WRITE;
/*!40000 ALTER TABLE `platform_mapping` DISABLE KEYS */;
INSERT INTO `platform_mapping` VALUES (1,6,1),(2,9,2),(3,48,2),(4,167,2),(5,12,3),(6,49,3),(7,169,3),(8,130,4);
/*!40000 ALTER TABLE `platform_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `platforms`
--

DROP TABLE IF EXISTS `platforms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `platforms` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` varchar(25) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `platforms`
--

LOCK TABLES `platforms` WRITE;
/*!40000 ALTER TABLE `platforms` DISABLE KEYS */;
INSERT INTO `platforms` VALUES (1,'PC'),(2,'Playstation'),(3,'Xbox'),(4,'Nintendo');
/*!40000 ALTER TABLE `platforms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preferences`
--

DROP TABLE IF EXISTS `preferences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `preferences` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bio` varchar(255) NOT NULL,
  `gamertag` varchar(255) NOT NULL,
  `mature_language` bit(1) NOT NULL,
  `favorite_game_id` bigint DEFAULT NULL,
  `location_id` bigint DEFAULT NULL,
  `rating_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK15826341ucg45qjitxvjtrau1` (`favorite_game_id`),
  KEY `FKcwootpsc03d2x5001itsdpqgf` (`location_id`),
  KEY `FKc0bwaq8lflcsh5a7tvgmnumlq` (`rating_id`),
  CONSTRAINT `FK15826341ucg45qjitxvjtrau1` FOREIGN KEY (`favorite_game_id`) REFERENCES `games` (`id`),
  CONSTRAINT `FKc0bwaq8lflcsh5a7tvgmnumlq` FOREIGN KEY (`rating_id`) REFERENCES `ratings` (`id`),
  CONSTRAINT `FKcwootpsc03d2x5001itsdpqgf` FOREIGN KEY (`location_id`) REFERENCES `locations` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preferences`
--

LOCK TABLES `preferences` WRITE;
/*!40000 ALTER TABLE `preferences` DISABLE KEYS */;
INSERT INTO `preferences` VALUES (1,'This is my bio','Pfirewire',_binary '',2,7,7),(2,'this is my bio!','thee_dynamite',_binary '',NULL,7,6),(3,'Hello, it\'s me, ya boi, Aiden Luce. This is my biography with a lot of information about myself, my hobbies, and playstyle!','SUPERRMEGGA',_binary '',NULL,7,7),(4,'Rawr','NikitaFiz ',_binary '',NULL,7,6),(5,'Busy burninating the peasants.','Trogdor',_binary '',NULL,7,5),(6,'This is testuser1\'s bio','testuser1',_binary '',NULL,7,7),(7,'This is testuser2\'s bio','testuser2',_binary '',NULL,7,7),(8,'This is testuser3\'s bio','testuser3',_binary '',NULL,7,7),(9,'This is testuser4\'s bio','testuser2',_binary '',NULL,7,7),(10,'This is testuser5\'s bio','testuser2',_binary '',NULL,7,7),(11,'This is testuser6\'s bio','testuser2',_binary '',NULL,7,7),(12,'This is testuser7\'s bio','testuser2',_binary '',NULL,7,7),(13,'This is testuser8\'s bio','testuser2',_binary '',NULL,7,7),(14,'This is testuser9\'s bio','testuser2',_binary '',NULL,7,7),(15,'This is testuser10\'s bio','testuser2',_binary '',NULL,7,7),(16,'This is testuser11\'s bio','testuser2',_binary '',NULL,7,7),(17,'This is testuser12\'s bio','testuser2',_binary '',NULL,7,7),(18,'This is testuser13\'s bio','testuser2',_binary '',NULL,7,7),(19,'This is testuser14\'s bio','testuser2',_binary '',NULL,7,7),(20,'This is testuser15\'s bio','testuser2',_binary '',NULL,7,7),(21,'This is testuser16\'s bio','testuser2',_binary '',NULL,7,7),(22,'This is testuser17\'s bio','testuser2',_binary '',NULL,7,7),(23,'This is testuser18\'s bio','testuser2',_binary '',NULL,7,7),(24,'This is testuser19\'s bio','testuser2',_binary '',NULL,7,7),(25,'This is testuser20\'s bio','testuser2',_binary '',NULL,7,7),(26,'This is testuser21\'s bio','testuser2',_binary '',NULL,7,7),(27,'This is testuser22\'s bio','testuser2',_binary '',NULL,7,7),(28,'This is testuser23\'s bio','testuser2',_binary '',NULL,7,7),(29,'This is testuser24\'s bio','testuser2',_binary '',NULL,7,7),(30,'This is testuser25\'s bio','testuser2',_binary '',NULL,7,7);
/*!40000 ALTER TABLE `preferences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preferences_game`
--

DROP TABLE IF EXISTS `preferences_game`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `preferences_game` (
  `preferences_id` bigint NOT NULL,
  `game_id` bigint NOT NULL,
  PRIMARY KEY (`preferences_id`,`game_id`),
  KEY `FKplyn56dqgvbs9g2q3a5oq4gul` (`game_id`),
  CONSTRAINT `FK8kwd04te1bw6kmexoakv3gmu9` FOREIGN KEY (`preferences_id`) REFERENCES `preferences` (`id`),
  CONSTRAINT `FKplyn56dqgvbs9g2q3a5oq4gul` FOREIGN KEY (`game_id`) REFERENCES `games` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preferences_game`
--

LOCK TABLES `preferences_game` WRITE;
/*!40000 ALTER TABLE `preferences_game` DISABLE KEYS */;
INSERT INTO `preferences_game` VALUES (1,2),(1,5),(1,8),(1,24),(1,27),(6,38),(29,40);
/*!40000 ALTER TABLE `preferences_game` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preferences_genre`
--

DROP TABLE IF EXISTS `preferences_genre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `preferences_genre` (
  `preferences_id` bigint NOT NULL,
  `genre_id` bigint NOT NULL,
  PRIMARY KEY (`preferences_id`,`genre_id`),
  KEY `FKga2t3kjinbq7uwq4key43gr55` (`genre_id`),
  CONSTRAINT `FKga2t3kjinbq7uwq4key43gr55` FOREIGN KEY (`genre_id`) REFERENCES `genres` (`id`),
  CONSTRAINT `FKli3aq7kjxiancmwhgehw7rea7` FOREIGN KEY (`preferences_id`) REFERENCES `preferences` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preferences_genre`
--

LOCK TABLES `preferences_genre` WRITE;
/*!40000 ALTER TABLE `preferences_genre` DISABLE KEYS */;
INSERT INTO `preferences_genre` VALUES (1,1),(6,1),(29,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,11);
/*!40000 ALTER TABLE `preferences_genre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preferences_language`
--

DROP TABLE IF EXISTS `preferences_language`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `preferences_language` (
  `preferences_id` bigint NOT NULL,
  `language_id` bigint NOT NULL,
  PRIMARY KEY (`preferences_id`,`language_id`),
  KEY `FKi5adw4b42udi2tv7kip9x3tlt` (`language_id`),
  CONSTRAINT `FK9h95tq18aia7ek3v5gwako52t` FOREIGN KEY (`preferences_id`) REFERENCES `preferences` (`id`),
  CONSTRAINT `FKi5adw4b42udi2tv7kip9x3tlt` FOREIGN KEY (`language_id`) REFERENCES `languages` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preferences_language`
--

LOCK TABLES `preferences_language` WRITE;
/*!40000 ALTER TABLE `preferences_language` DISABLE KEYS */;
INSERT INTO `preferences_language` VALUES (1,1),(2,1),(3,1),(4,1),(5,1),(6,1),(7,1),(3,2);
/*!40000 ALTER TABLE `preferences_language` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preferences_platform`
--

DROP TABLE IF EXISTS `preferences_platform`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `preferences_platform` (
  `preferences_id` bigint NOT NULL,
  `platform_id` bigint NOT NULL,
  PRIMARY KEY (`preferences_id`,`platform_id`),
  KEY `FKq88ho8f3fk93imqj0y3xh73sk` (`platform_id`),
  CONSTRAINT `FKg76xbieybk73i0o0ujc5ei2p0` FOREIGN KEY (`preferences_id`) REFERENCES `preferences` (`id`),
  CONSTRAINT `FKq88ho8f3fk93imqj0y3xh73sk` FOREIGN KEY (`platform_id`) REFERENCES `platforms` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preferences_platform`
--

LOCK TABLES `preferences_platform` WRITE;
/*!40000 ALTER TABLE `preferences_platform` DISABLE KEYS */;
INSERT INTO `preferences_platform` VALUES (1,1),(2,1),(5,1),(6,1),(7,1),(8,1),(9,1),(10,1),(11,1),(12,1),(13,1),(14,1),(15,1),(16,1),(17,1),(18,1),(19,1),(20,1),(21,1),(22,1),(23,1),(24,1),(25,1),(26,1),(27,1),(28,1),(29,1),(30,1),(3,2),(4,2),(1,3),(3,4),(4,4);
/*!40000 ALTER TABLE `preferences_platform` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ratings`
--

DROP TABLE IF EXISTS `ratings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ratings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `igdb_id` int NOT NULL,
  `rating` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ratings`
--

LOCK TABLES `ratings` WRITE;
/*!40000 ALTER TABLE `ratings` DISABLE KEYS */;
INSERT INTO `ratings` VALUES (1,7,'Early Childhood'),(2,8,'Everyone'),(3,9,'Everyone 10 & up'),(4,10,'Teen (13 & up)'),(5,11,'Mature (17 & up)'),(6,12,'Adults Only (18 & up)'),(7,6,'Rating Pending');
/*!40000 ALTER TABLE `ratings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recruits`
--

DROP TABLE IF EXISTS `recruits`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recruits` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date_recruited` datetime(6) NOT NULL,
  `is_accepted` bit(1) NOT NULL,
  `is_rejected` bit(1) NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `recruit_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlgmy71c5rrncuoid05ebrdabl` (`user_id`),
  KEY `FK75pfrbg9s8i5p5jnv4eyw403a` (`recruit_id`),
  CONSTRAINT `FK75pfrbg9s8i5p5jnv4eyw403a` FOREIGN KEY (`recruit_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKlgmy71c5rrncuoid05ebrdabl` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recruits`
--

LOCK TABLES `recruits` WRITE;
/*!40000 ALTER TABLE `recruits` DISABLE KEYS */;
INSERT INTO `recruits` VALUES (1,'2023-01-04 11:48:01.680000',_binary '\0',_binary '\0',1,6),(2,'2023-01-04 11:48:01.707000',_binary '\0',_binary '\0',6,1),(3,'2023-01-04 12:49:34.866000',_binary '\0',_binary '\0',6,29),(4,'2023-01-04 12:49:34.932000',_binary '\0',_binary '\0',1,29),(5,'2023-01-04 12:49:34.940000',_binary '\0',_binary '\0',29,6),(6,'2023-01-04 12:49:34.944000',_binary '\0',_binary '\0',29,1);
/*!40000 ALTER TABLE `recruits` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `strays`
--

DROP TABLE IF EXISTS `strays`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `strays` (
  `id` bigint NOT NULL,
  `body` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `fname` varchar(255) DEFAULT NULL,
  `lname` varchar(255) DEFAULT NULL,
  `phone_number` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `strays`
--

LOCK TABLES `strays` WRITE;
/*!40000 ALTER TABLE `strays` DISABLE KEYS */;
/*!40000 ALTER TABLE `strays` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(75) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(50) NOT NULL,
  `preferences_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`),
  KEY `FK8khbqromxxbc38faymkxiky2` (`preferences_id`),
  CONSTRAINT `FK8khbqromxxbc38faymkxiky2` FOREIGN KEY (`preferences_id`) REFERENCES `preferences` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'stephen@pflug.us','$2a$10$CZDensNrGePVIz5LoJmti.7MHOpbm61T2gIigV2IZcMjmRsOGVqnW','pfirewire',1),(2,'cesarlopez9506@gmail.com','$2a$10$W6s5Dv9WS5MS/SDQajSMNu3HQTPG2g7XH/z2n1Cg0Pmbz8hgzEQhu','cesar',2),(3,'aiden.l.luce@gmail.com','$2a$10$Z9Yno.s/3hiYy8bQVGu19.U2Q6e2i5P05Ah8eH92hkro16gwpx25S','Aiden Luce',3),(4,'jazzhud@gmail.com','$2a$10$ZlxuLPh49ZGtX2S9XLf2aODw41/XtCAYch.Waz8/SotBq5O7q62kS','jazz',4),(5,'jack.blair.graham@gmail.com','$2a$10$SjG9VgplorfjdW8pMdypju5E3BTUMBodKNZYS8C0vhu1nk64/dGXG','Trogdor',5),(6,'testuser1@squadup.pro','$2a$10$OPiQkW0gd9ohNDOhi.KvluireeVDarEeKoSzPUJszwK2i3dKmbvh2','testuser1',6),(7,'testuser2@squadup.pro','$2a$10$h2Upm3FuhVp9.UPQ9bDHpeLXL2JOd.KCCKTp5zAc8bJuWZFDWlZye','testuser2',7),(8,'testuser3@squadup.pro','$2a$10$h2Upm3FuhVp9.UPQ9bDHpeLXL2JOd.KCCKTp5zAc8bJuWZFDWlZye','testuser3',8),(9,'testuser4@squadup.pro','$2a$10$h2Upm3FuhVp9.UPQ9bDHpeLXL2JOd.KCCKTp5zAc8bJuWZFDWlZye','testuser4',9),(10,'testuser5@squadup.pro','$2a$10$h2Upm3FuhVp9.UPQ9bDHpeLXL2JOd.KCCKTp5zAc8bJuWZFDWlZye','testuser5',10),(11,'testuser6@squadup.pro','$2a$10$h2Upm3FuhVp9.UPQ9bDHpeLXL2JOd.KCCKTp5zAc8bJuWZFDWlZye','testuser6',11),(12,'testuser7@squadup.pro','$2a$10$h2Upm3FuhVp9.UPQ9bDHpeLXL2JOd.KCCKTp5zAc8bJuWZFDWlZye','testuser7',12),(13,'testuser8@squadup.pro','$2a$10$h2Upm3FuhVp9.UPQ9bDHpeLXL2JOd.KCCKTp5zAc8bJuWZFDWlZye','testuser8',13),(14,'testuser9@squadup.pro','$2a$10$h2Upm3FuhVp9.UPQ9bDHpeLXL2JOd.KCCKTp5zAc8bJuWZFDWlZye','testuser9',14),(15,'testuser10@squadup.pro','$2a$10$h2Upm3FuhVp9.UPQ9bDHpeLXL2JOd.KCCKTp5zAc8bJuWZFDWlZye','testuser10',15),(16,'testuser11@squadup.pro','$2a$10$h2Upm3FuhVp9.UPQ9bDHpeLXL2JOd.KCCKTp5zAc8bJuWZFDWlZye','testuser11',16),(17,'testuser12@squadup.pro','$2a$10$h2Upm3FuhVp9.UPQ9bDHpeLXL2JOd.KCCKTp5zAc8bJuWZFDWlZye','testuser12',17),(18,'testuser13@squadup.pro','$2a$10$h2Upm3FuhVp9.UPQ9bDHpeLXL2JOd.KCCKTp5zAc8bJuWZFDWlZye','testuser13',18),(19,'testuser14@squadup.pro','$2a$10$h2Upm3FuhVp9.UPQ9bDHpeLXL2JOd.KCCKTp5zAc8bJuWZFDWlZye','testuser14',19),(20,'testuser15@squadup.pro','$2a$10$h2Upm3FuhVp9.UPQ9bDHpeLXL2JOd.KCCKTp5zAc8bJuWZFDWlZye','testuser15',20),(21,'testuser16@squadup.pro','$2a$10$h2Upm3FuhVp9.UPQ9bDHpeLXL2JOd.KCCKTp5zAc8bJuWZFDWlZye','testuser16',21),(22,'testuser17@squadup.pro','$2a$10$h2Upm3FuhVp9.UPQ9bDHpeLXL2JOd.KCCKTp5zAc8bJuWZFDWlZye','testuser17',22),(23,'testuser18@squadup.pro','$2a$10$h2Upm3FuhVp9.UPQ9bDHpeLXL2JOd.KCCKTp5zAc8bJuWZFDWlZye','testuser18',23),(24,'testuser19@squadup.pro','$2a$10$h2Upm3FuhVp9.UPQ9bDHpeLXL2JOd.KCCKTp5zAc8bJuWZFDWlZye','testuser19',24),(25,'testuser20@squadup.pro','$2a$10$h2Upm3FuhVp9.UPQ9bDHpeLXL2JOd.KCCKTp5zAc8bJuWZFDWlZye','testuser20',25),(26,'testuser21@squadup.pro','$2a$10$h2Upm3FuhVp9.UPQ9bDHpeLXL2JOd.KCCKTp5zAc8bJuWZFDWlZye','testuser21',26),(27,'testuser22@squadup.pro','$2a$10$h2Upm3FuhVp9.UPQ9bDHpeLXL2JOd.KCCKTp5zAc8bJuWZFDWlZye','testuser22',27),(28,'testuser23@squadup.pro','$2a$10$h2Upm3FuhVp9.UPQ9bDHpeLXL2JOd.KCCKTp5zAc8bJuWZFDWlZye','testuser23',28),(29,'testuser24@squadup.pro','$2a$10$h2Upm3FuhVp9.UPQ9bDHpeLXL2JOd.KCCKTp5zAc8bJuWZFDWlZye','testuser24',29),(30,'testuser25@squadup.pro','$2a$10$h2Upm3FuhVp9.UPQ9bDHpeLXL2JOd.KCCKTp5zAc8bJuWZFDWlZye','testuser25',30);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-01-04 12:52:55
