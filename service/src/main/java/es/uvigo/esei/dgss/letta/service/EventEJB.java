package es.uvigo.esei.dgss.letta.service;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.service.util.exceptions.AlreadyRegisteredException;

import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;

import static org.apache.commons.lang3.Validate.isTrue;

/**
 * {@linkplain EventEJB} is a service bean providing all the required
 * {@linkplain Event event-related} methods.
 *
 * @author Aitor Blanco Míguez
 * @author Borja Cordeiro González
 * @author Alberto Pardellas Soto
 * @author Adolfo Álvarez López
 * @author Adrián Rodríguez Fariña
 * @author Alberto Gutiérrez Jácome
 */
@Stateless
public class EventEJB {

	@PersistenceContext
	private EntityManager em;

	@EJB
	private UserAuthorizationEJB auth;

	/**
	 * Creates a new {@link Event} in the database, setting its creator as the
	 * current identified {@link User}.
	 *
	 * @param event
	 *            The {@link Event} to be inserted into the database.
	 *
	 * @return The created {@link Event} object with all its auto-generated
	 *         fields (the event {@linkplain Event#getId()} id) already set.
	 *
	 * @throws IllegalArgumentException
	 *             if the received {@link Event} is null
	 * @throws SecurityException
	 *             if the currently identified user is not found in the database
	 *             (!!!).
	 */
	@RolesAllowed("USER")
	public Event createEvent(final Event event) throws IllegalArgumentException, SecurityException {
		isTrue(nonNull(event), "Event to create cannot be null");

		event.setCreator(auth.getCurrentUser());

		em.persist(event);
		return event;
	}

	/**
	 * Returns a paginated {@link List} of {@link Event Events}, sorted by
	 * ascending date (which means that older events will be first on the list).
	 *
	 * @param start
	 *            The first {@link Event} position to return, numbered from 0.
	 * @param count
	 *            The number of {@link Event Events} to return.
	 *
	 * @return A sorted {@link List} with the specified number of {@link Event
	 *         Events}, sorted by ascending date and counting from the received
	 *         start point.
	 */
	@PermitAll
	public List<Event> listByDate(final int start, final int count) {
		if (count == 0)
			return emptyList();

		return em.createQuery("SELECT e from Event e ORDER BY e.date ASC", Event.class).setFirstResult(start)
				.setMaxResults(count).getResultList();
	}

	/**
	 * Returns a paginated {@link List} of {@link Event Events} with all the
	 * currently highlighted events, sorted by a magician.
	 *
	 * @param start
	 *            The first {@link Event} position to return, numbered from 0.
	 * @param count
	 *            The number of {@link Event Events} to return.
	 *
	 * @return A sorted {@link List} with the specified number of {@link Event
	 *         Events}, magically sorted and counting from the received start
	 *         point.
	 */
	@PermitAll
	public List<Event> listHighlighted(final int start, final int count) {
		if (count == 0)
			return emptyList();

		return em.createQuery("SELECT e from Event e ORDER BY RAND()", Event.class).setFirstResult(0)
				.setMaxResults(count).getResultList();
	}

	/**
	 * Searches for {@link Event Events} matching some given {@link String}
	 * query, and returns the results as a paginated {@link List}. If the given
	 * search pattern is null, a {@link NullPointerException} will be thrown.
	 * <br>
	 * The method will search inside the event's {@link Event#getTitle() title}
	 * and {@link Event#getShortDescription() short description}. Results are
	 * sorted by ascending date and descending number of attendees.
	 *
	 * @param search
	 *            The search pattern as a simple {@link String}.
	 * @param start
	 *            The first {@link Event} position to return, from 0.
	 * @param count
	 *            The number of {@link Event Events} to return.
	 *
	 * @return A sorted {@link List} with the specified number of {@link Event
	 *         Events} that have matched the search query.
	 *
	 * @throws IllegalArgumentException
	 *             if the received {@link String} with the search terms is null.
	 */
	@PermitAll
	public List<Event> search(final String search, final int start, final int count) throws IllegalArgumentException {
		isTrue(nonNull(search), "Search query cannot be null");

		if (count == 0)
			return emptyList();

		// TODO: Pending sort by number of attendees.
		final TypedQuery<Event> query = em.createQuery("SELECT e FROM Event e "
				+ "WHERE e.title LIKE :search OR e.shortDescription LIKE :search " + "ORDER BY e.date ASC ",
				Event.class);

		return query.setParameter("search", "%" + search + "%").setFirstResult(start).setMaxResults(count)
				.getResultList();
	}

	/**
	 * Register the current identified {@link User} into a {@link Event}. It the
	 * the current {@link User} is already registered for the event the method
	 * will throw an {@link AlreadyRegisteredException}, else will register the
	 * {@link User} to the event will throw an exception
	 * 
	 * @param event
	 *            Indicates the {@link Event} that the {@link User} want to
	 *            register.
	 * @throws AlreadyRegisteredException
	 *             if the current identified {@link User} is already registered
	 *             for the {@link Event}.
	 */
	@RolesAllowed("USER")
	public void registerToEvent(Event event) throws AlreadyRegisteredException {

		User user = this.auth.getCurrentUser();
		List<Event> eventsOfUser = user.getUsersJoinsEvents();
		List<User> usersOfEvent = event.getEventsJoinedByUsers();
		if (!eventsOfUser.contains(event) && !usersOfEvent.contains(user)) {
			eventsOfUser.add(event);
			usersOfEvent.add(user);
		} else {
			throw new AlreadyRegisteredException("The user is already registered for the event");
		}
	}
}
