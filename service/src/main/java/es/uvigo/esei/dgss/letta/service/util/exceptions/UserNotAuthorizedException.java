package es.uvigo.esei.dgss.letta.service.util.exceptions;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.User;

public class UserNotAuthorizedException extends Exception {
	private static final long serialVersionUID = 1L;

	public UserNotAuthorizedException(final User user, final Event event) {
		super(format("User '%s' is not authorized to perform operations "
				+ "with event '%d'",
			requireNonNull(user).getLogin(),
			requireNonNull(event).getId()
		));
	}
}
