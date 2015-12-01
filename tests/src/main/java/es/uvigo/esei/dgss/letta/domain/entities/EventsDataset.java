package es.uvigo.esei.dgss.letta.domain.entities;

import java.util.Date;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.Arrays.asList;
import static java.util.Arrays.copyOf;
import static java.util.Arrays.stream;

import static es.uvigo.esei.dgss.letta.domain.entities.EventType.*;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.existentUser;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.users;

/**
 * Utility test class that contains a set of {@link Event} entities. This
 * dataset is replicated in the {@code event*.xml} dataset files.
 *
 * @author Jesús Álvarez Casanova
 * @author Adrián Rodríguez Fariña
 * @author Alberto Gutiérrez Jácome
 */
public final class EventsDataset {

    // Disallow construction
    private EventsDataset() { }

    /**
     * An array of twenty-five events that should exist in the database. This
     * data is replicated in the {@code events.xml} file.
     *
     * @return An array of twenty-five events.
     */
    public static Event[] events() {
        final User[] users = users();

        return new Event[] {
            new Event( 1, LITERATURE, "Example1 literature", "This is a description literature 1", new Date(946684861000L), "Location X", users[0]),
            new Event( 2, LITERATURE, "Example2 literature", "This is a description literature 2", new Date(946684861000L), "Location X", users[0]),
            new Event( 3,      MUSIC, "Example1 music"     , "This is a description music 1"     , new Date(946684861000L), "Location X", users[0]),
            new Event( 4,      MUSIC, "Example2 music"     , "This is a description music 2"     , new Date(946684861000L), "Location X", users[0]),
            new Event( 5,     CINEMA, "Example1 cinema"    , "This is a description cinema 1"    , new Date(946684861000L), "Location X", users[0]),
            new Event( 6,     CINEMA, "Example2 cinema"    , "This is a description cinema 2"    , new Date(946684861000L), "Location X", users[1]),
            new Event( 7,         TV, "Example1 tv"        , "This is a description tv 1"        , new Date(946684861000L), "Location X", users[1]),
            new Event( 8,         TV, "Example2 tv"        , "This is a description tv 2"        , new Date(946684861000L), "Location X", users[1]),
            new Event( 9,     SPORTS, "Example1 sports"    , "This is a description sports 1"    , new Date(946684861000L), "Location X", users[1]),
            new Event(10,     SPORTS, "Example2 sports"    , "This is a description sports 2"    , new Date(946684861000L), "Location X", users[1]),
            new Event(11,   INTERNET, "Example1 internet"  , "This is a description internet 1"  , new Date(946684861000L), "Location X", users[2]),
            new Event(12,   INTERNET, "Example2 internet"  , "This is a description internet 2"  , new Date(946684861000L), "Location X", users[2]),
            new Event(13,    TRAVELS, "Example1 travels"   , "This is a description travels 1"   , new Date(946684861000L), "Location X", users[2]),
            new Event(14,    TRAVELS, "Example2 travels"   , "This is a description travels 2"   , new Date(946684861000L), "Location X", users[2]),
            new Event(15,    THEATRE, "Example1 theatre"   , "This is a description theatre 1"   , new Date(946684861000L), "Location X", users[2]),
            new Event(16,    THEATRE, "Example2 theatre"   , "This is a description theatre 2"   , new Date(946684861000L), "Location X", users[3]),
            new Event(17,     SPORTS, "Example3 sports"    , "This is a description sports 3"    , new Date(946684861000L), "Location X", users[3]),
            new Event(18,   INTERNET, "Example3 internet"  , "This is a description internet 3"  , new Date(946684861000L), "Location X", users[3]),
            new Event(19,    TRAVELS, "Example3 travels"   , "This is a description travels 3"   , new Date(946684861000L), "Location X", users[3]),
            new Event(20,     CINEMA, "Example3 cinema"    , "This is a description cinema 3"    , new Date(946684861000L), "Location X", users[3]),
            new Event(21,         TV, "Example3 tv"        , "This is a description tv 3"        , new Date(946684861000L), "Location X", users[4]),
            new Event(22,      MUSIC, "Example3 music"     , "This is a description music 3"     , new Date(946684861000L), "Location X", users[4]),
            new Event(23, LITERATURE, "Example3 literature", "This is a description literature 3", new Date(946684861000L), "Location X", users[4]),
            new Event(24, LITERATURE, "Example4 literature", "This is a description literature 4", new Date(946684861000L), "Location X", users[4]),
            new Event(25, LITERATURE, "Example5 literature", "This is a description literature 5", new Date(946684861000L), "Location X", users[4])
        };
    }

    /**
     * Returns the {@link #events()} that satisfy a given {@link Predicate}.
     *
     * @param f The predicate that every resulting {@link Event} must satisfy.
     *
     * @return An array with the Events that satisfy the given predicate.
     */
    public static Event[] filterEvents(final Predicate<Event> f) {
        return stream(events()).filter(f).toArray(Event[]::new);
    }

    /**
     * Returns an {@link Optional} holding or not the first found {@link Event}
     * in {@link #events()} that satisfies a given {@link Predicate}.
     *
     * @param f The predicate that the resulting {@link Event} must satisfy.
     *
     * @return An optional event that satisfies the given predicate.
     */
    public static Optional<Event> findEvent(final Predicate<Event> f) {
        return stream(events()).filter(f).findFirst();
    }

