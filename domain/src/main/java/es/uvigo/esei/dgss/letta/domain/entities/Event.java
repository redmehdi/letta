package es.uvigo.esei.dgss.letta.domain.entities;

import static java.util.Objects.requireNonNull;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * An entity that represents an event of the application.
 * 
 * @author aalopez
 * @author apsoto
 * @author abmiguez
 *
 */
@Entity
public class Event {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Enumerated(EnumType.STRING)
	@NotNull
	private EventType eventType;
	@NotNull
	@Column(length = 20)
	private String title;
	@NotNull
	@Column(length = 50)
	private String shortDescription;
	@NotNull
	private Date date;
	@ManyToMany(mappedBy = "usersJoinsEvents")
	private List<User> eventsJoinedByUsers = new LinkedList<User>();
	@Column(length = 100, nullable = false, updatable = true)
	private String location;
	@NotNull
	@ManyToOne
	@JoinColumn(name = "creator", referencedColumnName = "login")
	private User creator;

	Event() {
	}

	/**
	 * Creates a new instance of {@code Event}.
	 *
	 * @param eventType
	 *            the event type of the event. This paramenter must be a non
	 *            {@code null} {@code EventType}.
	 * @param title
	 *            the title of the envent. This paramenter must be a non
	 *            {@code null} string with with a maximum length of 20.
	 * @param shortDescription
	 *            the description of the event. This paramenter must be a non
	 *            {@code null} string with with a maximum length of 50.
	 * @param date
	 *            the date of the event. This paramenter must be a non
	 *            {@code null}.
	 * @param location
	 *            the location of the event
	 */
	public Event(final EventType eventType, final String title,
			final String shortDescription, final Date date,
			final String location) {
		this.setEventType(eventType);
		this.setTitle(title);
		this.setShortDescription(shortDescription);
		this.setDate(date);
		this.setLocation(location);
		this.setCreator(new User());
	}

	/**
	 * Creates a new instance of {@code Event}.
	 *
	 * @param eventType
	 *            the event type of the event. This paramenter must be a non
	 *            {@code null} {@code EventType}.
	 * @param title
	 *            the title of the envent. This paramenter must be a non
	 *            {@code null} string with with a maximum length of 20.
	 * @param shortDescription
	 *            the description of the event. This paramenter must be a non
	 *            {@code null} string with with a maximum length of 50.
	 * @param date
	 *            the date of the event. This paramenter must be a non
	 *            {@code null}.
	 * @param location
	 *            the location of the event
	 * @param creator
	 *            the creator of the event
	 */
	public Event(final EventType eventType, final String title,
			final String shortDescription, final Date date,
			final String location, final User creator) {
		this.setEventType(eventType);
		this.setTitle(title);
		this.setShortDescription(shortDescription);
		this.setDate(date);
		this.setLocation(location);
		this.setCreator(creator);
	}
	
	/**
	 * Creates a new instance of {@code Event}.
	 *
	 * @param eventType
	 *            the event type of the event. This paramenter must be a non
	 *            {@code null} {@code EventType}.
	 * @param title
	 *            the title of the envent. This paramenter must be a non
	 *            {@code null} string with with a maximum length of 20.
	 * @param shortDescription
	 *            the description of the event. This paramenter must be a non
	 *            {@code null} string with with a maximum length of 50.
	 * @param date
	 *            the date of the event. This paramenter must be a non
	 *            {@code null}.
	 * @param location
	 *            the location of the event
	 * @param creator
	 *            the creator of the event
	 * @param eventsJoinedByUsers
	 *            Users who was joined to the event
	 */
	public Event(final EventType eventType, final String title,
			final String shortDescription, final Date date,
			final String location, final User creator,
			final List<User> eventsJoinedByUsers) {
		this.setEventType(eventType);
		this.setTitle(title);
		this.setShortDescription(shortDescription);
		this.setDate(date);
		this.setLocation(location);
		this.setCreator(creator);
		for(User userJoin : eventsJoinedByUsers) {
			this.eventsJoinedByUsers.add(userJoin);
			final List <Event> listEvent = new LinkedList<Event>();
			listEvent.add(this);
			userJoin.setUsersJoinsEvents(listEvent);
		}
	}

