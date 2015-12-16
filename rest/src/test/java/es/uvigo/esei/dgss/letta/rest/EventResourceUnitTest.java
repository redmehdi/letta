package es.uvigo.esei.dgss.letta.rest;

import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.events;
import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.filterEvents;
import static es.uvigo.esei.dgss.letta.domain.matchers.IsEqualToEvent.containsEventsListInOrder;
import static es.uvigo.esei.dgss.letta.http.util.HasHttpStatus.hasHttpStatus;
import static java.util.Arrays.asList;
import static java.util.Arrays.copyOf;
import static java.util.Collections.emptyList;
import static javax.ws.rs.core.Response.Status.OK;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.ws.rs.core.Response;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.EventsDataset;
import es.uvigo.esei.dgss.letta.service.EventEJB;

@RunWith(EasyMockRunner.class)
public class EventResourceUnitTest extends EasyMockSupport {

	@TestSubject
	private EventResource resource = new EventResource();

	@Mock
	private EventEJB eventEJB;


	@SuppressWarnings("unchecked")
	private void assertThatResponseContainsEvents(final Response response,
			final List<Event> expected) {
		assertThat(response.getEntity(), is(instanceOf(List.class)));
		assertThat((List<Event>) response.getEntity(),
				containsEventsListInOrder(expected));
	}

