package es.uvigo.esei.dgss.letta.rest;

import java.net.URL;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.uvigo.esei.dgss.letta.domain.entities.Event;

import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.Response.Status.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.*;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.existentLogin;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.nonExistentUser;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.passwordFor;
import static es.uvigo.esei.dgss.letta.domain.matchers.IsEqualToEvent.containsEventsInAnyOrder;
import static es.uvigo.esei.dgss.letta.domain.matchers.IsEqualToEvent.equalToEvent;
import static es.uvigo.esei.dgss.letta.http.util.HasHttpStatus.hasHttpStatus;
import static es.uvigo.esei.dgss.letta.rest.util.RestIntegrationTestBuilder.deployment;
import static es.uvigo.esei.dgss.letta.rest.util.RestIntegrationTestUtils.asEventList;
import static es.uvigo.esei.dgss.letta.rest.util.RestIntegrationTestUtils.asUserSet;
import static es.uvigo.esei.dgss.letta.rest.util.RestIntegrationTestUtils.buildResourceTarget;
import static es.uvigo.esei.dgss.letta.rest.util.RestIntegrationTestUtils.getAuthHeaderContent;

@RunWith(Arquillian.class)
@Cleanup(phase = TestExecutionPhase.NONE)
public class EventResourceRestTest {

    @ArquillianResource
    private URL deploymentURL;

    @Deployment
    public static Archive<WebArchive> deploy() {
        return deployment().withClasses(
            EventResource.class
        ).build();
    }

    private WebTarget eventTarget() {
        return buildResourceTarget(deploymentURL, EventResource.class);
    }

    private WebTarget eventTarget(final int id) {
        return eventTarget().path("" + id);
    }

    @Test
    @InSequence(10) // test 1, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestGetReturnsTheEventWithTheGivenId() { }

    @Test
    @RunAsClient
    @InSequence(11) // test 1, sequence 1
    public void testGetReturnsTheEventWithTheGivenId() {
        final Event event = existentEvent();

        final Response res = eventTarget(event.getId()).request().get();

        assertThat(res, hasHttpStatus(OK));
        assertThat(res.readEntity(Event.class), is(equalToEvent(event)));
    }

    @Test
    @InSequence(12) // test 1, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestGetReturnsTheEventWithTheGivenId() { }


    @Test
    @InSequence(20) // test 2, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestGetWithNonExistentIdReturnsANotFoundStatus() { }

    @Test
    @RunAsClient
    @InSequence(21) // test 2, sequence 1
    public void testGetWithNonExistentIdReturnsANotFoundStatus() {
        final Response res = eventTarget(nonExistentEventId()).request().get();
        assertThat(res, hasHttpStatus(NOT_FOUND));
    }

    @Test
    @InSequence(22) // test 2, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestGetWithNonExistentIdReturnsANotFoundStatus() { }


    @Test
    @InSequence(30) // test 3, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestListWithDefaultArguments() { }

    @Test
    @RunAsClient
    @InSequence(31) // test 3, sequence 1
    public void testListWithDefaultArguments() {
        final Response res = eventTarget().request().get();;
        assertThat(res, hasHttpStatus(OK));

        // FIXME: cannot correctly test order of events because all events in
        // the events datasets have the same date.
        //
        // final Event[] events = copyOf(sortedEvents(Event::getDate), 20);
        // assertThat(
        //     response.readEntity(asEventList),
        //     IsEqualToEvent.containsEventsInOrder(events)
        // );

        assertThat(res.readEntity(asEventList), hasSize(5));
    }

    @Test
    @InSequence(32) // test 3, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestListWithDefaultArguments() { }


    @Test
    @InSequence(40) // test 4, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestListNonDefaultValidArguments() { }

    @Test
    @RunAsClient
    @InSequence(41) // test 4, sequence 1
    public void testListNonDefaultValidArguments() {
        // valid page numbers
        for (int page = 1; page <= 4; page += 1) {
            final Response res = eventTarget()
                .queryParam("page", page).queryParam("size", 5)
                .request().get();

            assertThat(res, hasHttpStatus(OK));
			if (page == 1) {
				assertThat(res.readEntity(asEventList), hasSize(5));
			} else {
				assertThat(res.readEntity(asEventList), hasSize(0));
			}
        }

        // valid page sizes
        for (int size = 0; size <= 20; size += 5) {
            final Response res = eventTarget()
                .queryParam("page", 1).queryParam("size", size)
                .request().get();

            assertThat(res, hasHttpStatus(OK));
			if (size == 0) {
				assertThat(res.readEntity(asEventList), hasSize(0));
			} else {
				assertThat(res.readEntity(asEventList), hasSize(5));
			}
        }
    }

