package es.uvigo.esei.dgss.letta.service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.User;

import static java.util.Optional.ofNullable;

/**
 * EJB for the events.
 *
 * @author Aitor Blanco Míguez
 * @author Borja Cordeiro González
 * @author Adrián Rodríguez Fariña
 * @author Alberto Gutiérrez Jácome
 */
@Stateless
public class EventEJB {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Principal principal;

    /**
     * Returns the front page events.
     *
     * @return the list of the top twenty events ordered by ascending date.
     */
    @PermitAll
    public List<Event> getFrontPage() {
        return em.createQuery(
            "SELECT e from Event e ORDER BY e.date ASC", Event.class
        ).setMaxResults(20).getResultList();
    }

    /**
     * Returns the highlighted front page events.
     *
     * @return a list of random five events
     */
    @PermitAll
    public List<Event> getFrontPageHighlights() {
        return em.createQuery(
            "SELECT e from Event e ORDER BY RAND()", Event.class
        ).setMaxResults(5).getResultList();
    }

    @RolesAllowed("USER")
    public Event createEvent(final Event event) {
        return getLoggedInUser()
              .map(user -> createEventFor(user, event))
              .orElseThrow(SecurityException::new);
    }

    private Event createEventFor(final User user, final Event event) {
        event.setCreator(user);
        em.persist(event);
        return event;
    }

    private Optional<User> getLoggedInUser() {
        return ofNullable(principal.getName())
              .flatMap(login -> ofNullable(em.find(User.class, login)));
    }

}
