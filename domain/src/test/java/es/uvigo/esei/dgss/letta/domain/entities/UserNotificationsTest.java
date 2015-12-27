package es.uvigo.esei.dgss.letta.domain.entities;

import static es.uvigo.esei.dgss.letta.domain.entities.NotificationParameters.existentNotification;
import static es.uvigo.esei.dgss.letta.domain.entities.UserNotificationsParameters.existentUserNotification;
import static es.uvigo.esei.dgss.letta.domain.entities.UserParameters.aLogin;
import static es.uvigo.esei.dgss.letta.domain.entities.UserParameters.validUser;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class UserNotificationsTest {

	@Test
	public void testConstructor() {
		final Notification notification = existentNotification();
		final User user = validUser();
		final UserNotifications userNotifications = new UserNotifications(
				validUser().getLogin(), existentNotification().getId(), false);
		assertThat(userNotifications.getNotificationId(),
				is(equalTo(notification.getId())));
		assertThat(userNotifications.getUserId(), is(equalTo(user.getLogin())));
	}

	@Test
	public void testDefaultConstructor() {
		new UserNotifications();
	}

	@Test(expected = NullPointerException.class)
	public void testConstructorNullUserLogin() {
		new UserNotifications(null, existentNotification().getId(), false);
	}

	@Test
	public void testGetUserId() {
		final UserNotifications userNotification = existentUserNotification();
		assertThat(userNotification.getUserId(), is(equalTo(aLogin())));
	}

	@Test
	public void testGetNotificationId() {
		final UserNotifications userNotification = existentUserNotification();
		assertThat(userNotification.getNotificationId(),
				is(equalTo(existentNotification().getId())));
	}

	@Test
	public void testGetReaded() {
		final UserNotifications userNotification = existentUserNotification();
		assertThat(userNotification.isReaded(), is(equalTo(false)));
	}

	@Test
	public void testSetReaded() {
		final UserNotifications userNotification = existentUserNotification();
		assertThat(userNotification.isReaded(), is(equalTo(false)));
		userNotification.setReaded(true);
		assertThat(userNotification.isReaded(), is(equalTo(true)));
	}

	@Test
	public void testGetUser() {
		final UserNotifications userNotification = new UserNotifications();
		userNotification.setUser(validUser());
		assertThat(userNotification.getUser(), is(equalTo(validUser())));
	}

	@Test
	public void testGetNotification() {
		final UserNotifications userNotification = new UserNotifications();
		final Notification notification = existentNotification();
		userNotification.setNotification(notification);
		assertThat(userNotification.getNotification(),
				is(equalTo(notification)));
	}

	@Test
	public void testSetUser() {
		final UserNotifications userNotification = new UserNotifications();
		userNotification.setUser(validUser());
		User user = userNotification.getUser();
		assertThat(userNotification.getUser(), is(equalTo(user)));
	}
}
