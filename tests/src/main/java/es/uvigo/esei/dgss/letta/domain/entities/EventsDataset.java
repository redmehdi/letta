package es.uvigo.esei.dgss.letta.domain.entities;

import static es.uvigo.esei.dgss.letta.domain.entities.EventType.CINEMA;
import static es.uvigo.esei.dgss.letta.domain.entities.EventType.INTERNET;
import static es.uvigo.esei.dgss.letta.domain.entities.EventType.LITERATURE;
import static es.uvigo.esei.dgss.letta.domain.entities.EventType.MUSIC;
import static es.uvigo.esei.dgss.letta.domain.entities.EventType.SPORTS;
import static es.uvigo.esei.dgss.letta.domain.entities.EventType.THEATRE;
import static es.uvigo.esei.dgss.letta.domain.entities.EventType.TRAVELS;
import static es.uvigo.esei.dgss.letta.domain.entities.EventType.TV;
import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.users;
import static java.util.Arrays.stream;

import java.util.Date;

/**
 * 
 * Dataset to test Events
 * 
 * @author jacasanova
 * @author arfarinha
 */
public class EventsDataset {
	public static final String EXISTENT_TITLE_MUSIC = "Example1 music";
	public static final String EXISTENT_TITLE_CINEMA = "Example1 cinema";
	public static final String EXISTENT_TITLE_SPORTS = "Example1 sports";
	public static final String EXISTENT_TITLE_INTERNET = "Example1 internet";
	public static final String EXISTENT_TITLE_TRAVELS = "Example1 travels";
	public static final String EXISTENT_TITLE_THEATRE = "Example1 theatre";
	public static final String EXISTENT_TITLE_LITERATURE = "Example1 literature";
	public static final String EXISTENT_TITLE_TV = "Example1 tv";

	public static final String EXISTENT_DESCRIPTION_TRAVELS = "This is a description travels 1";

	public static final String NON_EXISTENT_TITLE_MUSIC = "Example12 music";
	public static final String NON_EXISTENT_TITLE_CINEMA = "Example12 cinema";
	public static final String NON_EXISTENT_TITLE_SPORTS = "Example12 sports";
	public static final String NON_EXISTENT_TITLE_INTERNET = "Example12 internet";
	public static final String NON_EXISTENT_TITLE_TRAVELS = "Example12 travels";
	public static final String NON_EXISTENT_TITLE_THEATRE = "Example12 theatre";

	public static final String NON_EXISTENT_DESCRIPTION_TRAVELS = "This is not a description travels 1";

	public static final User[] users = users();

	/**
	 * Returns an {@link Event} with {@code title}
	 * 
	 * @param title
	 *            indicates the {@link Event} title
	 * @return the {@link Event} with {@code title}
	 */
	public static Event eventWithTitle(final String title) {
		return stream(events()).filter(event -> event.getTitle().equals(title))
				.findFirst().orElseThrow(IllegalArgumentException::new);
	}

	/**
	 * Returns an {@link Event} with {@code eventType}
	 * 
	 * @param eventType
	 *            indicates the {@link EventType}
	 * @return the {@link Event} with {@code eventType}
	 */
	public static Event eventWithType(final EventType eventType) {
		return stream(events())
				.filter(event -> event.getEventType().equals(eventType))
				.findFirst().orElseThrow(IllegalArgumentException::new);
	}

