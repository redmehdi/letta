package es.uvigo.esei.dgss.letta.domain.entities;

import java.time.LocalDateTime;

import es.uvigo.esei.dgss.letta.domain.entities.Event.Category;

import static org.apache.commons.lang3.RandomStringUtils.random;

import static es.uvigo.esei.dgss.letta.domain.entities.Role.USER;

/**
 * Test utility class with a set of parameters to be used when testing
 * {@link Event} entities.
 *
 * @author Alberto Gutiérrez Jácome
 */
final class EventParameters {

    // Disallow construction
    private EventParameters() { }

    /**
     * Returns a valid title.
     *
     * @return a valid event title.
     */
    public static String aTitle() {
        return "An event title";
    }

    /**
     * Returns a valid title different from {@link #aTitle()}.
     *
     * @return a valid event title different from {@link #aTitle()}.
     */
    public static String anotherTitle() {
        return "Another event title";
    }

    /**
     * Returns the shortest length valid title.
     *
     * @return a valid title with the shortest valid length.
     */
    public static String shortestValidTitle() {
        return random(1);
    }

    /**
     * Returns the longest length valid title.
     *
     * @return a valid title with the longest valid lenght.
     */
    public static String longestValidTitle() {
        return random(20);
    }

    /**
     * Returns an empty title (empty String).
     *
     * @return an empty login (empty String).
     */
    public static String emptyTitle() {
        return "";
    }

    /**
     * Returns a title whose length exceeds the maximum allowed.
     *
     * @return a title whose length exceeds the maximum allowed.
     */
    public static String tooLongTitle() {
        return random(21);
    }
    
    /**
     * Returns a valid description.
     *
     * @return a valid event description.
     */
    public static String aDescription() {
        return "This is an event summary";
    }

    /**
     * Returns a valid summary.
     *
     * @return a valid event summary.
     */
    public static String aSummary() {
        return "This is an event summary";
    }

    /**
     * Returns a valid summary different from {@link #aSummary()}.
     *
     * @return a valid event summary different from {@link #aSummary()}.
     */
    public static String anotherSummary() {
        return "This is another, distinct, event summary";
    }

    /**
     * Returns the shortest length valid summary.
     *
     * @return a valid summary with the shortest valid length.
     */
    public static String shortestValidSummary() {
        return random(1);
    }

    /**
     * Returns the longest length valid summary.
     *
     * @return a valid summary with the longest valid length.
     */
    public static String longestValidSummary() {
        return random(50);
    }

    /**
     * Returns an empty summary (empty String).
     *
     * @return an empty summary (empty String).
     */
    public static String emptySummary() {
        return "";
    }

    /**
     * Returns a summary whose length exceeds the maximum allowed.
     *
     * @return a summary whose length exceeds the maximum allowed.
     */
    public static String tooLongSummary() {
        return random(51);
    }

    /**
     * Returns a valid event date.
     *
     * @return a valid event date.
     */
    public static LocalDateTime aDate() {
        return LocalDateTime.of(2015, 11, 30, 21, 45);
    }

    /**
     * Returns a valid date different from {@link #aDate()}.
     *
     * @return a valid event date different from {@link #aDate()}.
     */
    public static LocalDateTime anotherDate() {
        return LocalDateTime.of(2016, 2, 17, 15, 30);
    }

    /**
     * Returns a valid event location.
     *
     * @return a valid event location.
     */
    public static String aLocation() {
        return "An event location";
    }

    /**
     * Returns a valid location different from {@link #aLocation()}.
     *
     * @return a valid location different from {@link #aLocation()}.
     */
    public static String anotherLocation() {
        return "Event location II";
    }

    /**
     * Returns the shortest length valid location.
     *
     * @return a valid location with the shortest valid length.
     */
    public static String shortestValidLocation() {
        return random(1);
    }

    /**
     * Returns the longest length valid location.
     *
     * @return a valid location with the shortest valid length.
     */
    public static String longestValidLocation() {
        return random(20);
    }

    /**
     * Returns an empty location (empty String).
     *
     * @return an empty location (empty String).
     */
    public static String emptyLocation() {
        return "";
    }

    /**
     * Returns a location whose length exceeds the maximum allowed.
     *
     * @return a location whose length exceeds the maximum allowed.
     */
    public static String tooLongLocation() {
        return random(101);
    }

    /**
     * Returns a valid event category.
     *
     * @return a valid event category.
     */
    public static Category aCategory() {
        return Category.MOVIES;
    }

    /**
     * Returns a valid event category different from {@link #aCategory()}.
     *
     * @return a valid event category different from {@link #aCategory()}.
     */
    public static Category anotherCategory() {
        return Category.BOOKS;
    }

    /**
     * Returns a valid event owner.
     *
     * @return a valid event owner.
     */
    public static User anOwner() {
        return new User("john", "johnpass", "john@email.com", USER);
    }

    /**
     * Returns a valid event owner different from {@link #anOwner()}.
     *
     * @return a valid event owner different from {@link #anOwner()}.
     */
    public static User anotherOwner() {
        return new User("anne", "annepass", "anne@email.com", USER);
    }

    /**
     * Returns an array of {@link User Users} to be used as event attendees.
     *
     * @return an array of event attendees.
     */
    public static User[] someAttendees() {
        return new User[] {
            new User("john", "johnpass", "john@email.com"),
            new User("anne", "annepass", "anne@email.com"),
            new User("mary", "marypass", "mary@email.com"),
            new User("joan", "joanpass", "joan@email.com"),
            new User("mike", "mikepass", "mike@email.com")
        };
    }

    /**
     * Returns a single {@link User} to be used as event attendee. It is
     * extracted from the first user of {@link #someAttendees()}.
     *
     * @return a single event attendee.
     */
    public static User anAttendee() {
        return someAttendees()[0];
    }

    /**
     * Returns a valid event with the {@link #aCategory()}, {@link #aTitle()},
     * {@link #aSummary()}, {@link #aDate()} and {@link #aLocation()}
     * parameters.
     *
     * @return A valid event with the {@link #aCategory()}, {@link #aTitle()},
     *         {@link #aSummary()}, {@link #aDate()} and {@link #aLocation()}
     *         parameters.
     */
    public static Event validEvent() {
        return new Event(aCategory(), aTitle(), aSummary(), aDate(), aLocation(), aDescription());
    }

}