	@Test
	public void testListReturnsAValidListOfEvents() {
		final List<Event> events = asList(events());

		expect(eventEJB.listByDate(0, events.size())).andReturn(events);
		replay(eventEJB);

		final Response response = resource.list(1, events.size());
		verify(eventEJB);

		assertThat(response, hasHttpStatus(OK));
		assertThatResponseContainsEvents(response, events);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testListReturnsEmptyListIfNoEventsInEJB() {
		expect(eventEJB.listByDate(0, 20)).andReturn(emptyList());
		replay(eventEJB);

		final Response response = resource.list(1, 20);
		verify(eventEJB);

		assertThat(response, hasHttpStatus(OK));
		assertThat(response.getEntity(), is(instanceOf(List.class)));
		assertThat((List<Event>) response.getEntity(), is(empty()));
	}

	@Test
	public void testListRetrievesTheSpecifiedNumberOfEventsPerPage() {
		for (int pageSize = 1; pageSize <= 20; ++pageSize) {
			final List<Event> events = asList(events()).subList(0, pageSize);

			expect(eventEJB.listByDate(0, pageSize)).andReturn(events);
			replay(eventEJB);

			final Response response = resource.list(1, pageSize);
			verify(eventEJB);
			reset(eventEJB);

			assertThatResponseContainsEvents(response, events);
		}
	}

	@Test
	public void testListRetrievesTheSpecifiedPageNumber() {
		for (int pageNumber = 1; pageNumber < 5; ++pageNumber) {
			final int from = (pageNumber - 1) * 5;

			final List<Event> events = asList(events()).subList(from, from + 5);
			expect(eventEJB.listByDate(from, 5)).andReturn(events);
			replay(eventEJB);

			final Response response = resource.list(pageNumber, 5);
			verify(eventEJB);
			reset(eventEJB);

			assertThatResponseContainsEvents(response, events);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testListThrowsIllegalArgumentExceptionWhenPageLessThanOneIsGiven() {
		resource.list(nextInt(Integer.MIN_VALUE, 1), 5);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testListThrowsIllegalArgumentExceptionWhenPageSizeLessThanZeroIsGiven() {
		resource.list(1, nextInt(Integer.MIN_VALUE, 0));
	}

	@Test
	public void testHighlightedReturnsAValidListOfEvents() {
		final List<Event> events = asList(copyOf(events(), 5));

		expect(eventEJB.listHighlighted()).andReturn(events);
		replay(eventEJB);

		final Response response = resource.highlighted();
		verify(eventEJB);

		assertThat(response, hasHttpStatus(OK));
		assertThatResponseContainsEvents(response, events);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testHighlightedReturnsEmptyListIfNoEventsInEJB() {
		expect(eventEJB.listHighlighted()).andReturn(emptyList());
		replay(eventEJB);

		final Response response = resource.highlighted();
		verify(eventEJB);

		assertThat(response, hasHttpStatus(OK));
		assertThat(response.getEntity(), is(instanceOf(List.class)));
		assertThat((List<Event>) response.getEntity(), is(empty()));
	}

	@Test
	public void testSearchReturnsAValidListOfEvents() {
		final List<Event> events = asList(
				filterEvents(e -> e.getTitle().contains("music")
						|| e.getSummary().contains("music")));

		expect(eventEJB.search("music", 0, events.size())).andReturn(events);
		replay(eventEJB);

		final Response response = resource.search("music", 1, events.size());
		verify(eventEJB);

		assertThat(response, hasHttpStatus(OK));
		assertThatResponseContainsEvents(response, events);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testSearchReturnsEmptyListIfNoEventsInEJB() {
		expect(eventEJB.search("asd", 0, 20)).andReturn(emptyList());
		replay(eventEJB);

		final Response response = resource.search("asd", 1, 20);
		verify(eventEJB);

		assertThat(response, hasHttpStatus(OK));
		assertThat(response.getEntity(), is(instanceOf(List.class)));
		assertThat((List<Event>) response.getEntity(), is(empty()));
	}

	@Test
	public void testSearchReturnsAllEventsWhenAnEmptyQueryIsExecuted() {
		final List<Event> events = asList(copyOf(events(), 20));

		expect(eventEJB.search("", 0, 20)).andReturn(events);
		replay(eventEJB);

		final Response response = resource.search("", 1, 20);
		verify(eventEJB);

		assertThat(response, hasHttpStatus(OK));
		assertThatResponseContainsEvents(response, events);
	}

	@Test
	public void testSearchRetrievesTheSpecifiedNumberOfEventsPerPage() {
		for (int pageSize = 1; pageSize <= 10; ++pageSize) {
			final List<Event> events = asList(
					filterEvents(e -> e.getTitle().contains("Example")))
							.subList(0, pageSize);

			expect(eventEJB.search("Example", 0, pageSize)).andReturn(events);
			replay(eventEJB);

			final Response response = resource.search("Example", 1, pageSize);
			verify(eventEJB);
			reset(eventEJB);

			assertThatResponseContainsEvents(response, events);
		}
	}

	@Test
	public void testSearchRetrievesTheSpecifiedPageNumber() {
		for (int pageNumber = 1; pageNumber < 5; ++pageNumber) {
			final int from = (pageNumber - 1) * 5;

			final List<Event> events = asList(
					filterEvents(e -> e.getTitle().contains("Example")))
							.subList(from, from + 5);

			expect(eventEJB.search("Example", from, 5)).andReturn(events);
			replay(eventEJB);

			final Response response = resource.search("Example", pageNumber, 5);
			verify(eventEJB);
			reset(eventEJB);

			assertThatResponseContainsEvents(response, events);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSearchThrowsIllegalArgumentExceptionWhenPageLessThanOneIsGiven() {
		resource.search(random(5), nextInt(Integer.MIN_VALUE, 1), 5);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSearchThrowsIllegalArgumentExceptionWhenPageSizeLessThanZeroIsGiven() {
		resource.search(random(5), 1, nextInt(Integer.MIN_VALUE, 0));
	}

	@Test
	public void testGetEventInfo() {
		final Event event = EventsDataset.existentEvent();

		expect(eventEJB.getEvent(1)).andReturn(event);
		replayAll();

		final Response response = resource.getEventInfo(event.getId());
		verify(eventEJB);
		reset(eventEJB);

		assertThat(response, hasHttpStatus(OK));
		assertThat(response.getEntity(), is(instanceOf(Event.class)));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetNonExistentEventInfo() {
		final int eventId = EventsDataset.existentEventId();

		expect(eventEJB.getEvent(eventId)).andReturn(null);

		replayAll();

		resource.getEventInfo(eventId);
	}

}