	/**
	 * Returns all the {@link Event} with his creator {@link User}
	 * 
	 * @return all the {@link Event} with his creator {@link User}
	 */
	public static Event[] eventsWithUser() {
		return new Event[] {
				new Event(LITERATURE, EXISTENT_TITLE_LITERATURE,
						"This is a description literature 1",
						new Date(946684861000L), "Location X", users[0]),
				new Event(LITERATURE, "Example2 literature",
						"This is a description literature 1",
						new Date(946684861000L), "Location X", users[0]),
				new Event(MUSIC, EXISTENT_TITLE_MUSIC,
						"This is a description music 1",
						new Date(946684861000L), "Location X", users[0]),
				new Event(MUSIC, "Example2 music",
						"This is a description music 2",
						new Date(946684861000L), "Location X", users[0]),
				new Event(CINEMA, EXISTENT_TITLE_CINEMA,
						"This is a description cinema 1",
						new Date(946684861000L), "Location X", users[0]),
				new Event(CINEMA, "Example2 cinema",
						"This is a description cinema 2",
						new Date(946684861000L), "Location X", users[1]),
				new Event(TV, EXISTENT_TITLE_TV, "This is a description tv 1",
						new Date(946684861000L), "Location X", users[1]),
				new Event(TV, "Example2 tv", "This is a description tv 1",
						new Date(946684861000L), "Location X", users[1]),
				new Event(SPORTS, EXISTENT_TITLE_SPORTS,
						"This is a description sports 1",
						new Date(946684861000L), "Location X", users[1]),
				new Event(SPORTS, "Example2 tv",
						"This is a description sports 2",
						new Date(946684861000L), "Location X", users[1]),
				new Event(INTERNET, EXISTENT_TITLE_INTERNET,
						"This is a description internet 1",
						new Date(946684861000L), "Location X", users[2]),
				new Event(INTERNET, "Example2 internet",
						"This is a description internet 2",
						new Date(946684861000L), "Location X", users[2]),
				new Event(TRAVELS, EXISTENT_TITLE_TRAVELS,
						EXISTENT_DESCRIPTION_TRAVELS, new Date(946684861000L),
						"Location X", users[2]),
				new Event(TRAVELS, "Example2 travels",
						"This is a description travels 2",
						new Date(946684861000L), "Location X", users[2]),
				new Event(THEATRE, EXISTENT_TITLE_THEATRE,
						"This is a description theatre 1",
						new Date(946684861000L), "Location X", users[2]),
				new Event(THEATRE, "Example2 theatre",
						"This is a description theatre 2",
						new Date(946684861000L), "Location X", users[3]),
				new Event(SPORTS, "Example3 sports",
						"This is a description sports 3",
						new Date(946684861000L), "Location X", users[3]),
				new Event(INTERNET, "Example3 internet",
						"This is a description internet 3",
						new Date(946684861000L), "Location X", users[3]),
				new Event(TRAVELS, "Example3 travels",
						"This is a description travels 3",
						new Date(946684861000L), "Location X", users[3]),
				new Event(CINEMA, "Example3 cinema",
						"This is a description cinema 3",
						new Date(946684861000L), "Location X", users[3]),
				new Event(TV, "Example3 tv", "This is a description tv 3",
						new Date(946684861000L), "Location X", users[4]),
				new Event(MUSIC, "Example3 music",
						"This is a description music 3",
						new Date(946684861000L), "Location X", users[4]),
				new Event(LITERATURE, "Example3 literature",
						"This is a description literature 3",
						new Date(946684861000L), "Location X", users[4]),
				new Event(LITERATURE, "Example4 literature",
						"This is a description literature 4",
						new Date(946684861000L), "Location X", users[4]),
				new Event(LITERATURE, "Example5 literature",
						"This is a description literature 5",
						new Date(946684861000L), "Location X", users[4]) };
	}

	/**
	 * Returns all the {@link Event}
	 * 
	 * @return all the {@link Event}
	 */
	public static Event[] events() {
		return new Event[] {
				new Event(LITERATURE, EXISTENT_TITLE_LITERATURE,
						"This is a description literature 1",
						new Date(946684861000L), "Location X"),
				new Event(LITERATURE, "Example2 literature",
						"This is a description literature 1",
						new Date(946684861000L), "Location X"),
				new Event(MUSIC, EXISTENT_TITLE_MUSIC,
						"This is a description music 1",
						new Date(946684861000L), "Location X"),
				new Event(MUSIC, "Example2 music",
						"This is a description music 2",
						new Date(946684861000L), "Location X"),
				new Event(CINEMA, EXISTENT_TITLE_CINEMA,
						"This is a description cinema 1",
						new Date(946684861000L), "Location X"),
				new Event(CINEMA, "Example2 cinema",
						"This is a description cinema 2",
						new Date(946684861000L), "Location X"),
				new Event(TV, EXISTENT_TITLE_TV, "This is a description tv 1",
						new Date(946684861000L), "Location X"),
				new Event(TV, "Example2 tv", "This is a description tv 1",
						new Date(946684861000L), "Location X"),
				new Event(SPORTS, EXISTENT_TITLE_SPORTS,
						"This is a description sports 1",
						new Date(946684861000L), "Location X"),
				new Event(SPORTS, "Example2 tv",
						"This is a description sports 2",
						new Date(946684861000L), "Location X"),
				new Event(INTERNET, EXISTENT_TITLE_INTERNET,
						"This is a description internet 1",
						new Date(946684861000L), "Location X"),
				new Event(INTERNET, "Example2 internet",
						"This is a description internet 2",
						new Date(946684861000L), "Location X"),
				new Event(TRAVELS, EXISTENT_TITLE_TRAVELS,
						EXISTENT_DESCRIPTION_TRAVELS, new Date(946684861000L),
						"Location X"),
				new Event(TRAVELS, "Example2 travels",
						"This is a description travels 2",
						new Date(946684861000L), "Location X"),
				new Event(THEATRE, EXISTENT_TITLE_THEATRE,
						"This is a description theatre 1",
						new Date(946684861000L), "Location X"),
				new Event(THEATRE, "Example2 theatre",
						"This is a description theatre 2",
						new Date(946684861000L), "Location X"),
				new Event(SPORTS, "Example3 sports",
						"This is a description sports 3",
						new Date(946684861000L), "Location X"),
				new Event(INTERNET, "Example3 internet",
						"This is a description internet 3",
						new Date(946684861000L), "Location X"),
				new Event(TRAVELS, "Example3 travels",
						"This is a description travels 3",
						new Date(946684861000L), "Location X"),
				new Event(CINEMA, "Example3 cinema",
						"This is a description cinema 3",
						new Date(946684861000L), "Location X"),
				new Event(TV, "Example3 tv", "This is a description tv 3",
						new Date(946684861000L), "Location X"),
				new Event(MUSIC, "Example3 music",
						"This is a description music 3",
						new Date(946684861000L), "Location X"),
				new Event(LITERATURE, "Example3 literature",
						"This is a description literature 3",
						new Date(946684861000L), "Location X"),
				new Event(LITERATURE, "Example4 literature",
						"This is a description literature 4",
						new Date(946684861000L), "Location X"),
				new Event(LITERATURE, "Example5 literature",
						"This is a description literature 5",
						new Date(946684861000L), "Location X") };
	}

