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
import java.util.LinkedList;
import java.util.List;

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
	 * Returns an {@link Event} with the provided id.
	 * 
	 * @param id
	 *            identifier of the event to return.
	 * @return an event of the dataset with the provided id.
	 * @throws IllegalArgumentException
	 *             if there is no event with the provided id.
	 */
	public static Event eventWithId(int id) {
		return stream(events())
			.filter(event -> event.getId() == id)
			.findFirst().orElseThrow(IllegalArgumentException::new);
	}

	/**
	 * Returns all the {@link Event}
	 * 
	 * @return all the {@link Event}
	 */
	public static Event[] events() {
		return new Event[] {
			new Event(1, LITERATURE, EXISTENT_TITLE_LITERATURE,
				"This is a description literature 1",
				new Date(946684861000L), "Location X", users[0]),
			new Event(2, LITERATURE, "Example2 literature",
				"This is a description literature 2",
				new Date(946684861000L), "Location X", users[0]),
			new Event(3, MUSIC, EXISTENT_TITLE_MUSIC,
				"This is a description music 1",
				new Date(946684861000L), "Location X", users[0]),
			new Event(4, MUSIC, "Example2 music",
				"This is a description music 2",
				new Date(946684861000L), "Location X", users[0]),
			new Event(5, CINEMA, EXISTENT_TITLE_CINEMA,
				"This is a description cinema 1",
				new Date(946684861000L), "Location X", users[0]),
			new Event(6, CINEMA, "Example2 cinema",
				"This is a description cinema 2",
				new Date(946684861000L), "Location X", users[1]),
			new Event(7, TV, EXISTENT_TITLE_TV,
				"This is a description tv 1",
				new Date(946684861000L), "Location X", users[1]),
			new Event(8, TV, "Example2 tv",
				"This is a description tv 1",
				new Date(946684861000L), "Location X", users[1]),
			new Event(9, SPORTS, EXISTENT_TITLE_SPORTS,
				"This is a description sports 1",
				new Date(946684861000L), "Location X", users[1]),
			new Event(10, SPORTS, "Example2 sports",
				"This is a description sports 2",
				new Date(946684861000L), "Location X", users[1]),
			new Event(11, INTERNET, EXISTENT_TITLE_INTERNET,
				"This is a description internet 1",
				new Date(946684861000L), "Location X", users[2]),
			new Event(12, INTERNET, "Example2 internet",
				"This is a description internet 2",
				new Date(946684861000L), "Location X", users[2]),
			new Event(13, TRAVELS, EXISTENT_TITLE_TRAVELS,
				EXISTENT_DESCRIPTION_TRAVELS, new Date(946684861000L),
				"Location X", users[2]),
			new Event(14, TRAVELS, "Example2 travels",
				"This is a description travels 2",
				new Date(946684861000L), "Location X", users[2]),
			new Event(15, THEATRE, EXISTENT_TITLE_THEATRE,
				"This is a description theatre 1",
				new Date(946684861000L), "Location X", users[2]),
			new Event(16, THEATRE, "Example2 theatre",
				"This is a description theatre 2",
				new Date(946684861000L), "Location X", users[3]),
			new Event(17, SPORTS, "Example3 sports",
				"This is a description sports 3",
				new Date(946684861000L), "Location X", users[3]),
			new Event(18, INTERNET, "Example3 internet",
				"This is a description internet 3",
				new Date(946684861000L), "Location X", users[3]),
			new Event(19, TRAVELS, "Example3 travels",
				"This is a description travels 3",
				new Date(946684861000L), "Location X", users[3]),
			new Event(20, CINEMA, "Example3 cinema",
				"This is a description cinema 3",
				new Date(946684861000L), "Location X", users[3]),
			new Event(21, TV, "Example3 tv",
				"This is a description tv 3",
				new Date(946684861000L), "Location X", users[4]),
			new Event(22, MUSIC, "Example3 music",
				"This is a description music 3",
				new Date(946684861000L), "Location X", users[4]),
			new Event(23, LITERATURE, "Example3 literature",
				"This is a description literature 3",
				new Date(946684861000L), "Location X", users[4]),
			new Event(24, LITERATURE, "Example4 literature",
				"This is a description literature 4",
				new Date(946684861000L), "Location X", users[4]),
			new Event(25, LITERATURE, "Example5 literature",
				"This is a description literature 5",
				new Date(946684861000L), "Location X", users[4])
		};
	}

	/**
	 * Returns less than five {@link Event}
	 * 
	 * @return less than five {@link Event}
	 */
	public static Event[] lessThanFiveEvents() {
		final Event[] events = new Event[4];
		System.arraycopy(events(), 0, events, 0, events.length);
		
		return events;
	}

	/**
	 * Returns less than twenty {@link Event}
	 * 
	 * @return less than twenty {@link Event}
	 */
	public static Event[] lessThanTwentyEvents() {
		final Event[] events = new Event[19];
		System.arraycopy(events(), 0, events, 0, events.length);
		
		return events;
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

    public static Event newEvent() {
        return new Event(LITERATURE, "New literature event", "This is a description", new Date(946684861000L), "Location X");
    }
    

	/**
	 * Returns a {@link Event} with his creator {@link User} and his joined {@link User}s
	 * 
	 * @return a {@link Event} with his creator {@link User} and his joined {@link User}s
	 */
	public static Event[] eventsWithTwoJoinedUsers() {
		List<User> groupOfUsers0 = new LinkedList<>();
		groupOfUsers0.add(users[1]);
		groupOfUsers0.add(users[2]);
		List<User> groupOfUsers1 = new LinkedList<>();
		groupOfUsers1.add(users[0]);
		groupOfUsers1.add(users[3]);
		List<User> groupOfUsers2 = new LinkedList<>();
		groupOfUsers2.add(users[3]);
		groupOfUsers2.add(users[4]);		
		List<User> groupOfUsers3 = new LinkedList<>();
		groupOfUsers1.add(users[0]);
		groupOfUsers1.add(users[1]);		
		List<User> groupOfUsers4 = new LinkedList<>();
		groupOfUsers1.add(users[0]);
		groupOfUsers1.add(users[2]);
		return new Event[] {
			new Event(LITERATURE, EXISTENT_TITLE_LITERATURE,
					"This is a description literature 1", 
					new Date(946684861000L), "Location X", users[0],
					groupOfUsers0),
			new Event(LITERATURE, "Example2 literature",
					"This is a description literature 2",
					new Date(946684861000L), "Location X", users[0],
					groupOfUsers0),
			new Event(MUSIC, EXISTENT_TITLE_MUSIC,
					"This is a description music 1",
					new Date(946684861000L), "Location X", users[0],
					groupOfUsers0),
			new Event(MUSIC, "Example2 music",
					"This is a description music 2",
					new Date(946684861000L), "Location X", users[0],
					groupOfUsers0),
			new Event(CINEMA, EXISTENT_TITLE_CINEMA,
					"This is a description cinema 1",
					new Date(946684861000L), "Location X", users[0],
					groupOfUsers0),
			new Event(CINEMA, "Example2 cinema",
					"This is a description cinema 2",
					new Date(946684861000L), "Location X", users[1],
					groupOfUsers1),
			new Event(TV, EXISTENT_TITLE_TV, "This is a description tv 1",
					new Date(946684861000L), "Location X", users[1],
					groupOfUsers1),
			new Event(TV, "Example2 tv", "This is a description tv 1",
					new Date(946684861000L), "Location X", users[1],
					groupOfUsers1),
			new Event(SPORTS, EXISTENT_TITLE_SPORTS,
					"This is a description sports 1",
					new Date(946684861000L), "Location X", users[1],
					groupOfUsers1),
			new Event(SPORTS, "Example2 sports",
					"This is a description sports 2",
					new Date(946684861000L), "Location X", users[1],
					groupOfUsers1),
			new Event(INTERNET, EXISTENT_TITLE_INTERNET,
					"This is a description internet 1",
					new Date(946684861000L), "Location X", users[2],
					groupOfUsers2),
			new Event(INTERNET, "Example2 internet",
					"This is a description internet 2",
					new Date(946684861000L), "Location X", users[2],
					groupOfUsers2),
			new Event(TRAVELS, EXISTENT_TITLE_TRAVELS,
					EXISTENT_DESCRIPTION_TRAVELS, new Date(946684861000L),
					"Location X", users[2],
					groupOfUsers2),
			new Event(TRAVELS, "Example2 travels",
					"This is a description travels 2",
					new Date(946684861000L), "Location X", users[2],
					groupOfUsers2),
			new Event(THEATRE, EXISTENT_TITLE_THEATRE,
					"This is a description theatre 1",
					new Date(946684861000L), "Location X", users[2],
					groupOfUsers2),
			new Event(THEATRE, "Example2 theatre",
					"This is a description theatre 2",
					new Date(946684861000L), "Location X", users[3],
					groupOfUsers3),
			new Event(SPORTS, "Example3 sports",
					"This is a description sports 3",
					new Date(946684861000L), "Location X", users[3],
					groupOfUsers3),
			new Event(INTERNET, "Example3 internet",
					"This is a description internet 3",
					new Date(946684861000L), "Location X", users[3],
					groupOfUsers3),
			new Event(TRAVELS, "Example3 travels",
					"This is a description travels 3",
					new Date(946684861000L), "Location X", users[3],
					groupOfUsers3),
			new Event(CINEMA, "Example3 cinema",
					"This is a description cinema 3",
					new Date(946684861000L), "Location X", users[3],
					groupOfUsers3),
			new Event(TV, "Example3 tv", "This is a description tv 3",
					new Date(946684861000L), "Location X", users[4],
					groupOfUsers4),
			new Event(MUSIC, "Example3 music",
					"This is a description music 3",
					new Date(946684861000L), "Location X", users[4],
					groupOfUsers4),
			new Event(LITERATURE, "Example3 literature",
					"This is a description literature 3",
					new Date(946684861000L), "Location X", users[4],
					groupOfUsers4),
			new Event(LITERATURE, "Example4 literature",
					"This is a description literature 4",
					new Date(946684861000L), "Location X", users[4],
					groupOfUsers4),
			new Event(LITERATURE, "Example5 literature",
					"This is a description literature 5",
					new Date(946684861000L), "Location X", users[4],
					groupOfUsers4)
		};
	}

}
