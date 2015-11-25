package es.uvigo.esei.dgss.letta.domain.entities;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import static es.uvigo.esei.dgss.letta.domain.entities.UserParameters.validUser;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Class for test the {@code Event} class.
 * 
 * @author aalopez, apsoto
 *
 */
public class EventTest {

	private EventType eventType;
	private String title;
	private String shortDescription;
	private String location;
	private Date date;
	private Event event;
	private User creator;
	
	/**
	 * Method for set up the attributes.
	 */
	@Before
	public void setUp() {
		this.eventType = EventType.CINEMA;
		this.title = "Event title";
		this.shortDescription = "Event description";
		this.location = "Event location";
		this.date = new Date();
		this.event = new Event();
		this.creator = validUser();
	}

	/**
	 * Method for test {@code Event} constructor.
	 */
	@Test
	public void testEvent() {
		Event event = new Event(this.eventType, this.title, this.shortDescription, this.date, this.location, this.creator);
		assertThat(event.getEventType(), is(equalTo(this.eventType)));
		assertThat(event.getTitle(), is(equalTo(this.title)));
		assertThat(event.getShortDescription(), is(equalTo(this.shortDescription)));
		assertThat(event.getDate(), is(equalTo(this.date)));
		assertThat(event.getLocation(), is(equalTo(this.location)));
		assertThat(event.getCreator(), is(equalTo(this.creator)));
	}
	
	/**
	 * Method for test {@code Event} constructor with null creator.
	 */
	@Test(expected = NullPointerException.class)
	public void testEventNullCreator() {
		new Event(this.eventType, this.title, this.shortDescription, this.date, this.location, null);
	}

	/**
	 * Method for test {@code Event} constructor with null {@code EventType}.
	 */
	@Test(expected = NullPointerException.class)
	public void testEventNullEventType() {
		new Event(null, this.title, this.shortDescription, this.date, this.location, this.creator);
	}

	/**
	 * Method for test {@code Event} constructor with null title.
	 */
	@Test(expected = NullPointerException.class)
	public void testEventNullTitle() {
		new Event(this.eventType, null, this.shortDescription, this.date, this.location, this.creator);
	}

	/**
	 * Method for test {@code Event} constructor with null description.
	 */
	@Test(expected = NullPointerException.class)
	public void testEventNullDescription() {
		new Event(this.eventType, this.title, null, this.date, this.location, this.creator);
	}

	/**
	 * Method for test {@code Event} constructor with empty title.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEventEmptyTitle() {
		new Event(this.eventType, "", this.shortDescription, this.date, this.location, this.creator);
	}

	/**
	 * Method for test {@code Event} constructor with empty description.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEventEmptyDescription() {
		new Event(this.eventType, this.title, "", this.date, this.location, this.creator);
	}

	/**
	 * Method for test {@code Event} constructor with title longer than 20.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEventTooLongTitle() {
		new Event(this.eventType, String.format("%1$" + 21 + "s", ""), this.shortDescription, this.date, this.location, this.creator);
	}

	/**
	 * Method for test {@code Event} constructor with description longer than
	 * 50.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEventTooLongDescription() {
		new Event(this.eventType, this.title, String.format("%1$" + 51 + "s", ""), this.date, this.location, this.creator);
	}

	@Test(expected = NullPointerException.class)
    public void testEventNullLocation() {
        new Event(this.eventType, this.title, this.shortDescription, this.date, null, this.creator);
    }

	@Test(expected = IllegalArgumentException.class)
	public void testEventEmptyLocation() {
	    new Event(this.eventType, this.title, this.shortDescription, this.date, "", this.creator);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEventTooLongLocation() {
	    new Event(this.eventType, this.title, this.shortDescription, this.date, String.format("%1$" + 101 + "s", ""), this.creator);
	}
	
	/**
	 * Method for test creator setter with null argument.
	 */
	@Test(expected = NullPointerException.class)
	public void testSetCreator() {
		this.event.setCreator(null);
	}

	/**
	 * Method for test {@code EventType} setter with null argument.
	 */
	@Test(expected = NullPointerException.class)
	public void testSetEventType() {
		this.event.setEventType(null);
	}

	/**
	 * Method for test title setter with null argument.
	 */
	@Test(expected = NullPointerException.class)
	public void testSetTitleWithNullArgument() {
		this.event.setTitle(null);
	}

	/**
	 * Method for test title setter with empty argument.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetTitleWithEmptyArgument() {
		this.event.setTitle("");
	}

	/**
	 * Method for test title setter with argument longer than 20.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetTitleWithTooLongArgument() {
		this.event.setTitle(String.format("%1$" + 21 + "s", ""));
	}

	/**
	 * Method for test description setter with null argument.
	 */
	@Test(expected = NullPointerException.class)
	public void testSetDescriptionWithNullArgument() {
		this.event.setShortDescription(null);
	}

	/**
	 * Method for test description setter with empty argument.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetDescriptionWithEmptyArgument() {
		this.event.setShortDescription("");
	}

	/**
	 * Method for test description setter with argument longer than 50.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetDescriptionWithTooLongArgument() {
		this.event.setShortDescription(String.format("%1$" + 51 + "s", ""));
	}

	/**
	 * Method for test {@code Date} setter with null argument.
	 */
	@Test(expected = NullPointerException.class)
	public void testSetDateWithNullArgument() {
		this.event.setDate(null);
	}

	@Test(expected = NullPointerException.class)
    public void testSetLocationWithNullArgument() {
        this.event.setDate(null);
    }

	/**
     * Method for test description setter with empty argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetLocationWithEmptyArgument() {
        this.event.setShortDescription("");
    }

    /**
     * Method for test description setter with argument longer than 50.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetLocationWithTooLongArgument() {
        this.event.setShortDescription(String.format("%1$" + 101 + "s", ""));
    }

}
