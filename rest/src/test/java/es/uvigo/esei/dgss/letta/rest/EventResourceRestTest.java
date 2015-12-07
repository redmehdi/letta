package es.uvigo.esei.dgss.letta.rest;

import java.util.List;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.rest.util.mapper.IllegalArgumentExceptionMapper;
import es.uvigo.esei.dgss.letta.service.EventEJB;

import static javax.ws.rs.core.Response.Status.OK;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import static es.uvigo.esei.dgss.letta.http.util.HasHttpStatus.hasHttpStatus;

@RunWith(Arquillian.class)
@Cleanup(phase = TestExecutionPhase.NONE)
public class EventResourceRestTest {

    private static final String basePath = "api/public/event";

    private static final GenericType<List<Event>> asEventList = new GenericType<List<Event>>() { };

    @Deployment
    public static Archive<WebArchive> deploy() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
              .addClass(EventResource.class)
              .addPackage(IllegalArgumentExceptionMapper.class.getPackage())
              .addPackages(true, EventEJB.class.getPackage())
              .addPackages(true, Event.class.getPackage())
              .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
              .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
              .addAsWebInfResource("jboss-web.xml")
              .addAsWebInfResource("web.xml");
    }

    @Test
    @InSequence(10) // test 1, sequence 0
    @UsingDataSet({ "users.xml", "events.xml" })
    public void beforeTestListWithDefaultArguments() { }

    @Test
    @RunAsClient
    @InSequence(11) // test 1, sequence 1
    public void testListWithDefaultArguments(
        @ArquillianResteasyResource(basePath) final WebTarget target
    ) {
        final Response response = target.request().get();
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
    public void testHighlighted(
        @ArquillianResteasyResource(basePath) final WebTarget target
    ) {
        final Response response = target.path("highlighted").request().get();
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
    public void testSearchWithDefaultArguments(
        @ArquillianResteasyResource(basePath) final WebTarget target
    ) {
        final Response response = target.path("search").request().get();
        assertThat(response, hasHttpStatus(OK));

        assertThat(response.readEntity(asEventList), hasSize(20));
    }

    @Test
    @InSequence(32) // test 3, sequence 2
    @CleanupUsingScript("cleanup.sql")
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void afterTestSearchWithDefaultArguments() { }

}
