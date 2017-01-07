DELETE FROM EventAttendees;
DELETE FROM Event;
DELETE FROM UserNotifications;
DELETE FROM Notification;
DELETE FROM Registration;
DELETE FROM Friendship;
DELETE FROM User;
DELETE FROM Capital;
DELETE FROM CapitalDistances;

ALTER TABLE `Event` ALTER COLUMN `id` RESTART WITH 1;