	/**
	 * The getter for the eventType parameter.
	 *
	 * @return the event type for the event.
	 */
	public EventType getEventType() {
		return eventType;
	}

	/**
	 * The setter for the eventType parameter.
	 * 
	 * @param eventType
	 *            the event type of the event. This paramenter must be a non
	 *            {@code null} {@code EventType}.
	 * 
	 * @throws NullPointerException
	 *             if a {@code null} value is passed as the value for this
	 *             parameter.
	 */
	public void setEventType(EventType eventType) {
		if (eventType == null) {
			throw new NullPointerException("eventType can't be null");
		}
		this.eventType = eventType;
	}

	/**
	 * The getter for the title parameter.
	 * 
	 * @return a string with the title of the event.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * The setter for the title parameter.
	 * 
	 * @param title
	 *            the title of the envent. This paramenter must be a non
	 *            {@code null} string with with a maximum length of 20.
	 * 
	 * @throws NullPointerException
	 *             if a {@code null} value is passed as the value for this
	 *             parameter.
	 * @throws IllegalArgumentException
	 *             if value provided for the is not valid according to its
	 *             description.
	 */
	public void setTitle(String title) {

		if (title == null) {
			throw new NullPointerException("title can't be null");
		}
		if (title.isEmpty()) {
			throw new IllegalArgumentException("title can't be an empty string");
		}
		if (title.length() > 20) {
			throw new IllegalArgumentException(
					"title can't be more than 20 characters");
		}
		this.title = title;
	}

	/**
	 * The getter for the description parameter.
	 * 
	 * @return a string with the description of the event.
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * The setter for the description parameter.
	 * 
	 * @param shortDescription
	 *            the description of the event. This paramenter must be a non
	 *            {@code null} string with with a maximum length of 50.
	 * @throws NullPointerException
	 *             if a {@code null} value is passed as the value for this
	 *             parameter.
	 * @throws IllegalArgumentException
	 *             if value provided for description is not valid according to
	 *             its description.
	 */
	public void setShortDescription(String shortDescription) {
		if (shortDescription == null) {
			throw new NullPointerException("description can't be null");
		}
		if (shortDescription.isEmpty()) {
			throw new IllegalArgumentException(
					"description can't be an empty string");
		}
		if (shortDescription.length() > 50) {
			throw new IllegalArgumentException(
					"description can't be more than 50 characters");
		}
		this.shortDescription = shortDescription;
	}

	/**
	 * The getter for the date parameter.
	 * 
	 * @return a {@code Date} with the date of the event.
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * The setter for the {@code Date} parameter.
	 * 
	 * @param date
	 *            the date of the event. This paramenter must be a non
	 *            {@code null}.
	 * @throws NullPointerException
	 *             if a {@code null} value is passed as the value for any
	 *             parameter.
	 */
	public void setDate(Date date) {
		if (date == null) {
			throw new NullPointerException("date can't be null");
		}
		this.date = date;
	}

	/**
	 * Extracts the value of the current Event's location.
	 *
	 * @return A String representing the Event's location.
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Sets the location of the current event to the received String.
	 *
	 * @param location
	 *            A no-null, non-empty String, which length is not greater than
	 *            100 characters.
	 * @throws NullPointerException
	 *             If a null String is received.
	 * @throws IllegalArgumentException
	 *             If received String's length is not between 1 and 100.
	 */
	public void setLocation(final String location) throws NullPointerException,
			IllegalArgumentException {
		requireNonNull(location, "Location can't be null");

		if (location.isEmpty() || location.length() > 100)
			throw new IllegalArgumentException(
					"Location length must be between 1 and 100");

		this.location = location;
	}

	/**
	 * The getter for the id parameter.
	 * 
	 * @return the unique id for the instance.
	 */
	public int getId() {
		return id;
	}

	/**
	 * The getter for the event creator
	 * 
	 * @return the event creator
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * The setter for the event creator
	 * 
	 * @param creator
	 *            represents the event creator
	 */
	public void setCreator(final User creator) {
		if (creator == null)
			throw new NullPointerException("creator can't be null");
		else
			this.creator = creator;
	}

	/**
	 * Return all the users who have joined an event
	 * 
	 * @return all the users who have joined an event
	 */
	public List<User> getEventsJoinedByUsers() {
		return eventsJoinedByUsers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
