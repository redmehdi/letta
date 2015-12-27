package es.uvigo.esei.dgss.letta.domain.entities;

import static es.uvigo.esei.dgss.letta.domain.entities.NotificationParameters.aBody;
import static es.uvigo.esei.dgss.letta.domain.entities.NotificationParameters.aTitle;
import static es.uvigo.esei.dgss.letta.domain.entities.NotificationParameters.existentBody;
import static es.uvigo.esei.dgss.letta.domain.entities.NotificationParameters.existentNotification;
import static es.uvigo.esei.dgss.letta.domain.entities.NotificationParameters.existentTitle;
import static es.uvigo.esei.dgss.letta.domain.entities.NotificationParameters.longBody;
import static es.uvigo.esei.dgss.letta.domain.entities.NotificationParameters.longTitle;
import static es.uvigo.esei.dgss.letta.domain.entities.UserParameters.validUser;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class NotificationTest {

	@Test
	public void testConstructor() {
		final Notification notification = new Notification(existentTitle(),
				existentBody());
		assertThat(notification.getTitle(), is(equalTo(existentTitle())));
		assertThat(notification.getBody(), is(equalTo(existentBody())));
	}

	@Test
	public void testDefaultConstructor() {
		new Notification();
	}

	@Test(expected = NullPointerException.class)
	public void testConstructorNullTitle() {
		new Notification(null, aBody());
	}

	@Test(expected = NullPointerException.class)
	public void testConstructorNullBody() {
		new Notification(aTitle(), null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testLongTitle() {
		new Notification(longTitle(), aBody());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testLongBody() {
		new Notification(aTitle(), longBody());
	}

	@Test
	public void testGetTitle() {
		final Notification notification = existentNotification();
		assertThat(notification.getTitle(), is(equalTo(existentTitle())));
	}

	@Test
	public void testGetBody() {
		final Notification notification = existentNotification();
		assertThat(notification.getBody(), is(equalTo(existentBody())));
	}

	@Test
	public void testAddUserNotifications() {
		final User user = validUser();
		final UserNotifications userNotification = existentNotification()
				.addUserNotifications(user);
		assertThat(userNotification.getUser(), is(equalTo(user)));
		assertThat(userNotification.getUserId(), is(equalTo(user.getLogin())));
	}
}
