package es.uvigo.esei.dgss.letta.domain.entities;

import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.Validate.inclusiveBetween;
import static org.apache.commons.lang3.Validate.isTrue;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import es.uvigo.esei.dgss.letta.domain.util.adapters.LocalDateTimeAdapter;
import es.uvigo.esei.dgss.letta.domain.util.annotations.VisibleForJPA;
import es.uvigo.esei.dgss.letta.domain.util.annotations.VisibleForTesting;
import es.uvigo.esei.dgss.letta.domain.util.converters.LocalDateTimeConverter;

/**
 * {@linkplain Event} is a JPA entity that represents an event of the
 * application.
 *
 * @author Adolfo Álvarez López
 * @author Alberto Pardellas Soto
 * @author Aitor Blanco Míguez
 * @author Alberto Gutiérrez Jácome
 */
@Entity
@XmlAccessorType(XmlAccessType.FIELD)
public class Event {

    /**
     * Enum representing all the available event categories: Books, Internet,
     * Movies, Music, Sports, Television, Theatre and Travels.
     */
    public static enum Category {
        BOOKS, INTERNET, MOVIES, MUSIC, SPORTS, TELEVISION, THEATRE, TRAVELS
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private Category category;

    @Column(length = 20, nullable = false)
    private String title;

    @Column(length = 50, nullable = false)
    private String summary;

    @Column(nullable = false)
    @Convert(converter = LocalDateTimeConverter.class)
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime date;

    @Column(length = 20, nullable = false)
    private String location;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner", referencedColumnName = "login")
    private User owner;

    @XmlTransient
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "EventAttendees",
        joinColumns = { @JoinColumn(name = "event_id", referencedColumnName = "id") },
        inverseJoinColumns = { @JoinColumn(name = "user_login", referencedColumnName = "login") })
    private Set<User> attendees;
    
	@Column(nullable = false)
	private boolean cancelled;

	@Column(length = 1000, nullable = true)
	private String description;


    /**
     * Constructs a new instance of {@link Event}. This empty constructor is
     * required by the JPA framework and <strong>should never be used
     * directly</strong>.
     */
    @VisibleForJPA Event() { }

    /**
     * Constructs a new instance of {@link Event}. This constructor is expected
     * to be used <strong>only for testing purposes</strong>. Specifically, it
     * will only be used to create datasets that match specific database states.
     */
    @VisibleForTesting Event(
        final int           id,
        final Category      category,
        final String        title,
        final String        summary,
        final LocalDateTime date,
        final String        location,
        final User          owner,
        final Set<User>     attendees,
        final boolean		cancelled,
        final String        description
    ) throws NullPointerException {
        this.id          = id;
        this.category    = requireNonNull(category);
        this.title       = requireNonNull(title);
        this.summary     = requireNonNull(summary);
        this.date        = requireNonNull(date);
        this.location    = requireNonNull(location);
        this.owner       = requireNonNull(owner);
        this.attendees   = requireNonNull(attendees);
        this.cancelled   = requireNonNull(cancelled);
        this.description = requireNonNull(description);
    }

    /**
     * Constructs a new instance of {@link Event}.
     *
     * @param category The {@link Category} of the event. It cannot be null.
     * @param title A {@link String} with the title of the event. It must be
     *        non-null, non-empty and no greater than 20 characters.
     * @param summary A {@link String} with the summary of the event. It must
     *        be non-null, non-empty and no greater than 50 characters.
     * @param date The {@link LocalDateTime} of the event. It cannot be null.
     * @param location A {@link String} with the location of the event. It must
     *        be non-null, non-empty and no greater than 20 characters.
     *
     * @throws IllegalArgumentException If any of the {@link Event Event's}
     *         field requirements does not hold.
     * @throws NullPointerException If any of the given arguments is
     *         {@code null}.
     */
    public Event(
        final Category      category,
        final String        title,
        final String        summary,
        final LocalDateTime date,
        final String        location,
        final String		description
    ) throws IllegalArgumentException, NullPointerException {
        setCategory(category);
        setTitle(title);
        setSummary(summary);
        setDate(date);
        setLocation(location);
        setDescription(description);

        this.owner     = null;
        this.attendees = new LinkedHashSet<>();
        this.cancelled = false;
    }

    /**
     * Returns the identifier of the event.
     *
     * @return The identifier of the event.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the {@link Category} of the event.
     *
     * @return The category of the event.
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Changes the {@link Category} of the event.
     *
     * @param category The new event's category. It cannot be {@code null}.
     *
     * @throws NullPointerException If a {@code null} category is received.
     */
    public void setCategory(
        final Category category
    ) throws NullPointerException {
        this.category = requireNonNull(category, "Event's category cannot be null.");
    }

    /**
     * Returns the event's title as a {@link String}.
     *
     * @return The title of the event.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Changes the title of the event.
     *
     * @param title The new event's title as a {@link String}. It cannot be
     *        {@code null}, empty nor greater than 20 characters.
     *
     * @throws IllegalArgumentException If the title is empty or its length
     *         exceeds the limit of 20 characters.
     * @throws NullPointerException If a {@code null} title is received.
     */
    public void setTitle(
        final String title
    ) throws IllegalArgumentException, NullPointerException {
        requireNonNull(title, "Event's title cannot be null.");
        inclusiveBetween(1, 20, title.length(), "Event's title length must be in the range [1, 20].");

        this.title = WordUtils.capitalize(title);
    }

