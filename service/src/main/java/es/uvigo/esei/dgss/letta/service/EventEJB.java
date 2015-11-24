package es.uvigo.esei.dgss.letta.service;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import es.uvigo.esei.dgss.letta.domain.entities.Event;

/**
 * EJB for the events.
 *
 * @author abmiguez and bcgonzalez3
 *
 */
@PermitAll
@Stateless
public class EventEJB {
	@PersistenceContext
	EntityManager em;

	/**
	 * Returns the front page events.
	 *
	 * @return the list of the top twenty events ordered by ascending date.
	 */
	public List<Event> getFrontPage() {
		return em.createQuery("SELECT e from Event e ORDER BY e.date ASC", Event.class).setMaxResults(20)
				.getResultList();
	}

	/**
	 * Returns the highlighted front page events.
	 *
	 * @return a list of random five events
	 */
	public List<Event> getFrontPageHighlights() {
		return em.createQuery("SELECT e from Event e ORDER BY RAND()", Event.class).setMaxResults(5).getResultList();
	}
}