    @Test
    @InSequence(42) // test 4, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestListNonDefaultValidArguments() { }


    @Test
    @InSequence(50) // test 5, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestListWithInvalidPageNumber() { }

    @Test
    @RunAsClient
    @InSequence(51) // test 5, sequence 1
    public void testListWithInvalidPageNumber() {
        final Response res1 = eventTarget().queryParam("page",  0).request().get();
        final Response res2 = eventTarget().queryParam("page", -1).request().get();
        final Response res3 = eventTarget().queryParam("page", -5).request().get();

        assertThat(res1, hasHttpStatus(BAD_REQUEST));
        assertThat(res2, hasHttpStatus(BAD_REQUEST));
        assertThat(res3, hasHttpStatus(BAD_REQUEST));
    }

    @Test
    @InSequence(52) // test 5, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestListWithInvalidPageNumber() { }


    @Test
    @InSequence(60) // test 6, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestListWithInvalidPageSize() { }

    @Test
    @RunAsClient
    @InSequence(61) // test 6, sequence 1
    public void testListWithInvalidPageSize() {
        final Response res1 = eventTarget().queryParam("size", -1).request().get();
        final Response res2 = eventTarget().queryParam("size", -2).request().get();
        final Response res3 = eventTarget().queryParam("size", -5).request().get();

        assertThat(res1, hasHttpStatus(BAD_REQUEST));
        assertThat(res2, hasHttpStatus(BAD_REQUEST));
        assertThat(res3, hasHttpStatus(BAD_REQUEST));
    }

    @Test
    @InSequence(62) // test 6, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestListWithInvalidPageSize() { }


    @Test
    @InSequence(70) // test 7, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestHighlighted() { }

    @Test
    @RunAsClient
    @InSequence(71) // test 7, sequence 1
    public void testHighlighted() {
        final Response res = eventTarget().path("highlighted").request().get();

        assertThat(res, hasHttpStatus(OK));
        assertThat(res.readEntity(asEventList), hasSize(5));
    }

    @Test
    @InSequence(72) // test 7, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestHighlighted() { }


    
    @Test
    @InSequence(800)
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestAdvancedSearchWithDefaultArguments() { }

    
    
    @Test
    @RunAsClient
    @InSequence(801) 
    public void testAdvancedSearchWithDefaultArguments() {
        final Response response = eventTarget().path("advanced_search").request().get();
        assertThat(response, hasHttpStatus(OK));
        assertThat(response.readEntity(asEventList), hasSize(2));
    }
    
    @Test
    @InSequence(802) 
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestAdvancedSearchWithDefaultArguments() { }
    
    
    @Test
    @InSequence(803)
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestAdvancedSearchWithNonDefaultArguments() { }

    
    
    @Test
    @RunAsClient
    @InSequence(804) 
    public void testAdvancedSearchWithNonDefaultArguments() {
       // final Response response = eventTarget().path("advanced_search").request().get();
        final Response res = eventTarget().path("advanced_search")
                .queryParam("category", "TELEVISION")
                .request().get();
        assertThat(res, hasHttpStatus(OK));
        assertThat(res.readEntity(asEventList), hasSize(2));
    }
    
    @Test
    @InSequence(805) // test 8, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestAdvancedSearchWithNonDefaultArguments() { }
    
    
    
    
    
    
    @Test
    @InSequence(80) // test 8, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestSearchWithDefaultArguments() { }

    @Test
    @RunAsClient
    @InSequence(81) // test 8, sequence 1
    public void testSearchWithDefaultArguments() {
        final Response response = eventTarget().path("search").request().get();

        assertThat(response, hasHttpStatus(OK));
        assertThat(response.readEntity(asEventList), hasSize(5));
    }

    @Test
    @InSequence(82) // test 8, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestSearchWithDefaultArguments() { }


    @Test
    @InSequence(90) // test 9, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestSearchWithNonDefaultValidArguments() { }

