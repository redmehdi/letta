package es.uvigo.esei.dgss.letta.domain.entities;

public class NotificationsDataset {

	/**
	 * Returns all the {@link Notification} in the database.
	 * 
	 * @return all the {@link Notification} in the database.
	 */
	public static Notification[] notifications() {
		return new Notification[] {
				new Notification("Notification Title 1",
						"Notification body example"),
				new Notification("Notification Title 2",
						"Notification body example"),
				new Notification("Notification Title 3",
						"Notification body example"),
				new Notification("Notification Title 4",
						"Notification body example"),
				new Notification("Notification Title 5",
						"Notification body example"),
				new Notification("Notification Title 6",
						"Notification body example") };
	}

	/**
	 * Returns an {@link Notification} that should exist in the database.
	 *
	 * @return An {@link Notification} that should exist in the database.
	 */
	public static Notification existentEvent() {
		return notifications()[0];
	}
}
