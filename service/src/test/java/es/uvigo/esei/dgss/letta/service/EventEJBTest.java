package es.uvigo.esei.dgss.letta.service;

import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.EventsDataset;
import es.uvigo.esei.dgss.letta.service.util.security.RoleCaller;
import es.uvigo.esei.dgss.letta.service.util.security.TestPrincipal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;

import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.newEvent;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.existentLogin;

@RunWith(Arquillian.class)
@CleanupUsingScript("cleanup.sql")
public class EventEJBTest {

    @Inject
    private EventEJB facade;

    @EJB(name = "user-caller")
    private RoleCaller asUser;

    @Inject
    private TestPrincipal principal;

    @Deployment
    public static Archive<?> createDeploymentPackage() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
              .addClass(EventEJB.class)
              .addPackage(Event.class.getPackage())
              .addPackage(EventsDataset.class.getPackage())
              .addPackage(TestPrincipal.class.getPackage())
              .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
              .addAsWebInfResource("jboss-web.xml")
              .addAsWebInfResource("beans-principal.xml", "beans.xml");
    }

    @Test
    public void testGetFrontPageEmpty() {
        final List<Event> frontPage = facade.getFrontPage();
        assertThat(frontPage, is(empty()));
    }

    @Test
    @UsingDataSet("events.xml")
    @ShouldMatchDataSet("events.xml")
    public void testGetFrontPageEquals20() {
        final List<Event> frontPage = facade.getFrontPage();
        assertThat(frontPage.size(), is(equalTo(20)));
    }

    @Test
    @UsingDataSet("events-less-than-twenty.xml")
    @ShouldMatchDataSet("events-less-than-twenty.xml")
    public void testGetFrontPageLessThan20() {
        final List<Event> frontPage = facade.getFrontPage();
        assertThat(frontPage.size(), is(lessThan(20)));
    }

    @Test
    public void testGetFrontPageHighlightsEmpty() {
        final List<Event> frontPage = facade.getFrontPageHighlights();
        assertThat(frontPage, is(empty()));
    }

    @Test
    @UsingDataSet("events.xml")
    @ShouldMatchDataSet("events.xml")
    public void testGetFrontPageHighlightsEquals5() {
        final List<Event> frontPage = facade.getFrontPageHighlights();
        assertThat(frontPage.size(), is(equalTo(5)));
    }

    @Test
    @UsingDataSet("events-less-than-five.xml")
    @ShouldMatchDataSet("events-less-than-five.xml")
    public void testGetFrontPageHighlightsLessThan5() {
        final List<Event> frontPage = facade.getFrontPageHighlights();
        assertThat(frontPage.size(), is(lessThan(5)));
    }

    @Test
    @UsingDataSet("users.xml")
    @ShouldMatchDataSet({ "users.xml", "users-create-event.xml" })
    public void testCreateEvent() {
        principal.setName(existentLogin());
        asUser.call(() -> facade.createEvent(newEvent()));
    }

}
