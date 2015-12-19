package es.uvigo.esei.dgss.letta.service;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBAccessException;
import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.persistence.CleanupUsingScript;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.Event.Category;
import es.uvigo.esei.dgss.letta.domain.entities.EventsDataset;
import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.domain.entities.UsersDataset;
import es.uvigo.esei.dgss.letta.service.util.exceptions.EventAlredyJoinedException;
import es.uvigo.esei.dgss.letta.service.util.exceptions.EventIsCancelledException;
import es.uvigo.esei.dgss.letta.service.util.exceptions.EventNotJoinedException;
import es.uvigo.esei.dgss.letta.service.util.exceptions.UserNotAuthorizedException;
import es.uvigo.esei.dgss.letta.service.util.security.RoleCaller;
import es.uvigo.esei.dgss.letta.service.util.security.TestPrincipal;
import static java.time.LocalDateTime.ofInstant;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;
import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.events;
import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.existentEvent;
import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.filterEvents;
import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.filterEventsWithTwoJoinedUsers;
import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.newEventWithoutCreator;
import static es.uvigo.esei.dgss.letta.domain.entities.EventsDataset.nonExistentEvent;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.existentUser;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.newUser;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.nonExistentUser;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.userWithLogin;
import static es.uvigo.esei.dgss.letta.domain.matchers.IsEqualToEvent.containsEventsInAnyOrder;
import static es.uvigo.esei.dgss.letta.domain.matchers.IsEqualToEvent.equalToEventWithOwner;
import static es.uvigo.esei.dgss.letta.service.util.ServiceIntegrationTestBuilder.deployment;

@RunWith(Arquillian.class)
@CleanupUsingScript("cleanup.sql")
public class EventEJBTest {

    @Inject
    private EventEJB events;

    @Inject
    private TestPrincipal principal;

    @EJB(name = "user-caller")
    private RoleCaller asUser;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Deployment
    public static Archive<WebArchive> deploy() {
        return deployment().withTestPrincipal().withClasses(
            EventEJB.class,
            UserAuthorizationEJB.class
        ).build();
    }

    @Test
    public void testListByDateReturnsEmptyListIfNoEventsInDatabase() {
        assertThat(events.listByDate(0, 1), is(empty()));
        assertThat(events.listByDate(0, 50), is(empty()));
        assertThat(events.listByDate(0, 100), is(empty()));
    }

    @Test
    @UsingDataSet({ "users.xml", "events-less-than-twenty.xml" })
    @ShouldMatchDataSet({ "users.xml", "events-less-than-twenty.xml" })
    public void testListByDateReturnsTheSpecifiedNumberOfEvents() {
        assertThat(events.listByDate(0, 1), hasSize(1));
        assertThat(events.listByDate(0, 5), hasSize(5));
        assertThat(events.listByDate(0, 10), hasSize(10));
    }

    @Test
    @UsingDataSet({ "users.xml", "events-less-than-five.xml" })
    @ShouldMatchDataSet({ "users.xml", "events-less-than-five.xml" })
    public void testListByDateReturnsAllEventsIfCountIsGreaterThanDatabaseSize() {
        assertThat(events.listByDate(0, 6), hasSize(lessThan(5)));
        assertThat(events.listByDate(0, 10), hasSize(lessThan(5)));
        assertThat(events.listByDate(0, 50), hasSize(lessThan(5)));
    }

    @Test
    @UsingDataSet({ "users.xml", "events-less-than-five.xml" })
    @ShouldMatchDataSet({ "users.xml", "events-less-than-five.xml" })
    public void testListByDateReturnsEmptyListIfCountIsZero() {
        assertThat(events.listByDate(0, 0), is(empty()));
    }

    @Test
    @UsingDataSet({ "users.xml", "events.xml" })
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void testListByDateReturnsValidEvents() {
        assertThat(
            events.listByDate(0, 100),
            containsEventsInAnyOrder(events())
        );
    }

    @Test
    public void testListHighlightedReturnsEmptyListIfNoEventsInDatabase() {
        assertThat(events.listHighlighted(), is(empty()));
    }

