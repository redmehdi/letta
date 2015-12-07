package es.uvigo.esei.dgss.letta.rest;

import java.util.List;

import javax.ws.rs.core.Response;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.service.EventEJB;

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

import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.events;
import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.filterEvents;
import static es.uvigo.esei.dgss.letta.domain.entities.IsEqualToEvent.containsEventsListInOrder;
import static es.uvigo.esei.dgss.letta.http.util.HasHttpStatus.hasHttpStatus;

@RunWith(EasyMockRunner.class)
public class EventResourceUnitTest {

    @TestSubject
    private EventResource resource = new EventResource();

    @Mock
    private EventEJB eventsEJB;

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

        expect(eventsEJB.listByDate(0, events.size())).andReturn(events);
        replay(eventsEJB);

        final Response response = resource.list(1, events.size());
        verify(eventsEJB);

        assertThat(response, hasHttpStatus(OK));
        assertThatResponseContainsEvents(response, events);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testListReturnsEmptyListIfNoEventsInEJB() {
        expect(eventsEJB.listByDate(0, 20)).andReturn(emptyList());
        replay(eventsEJB);

        final Response response = resource.list(1, 20);
        verify(eventsEJB);

        assertThat(response, hasHttpStatus(OK));
        assertThat(response.getEntity(), is(instanceOf(List.class)));
        assertThat((List<Event>) response.getEntity(), is(empty()));
    }

    @Test
    public void testListRetrievesTheSpecifiedNumberOfEventsPerPage() {
        for (int pageSize = 1; pageSize <= 20; ++pageSize) {
            final List<Event> events = asList(events()).subList(0, pageSize);

            expect(eventsEJB.listByDate(0, pageSize)).andReturn(events);
            replay(eventsEJB);

            final Response response = resource.list(1, pageSize);
            verify(eventsEJB);
            reset(eventsEJB);

            assertThatResponseContainsEvents(response, events);
        }
    }

    @Test
    public void testListRetrievesTheSpecifiedPageNumber() {
        for (int pageNumber = 1; pageNumber < 5; ++pageNumber) {
            final int from = (pageNumber - 1) * 5;

            final List<Event> events = asList(events()).subList(from, from + 5);
            expect(eventsEJB.listByDate(from, 5)).andReturn(events);
            replay(eventsEJB);

            final Response response = resource.list(pageNumber, 5);
            verify(eventsEJB);
            reset(eventsEJB);

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

        expect(eventsEJB.listHighlighted()).andReturn(events);
        replay(eventsEJB);

        final Response response = resource.highlighted();
        verify(eventsEJB);

        assertThat(response, hasHttpStatus(OK));
        assertThatResponseContainsEvents(response, events);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testHighlightedReturnsEmptyListIfNoEventsInEJB() {
        expect(eventsEJB.listHighlighted()).andReturn(emptyList());
        replay(eventsEJB);

        final Response response = resource.highlighted();
        verify(eventsEJB);

        assertThat(response, hasHttpStatus(OK));
        assertThat(response.getEntity(), is(instanceOf(List.class)));
        assertThat((List<Event>) response.getEntity(), is(empty()));
    }

    @Test
    public void testSearchReturnsAValidListOfEvents() {
        final List<Event> events = asList(filterEvents(
            e -> e.getTitle().contains("music")
              || e.getShortDescription().contains("music")
        ));

        expect(eventsEJB.search("music", 0, events.size())).andReturn(events);
        replay(eventsEJB);

        final Response response = resource.search("music", 1, events.size());
        verify(eventsEJB);

        assertThat(response, hasHttpStatus(OK));
        assertThatResponseContainsEvents(response, events);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSearchReturnsEmptyListIfNoEventsInEJB() {
        expect(eventsEJB.search("asd", 0, 20)).andReturn(emptyList());
        replay(eventsEJB);

        final Response response = resource.search("asd", 1, 20);
        verify(eventsEJB);

        assertThat(response, hasHttpStatus(OK));
        assertThat(response.getEntity(), is(instanceOf(List.class)));
        assertThat((List<Event>) response.getEntity(), is(empty()));
    }

    @Test
    public void testSearchReturnsAllEventsWhenAnEmptyQueryIsExecuted() {
        final List<Event> events = asList(copyOf(events(), 20));

        expect(eventsEJB.search("", 0, 20)).andReturn(events);
        replay(eventsEJB);

        final Response response = resource.search("", 1, 20);
        verify(eventsEJB);

        assertThat(response, hasHttpStatus(OK));
        assertThatResponseContainsEvents(response, events);
    }

    @Test
    public void testSearchRetrievesTheSpecifiedNumberOfEventsPerPage() {
        for (int pageSize = 1; pageSize <= 10; ++pageSize) {
            final List<Event> events = asList(filterEvents(
                e -> e.getTitle().contains("Example")
            )).subList(0, pageSize);

            expect(eventsEJB.search("Example", 0, pageSize)).andReturn(events);
            replay(eventsEJB);

            final Response response = resource.search("Example", 1, pageSize);
            verify(eventsEJB);
            reset(eventsEJB);

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

            expect(eventsEJB.search("Example", from, 5)).andReturn(events);
            replay(eventsEJB);

            final Response response = resource.search("Example", pageNumber, 5);
            verify(eventsEJB);
            reset(eventsEJB);

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

}
