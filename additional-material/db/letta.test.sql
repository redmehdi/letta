DROP DATABASE IF EXISTS `letta-test`;

CREATE DATABASE `letta-test`;
USE `letta-test`;

GRANT ALL PRIVILEGES ON `letta-test`.* TO 'letta'@'localhost' IDENTIFIED BY 'lettapass';
FLUSH PRIVILEGES;

-- Table creation and data insertion queries.