    @Test
    @UsingDataSet({ "users.xml", "events-less-than-twenty.xml" })
    @ShouldMatchDataSet({ "users.xml", "events-less-than-twenty.xml" })
    public void testListHighlightedReturnsNoMoreEventsThanExisting() {
        assertThat(events.listHighlighted(), hasSize(lessThan(20)));
    }

    @Test
    @UsingDataSet("users.xml")
    @ShouldMatchDataSet({ "users.xml", "new-event.xml" })
    public void testCreateEventCorrectlyInsertsValidEventInDatabase() {
        principal.setName(existentUser().getLogin());
        asUser.run(() -> events.createEvent(newEventWithoutCreator()));
    }

    @Test
    public void testCreateEventCannotBeCalledByUnauthorizedUsers() {
        thrown.expect(EJBAccessException.class);

        events.createEvent(newEventWithoutCreator());
    }

    @Test
    public void testCreateEventCannotBeCalledByANonExistentUser() {
        thrown.expect(EJBTransactionRolledbackException.class);
        thrown.expectCause(is(instanceOf(SecurityException.class)));

        principal.setName(nonExistentUser().getLogin());
        asUser.throwingRun(() -> events.createEvent(newEventWithoutCreator()));
    }

    @Test
    @UsingDataSet("users.xml")
    @ShouldMatchDataSet({ "users.xml", "new-event.xml" })
    public void testCreateReturnsTheInsertedEvent() {
        principal.setName(existentUser().getLogin());

        final Event expect = newEventWithoutCreator();
        expect.setOwner(existentUser());

        final Event actual = asUser.call(
            () -> events.createEvent(newEventWithoutCreator())
        );

        assertThat(actual, is(equalToEventWithOwner(expect)));
    }


    @Test
    @UsingDataSet({ "users.xml", "events.xml" })
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void testSearchTitleSingleResult() {
        assertThat(events.search("Example1 literature", 0, 25), hasSize(1));
    }

    @Test
    @UsingDataSet({ "users.xml", "events.xml" })
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void testSearchTitleMultipleResult() {
        assertThat(events.search("Example", 0, 25), hasSize(20));
    }

    @Test
    @UsingDataSet({ "users.xml", "events.xml" })
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void testSearchDescriptionSingleResult() {
        assertThat(events.search("This is a summary of literature 1", 0, 25), hasSize(1));
    }

    @Test
    @UsingDataSet({ "users.xml", "events.xml" })
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void testSearchDescriptionMultipleResult() {
        assertThat(events.search("This is a summary", 0, 25), hasSize(20));
    }

    @Test
    @UsingDataSet({ "users.xml", "events.xml" })
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void testSearchOnTitleAndDescription() {
        assertThat(events.search("literature", 0, 25), hasSize(2));
    }

    @Test
    @UsingDataSet({ "users.xml", "events.xml" })
    @ShouldMatchDataSet({ "users.xml", "events.xml" })
    public void testSearchNoResultException() {
        assertThat(events.search(EventsDataset.nonExistentTitle(), 0, 25), is(empty()));
        assertThat(events.search(EventsDataset.nonExistentDescription(), 0, 25), is(empty()));
    }

    @Test
    @UsingDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
    public void testSearchCountZero(){
    	assertThat(events.search("Example", 0, 0), hasSize(0));
    }

    @Test
    @UsingDataSet({ "users.xml", "events.xml" })
    @ShouldMatchDataSet({ "users.xml", "events.xml", "anne-attends-event-15.xml" })
    public void testRegisterUserToEvent() throws Exception {
        User user = UsersDataset.userWithLogin("anne");

        principal.setName(user.getLogin());
        asUser.throwingRun(() -> events.attendToEvent(15));
    }

    @Test
    @UsingDataSet({ "users.xml", "events.xml", "anne-attends-event-15.xml" })
    @ShouldMatchDataSet({ "users.xml", "events.xml", "anne-attends-event-15.xml" })
    public void testRegisterUserAlreadyRegistered() throws Exception {
        thrown.expect(EventAlredyJoinedException.class);

        final User user = userWithLogin("anne");
        principal.setName(user.getLogin());

        asUser.throwingRun(() -> events.attendToEvent(15));
    }
    
