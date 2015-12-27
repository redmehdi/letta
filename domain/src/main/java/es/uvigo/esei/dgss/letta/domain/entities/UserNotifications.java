package es.uvigo.esei.dgss.letta.domain.entities;

import static java.util.Objects.requireNonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "UserNotifications")
@IdClass(NotificationId.class)
public class UserNotifications {
	@Id
	private String userId;
	@Id
	private int notificationId;
	@Column(name = "readed")
	private boolean readed;
	@ManyToOne
	@JoinColumn(name = "userid", updatable = false, insertable = false, referencedColumnName = "login")
	private User user;
	@ManyToOne
	@JoinColumn(name = "notificationid", updatable = false, insertable = false, referencedColumnName = "id")
	private Notification notification;

	public UserNotifications() {
	}

	/**
	 * Constructor of {@link UserNotifications}
	 * 
	 * @param userId
	 *            global variable
	 * @param notificationId
	 *            global variable
	 */
	public UserNotifications(String userId, int notificationId,
			boolean readed) {
		setUserId(userId);
		setNotificationId(notificationId);
		this.readed = readed;
	}

	/**
	 * Getter method of readed global variable
	 * 
	 * @return readed global variable
	 */
	public boolean isReaded() {
		return readed;
	}

	/**
	 * Setter method of readed global variable
	 * 
	 * @param readed
	 *            global variable
	 */
	public void setReaded(boolean readed) {
		this.readed = readed;
	}

	/**
	 * Getter method of user global variable
	 * 
	 * @return user global variable
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Setter method of user global variable and userId global variable
	 * 
	 * @param user
	 *            global variable
	 */
	public void setUser(User user) {
		this.user = requireNonNull(user,
				"UserNotification's user cannot be null.");
	}

	/**
	 * Getter method of notification global variable
	 * 
	 * @return notification global variable
	 */
	public Notification getNotification() {
		return notification;
	}

	/**
	 * Setter method of notification global variable
	 * 
	 * @param notification
	 *            global variable
	 */
	public void setNotification(Notification notification) {
		this.notification = requireNonNull(notification,
				"UserNotification's notification cannot be null.");
	}

	/**
	 * Getter method of userId global variable
	 * 
	 * @return userId global variable
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Setter method of notificationId global variable
	 * 
	 * @param userId
	 *            global variable
	 */
	public void setUserId(String userId) {
		this.userId = requireNonNull(userId,
				"UserNotification's user login cannot be null.");
	}

	/**
	 * Getter method of notificationId global variable
	 * 
	 * @return notificationId global variable
	 */
	public int getNotificationId() {
		return notificationId;
	}

	/**
	 * Setter method of notificationId global variable
	 * 
	 * @param notificationId
	 *            global variable
	 */
	public void setNotificationId(int notificationId) {
		this.notificationId = requireNonNull(notificationId,
				"UserNotification's notificationId cannot be null");
	}

}
