package es.uvigo.esei.dgss.letta.domain.entities;

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
	private boolean readed = false;
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
	 *            {@link User} global variable
	 * @param notificationId
	 *            {@link Notification} global variable
	 * @param readed
	 *            indicates if the notification is readed or not
	 */
	public UserNotifications(String userId, int notificationId,
			boolean readed) {
		this.userId = userId;
		this.notificationId = notificationId;
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
	 * Setter method of user global variable
	 * 
	 * @param user
	 *            global variable
	 */
	public void setUser(User user) {
		this.user = user;
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
		this.notification = notification;
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
	 * Setter method of userId global variable
	 * 
	 * @param userId
	 *            global variable
	 */
	public void setUserId(String userId) {
		this.userId = userId;
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
		this.notificationId = notificationId;
	}

}