    @Test(expected = EventIsCancelledException.class)
    @UsingDataSet({ "users.xml", "events.xml"})
    @ShouldMatchDataSet({ "users.xml", "events.xml", "anne-attends-event-cancelled-25.xml" })
    public void testRegisterUserEventCancelled() throws Exception {
        final User user = userWithLogin("anne");
        principal.setName(user.getLogin());

        asUser.throwingRun(() -> events.attendToEvent(25));
    }

    @Test
    @UsingDataSet({ "users.xml", "new-user.xml", "events.xml", "event-attendees.xml" })
    public void testGetEventsJoinedByUserEmpty(){
        final User user = newUser();
        principal.setName(user.getLogin());

        final List<Event> joinedEvents = asUser.call(
            () -> events.getAttendingEvents(0,100)
        );

        assertThat(joinedEvents, is(empty()));
    }

    @Test
    @UsingDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
    public void testGetEventsJoinedByUserCountZero(){
    	final User user = userWithLogin("anne");

    	principal.setName(user.getLogin());

    	final List<Event> joinedEvents = asUser.call(
    	    () -> events.getAttendingEvents(0, 0)
        );

    	assertThat(joinedEvents, is(empty()));
    }
    
    @Test
    @UsingDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
    public void testGetEventsJoinedByUserCountNegative(){
        final User user = userWithLogin("anne");
        principal.setName(user.getLogin());

        final Event[] expectedEvents = filterEventsWithTwoJoinedUsers(
            event -> event.hasAttendee(user)
        );

        final List<Event> joinedEvents = asUser.call(
            () -> events.getAttendingEvents(0, -1)
        );
        
        assertThat(joinedEvents, containsEventsInAnyOrder(expectedEvents));
    }

    @Test
    @UsingDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
    public void testGetEventsJoinedByUserNotEmpty(){
        final User user = userWithLogin("anne");
        principal.setName(user.getLogin());

        final Event[] expectedEvents = filterEventsWithTwoJoinedUsers(
            event -> event.hasAttendee(user)
        );

        final List<Event> joinedEvents = asUser.call(
            () -> events.getAttendingEvents(0, 100)
        );

        assertThat(joinedEvents, containsEventsInAnyOrder(expectedEvents));
    }

    @Test
    @UsingDataSet({ "users.xml", "events.xml" })
    public void testGetEventsCreatedByUser() {
        principal.setName(existentUser().getLogin());

        final Event[] userEvents = filterEvents(
            event -> event.getOwner().equals(existentUser())
        );

        final List<Event> eventsCreatedByUser = asUser.call(
            () -> events.getEventsOwnedByCurrentUser()
        );

        assertThat(eventsCreatedByUser, containsEventsInAnyOrder(userEvents));
    }

    @Test
    public void testCountEmpty() {
        final int count = events.count("");
        assertThat(count, is(0));
    }

    @Test
    @UsingDataSet({ "users.xml", "events.xml" })
    public void testCountNotEmpty() {

        final int count = events.count("");
        assertThat(count, is(20));
    }
    @Test
    @UsingDataSet({ "users.xml", "events.xml" })
    public void testCountExampleTerm() {

        final int count = events.count("example");
        assertThat(count, is(20));
    }
    @Test
    @UsingDataSet({ "users.xml", "new-user.xml", "events.xml", "event-attendees.xml" })
    public void testGetCountEventsJoinedByUserEmpty(){
    	principal.setName(newUser().getLogin());

    	final int count = asUser.call(events::countAttendingEvents);

    	assertThat(count, is(0));
    }

    @Test
    @UsingDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
    public void testGetCountEventsJoinedByUserNotEmpty() {
        principal.setName("anne");

        final int count = asUser.call(events::countAttendingEvents);

        assertThat(count, is(10));
    }
    
