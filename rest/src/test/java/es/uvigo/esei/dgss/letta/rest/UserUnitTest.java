package es.uvigo.esei.dgss.letta.rest;

import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.events;
import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.eventsWithTwoJoinedUsers;
import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.eventsWithUserJoined;
import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.existentEvent;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.userWithLogin;
import static es.uvigo.esei.dgss.letta.http.util.HasHttpStatus.hasHttpStatus;
import static javax.ws.rs.core.Response.Status.OK;
import static org.easymock.EasyMock.expect;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.EventsDataset;
import es.uvigo.esei.dgss.letta.service.EventEJB;
import es.uvigo.esei.dgss.letta.service.UserAuthorizationEJB;

@RunWith(EasyMockRunner.class)
public class UserUnitTest extends EasyMockSupport {

	private static final String EXISTENT_LOGIN = "anne";
	private static int EVENT_ID = 1;

	@TestSubject
	private UserPrivateRest resource = new UserPrivateRest();

	@Mock
	private EventEJB eventEJB;

	@Mock
	private UriBuilder uriBuilder;

	@Mock
	private UserAuthorizationEJB auth;

	@After
	public void tearDown() throws Exception {
		verifyAll();
	}

	@Test
	public void testGetOwnEvents() {
		List<Event> eventsCreated = eventsWithUserJoined(EXISTENT_LOGIN);
		expect(eventEJB.getEventsCreatedByOwnerUser()).andReturn(eventsCreated);
		expect(auth.getCurrentUser()).andReturn(userWithLogin(EXISTENT_LOGIN));
		replayAll();

		final Response response = resource
				.getEventsCreatedByUser(EXISTENT_LOGIN);

		assertThat(response, hasHttpStatus(OK));
		assertThat(response.getEntity(), is(instanceOf(List.class)));
	}

	@Test
	public void testGetJoinedEvents() {
		List<Event> eventsJoined = eventsWithUserJoined(EXISTENT_LOGIN);
		expect(eventEJB.getEventsJoinedByUser(0, Integer.MAX_VALUE))
				.andReturn(eventsJoined);
		expect(auth.getCurrentUser()).andReturn(userWithLogin(EXISTENT_LOGIN));
		replayAll();

		final Response response = resource.getEventsJoinedByUser(EXISTENT_LOGIN,
				0, Integer.MAX_VALUE);

		assertThat(response, hasHttpStatus(OK));
		assertThat(response.getEntity(), is(instanceOf(List.class)));
	}

	@Test
	public void testJoinEvent() throws Exception {
		eventEJB.registerToEvent(EVENT_ID);
		expect(auth.getCurrentUser()).andReturn(userWithLogin(EXISTENT_LOGIN));
		replayAll();

		final Response response = resource.joinEvent(EXISTENT_LOGIN, EVENT_ID);

		assertThat(response, hasHttpStatus(OK));
	}
}
