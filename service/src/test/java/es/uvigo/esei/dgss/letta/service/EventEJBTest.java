package es.uvigo.esei.dgss.letta.service;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.EventsDataset;
import es.uvigo.esei.dgss.letta.domain.entities.IsEqualToEvent;
import es.uvigo.esei.dgss.letta.service.util.security.RoleCaller;
import es.uvigo.esei.dgss.letta.service.util.security.TestPrincipal;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.events;
import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.newEvent;
import static es.uvigo.esei.dgss.letta.domain.entities.IsEqualToEvent.containsEventsInAnyOrder;
import static es.uvigo.esei.dgss.letta.domain.entities.IsEqualToEvent.equalToEventWithCreator;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.existentUser;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.nonExistentUser;

@RunWith(Arquillian.class)
@CleanupUsingScript(value = "cleanup.sql")
public class EventEJBTest {

    @Inject
    private EventEJB facade;

    @Inject
    private TestPrincipal principal;

    @EJB(name = "user-caller")
    private RoleCaller asUser;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Deployment
    public static Archive<?> createDeploymentPackage() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
              .addClasses(EventEJB.class, UserAuthorizationEJB.class)
              .addPackage(EventsDataset.class.getPackage())
              .addPackage(IsEqualToEvent.class.getPackage())
              .addPackage(TestPrincipal.class.getPackage())
              .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
              .addAsWebInfResource("jboss-web.xml")
              .addAsWebInfResource("beans-principal.xml", "beans.xml");
    }

    @Test
    public void testListByDateReturnsEmptyListIfNoEventsInDatabase() {
        assertThat(facade.listByDate(0,   1), is(empty()));
        assertThat(facade.listByDate(0,  50), is(empty()));
        assertThat(facade.listByDate(0, 100), is(empty()));
    }

    @Test
    @UsingDataSet("events-less-than-twenty.xml")
    @ShouldMatchDataSet("events-less-than-twenty.xml")
    public void testListByDateReturnsTheSpecifiedNumberOfEvents() {
        assertThat(facade.listByDate(0,  1), hasSize( 1));
        assertThat(facade.listByDate(0,  5), hasSize( 5));
        assertThat(facade.listByDate(0, 10), hasSize(10));
    }

    @Test
    @UsingDataSet("events-less-than-five.xml")
    @ShouldMatchDataSet("events-less-than-five.xml")
    public void testListByDateReturnsAllEventsIfCountIsGreaterThanDatabaseSize() {
        assertThat(facade.listByDate(0,  6), hasSize(lessThan(5)));
        assertThat(facade.listByDate(0, 10), hasSize(lessThan(5)));
        assertThat(facade.listByDate(0, 50), hasSize(lessThan(5)));
    }

    @Test
    @UsingDataSet("events-less-than-five.xml")
    @ShouldMatchDataSet("events-less-than-five.xml")
    public void testListByDateReturnsEmptyListIfCountIsZero() {
        assertThat(facade.listByDate(0, 0), is(empty()));
    }

    @Test
    @UsingDataSet("events.xml")
    @ShouldMatchDataSet("events.xml")
    public void testListByDateReturnsValidEvents() {
        assertThat(
            facade.listByDate(0, 100),
            containsEventsInAnyOrder(events())
        );
    }

    @Test
    public void testListHighlightedReturnsEmptyListIfNoEventsInDatabase() {
        assertThat(facade.listHighlighted(0,  1), is(empty()));
        assertThat(facade.listHighlighted(0,  5), is(empty()));
        assertThat(facade.listHighlighted(0, 20), is(empty()));
    }

    @Test
    @UsingDataSet("events-less-than-twenty.xml")
    @ShouldMatchDataSet("events-less-than-twenty.xml")
    public void testListHighlightedReturnsNoMoreEventsThanExisting() {
        assertThat(facade.listHighlighted(0,  1), hasSize(lessThan(20)));
        assertThat(facade.listHighlighted(0,  5), hasSize(lessThan(20)));
        assertThat(facade.listHighlighted(0, 10), hasSize(lessThan(20)));
    }

    @Test
    @UsingDataSet("users.xml")
    @ShouldMatchDataSet({ "users.xml", "users-create-event.xml" })
    public void testCreateEventCorrectlyInsertsValidEventInDatabase() {
        principal.setName(existentUser().getLogin());
        asUser.run(() -> facade.createEvent(newEvent()));
    }

    @Test
    public void testCreateEventCannotBeCalledByUnauthorizedUsers() {
        thrown.expect(EJBException.class);

        facade.createEvent(newEvent());
    }

    @Test
    public void testCreateEventCannotBeCalledByANonExistentUser() {
        thrown.expect(EJBTransactionRolledbackException.class);
        thrown.expectCause(is(instanceOf(SecurityException.class)));

        principal.setName(nonExistentUser().getLogin());
        asUser.throwingRun(() -> facade.createEvent(newEvent()));
    }

    @Test
    @UsingDataSet("users.xml")
    @ShouldMatchDataSet({ "users.xml", "users-create-event.xml" })
    public void testCreateReturnsTheInsertedEvent() {
        principal.setName(existentUser().getLogin());

        final Event expect = newEvent();
        expect.setCreator(existentUser());

        final Event actual = asUser.call(() -> facade.createEvent(newEvent()));

        assertThat(actual, is(equalToEventWithCreator(expect)));
    }

}
