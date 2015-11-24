package es.uvigo.esei.dgss.letta.jsf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.EventType;
import es.uvigo.esei.dgss.letta.service.EventEJB;

import static es.uvigo.esei.dgss.letta.domain.entities.EventType.*;

/**
 * {@linkplain IndexPageController} is a {@link JSFController} for LETTA's front
 * page. Its solely purpose is to retrieve the required data from the
 * {@link EventEJB}.
 *
 * @author Redouane Mehdi
 * @author Alberto Gutiérrez Jácome
 */
@RequestScoped
@ManagedBean(name = "indexController")
public class IndexPageController implements JSFController {

    private static final Map<EventType, String> eventIcons;

    static {
        eventIcons = new HashMap<>();
        eventIcons.put(LITERATURE , "img/book.svg");
        eventIcons.put(MUSIC      , "img/music.svg");
        eventIcons.put(CINEMA     , "img/movie.svg");
        eventIcons.put(TV         , "img/tv.svg");
        eventIcons.put(INTERNET   , "img/internet.svg");
    }

    @Inject
    private EventEJB eventEJB;

    /**
     * Retrieves a {@link List} of {@link Event}s from
     * {@link EventEJB#getFrontPage()}, ordered by ascending date and descending
     * number of attendees.
     *
     * @return an ordered List of twenty events.
     */
    public List<Event> getEventList() {
        return eventEJB.getFrontPage();
    }

    /**
     * Retrieves a {@link List} of highlighted {@link Event}s from
     * {@link EventEJB#getFrontPageHighlights()}
     *
     * @return a List of the five highlighted events.
     */
    public List<Event> getHighlights() {
        return eventEJB.getFrontPageHighlights();
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
        return eventIcons.getOrDefault(
            eventType, "http://placehold.it/512x512"
        );
    }

}
