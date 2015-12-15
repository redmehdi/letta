package es.uvigo.esei.dgss.letta.service.util.exceptions;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import es.uvigo.esei.dgss.letta.domain.entities.Event;

public class EventIsCancelledException extends Exception {
	private static final long serialVersionUID = 1L;

	public EventIsCancelledException(final Event event) {
		super(format("Event with ID '%d' is cancelled",
			requireNonNull(event).getId()
		));
	}
}