    /**
     * Returns the first {@link Event} from {@link #events()} that satisfies the
     * given {@link Predicate}.
     *
     * @param f The predicate that the resulting {@link Event} must satisfy.
     *
     * @return An Event that satisfies the given predicate.
     *
     * @throws IllegalArgumentException if no Event satisfies the predicate.
     */
    public static Event findEventOrThrow(final Predicate<Event> f) {
        return findEvent(f).orElseThrow(() -> new IllegalArgumentException(
            "No event found satisfying the specified search predicate."
        ));
    }

    /**
     * Returns an Event from the {@link #events()} array that has the provided
     * title.
     *
     * @param title The title of the {@link Event} to be returned.
     *
     * @return An Event with the provided login.
     *
     * @throws IllegalArgumentException if there is no event with the provided
     *         title.
     */
    public static Event eventWithTitle(final String title) {
        return findEventOrThrow(e -> e.getTitle().equals(title));
    }

    /**
     * Returns an {@link Event} from the {@link #events()} array that has the
     * provided {@link EventType type}.
     *
     * @param type The {@link EventType} of the {@link Event} to be returned.
     *
     * @return An Event with the provided type.
     *
     * @throws IllegalArgumentException if there is no event with the provided
     *         type.
     */
    public static Event eventWithType(final EventType type) {
        return findEventOrThrow(e -> e.getEventType().equals(type));
    }

    /**
     * Returns the unique {@link Event} from the {@link #events()} array that
     * has the provided ID.
     *
     * @param id The id of the {@link Event} to be returned, as an integer.
     *
     * @return The unique {@link Event} with the provided ID.
     *
     * @throws IllegalArgumentException if there is no event with the provided
     *         ID.
     */
    public static Event eventWithId(final int id) {
        return findEventOrThrow(e -> e.getId() == id);
    }

    /**
     * An array of less than five events that should exist in the database. This
     * data is replicated in the {@code events-less-than-five.xml} file.
     *
     * @return An array of less than five events.
     */
    public static Event[] lessThanFiveEvents() {
        return copyOf(events(), 4);
    }

    /**
     * An array of less than twenty events that should exist in the database.
     * This data is replicated in the {@code events-less-than-twenty.xml} file.
     *
     * @return An array of less than twenty events.
     */
    public static Event[] lessThanTwentyEvents() {
        return copyOf(events(), 19);
    }

    /**
     * Returns an {@link Event} that should exist in the database.
     *
     * @return An {@link Event} that should exist in the database.
     */
    public static Event existentEvent() {
        return events()[0];
    }

    /**
     * Returns the ID of a {@link Event} that should exist in the database. This
     * is the id of the event returned by {@link #existentEvent()}.
     *
     * @return The id of a event that should exist in the database.
     */
    public static int existentEventId() {
        return existentEvent().getId();
    }

    /**
     * Returns an {@link Event} ID that should not exist in the database.
     *
     * @return an event ID that should not exist in the database.
     */
    public static int nonExistentEventId() {
        return events().length + 42;
    }

    /**
     * Returns an {@link Event} title that should not exist in the database.
     *
     * @return an event title that should not exist in the database.
     */
    public static String nonExistentTitle() {
        return "Example12 travels";
    }

    /**
     * Returns an {@link Event} description that should not exist in the
     * database.
     *
     * @return an event description that should not exist in the database.
     */
    public static String nonExistentDescription() {
        return "This is a description travels 12";
    }

    /**
     * Returns an {@link Event} that should not exist in the database and that
     * may be used to create a new event. This includes a {@link User} as
     * creator, retrieved from {@link UsersDataset#existentUser()}.
     *
     * @return An event that should not exist in the database.
     */
    public static Event newEvent() {
        return new Event(
            TRAVELS,
            "New travels event",
            "New travels description",
            new Date(587684861000L),
            "the inexistent location",
            existentUser()
        );
    }

    /**
     * Returns an {@link Event} that should not exist in the database and that
     * may be used to create a new event. This does not include a creator, if
     * that is required, consider using {@link #newEvent()} instead.
     *
     * @return An event that should not exist in the database, with it's
     *         {@link Event#setCreator(User) creator} not set.
     */
    public static Event newEventWithoutCreator() {
        return new Event(
            LITERATURE,
            "New literature event",
            "This is a description",
            new Date(946684861000L),
            "Location X"
        );
    }

    /**
     * An array of twenty-five events that should exist in the database, and
     * with already joined users (two per event) to each event. This data is
     * replicated in the {@code users-joins-event.xml} file.
     *
     * @return An array of twenty-five events with two users joined to each one.
     */
    public static Event[] eventsWithTwoJoinedUsers() {
        final User[] users = users();

        final User[][] groups = new User[][] {
            new User[] { users[1], users[2] },
            new User[] { users[0], users[3] },
            new User[] { users[3], users[4] },
            new User[] { users[0], users[1] },
            new User[] { users[0], users[2] }
        };

        return stream(events()).map(event -> {
            final int group = (event.getId() - 1) / 5;
            event.getEventsJoinedByUsers().addAll(asList(groups[group]));
            return event;
        }).toArray(Event[]::new);
    }

}
