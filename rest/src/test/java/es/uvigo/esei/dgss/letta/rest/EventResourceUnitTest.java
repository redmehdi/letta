package es.uvigo.esei.dgss.letta.rest;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.service.EventEJB;

import static java.util.Arrays.asList;
import static java.util.Arrays.copyOf;
import static java.util.Collections.emptyList;

import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.*;
import static es.uvigo.esei.dgss.letta.domain.matchers.IsEqualToEvent.containsEventsListInOrder;
import static es.uvigo.esei.dgss.letta.http.util.HasHttpStatus.hasHttpStatus;

@RunWith(EasyMockRunner.class)
public class EventResourceUnitTest extends EasyMockSupport {

    @TestSubject
    private EventResource resource = new EventResource();

    @Mock
    private EventEJB eventEJB;

    @Mock
    private UriBuilder uriBuilder;

    @Mock
    private UriInfo uriInfo;


    @SuppressWarnings("unchecked")
    private void assertThatResponseContainsEvents(
        final Response response, final List<Event> expected
    ) {
        assertThat(response.getEntity(), is(instanceOf(List.class)));
        assertThat(
            (List<Event>) response.getEntity(),
            containsEventsListInOrder(expected)
        );
    }

    @Test
    public void testListReturnsAValidListOfEvents() {
        final List<Event> events = asList(events());

        expect(eventEJB.listByDate(0, events.size())).andReturn(events);
        replayAll();

        final Response response = resource.list(1, events.size());
        verifyAll();

        assertThat(response, hasHttpStatus(OK));
        assertThatResponseContainsEvents(response, events);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testListReturnsEmptyListIfNoEventsInEJB() {
        expect(eventEJB.listByDate(0, 20)).andReturn(emptyList());
        replayAll();

        final Response response = resource.list(1, 20);
        verifyAll();

        assertThat(response, hasHttpStatus(OK));
        assertThat(response.getEntity(), is(instanceOf(List.class)));
        assertThat((List<Event>) response.getEntity(), is(empty()));
    }

    @Test
    public void testListRetrievesTheSpecifiedNumberOfEventsPerPage() {
        for (int pageSize = 1; pageSize <= 20; ++pageSize) {
            final List<Event> events = asList(events()).subList(0, pageSize);

            expect(eventEJB.listByDate(0, pageSize)).andReturn(events);
            replayAll();

            final Response response = resource.list(1, pageSize);
            verifyAll();
            resetAll();

            assertThatResponseContainsEvents(response, events);
        }
    }

    @Test
    public void testListRetrievesTheSpecifiedPageNumber() {
        for (int pageNumber = 1; pageNumber < 5; ++pageNumber) {
            final int from = (pageNumber - 1) * 5;

            final List<Event> events = asList(events()).subList(from, from + 5);
            expect(eventEJB.listByDate(from, 5)).andReturn(events);
            replayAll();

            final Response response = resource.list(pageNumber, 5);
            verifyAll();
            resetAll();

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
        replayAll();

        final Response response = resource.highlighted();
        verifyAll();

        assertThat(response, hasHttpStatus(OK));
        assertThatResponseContainsEvents(response, events);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testHighlightedReturnsEmptyListIfNoEventsInEJB() {
        expect(eventEJB.listHighlighted()).andReturn(emptyList());
        replayAll();

        final Response response = resource.highlighted();
        verifyAll();

        assertThat(response, hasHttpStatus(OK));
        assertThat(response.getEntity(), is(instanceOf(List.class)));
        assertThat((List<Event>) response.getEntity(), is(empty()));
    }

    @Test
    public void testSearchReturnsAValidListOfEvents() {
        final List<Event> events = asList(filterEvents(
            e -> e.getTitle().contains("music")
              || e.getSummary().contains("music")
        ));

        expect(eventEJB.search("music", 0, events.size())).andReturn(events);
        replayAll();

        final Response response = resource.search("music", 1, events.size());
        verifyAll();

        assertThat(response, hasHttpStatus(OK));
        assertThatResponseContainsEvents(response, events);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSearchReturnsEmptyListIfNoEventsInEJB() {
        expect(eventEJB.search("asd", 0, 20)).andReturn(emptyList());
        replayAll();

        final Response response = resource.search("asd", 1, 20);
        verifyAll();

        assertThat(response, hasHttpStatus(OK));
        assertThat(response.getEntity(), is(instanceOf(List.class)));
        assertThat((List<Event>) response.getEntity(), is(empty()));
    }

    @Test
    public void testSearchReturnsAllEventsWhenAnEmptyQueryIsExecuted() {
        final List<Event> events = asList(copyOf(events(), 20));

        expect(eventEJB.search("", 0, 20)).andReturn(events);
        replayAll();

        final Response response = resource.search("", 1, 20);
        verifyAll();

        assertThat(response, hasHttpStatus(OK));
        assertThatResponseContainsEvents(response, events);
    }

    @Test
    public void testSearchRetrievesTheSpecifiedNumberOfEventsPerPage() {
        for (int pageSize = 1; pageSize <= 10; ++pageSize) {
            final List<Event> events = asList(filterEvents(
                e -> e.getTitle().contains("Example")
            )).subList(0, pageSize);

            expect(eventEJB.search("Example", 0, pageSize)).andReturn(events);
            replayAll();

            final Response response = resource.search("Example", 1, pageSize);
            verifyAll();
            resetAll();

            assertThatResponseContainsEvents(response, events);
        }
    }

    @Test
    public void testSearchRetrievesTheSpecifiedPageNumber() {
        for (int pageNumber = 1; pageNumber < 5; ++pageNumber) {
            final int from = (pageNumber - 1) * 5;

            final List<Event> events = asList(filterEvents(
                e -> e.getTitle().contains("Example")
            )).subList(from, from + 5);

            expect(eventEJB.search("Example", from, 5)).andReturn(events);
            replayAll();

            final Response response = resource.search("Example", pageNumber, 5);
            verifyAll();
            resetAll();

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
        final Event event = existentEvent();

        expect(eventEJB.get(1)).andReturn(Optional.of(event));
        replayAll();

        final Response response = resource.get(event.getId());
        verifyAll();

        assertThat(response, hasHttpStatus(OK));
        assertThat(response.getEntity(), is(instanceOf(Event.class)));
    }

    @Test
    public void testGetNonExistentEventInfo() {
        final int id = nonExistentEventId();

        expect(eventEJB.get(id)).andReturn(Optional.empty());
        replayAll();

        final Response response = resource.get(id);
        verifyAll();

        assertThat(response, hasHttpStatus(NOT_FOUND));
    }


    @Test
    public void testJoinEvent() throws Exception {
        eventEJB.attendToEvent(existentEventId());
        expectLastCall();
        replayAll();

        final Response response = resource.attendToEvent(existentEventId());
        verifyAll();

        assertThat(response, hasHttpStatus(NO_CONTENT));
    }

    @Test
    public void testCreateEvent() throws Exception {
        final Event newEvent     = newEventWithoutOwner();
        final Event createdEvent = newEvent();

        final URI mockUri = new URI(
            "http://host/api/event/" + createdEvent.getId()
        );

        expect(eventEJB.createEvent(newEvent)).andReturn(createdEvent);
        expect(uriInfo.getAbsolutePathBuilder()).andReturn(uriBuilder);
        expect(uriBuilder.path("" + createdEvent.getId())).andReturn(uriBuilder);
        expect(uriBuilder.build()).andReturn(mockUri);
        replayAll();

        final Response response = resource.create(newEvent);
        verifyAll();

        assertThat(response, hasHttpStatus(CREATED));
        assertThat(
            response.getHeaderString("Location"),
            is(equalTo(mockUri.toString()))
        );
    }

    @Test
    public void testModifyEvent() throws Exception {
        final Event updated = existentEvent();
        updated.setTitle("New title");

        final URI mockUri = new URI("http://host/api/event/" + updated.getId());

        eventEJB.modifyEvent(updated);
        expectLastCall();
        expect(uriInfo.getAbsolutePathBuilder()).andReturn(uriBuilder);
        expect(uriBuilder.path("" + updated.getId())).andReturn(uriBuilder);
        expect(uriBuilder.build()).andReturn(mockUri);
        replayAll();

        final Response response = resource.update(existentEventId(), updated);
        verifyAll();

        assertThat(response, hasHttpStatus(OK));
    }

}
