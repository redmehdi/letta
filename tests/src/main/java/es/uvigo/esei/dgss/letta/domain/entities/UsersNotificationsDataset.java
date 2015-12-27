package es.uvigo.esei.dgss.letta.domain.entities;

public class UsersNotificationsDataset {
	/**
	 * Returns all the {@link UsersNotificationsDataset} in the database.
	 * 
	 * @return all the {@link UsersNotificationsDataset} in the database.
	 */
	public static UserNotifications[] usersNotifications() {
		return new UserNotifications[] {
				new UserNotifications("anne", 1, false),
				new UserNotifications("anne", 2, false),
				new UserNotifications("john", 3, false),
				new UserNotifications("joan", 5, false),
				new UserNotifications("mike", 3, true),
				new UserNotifications("john", 2, true),
				new UserNotifications("joan", 4, true),
				new UserNotifications("mary", 4, true) };
	}

	/**
	 * Returns an {@link UsersNotificationsDataset} that should exist in the
	 * database.
	 *
	 * @return An {@link UsersNotificationsDataset} that should exist in the
	 *         database.
	 */
	public static UserNotifications existentUserNotifications() {
		return usersNotifications()[0];
	}
}