    @Test
    @UsingDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
    public void testGetCountEventsAfterUnJoin() throws SecurityException, EventNotJoinedException{
    	principal.setName("anne");
        asUser.throwingRun(() -> events.unattendToEvent(2));
    	final int count2=asUser.call(events::countAttendingEvents);
        assertThat(count2, is(9));
    }
    
    @Test(expected=EventNotJoinedException.class)
    @UsingDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
    public void testUnAttendNotJoinedException() throws SecurityException, EventNotJoinedException{
    	principal.setName("anne");
        asUser.throwingRun(() -> events.unattendToEvent(13));
    }
    
    @Test
    @UsingDataSet({ "users.xml", "events.xml", "event-attendees.xml" })
    public void testGetAttendees(){        
        assertThat(events.getAttendees(existentEvent()), is(2));
    }
    

    
    @Test  
    @UsingDataSet({ "users.xml", "events.xml" })
    public void testModifyEventTittle() 
    		throws SecurityException, IllegalArgumentException, 
    		UserNotAuthorizedException{
    	principal.setName("john");
    	final Event modified = existentEvent();
    	modified.setTitle("New tittle");
        asUser.throwingRun(() -> events.modifyEvent(modified));
        assertThat(events.getEvent(existentEvent().getId()), is(modified));
    }
    
    @Test  
    @UsingDataSet({ "users.xml", "events.xml" })
    public void testModifyEventSummary() 
    		throws SecurityException, IllegalArgumentException, 
    		UserNotAuthorizedException{
    	principal.setName("john");
    	final Event modified = existentEvent();
    	modified.setSummary("New Summary");
        asUser.throwingRun(() -> events.modifyEvent(modified));
        assertThat(events.getEvent(existentEvent().getId()), is(modified));
    }    
    
    @Test  
    @UsingDataSet({ "users.xml", "events.xml" })
    public void testModifyEventCategory() 
    		throws SecurityException, IllegalArgumentException, 
    		UserNotAuthorizedException{
    	principal.setName("john");
    	final Event modified = existentEvent();
    	modified.setCategory(Category.SPORTS);
        asUser.throwingRun(() -> events.modifyEvent(modified));
        assertThat(events.getEvent(existentEvent().getId()), is(modified));
    }    
    
    @Test  
    @UsingDataSet({ "users.xml", "events.xml" })
    public void testModifyEventLocation() 
    		throws SecurityException, IllegalArgumentException, 
    		UserNotAuthorizedException{
    	principal.setName("john");
    	final Event modified = existentEvent();
    	modified.setLocation("New location");
        asUser.throwingRun(() -> events.modifyEvent(modified));
        assertThat(events.getEvent(existentEvent().getId()), is(modified));
    }
    
    @Test  
    @UsingDataSet({ "users.xml", "events.xml" })
    public void testModifyEventDate() 
    		throws SecurityException, IllegalArgumentException, 
    		UserNotAuthorizedException{
    	principal.setName("john");
    	final Date date = new Date();
    	final Event modified = existentEvent();
    	modified.setDate(ofInstant(date.toInstant(), ZoneId.systemDefault()));
        asUser.throwingRun(() -> events.modifyEvent(modified));
        assertThat(events.getEvent(existentEvent().getId()), is(modified));
    }
    
    @Test(expected=UserNotAuthorizedException.class)  
    @UsingDataSet({ "users.xml", "events.xml" })
    public void testModifyEventUnatorized() 
    		throws SecurityException, IllegalArgumentException, 
    		UserNotAuthorizedException{
    	principal.setName("anne");
    	final Event modified = existentEvent();
    	modified.setLocation("New location");
        asUser.throwingRun(() -> events.modifyEvent(modified));
    } 
    
    
    @Test(expected=javax.ejb.EJBTransactionRolledbackException.class)
    @UsingDataSet({ "users.xml", "events.xml" })
    public void testModifyEventNotExists() 
    		throws SecurityException, IllegalArgumentException, 
    		UserNotAuthorizedException{
    	principal.setName("anne");
    	final Event modified = nonExistentEvent();
        asUser.throwingRun(() -> events.modifyEvent(modified));
    } 

}
