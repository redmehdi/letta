package es.uvigo.esei.dgss.letta.domain.entities;

import java.io.Serializable;

public class NotificationId implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;

	private int notificationId;

	/**
	 * Hashcode method of {@link NotificationId}
	 */
	public int hashCode() {
		return (int) (Integer.parseInt(userId) + notificationId);
	}

	/**
	 * Equals method of {@link NotificationId}
	 */
	public boolean equals(Object object) {
		if (object instanceof NotificationId) {
			NotificationId otherId = (NotificationId) object;
			return (otherId.userId == this.userId)
					&& (otherId.notificationId == this.notificationId);
		}
		return false;
	}
}
