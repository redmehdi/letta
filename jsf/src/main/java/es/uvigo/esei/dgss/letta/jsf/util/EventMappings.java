package es.uvigo.esei.dgss.letta.jsf.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.Event.Category;

import static es.uvigo.esei.dgss.letta.domain.entities.Event.Category.*;

/**
 * Provides utility methods to correctly display {@link Event Events} in a JSF
 * page.
 *
 * @author Alberto Gutiérrez Jácome
 * @author Redouane Mehdi
 */
public class EventMappings {

    // Disallow construction
    private EventMappings() { }

    /**
     * Returns a {@link String} representing the path to the corresponding
     * {@link Category} icon image.
     *
     * @param type The {@link Category} to be translated into an icon path.
     *
     * @return A {@link String} holding the path to the icon associated to the
     *         received {@link Category}.
     *
     * @throws IllegalArgumentException If the received {@link Category} does
     *         not have an associated icon.
     * @throws NullPointerException If the received {@link Category} is null.
     */
    public static String getIconFor(
        final Category type
    ) throws IllegalArgumentException, NullPointerException {
        final Map<Category, String> icons = new HashMap<>();
        icons.put(MOVIES     , "img/icons/movies.png");
        icons.put(INTERNET   , "img/icons/internet.png");
        icons.put(BOOKS      , "img/icons/books.png");
        icons.put(MUSIC      , "img/icons/music.png");
        icons.put(SPORTS     , "img/icons/sports.png");
        icons.put(THEATRE    , "img/icons/theatre.png");
        icons.put(TRAVELS    , "img/icons/travels.png");
        icons.put(TELEVISION , "img/icons/television.png");

        return Optional.ofNullable(icons.get(type))
              .orElseThrow(IllegalArgumentException::new);
    }

}