    @Test
    @RunAsClient
    @InSequence(91) // test 9, sequence 1
    public void testSearchWithNonDefaultValidArguments() {
        // valid page numbers
        for (int page = 1; page <= 4; page += 1) {
            final Response res = eventTarget().path("search")
                .queryParam("page", page).queryParam("size", 5)
                .request().get();

            assertThat(res, hasHttpStatus(OK));
			if (page == 1) {
				assertThat(res.readEntity(asEventList), hasSize(5));
			} else {
				assertThat(res.readEntity(asEventList), hasSize(0));
			}
        }

        // valid page sizes
        for (int size = 0; size <= 20; size += 5) {
            final Response res = eventTarget().path("search")
                .queryParam("page", 1).queryParam("size", size)
                .request().get();

            assertThat(res, hasHttpStatus(OK));
			if (size == 0) {
				assertThat(res.readEntity(asEventList), hasSize(0));
			} else {
				assertThat(res.readEntity(asEventList), hasSize(5));
			}
        }
    }

    @Test
    @InSequence(92) // test 9, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestSearchWithNonDefaultValidArguments() { }


    @Test
    @InSequence(100) // test 10, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestSearchReturnsMatchingListOfEvents() { }

    @Test
    @RunAsClient
    @InSequence(101) // test 10, sequence 1
    public void testSearchReturnsMatchingListOfEvents() {
        final String query = "literature";

        final Event[] events = filterEvents(
            e -> !e.isCancelled()
              && (e.getTitle().toLowerCase().contains(query.toLowerCase())
              || e.getSummary().toLowerCase().contains(query.toLowerCase())
              || e.getDescription().toLowerCase().contains(query.toLowerCase()))
         );

        final Response res = eventTarget().path("search")
            .queryParam("query", query).queryParam("size", events.length)
            .request().get();


        assertThat(res, hasHttpStatus(OK));
        assertThat(
            res.readEntity(asEventList),
            containsEventsInAnyOrder(events)
        );
    }

    @Test
    @InSequence(102) // test 10, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestSearchReturnsMatchingListOfEvents() { }


    @Test
    @InSequence(110) // test 11, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestSearchWithInvalidPageSize() { }

    @Test
    @RunAsClient
    @InSequence(111) // test 11, sequence 1
    public void testSearchWithInvalidPageSize() {
        final Response res1 = eventTarget().path("search").queryParam("size", -1).request().get();
        final Response res2 = eventTarget().path("search").queryParam("size", -2).request().get();
        final Response res3 = eventTarget().path("search").queryParam("size", -5).request().get();

        assertThat(res1, hasHttpStatus(BAD_REQUEST));
        assertThat(res2, hasHttpStatus(BAD_REQUEST));
        assertThat(res3, hasHttpStatus(BAD_REQUEST));
    }

    @Test
    @InSequence(112) // test 11, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestSearchWithInvalidPageSize() { }


    @Test
    @InSequence(120) // test 12, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestSearchWithInvalidPageNumber() { }

    @Test
    @RunAsClient
    @InSequence(121) // test 12, sequence 1
    public void testSearchWithInvalidPageNumber() {
        final Response res1 = eventTarget().path("search").queryParam("page",  0).request().get();
        final Response res2 = eventTarget().path("search").queryParam("page", -1).request().get();
        final Response res3 = eventTarget().path("search").queryParam("page", -5).request().get();

        assertThat(res1, hasHttpStatus(BAD_REQUEST));
        assertThat(res2, hasHttpStatus(BAD_REQUEST));
        assertThat(res3, hasHttpStatus(BAD_REQUEST));
    }

    @Test
    @InSequence(122) // test 12, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestSearchWithInvalidPageNumber() { }


    @Test
    @InSequence(130) // test 13, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestCreateEventCorrectlyCreatesAnEvent() { }

    @Test
    @RunAsClient
    @InSequence(131) // test 13, sequence 1
    public void testCreateEventCorrectlyCreatesAnEvent() {
        final String login = existentLogin();
        final String token = getAuthHeaderContent(login, passwordFor(login));

        final Builder  req = eventTarget().request().header(AUTHORIZATION, token);
        final Response res = req.post(json(newEventWithoutOwner()));

        assertThat(res, hasHttpStatus(CREATED));
    }

    @Test
    @InSequence(132) // test 13, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml", "events-create.xml" })
    public void afterTestCreateEventCorrectlyCreatesAnEvent() { }


    @Test
    @InSequence(140) // test 14, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestCreateReturnsUnauthorizedWithInvalidAuthorization() { }

