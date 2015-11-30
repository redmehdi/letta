package es.uvigo.esei.dgss.letta.service.util.exceptions;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.User;

import static java.text.MessageFormat.format;
import static java.util.Objects.requireNonNull;

public final class EventAlredyJoinedException extends Exception {

    private static final long serialVersionUID = 1L;

    private final User  user;
    private final Event event;

    public EventAlredyJoinedException(final User user, final Event event) {
        super(format(
            "User '{0}' already registered for the Event with ID '{1}'",
            requireNonNull(user).getLogin(),
            requireNonNull(event).getId()
        ));

        this.user  = user;
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public Event getEvent() {
        return event;
    }

}
