DELETE FROM EventAttendees;
DELETE FROM Event;
DELETE FROM User;
DELETE FROM Registration;
DELETE FROM Capital;
DELETE FROM Notification;
DELETE FROM UserNotifications;
DELETE FROM CapitalDistances;

ALTER TABLE `Event` ALTER COLUMN `id` RESTART WITH 1;