    /**
     * Returns the event's summary as a {@link String}.
     *
     * @return The summary of the event.
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Changes the summary of the event.
     *
     * @param summary The new event's summary as a {@link String}. It cannot be
     *        {@code null}, empty nor greater than 50 characters.
     *
     * @throws IllegalArgumentException If the summary is empty or its length
     *         exceeds the limit of 50 characters.
     * @throws NullPointerException If a {@code null} summary is received.
     */
    public void setSummary(
        final String summary
    ) throws IllegalArgumentException, NullPointerException {
        requireNonNull(summary, "Event's summary cannot be null.");
        inclusiveBetween(1, 50, summary.length(), "Event's summary length must be in the range [1, 50].");

        this.summary = StringUtils.capitalize(summary);
    }

    /**
     * Returns the event's date as a {@link LocalDateTime}.
     *
     * @return The date of the event.
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Changes the date of the event.
     *
     * @param date The new event's date as a {@link LocalDateTime}. It cannot be
     *        {@code null}.
     *
     * @throws NullPointerException If a {@code null} date is received.
     */
    public void setDate(
        final LocalDateTime date
    ) throws NullPointerException {
        this.date = requireNonNull(date, "Event's date cannot be null.");
    }

    /**
     * Returns the event's location as a {@link String}.
     *
     * @return The location of the event.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Changes the location of the event.
     *
     * @param location The new event's location as a {@link String}. It cannot
     *        be {@code null}, empty nor greater than 20 characters.
     *
     * @throws IllegalArgumentException If the location is empty or its length
     *         exceeds the limit of 20 characters.
     * @throws NullPointerException If a {@code null} location is received.
     */
    public void setLocation(
        final String location
    ) throws IllegalArgumentException, NullPointerException {
        requireNonNull(location, "Event's location cannot be null.");
        inclusiveBetween(1, 20, location.length(), "Event's location length must be in the range [1, 20].");

        this.location = StringUtils.capitalize(location);
    }

    /**
     * Returns the event's owner as an {@link User}.
     *
     * @return The owner of the event.
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Changes the owner of the event.
     *
     * @param owner The new event's owner as an {@link User}. It cannot be
     *        {@code null}.
     *
     * @throws NullPointerException If a {@code null} owner is received.
     */
    public void setOwner(final User owner) throws NullPointerException {
        this.owner = requireNonNull(owner, "Event's owner cannot be null.");
    }

	/**
	 * Returns if the event is cancelled
	 * 
	 * @return {@code true} if the event is cancelled, {@code false} otherwise
	 */
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * Changes if the event is cancelled
	 * 
	 * @param cancelled
	 *            global variable
	 */
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	/**
	 * Retrieves the long description of a event
	 * 
	 * @return the long description of a event
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Changes if the event long description is modified
	 * 
	 * @param description
	 *            global variable
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
     * Returns the current number of attendees of this event.
     *
     * @return The number of attendees of the event.
     */
    public int countAttendees() {
        return attendees.size();
    }

    /**
     * Checks whether a given {@link User} is an attendee of the current event.
     *
     * @param attendee The user to check if it is an attendee of the event.
     *
     * @return True if the user is an attendee, false otherwise.
     */
    public boolean hasAttendee(final User attendee) {
        requireNonNull(attendee, "User cannot be null.");
        return attendees.contains(attendee);
    }

    /**
     * Returns the attendees of this event, as an unmodifiable {@link Set} of
     * {@link User Users}.
     *
     * @return The attendees of the event.
     */
    public Set<User> getAttendees() {
        return unmodifiableSet(attendees);
    }

    /**
     * Adds a new {@link User} to this event's attendees set.
     *
     * @param attendee The new user to add to the attendees set. It cannot be
     *        {@code null} nor already contained in the attendees set.
     *
     * @throws IllegalArgumentException If the received user already is part of
     *         this event's attendees set.
     * @throws NullPointerException If a {@code null} attendee is received.
     */
    public void addAttendee(
        final User attendee
    ) throws IllegalArgumentException, NullPointerException {
        requireNonNull(attendee, "Event's attendee cannot be null.");
        isTrue(!attendees.contains(attendee), "Event's attendees already contains the given user.");

        attendees.add(attendee);
    }

    /**
     * Removes an {@link User} from this event's attendees set.
     *
     * @param attendee The user to remove from the attendees set. It cannot be
     *        {@code null}, and must be contained in the attendees set.
     *
     * @throws IllegalArgumentException If the received user is not part of this
     *         event's attendees set.
     * @throws NullPointerException If a {@code null} attendee is received.
     */
    public void removeAttendee(
        final User attendee
    ) throws IllegalArgumentException, NullPointerException {
        requireNonNull(attendee, "Event's attendee cannot be null");
        isTrue(attendees.contains(attendee), "Event's attendees dos not contain the given user.");

        attendees.remove(attendee);
    }

    @Override
    public final int hashCode() {
        return id;
    }

    @Override
    public final boolean equals(final Object that) {
        return this == that
            || nonNull(that)
            && that instanceof Event
            && this.id == ((Event) that).id;
    }

    @Override
    public String toString() {
        return String.format(
            "Event(%d; %s; '%s'; '%s'; %s; '%s'; %s; %d)",
            id,
            WordUtils.capitalizeFully(category.name()),
            title,
            summary,
            date.atZone(ZoneId.systemDefault()).format(RFC_1123_DATE_TIME),
            location,
            isNull(owner) ? "null" : owner.getLogin(),
            attendees.size()
        );
    }

}
