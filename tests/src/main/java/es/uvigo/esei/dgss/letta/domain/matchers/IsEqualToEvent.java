package es.uvigo.esei.dgss.letta.domain.matchers;

import java.util.function.Function;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import es.uvigo.esei.dgss.letta.domain.entities.Event;

import static java.util.Objects.isNull;
import static java.util.stream.StreamSupport.stream;

/**
 * {@linkplain IsEqualToEvent} implements a {@link TypeSafeMatcher} to check
 * for equality of {@link Event} objects.
 *
 * @author Alberto Gutiérrez Jácome
 * @author Adrián Rodríguez Fariña
 */
public class IsEqualToEvent extends IsEqualToEntity<Event> {

    private final boolean shouldCompareOwner;

    private IsEqualToEvent(final Event event, final boolean shouldCompareUser) {
        super(event);

        this.shouldCompareOwner = shouldCompareUser;
    }

    /**
     * Static constructor to create {@link IsEqualToEvent} instances with the
     * provided {@link Event} as the expected value.
     * <br>
     * This matcher will not compare the event's {@link Event#getOwner()
     * owner} field. If you do need to also compare it by equality, consider
     * using {@link IsEqualToEvent#equalToEventWithOwner(Event)} instead.
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
    public static IsEqualToEvent equalToEventWithOwner(final Event event) {
        return new IsEqualToEvent(event, true);
    }

    /**
     * Static constructor to create a {@link Matcher} instance with a set of
     * {@link Event Events} as the expected values, relying upon a set of
     * {@link IsEqualToEvent}, one per received {@link Event}.
     * <br>
     * This matcher will not compare the event's {@link Event#getOwner()
     * owner} field. If you do need to also compare it by equality, consider
     * using {@link IsEqualToEvent#equalToEventWithOwner(Event)} instead.
     *
     * @param events The {@link Event Events} to be used as expected values.
     *
     * @return A new {@link Matcher} including a set of {@link IsEqualToEvent}
     *         instances, created with each one of the received {@link Event
     *         Events} as their expected values.
     *
     * @see IsEqualToEntity#containsEntityInAnyOrder(java.util.function.Function, Object...)
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
     * <br>
     * This matcher will not compare the event's {@link Event#getOwner()
     * owner} field. If you do need to also compare it by equality, consider
     * using {@link IsEqualToEvent#equalToEventWithOwner(Event)} instead.
     *
     * @param events An {@link Iterable} of {@link Event Events} to be used as
     *        expected values.
     *
     * @return A new {@link Matcher} including a set of {@link IsEqualToEvent}
     *         instances, created with each one of the received {@link Event
     *         Events} as their expected values.
     *
     * @see IsEqualToEntity#containsEntityInAnyOrder(java.util.function.Function, Object...)
     */
    @Factory
    public static Matcher<Iterable<? extends Event>> containsEventsListInAnyOrder(
        final Iterable<Event> events
    ) {
        return containsEntityInAnyOrder(
            IsEqualToEvent::equalToEvent,
            stream(events.spliterator(), false).toArray(Event[]::new)
        );
    }

    /**
     * Static constructor to create a {@link Matcher} instance with a set of
     * {@link Event Events} as the expected values, relying upon a set of
     * {@link IsEqualToEvent}, one per received {@link Event}.
     * <br>
     * This matcher checks that the events compared are in the same order as the
     * received ones. If you do not need to check for ordering, consider using
     * {@link #containsEventsInAnyOrder(Event...)} instead.
     * <br>
     * This matcher will not compare the event's {@link Event#getOwner()
     * owner} field. If you do need to also compare it by equality, consider
     * using {@link IsEqualToEvent#equalToEventWithOwner(Event)} instead.
     *
     * @param events The {@link Event Events} to be used as expected values, to
     *        be compared in the same order as given.
     *
     * @return A new {@link Matcher} including a set of {@link IsEqualToEvent}
     *         instances, created with each one of the received {@link Event
     *         Events} as their expected values.
     *
     * @see IsEqualToEntity#containsEntityInOrder(java.util.function.Function, Object...)
     */
    @Factory
    public static Matcher<Iterable<? extends Event>> containsEventsInOrder(
        final Event ... events
    ) {
        return containsEntityInOrder(IsEqualToEvent::equalToEvent, events);
    }

    /**
     * Static constructor to create a {@link Matcher} instance with a set of
     * {@link Event Events} as the expected values, relying upon a set of
     * {@link IsEqualToEvent}, one per received {@link Event}.
     * <br>
     * This matcher checks that the events compared are in the same order as the
     * received ones. If you do not need to check for ordering, consider using
     * {@link #containsEventsInAnyOrder(Event...)} instead.
     * <br>
     * This matcher will not compare the event's {@link Event#getOwner()
     * owner} field. If you do need to also compare it by equality, consider
     * using {@link IsEqualToEvent#equalToEventWithOwner(Event)} instead.
     *
     * @param events An {@link Iterable} of {@link Event Events} to be used as
     *        expected values, to be compared in the same order as given.
     *
     * @return A new {@link Matcher} including a set of {@link IsEqualToEvent}
     *         instances, created with each one of the received {@link Event
     *         Events} as their expected values.
     *
     * @see IsEqualToEntity#containsEntityInOrder(java.util.function.Function, Object...)
     */
    @Factory
    public static Matcher<Iterable<? extends Event>> containsEventsListInOrder(
        final Iterable<Event> events
    ) {
        return containsEntityInOrder(
            IsEqualToEvent::equalToEvent,
            stream(events.spliterator(), false).toArray(Event[]::new)
        );
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
     * @see IsEqualToEntity#containsEntityInAnyOrder(java.util.function.Function, Object...)
     */
    @Factory
    public static Matcher<Iterable<? extends Event>> containsEventsWithOwnerInAnyOrder(
        final Event ... events
    ) {
        return containsEntityInAnyOrder(
            IsEqualToEvent::equalToEventWithOwner, events
        );
    }

    @Override
    protected boolean matchesSafely(final Event event) {
        clearDescribeTo();

        if (isNull(event)) {
            addTemplatedDescription("actual", expected.toString());
            return false;
        }

        final boolean userMatches = !shouldCompareOwner || checkAttribute(
            "owner", Event::getOwner, event
        );

        return userMatches
            && checkAttribute("category", Event::getCategory         , event)
            && checkAttribute("title"   , uncased(Event::getTitle)   , event)
            && checkAttribute("summary" , uncased(Event::getSummary) , event)
            && checkAttribute("date"    , Event::getDate             , event)
            && checkAttribute("location", uncased(Event::getLocation), event);
    }

    private <A> Function<A, String> uncased(final Function<A, String> f) {
        return f.andThen(String::toLowerCase);
    }

}
