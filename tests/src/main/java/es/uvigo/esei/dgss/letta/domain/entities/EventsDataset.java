package es.uvigo.esei.dgss.letta.domain.entities;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import es.uvigo.esei.dgss.letta.domain.entities.Event.Category;

import static java.util.Arrays.copyOf;
import static java.util.Arrays.stream;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import static es.uvigo.esei.dgss.letta.domain.entities.Event.Category.*;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.users;


/**
 * Utility test class that contains a set of {@link Event} entities. This
 * dataset is replicated in the {@code event*.xml} dataset files. Bear in mind
 * that every {@link Event} requires an {@link User} to be set as its
 * {@link Event#getOwner() owner}, so whenever a {@code event*.xml} dataset is
 * included, <strong>it is required that the {@code users.xml} dataset is also
 * present</strong>.
 *
 * @author Jesús Álvarez Casanova
 * @author Adrián Rodríguez Fariña
 * @author Alberto Gutiérrez Jácome
 * @author Borja Cordeiro González
 */
public final class EventsDataset {

	
    // Disallow construction
    private EventsDataset() { }
    
    /**
     * An array of five events that should exist in the database. This
     * data is replicated in the {@code events.xml} file.
     * 
     * @return An array of five events
     */
    public static Event[] futureEvents() {
    	final User[] users = users();
    	final LocalDateTime futureDate = LocalDateTime.of(2020, 1, 1, 1, 1, 1);
    	final LocalDateTime date = LocalDateTime.of(2000, 1, 1, 1, 1, 1);
    	return new Event[] {
    			    new Event( 1, BOOKS,      "Example1 literature", "This is a summary of literature 1", futureDate, "Location X", users[0], emptySet(), false, "This is a long description, with a max. size one thousand, of literature 1", "Segovia", date),
    	            new Event( 2, BOOKS,      "Example2 literature", "This is a summary of literature 2", futureDate, "Location X", users[0], emptySet(), false, "This is a long description, with a max. size one thousand, of literature 2", "Segovia", date),
    	            new Event( 3, MUSIC,      "Example1 music",      "This is a summary of music 1",      futureDate, "Location X", users[0], emptySet(), false, "This is a long description, with a max. size one thousand, of music 1"     , "Segovia", date),
    	            new Event( 4, MUSIC,      "Example2 music",      "This is a summary of music 2",      futureDate, "Location X", users[0], emptySet(), false, "This is a long description, with a max. size one thousand, of music 2"     , "Segovia", date),
    	            new Event( 5, MOVIES,     "Example1 cinema",     "This is a summary of cinema 1",     futureDate, "Location X", users[0], emptySet(), false, "This is a long description, with a max. size one thousand, of cinema 1"    , "Segovia", date),
    	         };
    }

