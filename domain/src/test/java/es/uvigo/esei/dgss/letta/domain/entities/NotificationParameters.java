package es.uvigo.esei.dgss.letta.domain.entities;

import static org.apache.commons.lang3.StringUtils.repeat;

/**
 * 
 * @author Jesús Álvarez Casanova
 *
 */
public class NotificationParameters {

	// Disallow constructor
	private NotificationParameters() {
	}

	/**
	 * Returns a valid title {@link Notification}.
	 *
	 * @return a valid title {@link Notification}.
	 */
	public static String aTitle() {
		return "notification title";
	}

	/**
	 * Returns a valid body {@link Notification}.
	 *
	 * @return a valid body {@link Notification}.
	 */
	public static String aBody() {
		return "notification body";
	}

	/**
	 * Returns an existent title {@link Notification}.
	 *
	 * @return an existent title {@link Notification}.
	 */
	public static String existentTitle() {
		return "Notification Title 1";
	}

	/**
	 * Returns an existent body {@link Notification}.
	 *
	 * @return an existent body {@link Notification}.
	 */
	public static String existentBody() {
		return "Notification body example";
	}

	/**
	 * Returns an invalid title longer than the maximum valid length.
	 * 
	 * @return an invalid title longer than the maximum valid length.
	 */
	public final static String longTitle() {
		return repeat('A', 101);
	}

	/**
	 * Returns an invalid body longer than the maximum valid length.
	 * 
	 * @return an invalid body longer than the maximum valid length.
	 */
	public final static String longBody() {
		return repeat('A', 1001);
	}

	/**
	 * Returns an existent {@link Notification}
	 * 
	 * @return an existent {@link Notification}
	 */
	public final static Notification existentNotification() {
		return new Notification(existentTitle(), existentBody());
	}

}
