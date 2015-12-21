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

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import static org.junit.Assert.assertThat;

import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.filterEvents;
import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.filterEventsWithTwoJoinedUsers;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.existentLogin;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.existentUser;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.nonExistentUser;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.passwordFor;
import static es.uvigo.esei.dgss.letta.domain.matchers.IsEqualToEvent.containsEventsInAnyOrder;
import static es.uvigo.esei.dgss.letta.http.util.HasHttpStatus.hasHttpStatus;
import static es.uvigo.esei.dgss.letta.rest.util.RestIntegrationTestBuilder.deployment;
import static es.uvigo.esei.dgss.letta.rest.util.RestIntegrationTestUtils.asEventList;
import static es.uvigo.esei.dgss.letta.rest.util.RestIntegrationTestUtils.buildResourceTarget;
import static es.uvigo.esei.dgss.letta.rest.util.RestIntegrationTestUtils.getAuthHeaderContent;

@RunWith(Arquillian.class)
@Cleanup(phase = TestExecutionPhase.NONE)
public class UserResourceRestTest {

    @ArquillianResource
    private URL deploymentURL;

    @Deployment
    public static Archive<WebArchive> deploy() {
        return deployment().withClasses(
            UserResource.class
        ).build();
    }

    private WebTarget userTarget() {
        return buildResourceTarget(deploymentURL, UserResource.class);
    }

    private WebTarget userTarget(final String login) {
        return userTarget().path(login);
    }

    @Test
    @InSequence(10)
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestGetCreatedEvents() { }

    @Test
    @RunAsClient
    @InSequence(11)
    public void testGetCreatedEvents() {
        final String login = existentLogin();
        final String token = getAuthHeaderContent(login, passwordFor(login));

        final Builder  req = userTarget(login).path("created").request();
        final Response res = req.header(AUTHORIZATION, token).get();

        final Event[] events = filterEvents(
            e -> e.getOwner().equals(existentUser())
        );

        assertThat(res, hasHttpStatus(OK));
        assertThat(
            res.readEntity(asEventList),
            containsEventsInAnyOrder(events)
        );
    }

    @Test
    @InSequence(12)
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestGetCreatedEvents() { }

    @Test
    @InSequence(21)
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestNonExistentUserCreated() { }

    @Test
    @RunAsClient
    @InSequence(22)
    public void testNonExistentUserCreated()  {
        final String login = nonExistentUser().getLogin();
        final String token = getAuthHeaderContent(login, passwordFor(login));

        final Builder  req = userTarget(login).path("created").request();
        final Response res = req.header(AUTHORIZATION, token).get();

        assertThat(res, hasHttpStatus(UNAUTHORIZED));
    }

    @Test
    @InSequence(23)
    @UsingDataSet({ "users.xml", "events.xml" })
    public void afterTestNonExistentUserCreated() { }


    @Test
    @InSequence(31)
    @UsingDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
    public void beforeTestGetJoinedEvents() { }

    @Test
    @InSequence(32)
    @RunAsClient
    public void testGetJoinedEvents() {
        final String login = existentLogin();
        final String token = getAuthHeaderContent(login, passwordFor(login));

        final Builder  req = userTarget(login).path("joined").request();
        final Response res = req.header(AUTHORIZATION, token).get();

        final Event[] events = filterEventsWithTwoJoinedUsers(
            e -> e.hasAttendee(existentUser())
        );

        assertThat(res, hasHttpStatus(OK));
        assertThat(
            res.readEntity(asEventList),
            containsEventsInAnyOrder(events)
        );
    }

    @Test
    @InSequence(33)
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
    public void afterTestGetJoinedEvents() { }

    @Test
    @InSequence(41)
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestNonExistentAuthorizationCreated() { }

    @Test
    @RunAsClient
    @InSequence(42)
    public void testNonExistentAuthorizationCreated() {
        final String login = existentLogin();

        final Response res = userTarget(login).path("created").request().get();

        assertThat(res, hasHttpStatus(UNAUTHORIZED));
    }

    @Test
    @InSequence(43)
    @UsingDataSet({ "users.xml", "events.xml" })
    public void afterTestNonExistentAuthorizationCreated() { }


    @Test
    @InSequence(51)
    @UsingDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
    public void beforeTestGetJoinedEventsIncorrectStartNumberCorrectCountNumber() { }

    @Test
    @RunAsClient
    @InSequence(52)
    public void testGetJoinedEventsIncorrectStartNumberCorrectCountNumber() {
        final String login = existentLogin();
        final String token = getAuthHeaderContent(login, passwordFor(login));

        final Response res = userTarget(login).path("joined")
            .queryParam("start", -1).queryParam("count", 0)
            .request().header(AUTHORIZATION, token).get();

        assertThat(res, hasHttpStatus(BAD_REQUEST));
    }

    @Test
    @InSequence(53)
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
    public void afterTestGetJoinedEventsIncorrectStartNumberCorrectCountNumber() { }

    @Test
    @InSequence(61)
    @UsingDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
    public void beforeTestGetJoinedEventsCorrectStartNumberIncorrectCountNumber() { }

    @Test
    @RunAsClient
    @InSequence(62)
    public void testGetJoinedEventsCorrectStartNumberIncorrectCountNumber() {
        final String login = existentLogin();
        final String token = getAuthHeaderContent(login, passwordFor(login));

        final Response res = userTarget(login).path("joined")
            .queryParam("start", 0).queryParam("count", -1)
            .request().header(AUTHORIZATION, token).get();

        assertThat(res, hasHttpStatus(BAD_REQUEST));
    }

    @Test
    @InSequence(63)
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
    public void afterTestGetJoinedEventsCorrectStartNumberIncorrectCountNumber() { }

    @Test
    @InSequence(71)
    @UsingDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
    public void beforeTestGetJoinedEventsIncorrectStartNumberIncorrectCountNumber() { }

    @Test
    @RunAsClient
    @InSequence(72)
    public void testGetJoinedEventsIncorrectStartNumberIncorrectCountNumber() {
        final String login = existentLogin();
        final String token = getAuthHeaderContent(login, passwordFor(login));

        final Response res = userTarget(login).path("joined")
            .queryParam("start", -1).queryParam("count", -1)
            .request().header(AUTHORIZATION, token).get();

        assertThat(res, hasHttpStatus(BAD_REQUEST));
    }

    @Test
    @InSequence(73)
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
    public void afterTestGetJoinedEventsIncorrectStartNumberIncorrectCountNumber() { }

    @Test
    @InSequence(81)
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestNonExistentUserJoined() { }

    @Test
    @RunAsClient
    @InSequence(82)
    public void testNonExistentUserJoined() {
        final String login = nonExistentUser().getLogin();
        final String token = getAuthHeaderContent(login, passwordFor(login));

        final Builder  req = userTarget(login).path("joined").request();
        final Response res = req.header(AUTHORIZATION, token).get();

        assertThat(res, hasHttpStatus(UNAUTHORIZED));
    }

    @Test
    @InSequence(83)
    @UsingDataSet({ "users.xml", "events.xml" })
    public void afterTestNonExistentUserJoined() { }

    @Test
    @InSequence(121)
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestNonExistentAuthorizationJoined() { }

    @Test
    @RunAsClient
    @InSequence(122)
    public void testNonExistentAuthorizationJoined() {
        final String login = existentLogin();

        final Response res = userTarget(login).path("joined").request().get();

        assertThat(res, hasHttpStatus(UNAUTHORIZED));
    }

    @Test
    @InSequence(123)
    @UsingDataSet({ "users.xml", "events.xml" })
    public void afterTestNonExistentAuthorizationJoined() { }

}
