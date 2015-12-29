package es.uvigo.esei.dgss.letta.service;

import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.Validate.isTrue;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import es.uvigo.esei.dgss.letta.domain.entities.Capital;
import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.Event.Category;
import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.service.util.exceptions.EventAlredyJoinedException;
import es.uvigo.esei.dgss.letta.service.util.exceptions.EventIsCancelledException;
import es.uvigo.esei.dgss.letta.service.util.exceptions.EventNotJoinedException;
import es.uvigo.esei.dgss.letta.service.util.exceptions.IllegalEventOwnerException;
import es.uvigo.esei.dgss.letta.service.util.exceptions.UserNotAuthorizedException;


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
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class EventEJB {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private UserAuthorizationEJB auth;

    @Resource
    private SessionContext ctx;

    @PermitAll
    public Optional<Event> get(final int id) {
        return Optional.ofNullable(em.find(Event.class, id));
    }

    @PermitAll
    @Deprecated
    public Event getEvent(final int id) {
        return get(id).orElse(null);
    }

    @PermitAll
    public Optional<Event> getWithAttendees(final int id) {
        final TypedQuery<Event> query = em.createQuery(
            "SELECT e FROM Event e JOIN FETCH e.attendees WHERE e.id = :id",
            Event.class
        ).setParameter("id", id);

        try {
            return Optional.of(query.getSingleResult());
        } catch (final NoResultException | NonUniqueResultException e) {
            return Optional.empty();
        }
    }

    /**
     * Counts how many {@link Event Events} currently exist in the database.
     *
     * @param search string to search
     *
     * @return An integer value representing the total number of events.
     */
    @PermitAll
    public int count(final String search) {
    	final TypedQuery<Event> query = em.createQuery(
                "SELECT e FROM Event e " +
                " WHERE ( LOWER(e.title) LIKE :search " +
                "    OR LOWER(e.summary) LIKE :search " +
                "OR LOWER(e.description) LIKE :search )" +
                "AND e.date > now()"+
                "AND e.cancelled =  FALSE " +
                "ORDER BY e.date ASC",
                Event.class
            ).setParameter("search", "%" + search.toLowerCase() + "%");

    	return query.getResultList().size();
    }
    


    /**
     * Search for an event with advanced criteria 
     * @param search Search term of the query(short description or title)
     * @param state State of the event(cancelled or open)
     * @param category Category of the event
     * @param start Start page of the query
     * @param count Number of elements per page
     * @return result list of events
     */
    @PermitAll
    public List<Event> advancedSearch(String search, Enum state, String category, int start, int count){
    	System.out.println("ENTREI DENTRO");
    	isTrue(nonNull(search), "Search query cannot be null");
        if (count == 0) return emptyList();
        Category cat= Category.valueOf(category);
        
        String query_string=
                "SELECT e FROM Event e " +
                " WHERE (LOWER(e.title) LIKE :search " +
                "    OR LOWER(e.summary) LIKE :search " +
                "OR LOWER(e.description) LIKE :search ) " +
                "AND e.category = :category_aux "+
                "AND e.cancelled =  :auxState " ;
               
        boolean aux=false;
        if(state.name().equals("EXPIRED")){
        	aux=true;
        	query_string+= "AND CURRENT_TIMESTAMP<e.date";
        }
        query_string+= " ORDER BY e.date ASC";
        final TypedQuery<Event> query = em.createQuery(query_string,
                Event.class
            ).setParameter("search", "%" + search.toLowerCase() + "%");
        query.setParameter("category_aux",cat);
        
        if(state.name().equals("CANCELLED")){
        	query.setParameter("auxState",true);
        }else {
        	query.setParameter("auxState",false);
        }
        	
        return query.setFirstResult(start).setMaxResults(count).getResultList();
   

    }
    
    
    
    
    
    /**
     * Returns a paginated {@link List} of {@link Event Events}, sorted by
     * ascending date (which means that older events will be first on the list).
     *
     * @param start The first {@link Event} position to return, numbered from 0.
     * @param count The number of {@link Event Events} to return.
     *
     * @return A sorted {@link List} with the specified number of {@link Event
     *         Events}, sorted by ascending date and counting from the received
     *         start point.
     *
     * @throws IllegalArgumentException if the start or count parameters are
     *         negative.
     */
    @PermitAll
    public List<Event> listByDate(
        final int start, final int count
    ) throws IllegalArgumentException {
        if (count == 0) return emptyList();

        // TODO: Pending sort by number of attendees.
        return em.createQuery(
            "SELECT e from Event e where e.date > now() ORDER BY e.date ASC",
            Event.class
        ).setFirstResult(start).setMaxResults(count).getResultList();
    }
    
    /**
     * Returns a paginated {@link List} of {@link Event Events}, sorted by
     * nearest locations and ascending date (which means that older events
     * will be first on the list).
     *
     * @param location The location of the logged {@link User}.
     * @param start The first {@link Event} position to return, numbered from 0.
     * @param count The number of {@link Event Events} to return.
     *
     * @return A sorted {@link List} with the specified number of {@link Event
     *         Events}, sorted by ascending date and counting from the received
     *         start point.
     *
     * @throws IllegalArgumentException if the start or count parameters are
     *         negative.
     */
    @RolesAllowed("USER")
    public List<Event> listByLocation(
        final String location, final int start, final int count
    ) throws IllegalArgumentException {
        if (count == 0) return emptyList();
         
        return em.createQuery(
               "SELECT e from Event e, CapitalDistances cd where cd.capital_A=:location and"
               + " cd.capital_B=e.place and e.date > now() ORDER BY cd.distance ASC, e.date ASC",
               Event.class
    	    ).setParameter("location", location).setFirstResult(start).setMaxResults(count).getResultList();
    }

    /**
     * Returns a paginated {@link List} of {@link Event Events} with all the
     * currently highlighted events, sorted by a magician.
     *
     * @return A sorted {@link List} containing all the highlighted {@link Event
     *         Events}, magically sorted and counting from the received start
     *         point.
     */
    @PermitAll
    public List<Event> listHighlighted() {
        return em.createQuery(
            "SELECT e from Event e where e.date > now() ORDER BY RAND()",
            Event.class
        ).setMaxResults(5).getResultList();
    }

    /**
     * Creates a new {@link Event} in the database, setting its creator as the
     * current identified {@link User}.
     *
     * @param event The {@link Event} to be inserted into the database.
     *
     * @return The created {@link Event} object with all its auto-generated
     *         fields (the event {@linkplain Event#getId()} id) already set.
     *
     * @throws IllegalArgumentException if the received {@link Event} is null
     * @throws SecurityException if the currently identified user is not found
     *         in the database (!!!).
     */
    @RolesAllowed("USER")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Event createEvent(
        final Event event
    ) throws IllegalArgumentException, SecurityException {
        isTrue(nonNull(event), "Event to create cannot be null");

        event.setOwner(auth.getCurrentUser());

        em.persist(event);
        return event;
    }

    /**
     * Searches for {@link Event Events} matching some given {@link String}
     * query, and returns the results as a paginated {@link List}. If the given
     * search pattern is null, a {@link NullPointerException} will be thrown.
     * <br>
     * The method will search inside the event's {@link Event#getTitle() title},
     * and {@link Event#getSummary() short description} and
     * {@link Event#getDescription() long description}. Results are sorted by
     * ascending date and descending number of attendees.
     *
     * @param search The search pattern as a simple {@link String}.
     * @param start The first {@link Event} position to return, counting from 0.
     * @param count
     *            The number of {@link Event Events} to return.
     *
     * @return A sorted {@link List} with the specified number of {@link Event
     *         Events} that have matched the search query.
     *
     * @throws IllegalArgumentException if the received {@link String} with the
     *         search terms is null, or if the start or count parameters are
     *         negative.
     */
    @PermitAll
    public List<Event> search(
        final String search, final int start, final int count
    ) throws IllegalArgumentException {
        isTrue(nonNull(search), "Search query cannot be null");

        if (count == 0) return emptyList();

        // TODO: Pending sort by number of attendees.
        final TypedQuery<Event> query = em.createQuery(
            "SELECT e FROM Event e " +
            " WHERE ( LOWER(e.title) LIKE :search " +
            "    OR LOWER(e.summary) LIKE :search " +
            "OR LOWER(e.description) LIKE :search ) " +
            "AND e.date > now()"+
            "AND e.cancelled =  FALSE " +
            "ORDER BY e.date ASC",
            Event.class
        ).setParameter("search", "%" + search.toLowerCase() + "%");

        return query.setFirstResult(start).setMaxResults(count).getResultList();
    }

    
    
    
    /**
     * Modification of the search method that orders by distance from the residence place
     * @param search The search term
     * @param start The start page
     * @param count The number of elements per page
     * @return a list with the results
     * @throws IllegalArgumentException
     */
    @PermitAll
    public List<Event> searchWhileLoggedIn(
        final String search, final String location, final int start, final int count
    ) throws IllegalArgumentException {
        isTrue(nonNull(search), "Search query cannot be null");
		final User user = auth.getCurrentUser();
		
        if (count == 0) return emptyList();
        //FIXME: Sort by number of attendees
        final TypedQuery<Event> query = em.createQuery(
            "SELECT e FROM Event e JOIN FETCH e.attendees,CapitalDistances cd " +
            " WHERE ( LOWER(e.title) LIKE :search " +
            "    OR LOWER(e.summary) LIKE :search " +
            " OR LOWER(e.description) LIKE :search ) " +
            " AND e.date > now()"+
            " AND e.cancelled =  FALSE " +
            " AND :user MEMBER OF e.attendees and cd.capital_A=:location AND cd.capital_B=e.place " +
            " ORDER BY cd.distance ASC, date DESC",
            Event.class
        ).setParameter("search", "%" + search.toLowerCase() + "%").setParameter("location", location).setParameter("user", auth.getCurrentUser());

        return query.setFirstResult(start).setMaxResults(count).getResultList();
    }

    
    
    
    /**
     * Register the current identified {@link User} into a {@link Event}. It the
     * the current {@link User} is already registered for the event the method
     * will throw an {@link EventAlredyJoinedException}, otherwise it will
     * register the {@link User} to the event.
     *
     * @param eventId Identifier of the {@link Event} that the {@link User}
     *        wants to register.
     *
     * @throws EventAlredyJoinedException if the current identified {@link User}
     *         is already registered for the {@link Event}.
     * @throws SecurityException if the currently identified user is not found
     *         in the database (!!!).
     * @throws EventIsCancelledException if the {@link Event} is cancelled
     *
     * @throws IllegalArgumentException if the {@link Event} does not exist
     */
	@RolesAllowed("USER")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void attendToEvent(final int eventId)
			throws EventAlredyJoinedException, SecurityException,
			EventIsCancelledException, IllegalArgumentException {
		final User user = auth.getCurrentUser();
		final Event event = em.find(Event.class, eventId);

		if (event == null) {
			ctx.setRollbackOnly();
			throw new IllegalArgumentException(
					"The Event with the ID " + eventId + " does not exist");
		}

		if (event.hasAttendee(user)) {
			ctx.setRollbackOnly();
			throw new EventAlredyJoinedException(user, event);
		}

		if (event.isCancelled()) {
			ctx.setRollbackOnly();
			throw new EventIsCancelledException(event);
		}

		event.addAttendee(user);
		em.merge(event);
	}

    /**
     * Return a {@link List} of {@link Event Events} that authenticated
     * {@link User} is joined.
     *
     * @return A {@link List} of {@link Event Events} that authenticated
     *         {@link User} is joined.
     */
    @RolesAllowed("USER")
    public List<Event> getAttendingEvents(final int start, final int count) {
        if (count == 0) return emptyList();

        final TypedQuery<Event> query = em.createQuery(
            "SELECT e FROM Event e JOIN FETCH e.attendees " +
            "WHERE :user MEMBER OF e.attendees " +
            "ORDER BY date DESC, title ASC",
            Event.class
        ).setParameter("user", auth.getCurrentUser());
        if (count < 0)
            return query.setFirstResult(0).setMaxResults(Integer.MAX_VALUE).getResultList();
        else
        	return query.setFirstResult(start).setMaxResults(count).getResultList();
    }
    
    /**
     * Return a {@link List} of {@link Event Events} that authenticated
     * {@link User} is joined ordered by location.
     *
     * @return A {@link List} of {@link Event Events} that authenticated
     *         {@link User} is joined ordered by location.
     */
    @RolesAllowed("USER")
    public List<Event> getAttendingEventsOrderLocation(final String location, final int start, final int count) {
        if (count == 0) return emptyList();        
        
        final TypedQuery<Event> query = em.createQuery(
            "SELECT e FROM Event e JOIN FETCH e.attendees, CapitalDistances cd " +
            "WHERE :user MEMBER OF e.attendees and cd.capital_A=:location and "+
            "cd.capital_B=e.place ORDER BY date DESC, cd.distance ASC, title ASC",
            Event.class
        ).setParameter("location", location).setParameter("user", auth.getCurrentUser());
        if (count < 0)
            return query.setFirstResult(0).setMaxResults(Integer.MAX_VALUE).getResultList();
        else
        	return query.setFirstResult(start).setMaxResults(count).getResultList();
    }

    /**
     * Retrieves a number of {@link Event Events} that authenticated
     * {@link User} is joined. This function is useful to do the paginate of
     * the list of events.
     *
     * @return A number (int) of {@link Event Events} that authenticated
     *         {@link User} is joined.
     */
    @RolesAllowed("USER")
    public int countAttendingEvents() throws SecurityException {
        final User currentUser = auth.getCurrentUser();

        final TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(DISTINCT e.id) FROM Event e JOIN e.attendees " +
            "WHERE :user MEMBER OF e.attendees ",
            Long.class
        ).setParameter("user", currentUser);

        return query.getSingleResult().intValue();
    }

	/**
	 * Get events created by a specific user.
	 *
	 * @param user
	 *            the owner of the events.
	 *
	 * @return a list of {@link Event} that contains the owner`s events.
	 *
	 * @throws IllegalArgumentException
	 *             if the user is null.
	 * 
	 * @deprecated Consider using
	 *             {@link EventEJB#getEventsOwnedBy(User, int, int)} instead.
	 *             The new method returns the same list but paginated
	 */
    @Deprecated
    @RolesAllowed("USER")
    private List<Event> getEventsOwnedBy(
        final User user
    ) throws IllegalArgumentException {
        isTrue(nonNull(user), "User cannot be null");

        // TODO: Pending sort by number of attendees.
        final TypedQuery<Event> query = em.createQuery(
            "SELECT e FROM Event e " +
            "WHERE e.owner = :owner " +
            "ORDER BY e.date DESC",
            Event.class
        );

        return query.setParameter("owner", user).getResultList();
    }

	/**
	 * Returns a list of {@link Event} that are created by the given {@link User}.
	 * 
	 * @param user
	 *            {@link User} owner of the {@link Event}.
	 * @param start
	 *            index of the first event for retrieve.
	 * @param count
	 *            number of events to retrieve.
	 * @return a list of {@link Event} that contains the given number of events
	 *         from the given position owned by the given {@link User}.
	 * @throws IllegalArgumentException
	 *             if the user is null.
	 */
	@RolesAllowed("USER")
	private List<Event> getEventsOwnedBy(final User user, final int start, final int count)
			throws IllegalArgumentException {
		isTrue(nonNull(user), "User cannot be null");
		if (count == 0)
			return emptyList();

		final TypedQuery<Event> query = em
				.createQuery("SELECT e FROM Event e WHERE e.owner = :owner ORDER BY e.date DESC", Event.class)
				.setParameter("owner", user);

		if (count < 0) {
			return query.setFirstResult(0).setMaxResults(Integer.MAX_VALUE).getResultList();
		} else {
			return query.setFirstResult(start).setMaxResults(count).getResultList();
		}
	}

	/**
	 * Returns the number of {@link Event} created by the given {@link User}
	 * 
	 * @param user
	 *            {@link User} for count his created {@link Event}
	 * @return The number of {@link Event} created by the given {@link User}
	 */
	@RolesAllowed("USER")
	public int countEventsOwnedBy(final User user) {
		final TypedQuery<Long> query = em
				.createQuery("SELECT COUNT(DISTINCT e.id) FROM Event e WHERE e.owner = :owner", Long.class)
				.setParameter("owner", user);

		return query.getSingleResult().intValue();
	}
	
	/**
	 * Get events created by the active user.
	 *
	 * @return a list of {@link Event} that contains events of the current user.
	 * 
	 * @deprecated Consider using
	 *             {@link EventEJB#getEventsOwnedByCurrentUser(int, int)}
	 *             instead, that calls to
	 *             {@link EventEJB#getEventsOwnedBy(User, int, int)}
	 */
	@Deprecated
    @RolesAllowed("USER")
    public List<Event> getEventsOwnedByCurrentUser() {
        return getEventsOwnedBy(auth.getCurrentUser());
    }

	/**
	 * Get the {@link Event} created by the active {@link User}.
	 * 
	 * @param start
	 *            index of the first {@link Event} to retrieve.
	 * @param count
	 *            number of {@link Event} to retrieve.
	 * @return a list of {@link Event} that contains the given number of events
	 *         from the given position owned by the current {@link User}.
	 */
    @RolesAllowed("USER")
    public List<Event> getEventsOwnedByCurrentUser(final int start, final int count) {
    	return getEventsOwnedBy(auth.getCurrentUser(), start, count);
    }
    
    /**
     * Returns the number of {@link Event} created by the current {@link User}
     * 
     * @return the number of {@link Event} created by the current {@link User}
     */
    @RolesAllowed("USER")
    public int countEventsOwnedByCurrentUser(){
    	return countEventsOwnedBy(auth.getCurrentUser());
    }

    @RolesAllowed("USER")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void unattendToEvent(
        final int eventId
    ) throws  SecurityException, EventNotJoinedException {
        final User user   = auth.getCurrentUser();
        final Event event = em.find(Event.class, eventId);

        if (event.hasAttendee(user))
            event.removeAttendee(user);
        else{
            ctx.setRollbackOnly();
            throw new EventNotJoinedException(user,event);

        }

        em.merge(event);
    }

    /**
     * Returns a {@code int} with the number of {@link User} attendants of
     * the {@link Event}.
     *
     * @param event the {@link Event} to get the attendants.
     *
     * @return A {@code int} with the number of attendants.
     */
    @PermitAll
    public int getAttendees(final Event event){
    	return em.createQuery(
            "SELECT size(e.attendees) from Event e WHERE e=:event",
            Integer.class
        ).setParameter("event", event).getSingleResult();
    }

	/**
	 * Modifies an existent {@link Event} if the currently identified user is
	 * the owner.
	 *
	 * @param modified the {@link Event} already modified
	 * @throws SecurityException  if the currently identified user is not found
     *         in the database or if he is not the owner of the event.
	 */
	@RolesAllowed("USER")
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void modifyEvent( final Event modified )
    		throws IllegalArgumentException, UserNotAuthorizedException, SecurityException{

        final User user   = auth.getCurrentUser();
        final Event event = em.find(Event.class, modified.getId());

        if (event == null) {
			ctx.setRollbackOnly();
			throw new IllegalArgumentException("The Event does not exist");
		} else if(event.getOwner().equals(user)){
            event.setLocation(modified.getLocation());
            event.setDate(modified.getDate());
            event.setCategory(modified.getCategory());
            event.setSummary(modified.getSummary());
            event.setTitle(modified.getTitle());
            event.setPlace(modified.getPlace());
        }else{
        	ctx.setRollbackOnly();
        	throw new UserNotAuthorizedException(user,event);
        }
        em.merge(event);
    }

    /**
     * Register the current identified {@link User} into a {@link Event}. It the
     * the current {@link User} is already registered for the event the method
     * will throw an {@link EventAlredyJoinedException}, otherwise it will
     * register the {@link User} to the event.
     *
     * @param eventId Identifier of the {@link Event} that the {@link User}
     *        wants to register.
     *
     * @throws SecurityException if the currently identified user is not found
     *         in the database (!!!).
     * @throws EventIsCancelledException if the {@link Event} is cancelled
     *
     * @throws IllegalArgumentException if the {@link Event} does not exist
     * @throws IllegalEventOwnerException  if the event does not exist
     */
	@RolesAllowed("USER")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void cancelEvent(final int eventId)
			throws SecurityException,
			EventIsCancelledException, IllegalArgumentException, IllegalEventOwnerException {
		final User user = auth.getCurrentUser();
		final Event event = em.find(Event.class, eventId);

		if (event == null) {
			ctx.setRollbackOnly();
			throw new IllegalArgumentException(
					"The Event with the ID " + eventId + " does not exist");
		}

		if (event.getOwner()!=user) {
			ctx.setRollbackOnly();
			throw new IllegalEventOwnerException(
					"The User " + user + " is not the owner of the event" + eventId);
		}

		if (event.isCancelled()) {
			ctx.setRollbackOnly();
			throw new EventIsCancelledException(event);
		}

		event.setCancelled(true);
		em.merge(event);
	}


	/**
	 * Retrieves if a event is cancelled.
	 *
	 * @param eventId the id of the {@link Event} to check
	 * @return true if the event is cancelled, false otherwhise
	 * @throws IllegalArgumentException if the event does not exist
	 */
	@PermitAll
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean isCancelled(final int eventId)
			throws
			IllegalArgumentException {
		final Event event = em.find(Event.class, eventId);

		if (event == null) {
			ctx.setRollbackOnly();
			throw new IllegalArgumentException(
					"The Event with the ID " + eventId + " does not exist");
		}

		return event.isCancelled();
	}
	
	/**
	 * Gets all the {@link Capital} in the database.
	 *
	 * @return The {@link List} with all the {@link Capital}
	 */
	@PermitAll
	public List<Capital> getCapitals() {
		return em.createQuery("SELECT c FROM Capital c",
				Capital.class).getResultList();
	}

}