	/**
	 * Returns less than five {@link Event}
	 * 
	 * @return less than five {@link Event}
	 */
	public static Event[] lessThanFiveEvents() {
		return new Event[] {
				new Event(LITERATURE, EXISTENT_TITLE_LITERATURE,
						"This is a description literature 1",
						new Date(946684861000L), "Location X"),
				new Event(LITERATURE, "Example2 literature",
						"This is a description literature 1",
						new Date(946684861000L), "Location X"),
				new Event(MUSIC, EXISTENT_TITLE_MUSIC,
						"This is a description music 1",
						new Date(946684861000L), "Location X"),
				new Event(MUSIC, "Example2 music",
						"This is a description music 2",
						new Date(946684861000L), "Location X") };
	}

	/**
	 * Returns less than five {@link Event} with his creator {@link User}
	 * 
	 * @return less than five {@link Event} with his creator {@link User}
	 */
	public static Event[] lessThanFiveEventsWithUser() {
		return new Event[] {
				new Event(LITERATURE, EXISTENT_TITLE_LITERATURE,
						"This is a description literature 1",
						new Date(946684861000L), "Location X", users[0]),
				new Event(LITERATURE, "Example2 literature",
						"This is a description literature 1",
						new Date(946684861000L), "Location X", users[2]),
				new Event(MUSIC, EXISTENT_TITLE_MUSIC,
						"This is a description music 1",
						new Date(946684861000L), "Location X", users[1]),
				new Event(MUSIC, "Example2 music",
						"This is a description music 2",
						new Date(946684861000L), "Location X", users[0]) };
	}

	/**
	 * Returns less than twenty {@link Event} with his creator {@link User}
	 * 
	 * @return less than twenty {@link Event} with his creator {@link User}
	 */
	public static Event[] lessThanTwentyEventsWithUser() {
		return new Event[] {
				new Event(LITERATURE, EXISTENT_TITLE_LITERATURE,
						"This is a description literature 1",
						new Date(946684861000L), "Location X", users[0]),
				new Event(LITERATURE, "Example2 literature",
						"This is a description literature 1",
						new Date(946684861000L), "Location X", users[0]),
				new Event(MUSIC, EXISTENT_TITLE_MUSIC,
						"This is a description music 1",
						new Date(946684861000L), "Location X", users[0]),
				new Event(MUSIC, "Example2 music",
						"This is a description music 2",
						new Date(946684861000L), "Location X", users[0]),
				new Event(CINEMA, EXISTENT_TITLE_CINEMA,
						"This is a description cinema 1",
						new Date(946684861000L), "Location X", users[0]),
				new Event(CINEMA, "Example2 cinema",
						"This is a description cinema 2",
						new Date(946684861000L), "Location X", users[1]),
				new Event(TV, EXISTENT_TITLE_TV, "This is a description tv 1",
						new Date(946684861000L), "Location X", users[1]),
				new Event(TV, "Example2 tv", "This is a description tv 1",
						new Date(946684861000L), "Location X", users[1]),
				new Event(SPORTS, EXISTENT_TITLE_SPORTS,
						"This is a description sports 1",
						new Date(946684861000L), "Location X", users[1]),
				new Event(SPORTS, "Example2 tv",
						"This is a description sports 2",
						new Date(946684861000L), "Location X", users[1]),
				new Event(INTERNET, EXISTENT_TITLE_INTERNET,
						"This is a description internet 1",
						new Date(946684861000L), "Location X", users[2]),
				new Event(INTERNET, "Example2 internet",
						"This is a description internet 2",
						new Date(946684861000L), "Location X", users[2]),
				new Event(TRAVELS, EXISTENT_TITLE_TRAVELS,
						EXISTENT_DESCRIPTION_TRAVELS, new Date(946684861000L),
						"Location X", users[2]),
				new Event(TRAVELS, "Example2 travels",
						"This is a description travels 2",
						new Date(946684861000L), "Location X", users[2]),
				new Event(THEATRE, EXISTENT_TITLE_THEATRE,
						"This is a description theatre 1",
						new Date(946684861000L), "Location X", users[4]),
				new Event(THEATRE, "Example2 theatre",
						"This is a description theatre 2",
						new Date(946684861000L), "Location X", users[3]) };
	}

