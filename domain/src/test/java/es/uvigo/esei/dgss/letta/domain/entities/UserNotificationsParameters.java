package es.uvigo.esei.dgss.letta.domain.entities;

import static es.uvigo.esei.dgss.letta.domain.entities.NotificationParameters.existentNotification;
import static es.uvigo.esei.dgss.letta.domain.entities.UserParameters.validUser;

public class UserNotificationsParameters {

	// Disallow constructor
	private UserNotificationsParameters() {
	}

	/**
	 * Returns an existent {@link User} login.
	 *
	 * @return an existent {@link User} login.
	 */
	public static String existentUserId() {
		return "john";
	}

	/**
	 * Returns an existent {@link Notification} id.
	 *
	 * @return an existent {@link Notification} id.
	 */
	public static int existentNotificationId() {
		return 1;
	}

	/**
	 * Returns an existent {@link UserNotifications}.
	 * 
	 * @return an existent {@link UserNotifications}.
	 */
	public static UserNotifications existentUserNotification() {
		return new UserNotifications(validUser().getLogin(),
				existentNotification().getId(), false);
	}
}
