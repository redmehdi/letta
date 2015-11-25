package es.uvigo.esei.dgss.letta.domain.entities;

import static es.uvigo.esei.dgss.letta.domain.entities.UsersDataset.users;
import static java.util.Arrays.stream;

import java.util.Date;

public class EventsDataset {
	public static final String EXISTENT_TITLE_MUSIC = "Example1 music";
	public static final String EXISTENT_TITLE_CINEMA = "Example1 cinema";
	public static final String EXISTENT_TITLE_SPORTS = "Example1 sports";
	public static final String EXISTENT_TITLE_INTERNET = "Example1 internet";
	public static final String EXISTENT_TITLE_TRAVELS = "Example1 travels";
	public static final String EXISTENT_TITLE_THEATRE = "Example1 theatre";
	public static final String EXISTENT_TITLE_LITERATURE = "Example1 literature";

	public static final String EXISTENT_DESCRIPTION_TRAVELS = "This is a description travels 1";

	public static final String NON_EXISTENT_TITLE_MUSIC = "Example12 music";
	public static final String NON_EXISTENT_TITLE_CINEMA = "Example12 cinema";
	public static final String NON_EXISTENT_TITLE_SPORTS = "Example12 sports";
	public static final String NON_EXISTENT_TITLE_INTERNET = "Example12 internet";
	public static final String NON_EXISTENT_TITLE_TRAVELS = "Example12 travels";
	public static final String NON_EXISTENT_TITLE_THEATRE = "Example12 theatre";

	public static final String NON_EXISTENT_DESCRIPTION_TRAVELS = "This is not a description travels 1";
	
	public static final User[] users = users(); 

	public static Event eventWithTitle(final String title) {
		return stream(events()).filter(event -> event.getTitle().equals(title))
				.findFirst().orElseThrow(IllegalArgumentException::new);
	}

	public static Event eventWithType(final EventType eventType) {
		return stream(events())
				.filter(event -> event.getEventType().equals(eventType))
				.findFirst().orElseThrow(IllegalArgumentException::new);
	}

	public static Event[] events() {
		return new Event[] {
		    new Event(EventType.CINEMA, EXISTENT_TITLE_CINEMA, "This is a description cinema 1", new Date(946684861000L), "Location X", users[0]),
		    new Event(EventType.INTERNET, EXISTENT_TITLE_INTERNET, "This is a description internet 1", new Date(946684861000L), "Location X", users[1]),
		    new Event(EventType.SPORTS, EXISTENT_TITLE_SPORTS, "This is a description sports 1", new Date(946684861000L), "Location X", users[2]),
		    new Event(EventType.MUSIC, EXISTENT_TITLE_MUSIC, "This is a description music 1", new Date(946684861000L), "Location X", users[3]),
		    new Event(EventType.THEATRE, EXISTENT_TITLE_THEATRE, "This is a description theatre 1", new Date(946684861000L), "Location X", users[4]),
		    new Event(EventType.THEATRE, EXISTENT_TITLE_LITERATURE, "This is a description literature 1", new Date(946684861000L), "Location X", users[4]),
		};
	}

	public static String anyTitle() {
		return EXISTENT_TITLE_TRAVELS;
	}

	public static EventType anyType() {
		return EventType.TRAVELS;
	}

	public static Event anyEventWithTitle() {
		return eventWithTitle(anyTitle());
	}

	public static Event anyEventWithType() {
		return eventWithType(anyType());
	}

	public static String nonExistentTitle() {
		return NON_EXISTENT_TITLE_TRAVELS;
	}

	public static String nonExistentDescription() {
		return NON_EXISTENT_TITLE_TRAVELS;
	}

	public static Event nonExistentEvent() {
		return new Event(EventType.TRAVELS, nonExistentTitle(), nonExistentDescription(), new Date(587684861000L), "the inexistent location", new User());
	}

	public static int existentEventId() {
		return 1;
	}

	public static int nonExistentEventId() {
		return 10000000;
	}

}
