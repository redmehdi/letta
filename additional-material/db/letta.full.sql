DROP DATABASE IF EXISTS `letta`;

CREATE DATABASE `letta`;
USE `letta`;

GRANT ALL PRIVILEGES ON letta.* TO 'letta'@'localhost' IDENTIFIED BY 'lettapass';
FLUSH PRIVILEGES;

-- Table creation and data insertion queries.
