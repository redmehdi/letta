package es.uvigo.esei.dgss.letta.jsf;

import java.io.IOException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import javax.inject.Inject;

import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.domain.entities.UserNotifications;
import es.uvigo.esei.dgss.letta.service.UserEJB;

@ManagedBean(name = "userNotifications")
@SessionScoped
public class userNotifications {
	@Inject
	private UserEJB userEJB;
	
	/**
	 * Get a list of {@link UserNotifications}.
	 * @return a sorted list of {@link UserNotifications}.
	 */
	public List<UserNotifications> getNotifications() {
		List<UserNotifications> userNotifications = userEJB.getNotifications();
		Collections.sort(userNotifications, (c1, c2) -> c2.getNotificationId() - c1.getNotificationId());
		
		return  userNotifications;
	}
	
	/**
	 * Get the current {@link UserNotifications}
	 * @return a {@link UserNotifications}.
	 * @throws IOException
	 */
	public UserNotifications getCurrentNotification() throws IOException {
		Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		int notificationId = Integer.parseInt(params.get("notificationId"));

		UserNotifications userNotifications = userEJB.getNotification(notificationId);

		if (userNotifications == null) {
			FacesContext.getCurrentInstance().getExternalContext().redirect("m.xhtml");
		}
		
		return userNotifications;
	}
	
	/**
	 * Count the unread {@link UserNotifications} by the current {@link User}.
	 * @return a number of unread {@link UserNotifications}
	 */
	public Long countUnreadNotifications() {
		return userEJB.countUnreadNotifications();
	}
}
