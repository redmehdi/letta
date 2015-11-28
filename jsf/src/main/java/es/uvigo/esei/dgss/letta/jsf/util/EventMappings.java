package es.uvigo.esei.dgss.letta.jsf.util;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.EventType;

import static es.uvigo.esei.dgss.letta.domain.entities.EventType.*;

/**
 * Provides utility methods to correctly display {@link Event Events} in a JSF
 * page.
 *
 * @author Alberto Gutiérrez Jácome
 * @author Redouane Mehdi
 */
@Singleton
public class EventMappings {

    /**
     * Returns a {@link String} representing the path to the corresponding
     * {@link EventType} icon image.
     *
     * @param type The {@link EventType} to be translated into an icon path.
     *
     * @return A {@link String} holding the path to the icon associated to the
     *         received {@link EventType}.
     */
    public String getIconFor(final EventType type) {
        final Map<EventType, String> icons = new HashMap<>();
        icons.put(LITERATURE , "img/book.svg");
        icons.put(MUSIC      , "img/music.svg");
        icons.put(CINEMA     , "img/movie.svg");
        icons.put(TV         , "img/tv.svg");
        icons.put(INTERNET   , "img/internet.svg");

        return icons.getOrDefault(type, "http://placehold.it/512x512");
    }


}
