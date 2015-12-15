package es.uvigo.esei.dgss.letta.domain.entities;

import java.time.LocalDateTime;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import es.uvigo.esei.dgss.letta.domain.entities.Event.Category;

import static java.util.Arrays.stream;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import static es.uvigo.esei.dgss.letta.domain.entities.EventParameters.*;

public final class EventTest {

    @Test
    public void testConstructorWithValidTitles() {
        final Category      category = aCategory();
        final String        summary  = aSummary();
        final LocalDateTime date     = aDate();
        final String        location = aLocation();

        final String[] validTitles = {
            aTitle(), anotherTitle(), longestValidTitle(), shortestValidTitle()
        };

        stream(validTitles).forEach(title -> {
            final Event event = new Event(
                category, title, summary, date, location
            );

            assertThat(event.getCategory(), is(equalTo(category)));
            assertThat(event.getTitle(),    is(equalToIgnoringCase(title)));
            assertThat(event.getSummary(),  is(equalToIgnoringCase(summary)));
            assertThat(event.getDate(),     is(equalTo(date)));
            assertThat(event.getLocation(), is(equalToIgnoringCase(location)));
        });
    }

    @Test
    public void testConstructorWithValidSummaries() {
        final Category      category = aCategory();
        final String        title    = aTitle();
        final LocalDateTime date     = aDate();
        final String        location = aLocation();

        final String[] validSummaries = {
            aSummary(),
            anotherSummary(),
            longestValidSummary(),
            shortestValidSummary(),
        };

        stream(validSummaries).forEach(summary -> {
            final Event event = new Event(
                category, title, summary, date, location
            );

            assertThat(event.getCategory(), is(equalTo(category)));
            assertThat(event.getTitle(),    is(equalToIgnoringCase(title)));
            assertThat(event.getSummary(),  is(equalToIgnoringCase(summary)));
            assertThat(event.getDate(),     is(equalTo(date)));
            assertThat(event.getLocation(), is(equalToIgnoringCase(location)));
        });
    }

    @Test
    public void testConstructorWithValidLocations() {
        final Category      category = aCategory();
        final String        title    = aTitle();
        final String        summary  = aSummary();
        final LocalDateTime date     = aDate();

        final String[] validLocations = {
            aLocation(),
            anotherLocation(),
            longestValidLocation(),
            shortestValidLocation(),
        };

        stream(validLocations).forEach(location -> {
            final Event event = new Event(
                category, title, summary, date, location
            );

            assertThat(event.getCategory(), is(equalTo(category)));
            assertThat(event.getTitle(),    is(equalToIgnoringCase(title)));
            assertThat(event.getSummary(),  is(equalToIgnoringCase(summary)));
            assertThat(event.getDate(),     is(equalTo(date)));
            assertThat(event.getLocation(), is(equalToIgnoringCase(location)));
        });
    }

    @Test(expected = NullPointerException.class)
    public void testThatConstructorThrowsExceptionOnNullCategory() {
        new Event(null, aTitle(), aSummary(), aDate(), aLocation());
    }

    @Test(expected = NullPointerException.class)
    public void testThatConstructorThrowsExceptionOnNullTitle() {
        new Event(aCategory(), null, aSummary(), aDate(), aLocation());
    }

    @Test(expected = NullPointerException.class)
    public void testThatConstructorThrowsExceptionOnNullSummary() {
        new Event(aCategory(), aTitle(), null, aDate(), aLocation());
    }

    @Test(expected = NullPointerException.class)
    public void testThatConstructorThrowsExceptionOnNullDate() {
        new Event(aCategory(), aTitle(), aSummary(), null, aLocation());
    }

