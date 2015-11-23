package es.uvigo.esei.dgss.letta.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import es.uvigo.esei.dgss.letta.domain.entities.Event;

/**
 * EJB for the basic search.
 * 
 * @author apsoto, aalopez
 *
 */
@Stateless
public class SearchEJB {
	@PersistenceContext
	EntityManager em;

	/**
	 * Search for {@code Event} that match title or description.
	 * 
	 * @param search
	 *            the search pattern.
	 * @throws NoResultException
	 *             if they could not find events.
	 * @return a list of {@code Event}.
	 */
	public List<Event> searchEvent(String search) {
		// Still missing ordination by number of attenders.
		String stringQuery = "SELECT e FROM Event e " + "WHERE e.title LIKE :search OR e.shortDescription LIKE :search "
				+ "ORDER BY e.date ASC";

		List<Event> events = em.createQuery(stringQuery, Event.class).setParameter("search", "%" + search + "%")
				.getResultList();

		return events;
	}
}
