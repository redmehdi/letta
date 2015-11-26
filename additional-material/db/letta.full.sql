DROP DATABASE IF EXISTS `letta`;

CREATE DATABASE `letta`;
USE `letta`;

GRANT ALL PRIVILEGES ON letta.* TO 'letta'@'localhost' IDENTIFIED BY 'lettapass';
FLUSH PRIVILEGES;


-- Table creation
CREATE TABLE `Event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `eventType` varchar(255) NOT NULL,
  `location` varchar(100) NOT NULL,
  `shortDescription` varchar(50) NOT NULL,
  `title` varchar(20) NOT NULL,
  `creator` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `K_Event_Creator` (`creator`),
  CONSTRAINT `FK_Event_Creator` FOREIGN KEY (`creator`) REFERENCES `User` (`login`)
);

CREATE TABLE `Registration` (
  `uuid` varchar(36) NOT NULL,
  `email` varchar(100) NOT NULL,
  `login` varchar(20) NOT NULL,
  `password` varchar(32) NOT NULL,
  `role` varchar(10) NOT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `UK_Registration_email` (`email`),
  UNIQUE KEY `UK_Registration_login` (`login`)
);

CREATE TABLE `User` (
  `login` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(32) NOT NULL,
  `role` varchar(10) NOT NULL,
  PRIMARY KEY (`login`),
  UNIQUE KEY `UK_User_email` (`email`)
);

CREATE TABLE `UserJoinsEvent` (
  `user_id` varchar(20) NOT NULL,
  `event_id` int(11) NOT NULL,
  KEY `K_UserJoinsEvent_user_id` (`user_id`),
  KEY `K_UserJoinsEvent_event_id` (`event_id`),
  CONSTRAINT `FK_UserJoinsEvent_user_id` FOREIGN KEY (`user_id`) REFERENCES `User` (`login`),
  CONSTRAINT `FK_FK_UserJoinsEvent_event_id` FOREIGN KEY (`event_id`) REFERENCES `Event` (`id`)
);


-- Data Insertion
INSERT INTO User (login, password, email, role)
VALUES 
  ("john","3bffe7a2bc163d273184e8902afe66b7", "john@email.com", "USER"), 
  ("anne","1afee8bef2d82a3bef6f52b2614f16ab", "anne@email.com", "USER"), 
  ("mary","a5446c2cfe2b8a015caa8a7e825bb8af", "mary@email.com", "USER"), 
  ("joan","ce8a660555a5701617403c77f6654d65", "joan@email.com", "USER"), 
  ("mike","b1b668f82813956ef1fe9688e6c05011", "mike@email.com", "USER");

INSERT INTO Event (id, eventType, title, shortDescription, date, location, creator)
VALUES 
  (1, "LITERATURE", "Example1 literature", "This is a description literature 1", "2000-01-01 01:01:01", "Location X", "john"),
  (2, "LITERATURE", "Example2 literature", "This is a description literature 2", "2000-01-01 01:01:01", "Location X", "john"),
  (3, "MUSIC", "Example1 music", "This is a description music 1", "2000-01-01 01:01:01", "Location X", "john"),
  (4, "MUSIC", "Example2 music", "This is a description music 2", "2000-01-01 01:01:01", "Location X", "john"),
  (5, "CINEMA", "Example1 cinema", "This is a description cinema 1", "2000-01-01 01:01:01", "Location X", "john"),
  (6, "CINEMA", "Example2 cinema", "This is a description cinema 2", "2000-01-01 01:01:01", "Location X", "anne"),
  (7, "TV", "Example1 tv", "This is a description tv 1", "2000-01-01 01:01:01", "Location X", "anne"),
  (8, "TV", "Example2 tv", "This is a description tv 2", "2000-01-01 01:01:01", "Location X", "anne"),
  (9, "SPORTS", "Example1 sports", "This is a description sports 1", "2000-01-01 01:01:01", "Location X", "anne"),
  (10, "SPORTS", "Example2 sports", "This is a description sports 2", "2000-01-01 01:01:01", "Location X", "anne"),
  (11, "INTERNET", "Example1 internet", "This is a description internet 1", "2000-01-01 01:01:01", "Location X", "mary"),
  (12, "INTERNET", "Example2 internet", "This is a description internet 2", "2000-01-01 01:01:01", "Location X", "mary"),
  (13, "TRAVELS", "Example1 travels", "This is a description travels 1", "2000-01-01 01:01:01", "Location X", "mary"),
  (14, "TRAVELS", "Example2 travels", "This is a description travels 2", "2000-01-01 01:01:01", "Location X", "mary"),
  (15, "THEATRE", "Example1 theatre", "This is a description theatre 1", "2000-01-01 01:01:01", "Location X", "mary"),
  (16, "THEATRE", "Example2 theatre", "This is a description theatre 2", "2000-01-01 01:01:01", "Location X", "joan"),
  (17, "SPORTS", "Example3 sports", "This is a description sports 3", "2000-01-01 01:01:01", "Location X", "joan"),
  (18, "INTERNET", "Example3 internet", "This is a description internet 3", "2000-01-01 01:01:01", "Location X", "joan"),
  (19, "TRAVELS", "Example3 travels", "This is a description travels 3", "2000-01-01 01:01:01", "Location X", "joan"),
  (20, "CINEMA", "Example3 cinema", "This is a description cinema 3", "2000-01-01 01:01:01", "Location X", "joan"),
  (21, "TV", "Example3 tv", "This is a description tv 3", "2000-01-01 01:01:01", "Location X", "mike"),
  (22, "MUSIC", "Example3 music", "This is a description music 3", "2000-01-01 01:01:01", "Location X", "mike"),
  (23, "LITERATURE", "Example3 literature", "This is a description literature 3", "2000-01-01 01:01:01", "Location X", "mike"),
  (24, "LITERATURE", "Example4 literature", "This is a description literature 4", "2000-01-01 01:01:01", "Location X", "mike"),
  (25, "LITERATURE", "Example5 literature", "This is a description literature 5", "2000-01-01 01:01:01", "Location X", "mike");