    /**
     * An array of twenty-five events that should exist in the database. This
     * data is replicated in the {@code events.xml} file.
     *
     * @return An array of twenty-five events.
     */
    public static Event[] events() {
        final User[] users = users();

        final LocalDateTime date = LocalDateTime.of(2000, 1, 1, 1, 1, 1);
        final LocalDateTime futureDate = LocalDateTime.of(2020, 1, 1, 1, 1, 1);

        return new Event[] {
            new Event( 1, BOOKS,      "Example1 literature", "This is a summary of literature 1", futureDate, "Location X", users[0], emptySet(), false, "This is a long description, with a max. size one thousand, of literature 1", "Segovia", date),
            new Event( 2, BOOKS,      "Example2 literature", "This is a summary of literature 2", futureDate, "Location X", users[0], emptySet(), false, "This is a long description, with a max. size one thousand, of literature 2", "Segovia", date),
            new Event( 3, MUSIC,      "Example1 music",      "This is a summary of music 1",      futureDate, "Location X", users[0], emptySet(), false, "This is a long description, with a max. size one thousand, of music 1"     , "Segovia", date),
            new Event( 4, MUSIC,      "Example2 music",      "This is a summary of music 2",      futureDate, "Location X", users[0], emptySet(), false, "This is a long description, with a max. size one thousand, of music 2"     , "Segovia", date),
            new Event( 5, MOVIES,     "Example1 cinema",     "This is a summary of cinema 1",     futureDate, "Location X", users[0], emptySet(), false, "This is a long description, with a max. size one thousand, of cinema 1"    , "Segovia", date),
            new Event( 6, MOVIES,     "Example2 cinema",     "This is a summary of cinema 2",     date,       "Location X", users[1], emptySet(), false, "This is a long description, with a max. size one thousand, of cinema 2"    , "Segovia", date),
            new Event( 7, TELEVISION, "Example1 tv",         "This is a summary of tv 1",         date,       "Location X", users[1], emptySet(), false, "This is a long description, with a max. size one thousand, of tv 1"        , "Segovia", date),
            new Event( 8, TELEVISION, "Example2 tv",         "This is a summary of tv 2",         date,       "Location X", users[1], emptySet(), false, "This is a long description, with a max. size one thousand, of tv 2"        , "Segovia", date),
            new Event( 9, SPORTS,     "Example1 sports",     "This is a summary of sports 1",     date,       "Location X", users[1], emptySet(), false, "This is a long description, with a max. size one thousand, of sports 1"    , "Segovia", date),
            new Event(10, SPORTS,     "Example2 sports",     "This is a summary of sports 2",     date,       "Location X", users[1], emptySet(), false, "This is a long description, with a max. size one thousand, of sports 2"    , "Segovia", date),
            new Event(11, INTERNET,   "Example1 internet",   "This is a summary of internet 1",   date,       "Location X", users[2], emptySet(), false, "This is a long description, with a max. size one thousand, of internet 1"  , "Segovia", date),
            new Event(12, INTERNET,   "Example2 internet",   "This is a summary of internet 2",   date,       "Location X", users[2], emptySet(), false, "This is a long description, with a max. size one thousand, of internet 2"  , "Segovia", date),
            new Event(13, TRAVELS,    "Example1 travels",    "This is a summary of travels 1",    date,       "Location X", users[2], emptySet(), false, "This is a long description, with a max. size one thousand, of travels 1"   , "Segovia", date),
            new Event(14, TRAVELS,    "Example2 travels",    "This is a summary of travels 2",    date,       "Location X", users[2], emptySet(), false, "This is a long description, with a max. size one thousand, of travels 2"   , "Segovia", date),
            new Event(15, THEATRE,    "Example1 theatre",    "This is a summary of theatre 1",    date,       "Location X", users[2], emptySet(), false, "This is a long description, with a max. size one thousand, of theatre 1"   , "Segovia", date),
            new Event(16, THEATRE,    "Example2 theatre",    "This is a summary of theatre 2",    date,       "Location X", users[3], emptySet(), false, "This is a long description, with a max. size one thousand, of theatre 2"   , "Segovia", date),
            new Event(17, SPORTS,     "Example3 sports",     "This is a summary of sports 3",     date,       "Location X", users[3], emptySet(), false, "This is a long description, with a max. size one thousand, of sports 3"    , "Segovia", date),
            new Event(18, INTERNET,   "Example3 internet",   "This is a summary of internet 3",   date,       "Location X", users[3], emptySet(), false, "This is a long description, with a max. size one thousand, of internet 3"  , "Segovia", date),
            new Event(19, TRAVELS,    "Example3 travels",    "This is a summary of travels 3",    date,       "Location X", users[3], emptySet(), false, "This is a long description, with a max. size one thousand, of travels 3"   , "Segovia", date),
            new Event(20, MOVIES,     "Example3 cinema",     "This is a summary of cinema 3",     date,       "Location X", users[3], emptySet(), false, "This is a long description, with a max. size one thousand, of cinema 3"    , "Segovia", date),
            new Event(21, TELEVISION, "Example3 tv",         "This is a summary of tv 3",         date,       "Location X", users[4], emptySet(), true , "This is a long description, with a max. size one thousand, of tv 3"        , "Segovia", date),
            new Event(22, MUSIC,      "Example3 music",      "This is a summary of music 3",      date,       "Location X", users[4], emptySet(), true , "This is a long description, with a max. size one thousand, of music 3"     , "Segovia", date),
            new Event(23, BOOKS,      "Example3 literature", "This is a summary of literature 3", date,       "Location X", users[4], emptySet(), true , "This is a long description, with a max. size one thousand, of literature 3", "Segovia", date),
            new Event(24, BOOKS,      "Example4 literature", "This is a summary of literature 4", date,       "Location X", users[4], emptySet(), true , "This is a long description, with a max. size one thousand, of literature 4", "Segovia", date),
            new Event(25, BOOKS,      "Example5 literature", "This is a summary of literature 5", date,       "Location X", users[4], emptySet(), true , "This is a long description, with a max. size one thousand, of literature 5", "Segovia", date)
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
     * Transforms the {@link #events()} array with a given {@link Function},
     * returning the resulting values in a {@link List}.
     *
     * @param <A> The type of the resulting mapping function.
     * @param f The mapper {@link Function} from {@link Event} to any type.
     *
     * @return A {@link List} with the results of applying the given function to
     *         all the {@link #events()}.
     */
    public static <A> List<A> mapEvents(final Function<Event, A> f) {
        return stream(events()).map(f).collect(toList());
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
     * @throws IllegalArgumentException If no Event satisfies the predicate.
     */
    public static Event findEventOrThrow(final Predicate<Event> f) {
        return findEvent(f).orElseThrow(() -> new IllegalArgumentException(
                "No event found satisfying the specified search predicate."));
    }

    /**
     * Returns the {@link #events()} array sorted with a given
     * {@link Comparator}.
     *
     * @param cmp The {@link Comparator} to sort the {@link Event Events}.
     *
     * @return An array with events sorted with the given comparator.
     */
    public static Event[] sortedEvents(final Comparator<Event> cmp) {
        return stream(events()).sorted(cmp).toArray(Event[]::new);
    }

    /**
     * Returns the {@link #events()} array sorted by a given transformation from
     * {@link Event} to {@link Comparable}.
     *
     * @param f The function mapping an {@link Event} to a {@link Comparable}
     *        instance.
     * @param <C> The type of the {@link Comparable} by which an {@link Event}
     *        should be compared.
     *
     * @return An array with the evens sorted by the given comparable.
     */
    public static <C extends Comparable<C>> Event[] sortedEvents(
            final Function<Event, C> f) {
        return sortedEvents((e1, e2) -> f.apply(e1).compareTo(f.apply(e2)));
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
     * provided {@link Category}.
     *
     * @param cat The {@link Category} of the {@link Event} to be returned.
     *
     * @return An Event with the provided category.
     *
     * @throws IllegalArgumentException If there is no event with the provided
     *         category.
     */
    public static Event eventWithCategory(final Category cat) {
        return findEventOrThrow(e -> e.getCategory().equals(cat));
    }

    /**
     * Returns the unique {@link Event} from the {@link #events()} array that
     * has the provided ID.
     *
     * @param id The id of the {@link Event} to be returned, as an integer.
     *
     * @return The unique {@link Event} with the provided ID.
     *
     * @throws IllegalArgumentException
     *             if there is no event with the provided ID.
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
     * Returns an {@link Event} that should exist in the database and was cancelled.
     *
     * @return An {@link Event} that should exist in the database and was cancelled.
     */
    public static int cancelledEventId() {
        Event event = events()[24];
        return event.getId();
    }
        
    /**
     * Returns an {@link Event} that has be modified.
     *
     * @return An {@link Event} that has be modified.
     */
    public static Event updatedEvent() {
        final Event modified = events()[7];
        modified.setTitle("New title");
        return modified;
    }
    
    public static Event nonExistentEvent() {
        return new Event( 
    		33, 
    		BOOKS, 
    		"Example1 literature", 
    		"This is a summary of literature 1",
    		LocalDateTime.of(2000, 1, 1, 1, 1, 1), 
    		"Location X",
    		users()[1],
    		emptySet(),
    		false, 
    		"This is a long description, with a max. size one thousand, of literature 1",
    		"Segovia",
            LocalDateTime.of(2000, 1, 1, 1, 1, 1)
        );        
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
        return "This is a summary travels 12";
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
            BOOKS,
            "New literature event",
            "This is a summary",
            LocalDateTime.of(2000, 1, 1, 1, 1, 1),
            "Location X",
            "This is a long description, with a max. size one thousand",
            "Segovia",
            LocalDateTime.of(2000, 1, 1, 1, 1, 1)
        );
    }

    /**
     * Returns an {@link Event} that should not exist in the database and that
     * may be used to create a new event. This does not include an owner, if
     * that is required, consider using {@link #newEvent()} instead.
     *
     * @return An event that should not exist in the database, with it's
     *         {@link Event#setOwner(User) owner} not set.
     */
    public static Event newEventWithoutOwner() {
        return new Event(
            BOOKS,
            "New literature event",
            "This is a summary",
            LocalDateTime.of(2000, 1, 1, 1, 1, 1),
            "Location X",
            "This is a long description, with a max. size one thousand",
            "Segovia",            
            LocalDateTime.of(2000, 1, 1, 1, 1, 1)
        );
    } 

    /**
     * An array of twenty-five events that should exist in the database, and
     * with already attending users (two per event) to each event. This data is
     * replicated in the {@code event-attendees.xml} file.
     *
     * @return An array of twenty-five events with two users attending to each
     *         one.
     */
    public static Event[] eventsWithTwoAttendees() {
        final User[] users = users();

        @SuppressWarnings("unchecked")
        final Set<User>[] groups = new Set[] {
            Stream.of(users[1], users[2]).collect(toSet()),
            Stream.of(users[0], users[3]).collect(toSet()),
            Stream.of(users[2], users[4]).collect(toSet()),
            Stream.of(users[0], users[2]).collect(toSet()),
            Stream.of(users[0], users[1]).collect(toSet())
        };

        return stream(events()).map(event -> {
            final int index = (event.getId() - 1) / 5;

            return new Event(
                event.getId(),
                event.getCategory(),
                event.getTitle(),
                event.getSummary(),
                event.getDate(),
                event.getLocation(),
                event.getOwner(),
                groups[index],
                event.isCancelled(),
                event.getDescription(),
                event.getPlace(),
                event.getCreatedAt()
            );
        }).toArray(Event[]::new);
    }

    /**
     * Returns the {@link #eventsWithTwoAttendees()} that satisfy a given
     * {@link Predicate}.
     *
     * @param f The predicate that every resulting {@link Event} must satisfy.
     *
     * @return An array with the Events that satisfy the given predicate.
     */
    public static Event[] filterEventsWithTwoJoinedUsers(
        final Predicate<Event> f
    ) {
        return stream(eventsWithTwoAttendees())
              .filter(f)
              .toArray(Event[]::new);
    }

}
