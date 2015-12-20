DROP DATABASE IF EXISTS `letta`;

CREATE DATABASE `letta`;
USE `letta`;

GRANT ALL PRIVILEGES ON letta.* TO 'letta'@'localhost' IDENTIFIED BY 'lettapass';
FLUSH PRIVILEGES;

CREATE TABLE `Registration` (
  `uuid`     varchar(36)  NOT NULL,
  `email`    varchar(100) NOT NULL,
  `login`    varchar(20)  NOT NULL,
  `password` varchar(32)  NOT NULL,
  `role`     varchar(10)  NOT NULL,
  `completeName` varchar(30) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `fbUrl`    varchar(50) DEFAULT NULL,
  `twURL`    varchar(50) DEFAULT NULL,
  `personalUrl` varchar(50) DEFAULT NULL,
  `image`    longblob DEFAULT NULL,

  PRIMARY KEY (`uuid`),
  UNIQUE KEY `UK_Registration_email` (`email`),
  UNIQUE KEY `UK_Registration_login` (`login`)
);

CREATE TABLE `User` (
  `login`    varchar(20)  NOT NULL,
  `email`    varchar(100) NOT NULL,
  `password` varchar(32)  NOT NULL,
  `role`     varchar(10)  NOT NULL,
  `completeName` varchar(30) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `fbUrl`    varchar(50) DEFAULT NULL,
  `twURL`    varchar(50) DEFAULT NULL,
  `personalUrl` varchar(50) DEFAULT NULL,
  `image`    longblob DEFAULT NULL,

  PRIMARY KEY (`login`),
  UNIQUE KEY `UK_User_email` (`email`)
);

-- Table creation
CREATE TABLE `Event` (
  `id`          int(11)      NOT NULL AUTO_INCREMENT,
  `date`        timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `category`    varchar(255) NOT NULL,
  `location`    varchar(100) NOT NULL,
  `summary`     varchar(50)  NOT NULL,
  `title`       varchar(20)  NOT NULL,
  `owner`       varchar(20)  NOT NULL,
  `cancelled`   tinyint  	 NOT NULL,
  `description` text,

  PRIMARY KEY (`id`),
  KEY `K_Event_Owner` (`owner`),

  CONSTRAINT `FK_Event_Owner` FOREIGN KEY (`owner`) REFERENCES `User` (`login`)
);

CREATE TABLE `EventAttendees` (
  `user_login` varchar(20) NOT NULL,
  `event_id`   int(11)     NOT NULL,

  KEY `K_UserJoinsEvent_user_login` (`user_login`),
  KEY `K_UserJoinsEvent_event_id`   (`event_id`),

  CONSTRAINT `FK_UserJoinsEvent_user_login`  FOREIGN KEY (`user_login`) REFERENCES `User` (`login`),
  CONSTRAINT `FK_FK_UserJoinsEvent_event_id` FOREIGN KEY (`event_id`)   REFERENCES `Event` (`id`)
);


-- Data Insertion
INSERT INTO User (login, password, email, role, completeName, description, fbUrl, twUrl, personalUrl) VALUES
  ("john","3bffe7a2bc163d273184e8902afe66b7", "john@email.com", "USER", "john name", "john description", "https://www.facebook.com/john", "https://twitter.com/john", "https://johnpersonal.com/"),
  ("anne","1afee8bef2d82a3bef6f52b2614f16ab", "anne@email.com", "USER", "anne name", "anne description", "https://www.facebook.com/anne", "https://twitter.com/anne", "https://annepersonal.com/"),
  ("mary","a5446c2cfe2b8a015caa8a7e825bb8af", "mary@email.com", "USER", "mary name", "mary description", "https://www.facebook.com/mary", "https://twitter.com/mary", "https://marypersonal.com/"),
  ("joan","ce8a660555a5701617403c77f6654d65", "joan@email.com", "USER", "joan name", "joan description", "https://www.facebook.com/joan", "https://twitter.com/joan", "https://joanpersonal.com/"),
  ("mike","b1b668f82813956ef1fe9688e6c05011", "mike@email.com", "USER", "mike name", "mike description", "https://www.facebook.com/mike", "https://twitter.com/mike", "https://mikepersonal.com/");

