package es.uvigo.esei.dgss.letta.domain.entities;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static java.util.Objects.isNull;

/**
 * {@linkplain IsEqualToEvent} implements a {@link TypeSafeMatcher} to check
 * for equality of {@link Event} objects.
 *
 * @author Alberto Gutiérrez Jácome
 * @author Adrián Rodríguez Fariña
 */
public class IsEqualToEvent extends IsEqualsToEntity<Event> {

    private final boolean shouldCompareUser;

    private IsEqualToEvent(final Event event, final boolean shouldCompareUser) {
        super(event);

        this.shouldCompareUser = shouldCompareUser;
    }

    /**
     * Static constructor to create {@link IsEqualToEvent} instances with the
     * provided {@link Event} as the expected value.
     * <br>
     * This matcher will not compare the event's {@link Event#getCreator()
     * creator} field. If you do need to also compare it by equality, consider
     * using {@link IsEqualToEvent#equalToEventWithCreator(Event)} instead.
     *
     * @param event The {@link Event} to be used as expected value.
     *
     * @return A new {@link IsEqualToEvent} matcher with the provided
     *         {@link Event} object as the expected value.
     */
    @Factory
    public static IsEqualToEvent equalToEvent(final Event event) {
        return new IsEqualToEvent(event, false);
    }

    /**
     * Static constructor to create {@link IsEqualToEvent} instances with the
     * provided {@link Event} as the expected value.
     *
     * @param event The {@link Event} to be used as expected value.
     *
     * @return A new {@link IsEqualToEvent} matcher with the provided
     *         {@link Event} object as the expected value.
     */
    @Factory
    public static IsEqualToEvent equalToEventWithCreator(final Event event) {
        return new IsEqualToEvent(event, true);
    }

    /**
     * Static constructor to create a {@link Matcher} instance with a set of
     * {@link Event Events} as the expected values, relying upon a set of
     * {@link IsEqualToEvent}, one per received {@link Event}.
     * <br>
     * This matcher will not compare the event's {@link Event#getCreator()
     * creator} field. If you do need to also compare it by equality, consider
     * using {@link IsEqualToEvent#equalToEventWithCreator(Event)} instead.
     *
     * @param events The {@link Event Events} to be used as expected values.
     *
     * @return A new {@link Matcher} including a set of {@link IsEqualToEvent}
     *         instances, created with each one of the received {@link Event
     *         Events} as their expected values.
     *
     * @see IsEqualsToEntity#containsEntityInAnyOrder(java.util.function.Function, Object...)
     */
    @Factory
    public static Matcher<Iterable<? extends Event>> containsEventsInAnyOrder(
        final Event ... events
    ) {
        return containsEntityInAnyOrder(IsEqualToEvent::equalToEvent, events);
    }

    /**
     * Static constructor to create a {@link Matcher} instance with a set of
     * {@link Event Events} as the expected values, relying upon a set of
     * {@link IsEqualToEvent}, one per received {@link Event}.
     *
     * @param events The {@link Event Events} to be used as expected values.
     *
     * @return A new {@link Matcher} including a set of {@link IsEqualToEvent}
     *         instances, created with each one of the received {@link Event
     *         Events} as their expected values.
     *
     * @see IsEqualsToEntity#containsEntityInAnyOrder(java.util.function.Function, Object...)
     */
    @Factory
    public static Matcher<Iterable<? extends Event>> containsEventsWithCreatorinAnyOrder(
        final Event ... events
    ) {
        return containsEntityInAnyOrder(
            IsEqualToEvent::equalToEventWithCreator, events
        );
    }

    @Override
    protected boolean matchesSafely(final Event event) {
        clearDescribeTo();

        if (isNull(event)) {
            addTemplatedDescription("actual", expected.toString());
            return false;
        }

        final boolean userMatches = !shouldCompareUser || checkAttribute(
            "creator", Event::getCreator, event
        );

        return userMatches
            && checkAttribute("eventType", Event::getEventType, event)
            && checkAttribute("title"    , Event::getTitle    , event)
            && checkAttribute("date"     , Event::getDate     , event)
            && checkAttribute("location" , Event::getLocation , event)
            && checkAttribute("shortDescription", Event::getShortDescription, event);
    }


}
