package es.uvigo.esei.dgss.letta.rest;

import java.net.URL;

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

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.existentEventId;
import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.nonExistentEventId;
import static es.uvigo.esei.dgss.letta.http.util.HasHttpStatus.hasHttpStatus;
import static es.uvigo.esei.dgss.letta.rest.util.RestIntegrationTestBuilder.deployment;
import static es.uvigo.esei.dgss.letta.rest.util.RestIntegrationTestUtils.asEventList;
import static es.uvigo.esei.dgss.letta.rest.util.RestIntegrationTestUtils.buildResourceTarget;

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
    public void beforeTestListWithDefaultArguments() { }

    @Test
    @RunAsClient
    @InSequence(11) // test 1, sequence 1
    public void testListWithDefaultArguments() {
        final Response response = eventTarget().request().get();;
        assertThat(response, hasHttpStatus(OK));

        // FIXME: cannot correctly test order of events because all events in
        // the events datasets have the same date. Will fix that soon and
        // update all tests where that ordering should be considered.
        //
        // final Event[] events = copyOf(sortedEvents(Event::getDate), 20);
        // assertThat(
        //     response.readEntity(asEventList),
        //     IsEqualToEvent.containsEventsInOrder(events)
        // );

        assertThat(response.readEntity(asEventList), hasSize(20));
    }

    @Test
    @InSequence(12) // test 1, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestListWithDefaultArguments() { }

    @Test
    @InSequence(20) // test 2, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestHighlighted() { }

    @Test
    @RunAsClient
    @InSequence(21) // test 2, sequence 1
    public void testHighlighted() {
        final Response response = eventTarget().path("highlighted").request().get();
        assertThat(response, hasHttpStatus(OK));

        assertThat(response.readEntity(asEventList), hasSize(5));
    }

    @Test
    @InSequence(22) // test 2, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestHighlighted() { }

    @Test
    @InSequence(30) // test 3, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestSearchWithDefaultArguments() { }

    @Test
    @RunAsClient
    @InSequence(31) // test 3, sequence 1
    public void testSearchWithDefaultArguments() {
        final Response response = eventTarget().path("search").request().get();
        assertThat(response, hasHttpStatus(OK));

        assertThat(response.readEntity(asEventList), hasSize(20));
    }

    @Test
    @InSequence(32) // test 3, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestSearchWithDefaultArguments() { }

    @Test
    @InSequence(40)
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestGetEventInfo() { }

    @Test
    @RunAsClient
    @InSequence(41)
    public void testGetEventInfo() {
        final Response response = eventTarget(existentEventId()).request().get();
        assertThat(response, hasHttpStatus(OK));
    }

    @Test
    @InSequence(42)
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestGetEventInfo() { }

    @Test
    @InSequence(50)
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestGetNonExistentEventInfo() { }

    @Test
    @RunAsClient
    @InSequence(51)
    public void testGetNonExistentEventInfo() {
        final Response response = eventTarget(nonExistentEventId()).request().get();
        assertThat(response, hasHttpStatus(NOT_FOUND));
    }

    @Test
    @InSequence(52)
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestGetNonExistentEventInfo() { }

}
