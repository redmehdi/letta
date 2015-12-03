package es.uvigo.esei.dgss.letta.service.util.exceptions;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.User;

public class EventAlredyJoinedException extends Exception {
	private static final long serialVersionUID = 1L;

	public EventAlredyJoinedException(final User user, final Event event) {
		super(format("User '%s' already registered for the Event with ID '%d'",
			requireNonNull(user).getLogin(),
			requireNonNull(event).getId()
		));
	}
}
