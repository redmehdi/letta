package es.uvigo.esei.dgss.letta.domain.entities;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.Validate.inclusiveBetween;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Jesús Álvarez Casanova
 *
 */
@Entity
@XmlAccessorType(XmlAccessType.FIELD)
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(length = 100, nullable = false)
	private String title;

	@Column(length = 1000, nullable = false)
	private String body;

	/**
	 * Default constructor
	 */
	public Notification() {
	}

	/**
	 * Constructor of {@link Notification}
	 *
	 * @param title
	 *            of the {@link Notification}
	 * @param body
	 *            of the {@link Notification}
	 */
	public Notification(String title, String body) {
		setTitle(title);
		setBody(body);
	}

	/**
	 * Returns the title of the {@link Notification}
	 *
	 * @return the title of the {@link Notification}
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of the {@link Notification}
	 *
	 * @param title
	 *            of the {@link Notification}
	 */
	public void setTitle(String title) {
		requireNonNull(title, "Notification's title cannot be null.");
		inclusiveBetween(1, 100, title.length(),
				"Notification's title length must be in the range [1, 100].");
		this.title = StringUtils.capitalize(title);
	}

	/**
	 * Returns the body of the {@link Notification}
	 *
	 * @return the body of the {@link Notification}
	 */
	public String getBody() {
		return body;
	}

	/**
	 * Sets the body of the {@link Notification}
	 *
	 * @param body
	 *            of the {@link Notification}
	 */
	public void setBody(String body) {
		requireNonNull(body, "Notification's body cannot be null.");
		inclusiveBetween(1, 1000, body.length(),
				"Notification's body length must be in the range [1, 1000].");
		this.body = StringUtils.capitalize(body);
	}

	/**
	 * Returns the id of the {@link Notification}
	 *
	 * @return the id of the {@link Notification}
	 */
	public int getId() {
		return id;
	}

	/**
	 * Adds an {@link UserNotifications} with an {@link User} and an
	 * {@link Notification}
	 *
	 * @param user
	 *            indicates the {@link User}
	 * @return a new notification.
	 */
	public UserNotifications addUserNotifications(User user) {
		UserNotifications userNotifications = new UserNotifications();
		userNotifications.setUser(user);
		userNotifications.setUserId(user.getLogin());
		userNotifications.setNotification(this);
		userNotifications.setNotificationId(this.getId());
		return userNotifications;
	}

}
