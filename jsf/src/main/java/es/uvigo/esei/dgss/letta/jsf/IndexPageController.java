package es.uvigo.esei.dgss.letta.jsf;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.EventType;
import es.uvigo.esei.dgss.letta.jsf.util.EventMappings;
import es.uvigo.esei.dgss.letta.service.EventEJB;

/**
 * {@linkplain IndexPageController} is a JSF controller for LETTA's front page.
 * Its solely purpose is to retrieve the required data from the
 * {@link EventEJB}.
 *
 * @author Redouane Mehdi
 * @author Alberto Gutiérrez Jácome
 */
@RequestScoped
@ManagedBean(name = "indexController")
public class IndexPageController {

    @Inject
    private EventMappings eventMapper;

    @Inject
    private EventEJB eventEJB;

    /**
     * Retrieves a {@link List} of {@link Event}s from
     * {@link EventEJB#listByDate(int, int)}, sorted by ascending date and
     * descending number of attendees.
     *
     * @return an ordered List of twenty events.
     */
    public List<Event> getEventList() {
        return eventEJB.listByDate(0, 20);
    }

    /**
     * Retrieves a {@link List} of highlighted {@link Event}s from
     * {@link EventEJB#listHighlighted(int, int)}
     *
     * @return a List of the five highlighted events.
     */
    public List<Event> getHighlights() {
        return eventEJB.listHighlighted(0, 5);
    }

    /**
     * Returns a {@link String} representing the path to the corresponding
     * {@link EventType} icon image.
     *
     * @param eventType
     *            the {@link EventType} to be translated into a icon path.
     *
     * @return a String with the path to the icon associated to the received
     *         event type.
     */
    public String getIconFor(final EventType eventType) {
        return eventMapper.getIconFor(eventType);
    }

}