INSERT INTO Event (id, category, title, summary, date, location, owner, cancelled, description) VALUES
  ( 1, "BOOKS",      "Example1 Literature", "This is a summary of literature 1", "2000-01-01 01:01:01", "Location X", "john", 0, "This is a long description, with a max. size one thousand, of literature 1"),
  ( 2, "BOOKS",      "Example2 Literature", "This is a summary of literature 2", "2000-01-01 01:01:01", "Location X", "john", 0, "This is a long description, with a max. size one thousand, of literature 2"),
  ( 3, "MUSIC",      "Example1 Music",      "This is a summary of music 1",      "2000-01-01 01:01:01", "Location X", "john", 0, "This is a long description, with a max. size one thousand, of music 1"     ),
  ( 4, "MUSIC",      "Example2 Music",      "This is a summary of music 2",      "2000-01-01 01:01:01", "Location X", "john", 0, "This is a long description, with a max. size one thousand, of music 2"     ),
  ( 5, "MOVIES",     "Example1 Cinema",     "This is a summary of cinema 1",     "2000-01-01 01:01:01", "Location X", "john", 0, "This is a long description, with a max. size one thousand, of cinema 1"    ),
  ( 6, "MOVIES",     "Example2 Cinema",     "This is a summary of cinema 2",     "2000-01-01 01:01:01", "Location X", "anne", 0, "This is a long description, with a max. size one thousand, of cinema 2"    ),
  ( 7, "TELEVISION", "Example1 Tv",         "This is a summary of tv 1",         "2000-01-01 01:01:01", "Location X", "anne", 0, "This is a long description, with a max. size one thousand, of tv 1"        ),
  ( 8, "TELEVISION", "Example2 Tv",         "This is a summary of tv 2",         "2000-01-01 01:01:01", "Location X", "anne", 0, "This is a long description, with a max. size one thousand, of tv 2"        ),
  ( 9, "SPORTS",     "Example1 Sports",     "This is a summary of sports 1",     "2000-01-01 01:01:01", "Location X", "anne", 0, "This is a long description, with a max. size one thousand, of sports 1"    ),
  (10, "SPORTS",     "Example2 Sports",     "This is a summary of sports 2",     "2000-01-01 01:01:01", "Location X", "anne", 0, "This is a long description, with a max. size one thousand, of sports 2"    ),
  (11, "INTERNET",   "Example1 Internet",   "This is a summary of internet 1",   "2000-01-01 01:01:01", "Location X", "mary", 0, "This is a long description, with a max. size one thousand, of internet 1"  ),
  (12, "INTERNET",   "Example2 Internet",   "This is a summary of internet 2",   "2000-01-01 01:01:01", "Location X", "mary", 0, "This is a long description, with a max. size one thousand, of internet 2"  ),
  (13, "TRAVELS",    "Example1 Travels",    "This is a summary of travels 1",    "2000-01-01 01:01:01", "Location X", "mary", 0, "This is a long description, with a max. size one thousand, of travels 1"   ),
  (14, "TRAVELS",    "Example2 Travels",    "This is a summary of travels 2",    "2000-01-01 01:01:01", "Location X", "mary", 0, "This is a long description, with a max. size one thousand, of travels 2"   ),
  (15, "THEATRE",    "Example1 Theatre",    "This is a summary of theatre 1",    "2000-01-01 01:01:01", "Location X", "mary", 0, "This is a long description, with a max. size one thousand, of theatre 1"   ),
  (16, "THEATRE",    "Example2 Theatre",    "This is a summary of theatre 2",    "2000-01-01 01:01:01", "Location X", "joan", 0, "This is a long description, with a max. size one thousand, of theatre 2"   ),
  (17, "SPORTS",     "Example3 Sports",     "This is a summary of sports 3",     "2000-01-01 01:01:01", "Location X", "joan", 0, "This is a long description, with a max. size one thousand, of sports 3"    ),
  (18, "INTERNET",   "Example3 Internet",   "This is a summary of internet 3",   "2000-01-01 01:01:01", "Location X", "joan", 0, "This is a long description, with a max. size one thousand, of internet 3"  ),
  (19, "TRAVELS",    "Example3 Travels",    "This is a summary of travels 3",    "2000-01-01 01:01:01", "Location X", "joan", 0, "This is a long description, with a max. size one thousand, of travels 3"   ),
  (20, "MOVIES",     "Example3 Cinema",     "This is a summary of cinema 3",     "2000-01-01 01:01:01", "Location X", "joan", 0, "This is a long description, with a max. size one thousand, of cinema 3"    ),
  (21, "TELEVISION", "Example3 Tv",         "This is a summary of tv 3",         "2000-01-01 01:01:01", "Location X", "mike", 1, "This is a long description, with a max. size one thousand, of tv 3"        ),
  (22, "MUSIC",      "Example3 Music",      "This is a summary of music 3",      "2000-01-01 01:01:01", "Location X", "mike", 1, "This is a long description, with a max. size one thousand, of music 3"     ),
  (23, "BOOKS",      "Example3 Literature", "This is a summary of literature 3", "2000-01-01 01:01:01", "Location X", "mike", 1, "This is a long description, with a max. size one thousand, of literature 3"),
  (24, "BOOKS",      "Example4 Literature", "This is a summary of literature 4", "2000-01-01 01:01:01", "Location X", "mike", 1, "This is a long description, with a max. size one thousand, of literature 4"),
  (25, "BOOKS",      "Example5 Literature", "This is a summary of literature 5", "2000-01-01 01:01:01", "Location X", "mike", 1, "This is a long description, with a max. size one thousand, of literature 5");

INSERT INTO EventAttendees (event_id, user_login) VALUES
  ( 1, "anne"),
  ( 1, "mary"),
  ( 2, "anne"),
  ( 2, "mary"),
  ( 3, "anne"),
  ( 3, "mary"),
  ( 4, "anne"),
  ( 4, "mary"),
  ( 5, "anne"),
  ( 5, "mary"),
  ( 6, "john"),
  ( 6, "joan"),
  ( 7, "john"),
  ( 7, "joan"),
  ( 8, "john"),
  ( 8, "joan"),
  ( 9, "john"),
  ( 9, "joan"),
  (10, "john"),
  (10, "joan"),
  (11, "mary"),
  (11, "mike"),
  (12, "mary"),
  (12, "mike"),
  (13, "mary"),
  (13, "mike"),
  (14, "mary"),
  (14, "mike"),
  (15, "mary"),
  (15, "mike"),
  (16, "john"),
  (16, "mary"),
  (17, "john"),
  (17, "mary"),
  (18, "john"),
  (18, "mary"),
  (19, "john"),
  (19, "mary"),
  (20, "john"),
  (20, "mary"),
  (21, "john"),
  (21, "anne"),
  (22, "john"),
  (22, "anne"),
  (23, "john"),
  (23, "anne"),
  (24, "john"),
  (24, "anne"),
  (25, "john"),
  (25, "anne");
