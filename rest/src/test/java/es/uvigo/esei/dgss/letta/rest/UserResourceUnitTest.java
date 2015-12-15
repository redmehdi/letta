package es.uvigo.esei.dgss.letta.rest;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.service.EventEJB;
import es.uvigo.esei.dgss.letta.service.UserAuthorizationEJB;

import static java.util.Arrays.asList;

import static javax.ws.rs.core.Response.Status.OK;

import static org.easymock.EasyMock.expect;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.filterEvents;
import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.filterEventsWithTwoJoinedUsers;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.existentLogin;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.existentUser;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.userWithLogin;
import static es.uvigo.esei.dgss.letta.http.util.HasHttpStatus.hasHttpStatus;

@RunWith(EasyMockRunner.class)
public class UserResourceUnitTest extends EasyMockSupport {

	private static int EVENT_ID = 1;

	@TestSubject
	private UserResource resource = new UserResource();

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
		List<Event> eventsCreated = asList(filterEvents(
		    event -> event.getOwner().equals(existentUser())
		));

		expect(eventEJB.getEventsOwnedByCurrentUser()).andReturn(eventsCreated);
		expect(auth.getCurrentUser()).andReturn(userWithLogin(existentLogin()));
		replayAll();

		final Response response = resource
				.getEventsCreatedByUser(existentLogin());

		assertThat(response, hasHttpStatus(OK));
		assertThat(response.getEntity(), is(instanceOf(List.class)));
	}

	@Test
	public void testGetJoinedEvents() {
		List<Event> eventsJoined = asList(filterEventsWithTwoJoinedUsers(
		    event -> event.getAttendees().contains(existentUser())
		));

		expect(eventEJB.getAttendingEvents(0, Integer.MAX_VALUE))
				.andReturn(eventsJoined);
		expect(auth.getCurrentUser()).andReturn(userWithLogin(existentLogin()));
		replayAll();

        final Response response = resource.getEventsJoinedByUser(
            existentLogin(), 0, Integer.MAX_VALUE
        );

		assertThat(response, hasHttpStatus(OK));
		assertThat(response.getEntity(), is(instanceOf(List.class)));
	}

	@Test
	public void testJoinEvent() throws Exception {
		eventEJB.attendToEvent(EVENT_ID);
		expect(auth.getCurrentUser()).andReturn(userWithLogin(existentLogin()));
		replayAll();

		final Response response = resource.joinEvent(existentLogin(), EVENT_ID);

		assertThat(response, hasHttpStatus(OK));
	}
}