    @Test
    @RunAsClient
    @InSequence(141) // test 14, sequence 1
    public void testCreateReturnsUnauthorizedWithtInvalidAuthorization() {
        final String login = existentLogin();
        final String token = getAuthHeaderContent(login, "invalid_password");

        final Builder  req = eventTarget().request().header(AUTHORIZATION, token);
        final Response res = req.post(json(newEventWithoutOwner()));

        assertThat(res, hasHttpStatus(UNAUTHORIZED));
    }

    @Test
    @InSequence(142) // test 14, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestCreateReturnsUnauthorizedWithInvalidAuthorization() { }


    @Test
    @InSequence(150) // test 15, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestCreateReturnsUnauthorizedWithNonExistentUser() { }

    @Test
    @RunAsClient
    @InSequence(151) // test 15, sequence 1
    public void testCreateReturnsUnauthorizedWithNonExistentUser() {
        final String login = nonExistentUser().getLogin();
        final String token = getAuthHeaderContent(login, passwordFor(login));

        final Builder  req = eventTarget().request().header(AUTHORIZATION, token);
        final Response res = req.post(json(newEventWithoutOwner()));

        assertThat(res, hasHttpStatus(UNAUTHORIZED));
    }

    @Test
    @InSequence(152) // test 15, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestCreateReturnsUnauthorizedWithNonExistentUser() { }


    @Test
    @InSequence(160) // test 16, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestUpdateEventCorrectlyUpdatesAnEvent() { }

    @Test
    @RunAsClient
    @InSequence(161) // test 16, sequence 1
    public void testUpdateEventCorrectlyUpdatesAnEvent() {
        final Event  event = updatedEvent();
        final String login = event.getOwner().getLogin();
        final String token = getAuthHeaderContent(login, passwordFor(login));

        final Builder  req = eventTarget(event.getId()).request();
        final Response res = req.header(AUTHORIZATION, token).put(json(event));

        assertThat(res, hasHttpStatus(OK));
    }

    @Test
    @InSequence(162) // test 16, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events-modified.xml" })
    public void afterTestUpdateEventCorrectlyUpdatesAnEvent() { }


    @Test
    @InSequence(170) // test 17, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestUpdateReturnsBadRequestWithUnmatchedPathAndEntityId() { }

    @Test
    @RunAsClient
    @InSequence(171) // test 17, sequence 1
    public void testUpdateReturnsBadRequestWithUnmatchedPathAndEntityId() {
        final Event  event = updatedEvent();
        final String login = event.getOwner().getLogin();
        final String token = getAuthHeaderContent(login, passwordFor(login));

        final Builder  req = eventTarget(event.getId() - 1).request();
        final Response res = req.header(AUTHORIZATION, token).put(json(event));

        assertThat(res, hasHttpStatus(BAD_REQUEST));
    }

    @Test
    @InSequence(172) // test 17, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestUpdateReturnsBadRequestWithUnmatchedPathAndEntityId() { }


    @Test
    @InSequence(180) // test 18, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestUpdateReturnsUnauthorizedWithoutValidAuthorization() { }

    @Test
    @RunAsClient
    @InSequence(181) // test 18, sequence 1
    public void testUpdateReturnsUnauthorizedWithoutValidAuthorization() {
        final Event  event = updatedEvent();
        final String login = event.getOwner().getLogin();
        final String token = getAuthHeaderContent(login, "invalid-password");

        final Builder  req = eventTarget(event.getId()).request();
        final Response res = req.header(AUTHORIZATION, token).put(json(event));

        assertThat(res, hasHttpStatus(UNAUTHORIZED));
    }

    @Test
    @InSequence(182) // test 18, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestUpdateReturnsUnauthorizedWithoutValidAuthorization() { }


    @Test
    @InSequence(190) // test 19, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestUpdateReturnsUnauthorizedWithNonOwner() { }

    @Test
    @RunAsClient
    @InSequence(191) // test 19, sequence 1
    public void testUpdateReturnsUnauthorizedWithNonOwner() {
        final Event  event = updatedEvent();
        final String login = existentLogin();
        final String token = getAuthHeaderContent(login, passwordFor(login));

        final Builder  req = eventTarget(event.getId()).request();
        final Response res = req.header(AUTHORIZATION, token).put(json(event));

        assertThat(res, hasHttpStatus(UNAUTHORIZED));
    }

    @Test
    @InSequence(192) // test 19, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestUpdateReturnsUnauthorizedWithNonOwner() { }


    @Test
    @InSequence(200) // test 20, sequence 0
    @UsingDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
    public void beforeTestGetAttendeesReturnsValidListOfAttendees() { }