	/**
	 * Returns less than twenty {@link Event}
	 * 
	 * @return less than twenty {@link Event}
	 */
	public static Event[] lessThanTwentyEvents() {
		return new Event[] {
				new Event(LITERATURE, EXISTENT_TITLE_LITERATURE,
						"This is a description literature 1",
						new Date(946684861000L), "Location X"),
				new Event(LITERATURE, "Example2 literature",
						"This is a description literature 1",
						new Date(946684861000L), "Location X"),
				new Event(MUSIC, EXISTENT_TITLE_MUSIC,
						"This is a description music 1",
						new Date(946684861000L), "Location X"),
				new Event(MUSIC, "Example2 music",
						"This is a description music 2",
						new Date(946684861000L), "Location X"),
				new Event(CINEMA, EXISTENT_TITLE_CINEMA,
						"This is a description cinema 1",
						new Date(946684861000L), "Location X"),
				new Event(CINEMA, "Example2 cinema",
						"This is a description cinema 2",
						new Date(946684861000L), "Location X"),
				new Event(TV, EXISTENT_TITLE_TV, "This is a description tv 1",
						new Date(946684861000L), "Location X"),
				new Event(TV, "Example2 tv", "This is a description tv 1",
						new Date(946684861000L), "Location X"),
				new Event(SPORTS, EXISTENT_TITLE_SPORTS,
						"This is a description sports 1",
						new Date(946684861000L), "Location X"),
				new Event(SPORTS, "Example2 tv",
						"This is a description sports 2",
						new Date(946684861000L), "Location X"),
				new Event(INTERNET, EXISTENT_TITLE_INTERNET,
						"This is a description internet 1",
						new Date(946684861000L), "Location X"),
				new Event(INTERNET, "Example2 internet",
						"This is a description internet 2",
						new Date(946684861000L), "Location X"),
				new Event(TRAVELS, EXISTENT_TITLE_TRAVELS,
						EXISTENT_DESCRIPTION_TRAVELS, new Date(946684861000L),
						"Location X"),
				new Event(TRAVELS, "Example2 travels",
						"This is a description travels 2",
						new Date(946684861000L), "Location X"),
				new Event(THEATRE, EXISTENT_TITLE_THEATRE,
						"This is a description theatre 1",
						new Date(946684861000L), "Location X"),
				new Event(THEATRE, "Example2 theatre",
						"This is a description theatre 2",
						new Date(946684861000L), "Location X") };
	}

	/**
	 * Return an existent title
	 * 
	 * @return an existent title
	 */
	public static String anyTitle() {
		return EXISTENT_TITLE_TRAVELS;
	}

	/**
	 * Return and existent {@link EventType}
	 * 
	 * @return and existent {@link EventType}
	 */
	public static EventType anyType() {
		return EventType.TRAVELS;
	}

	/**
	 * Return an event with a title
	 * 
	 * @return an event with a title
	 */
	public static Event anyEventWithTitle() {
		return eventWithTitle(anyTitle());
	}

	/**
	 * Return an event with a {@link EventType}
	 * 
	 * @return an event with a {@link EventType}
	 */
	public static Event anyEventWithType() {
		return eventWithType(anyType());
	}

	/**
	 * Return a non existent title
	 * 
	 * @return a non existent title
	 */
	public static String nonExistentTitle() {
		return NON_EXISTENT_TITLE_TRAVELS;
	}

	/**
	 * Return a non existent description
	 * 
	 * @return a non existent description
	 */
	public static String nonExistentDescription() {
		return NON_EXISTENT_TITLE_TRAVELS;
	}

	/**
	 * Return a non existent {@link Event}
	 * 
	 * @return a non existent {@link Event}
	 */
	public static Event nonExistentEvent() {
		return new Event(EventType.TRAVELS, nonExistentTitle(),
				nonExistentDescription(), new Date(587684861000L),
				"the inexistent location", new User());
	}

	/**
	 * Return an existent id
	 * 
	 * @return an existent id
	 */
	public static int existentEventId() {
		return 1;
	}

	/**
	 * Return a non existent id
	 * 
	 * @return a non existent id
	 */
	public static int nonExistentEventId() {
		return 10000000;
	}

}