    @Test(expected = NullPointerException.class)
    public void testThatConstructorThrowsExceptionOnNullLocation() {
        new Event(aCategory(), aTitle(), aSummary(), aDate(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatConstructorThrowsExceptionOnEmptyTitle() {
        new Event(aCategory(), emptyTitle(), aSummary(), aDate(), aLocation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatConstructorThrowsExceptionOnTooLongTitle() {
        new Event(aCategory(), tooLongTitle(), aSummary(), aDate(), aLocation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatConstructorThrowsExceptionOnEmptySummary() {
        new Event(aCategory(), aTitle(), emptySummary(), aDate(), aLocation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatConstructorThrowsExceptionOnTooLongSummary() {
        new Event(aCategory(), aTitle(), tooLongSummary(), aDate(), aLocation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatConstructorThrowsExceptionOnEmptLocation() {
        new Event(aCategory(), aTitle(), aSummary(), aDate(), emptyLocation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatConstructorThrowsExceptionOnTooLongLocation() {
        new Event(aCategory(), aTitle(), aSummary(), aDate(), tooLongLocation());
    }

    @Test
    public void testSetCategoryWithValidCategory() {
        final Event event = validEvent();

        event.setCategory(anotherCategory());
        assertThat(event.getCategory(), is(equalTo(anotherCategory())));
    }

    @Test(expected = NullPointerException.class)
    public void testThatSetCategoryThrowsExceptionOnNullCategory() {
        validEvent().setCategory(null);
    }

    @Test
    public void testSetTitleWithValidTitles() {
        final String[] validTitles = {
            anotherTitle(), longestValidTitle(), shortestValidTitle()
        };

        stream(validTitles).forEach(title -> {
            final Event event = validEvent();
            event.setTitle(title);
            assertThat(event.getTitle(), is(equalToIgnoringCase(title)));
        });
    }

    @Test(expected = NullPointerException.class)
    public void testThatSetTitleThrowsExceptionOnNullTitle() {
        validEvent().setTitle(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatSetTitleThrowsExceptionOnEmptyTitle() {
        validEvent().setTitle(emptyTitle());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatSetTitleThrowsExceptionOnTooLongTitle() {
        validEvent().setTitle(tooLongTitle());
    }

    @Test
    public void testSetSummaryWithValidSummaries() {
        final String[] validSummaries = {
            anotherSummary(), longestValidSummary(), shortestValidSummary()
        };

        stream(validSummaries).forEach(summary -> {
            final Event event = validEvent();
            event.setSummary(summary);
            assertThat(event.getSummary(), is(equalToIgnoringCase(summary)));
        });
    }

    @Test(expected = NullPointerException.class)
    public void testThatSetSummaryThrowsExceptionOnNullSummary() {
        validEvent().setSummary(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatSetSummaryThrowsExceptionOnEmptySummary() {
        validEvent().setSummary(emptySummary());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatSetSummaryThrowsExceptionOnTooLongSummary() {
        validEvent().setSummary(tooLongSummary());
    }

    @Test
    public void testSetDateWithValidDate() {
        final Event event = validEvent();

        event.setDate(anotherDate());
        assertThat(event.getDate(), is(equalTo(anotherDate())));
    }

    @Test(expected = NullPointerException.class)
    public void testThatSetDateThrowsExceptionOnNullDate() {
        validEvent().setDate(null);
    }

    @Test
    public void testSetLocationWithValidLocations() {
        final String[] validLocations = {
            anotherLocation(), longestValidLocation(), shortestValidLocation()
        };

        stream(validLocations).forEach(location -> {
            final Event event = validEvent();
            event.setLocation(location);
            assertThat(event.getLocation(), is(equalToIgnoringCase(location)));
        });
    }

    @Test(expected = NullPointerException.class)
    public void testThatSetLocationThrowsExceptionOnNullLocation() {
        validEvent().setLocation(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatSetLocationThrowsExceptionOnEmptyLocation() {
        validEvent().setLocation(emptyLocation());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatSetLocationThrowsExceptionOnTooLongLocation() {
        validEvent().setLocation(tooLongLocation());
    }

    @Test
    public void testSetOwnerWithValidOwners() {
        final User[] validOwners = { anOwner(), anotherOwner() };

        stream(validOwners).forEach(owner -> {
            final Event event = validEvent();
            event.setOwner(owner);
            assertThat(event.getOwner(), is(equalTo(owner)));
        });
    }

    @Test(expected = NullPointerException.class)
    public void testThatSetOwnerThrowsExceptionOnNullOwner() {
        validEvent().setOwner(null);
    }
    
    @Test
    public void testSetCancelled() {
        final Event event = validEvent();

        event.setCancelled(true);
        assertThat(event.isCancelled(), is(equalTo(true)));
    }
    

    @Test(expected = UnsupportedOperationException.class)
    public void testThatGetAttendeesReturnsAnUnmodifiableSet() {
        validEvent().getAttendees().add(anAttendee());
    }

    @Test
    public void testAddAttendeeWithValidAttendees() {
        final Event event = validEvent();

        stream(someAttendees()).forEach(attendee -> {
            assertThat(event.getAttendees(), not(hasItem(attendee)));
            event.addAttendee(attendee);
            assertThat(event.getAttendees(), hasItem(attendee));
        });

        assertThat(event.getAttendees(), containsInAnyOrder(someAttendees()));
    }

    @Test(expected = NullPointerException.class)
    public void testThatAddAttendeeThrowsExceptionOnNullAttendee() {
        validEvent().addAttendee(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatAddAttendeeThrowsExceptionOnAlreadyAddedAttendee() {
        final Event event = validEvent();
        event.addAttendee(anAttendee());
        event.addAttendee(anAttendee());
    }

    @Test
    public void testRemoveAttendeeWithValidAttendees() {
        final Event event = validEvent();
        stream(someAttendees()).forEach(event::addAttendee);

        stream(someAttendees()).forEach(attendee -> {
            assertThat(event.getAttendees(), hasItem(attendee));
            event.removeAttendee(attendee);
            assertThat(event.getAttendees(), not(hasItem(attendee)));
        });

        assertThat(event.getAttendees(), is(empty()));
    }

    @Test(expected = NullPointerException.class)
    public void testThatRemoveAttendeeThrowsExceptionOnNullAttendee() {
        validEvent().removeAttendee(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatRemoveAttendeeThrowsExceptionOnNonExistentAttendee() {
        validEvent().removeAttendee(anAttendee());
    }

    @Test
    public void testThatCountAttendeesIsInSyncWithGetAttendees() {
        final Event event = validEvent();

        stream(someAttendees()).forEach(event::addAttendee);
        assertThat(event.countAttendees(), is(someAttendees().length));
        assertThat(event.getAttendees()  , hasSize(event.countAttendees()));

        stream(someAttendees()).forEach(event::removeAttendee);
        assertThat(event.countAttendees(), is(0));
        assertThat(event.getAttendees()  , hasSize(event.countAttendees()));
    }

    @Test
    public void testThatHasAttendeeReturnsTrueIfAttendeeIsPresent() {
        final Event event = validEvent();
        event.addAttendee(anAttendee());
        assertThat(event.hasAttendee(anAttendee()), is(true));
    }

    @Test
    public void testThatHasAttendeeReturnsFalseIfAttendeeIsNotPresent() {
        assertThat(validEvent().hasAttendee(anAttendee()), is(false));
    }

    @Test(expected = NullPointerException.class)
    public void testThatHasAttendeeThrowsExceptionOnNullAttendee() {
        validEvent().hasAttendee(null);
    }

    @Test
    public void testThatHasAtendeeIsInSyncWithGetAttendees() {
        final Event event = validEvent();

        stream(someAttendees()).forEach(event::addAttendee);
        stream(someAttendees()).forEach(
            attendee -> assertThat(event.hasAttendee(attendee), is(true))
        );

        stream(someAttendees()).forEach(event::removeAttendee);
        stream(someAttendees()).forEach(
            attendee -> assertThat(event.hasAttendee(attendee), is(false))
        );
    }

    @Test
    public void testEqualsHashCodeContract() {
        EqualsVerifier.forClass(Event.class).suppress(
            Warning.NULL_FIELDS,
            Warning.NONFINAL_FIELDS
        ).verify();
    }

}
