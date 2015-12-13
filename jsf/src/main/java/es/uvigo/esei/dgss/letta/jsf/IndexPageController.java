package es.uvigo.esei.dgss.letta.jsf;

import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.Event.Category;
import es.uvigo.esei.dgss.letta.jsf.util.EventMappings;
import es.uvigo.esei.dgss.letta.service.EventEJB;

import static java.time.format.FormatStyle.MEDIUM;

import java.time.LocalDateTime;

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
     * {@link EventEJB#listHighlighted()}
     *
     * @return a List of the highlighted events.
     */
    public List<Event> getHighlights() {
        return eventEJB.listHighlighted();
    }

    /**
     * Returns a {@link String} representing the path to the corresponding
     * {@link Category} icon image.
     *
     * @param event the {@link Event} to be translated into a icon path.
     *
     * @return A String with the path to the icon associated to the received
     *         event.
     */
    public String getIconFor(final Event event) {
        return EventMappings.getIconFor(event.getCategory());
    }

    public String getDate(final Event event) {
        return event.getDate().format(
            DateTimeFormatter.ofLocalizedDateTime(MEDIUM)
        );
    }
    
    public boolean isAfter(final LocalDateTime d) {
	    return LocalDateTime.now().isAfter(d);
	}

}