    @Test
    @RunAsClient
    @InSequence(201) // test 20, sequence 1
    public void testGetAttendeesReturnsValidListOfAttendees() {
        final Event event = eventsWithTwoAttendees()[0];

        final Response res = eventTarget(event.getId()).path("attendees").request().get();

        assertThat(res, hasHttpStatus(OK));
        assertThat(res.readEntity(asUserSet), hasSize(event.countAttendees()));
    }

    @Test
    @InSequence(202) // test 20, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
    public void afterTestGetAttendeesReturnsValidListOfAttendees() { }


    @Test
    @InSequence(210) // test 21, sequence 0
    @UsingDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
    public void beforeTestGetAttendeesWithNonExistentIdReturnsANotFoundStatus() { }

    @Test
    @RunAsClient
    @InSequence(211) // test 21, sequence 1
    public void testGetAttemdeesWithNonExistentIdReturnsANotFoundStatus() {
        final Response res = eventTarget(nonExistentEventId()).path("attendees").request().get();
        assertThat(res, hasHttpStatus(NOT_FOUND));
    }

    @Test
    @InSequence(212) // test 21, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
    public void afterTestGetAttemdeesWithNonExistentIdReturnsANotFoundStatus() { }


    @Test
    @InSequence(220) // test 22, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestAttendToEventAddsTheLoggedInUserToTheAttendeeSet() { }

    @Test
    @RunAsClient
    @InSequence(221) // test 22, sequence 1
    public void testAttendToEventAddsTheLoggedInUserToTheAttendeeSet() {
        final String login = "anne";
        final String token = getAuthHeaderContent(login, passwordFor(login));

        final Builder  req = eventTarget(15).path("attendees").request();
        final Response res = req.header(AUTHORIZATION, token).post(null);

        assertThat(res, hasHttpStatus(NO_CONTENT));
    }

    @Test
    @InSequence(222) // test 22, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml", "anne-attends-event-15.xml" })
    public void afterTestAttendToEventAddsTheLoggedInUserToTheAttendeeSet() { }


    @Test
    @InSequence(230) // test 23, sequence 0
    @UsingDataSet({ "users.xml", "events.xml", "anne-attends-event-15.xml" })
    public void beforeTestAttendToEventReturnsBadRequestIfCurrentUserIsAlreadyAnAttendee() { }

    @Test
    @RunAsClient
    @InSequence(231) // test 23, sequence 1
    public void testAttendToEventReturnsBadRequestIfCurrentUserIsAlreadyAnAttendee() {
        final String login = "anne";
        final String token = getAuthHeaderContent(login, passwordFor(login));

        final Builder  req = eventTarget(15).path("attendees").request();
        final Response res = req.header(AUTHORIZATION, token).post(null);

        assertThat(res, hasHttpStatus(BAD_REQUEST));
    }

    @Test
    @InSequence(232) // test 22, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml", "anne-attends-event-15.xml" })
    public void afterTestAttendToEventReturnsBadRequestIfCurrentUserIsAlreadyAnAttendee() { }


    @Test
    @InSequence(240) // test 24, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestAttendToEventReturnsUnauthorizedWithInvalidAuthorization() { }

    @Test
    @RunAsClient
    @InSequence(241) // test 24, sequence 1
    public void testAttendToEventReturnsUnauthorizedWithInvalidAuthorization() {
        final String login = existentLogin();
        final String token = getAuthHeaderContent(login, "invalid-password");

        final Builder  req = eventTarget(existentEventId()).request();
        final Response res = req.header(AUTHORIZATION, token).post(null);

        assertThat(res, hasHttpStatus(UNAUTHORIZED));
    }

    @Test
    @InSequence(242) // test 24, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestAttendToEventReturnsUnauthorizedWithoutValidAuthorization() { }


    @Test
    @InSequence(250) // test 25, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestAttendToEventReturnsUnauthorizedWithNonExistentUser() { }

    @Test
    @RunAsClient
    @InSequence(251) // test 25, sequence 1
    public void testAttendToEventReturnsUnauthorizedWithNonExistentUser() {
        final String login = nonExistentUser().getLogin();
        final String token = getAuthHeaderContent(login, passwordFor(login));

        final Builder  req = eventTarget(existentEventId()).request();
        final Response res = req.header(AUTHORIZATION, token).post(null);

        assertThat(res, hasHttpStatus(UNAUTHORIZED));
    }

    @Test
    @InSequence(252) // test 25, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestAttendToEventReturnsUnauthorizedWithNonExistentUser() { }

}
