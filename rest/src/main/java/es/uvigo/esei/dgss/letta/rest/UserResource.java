package es.uvigo.esei.dgss.letta.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.domain.entities.User;
import es.uvigo.esei.dgss.letta.service.EventEJB;
import es.uvigo.esei.dgss.letta.service.UserAuthorizationEJB;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import static org.apache.commons.lang3.Validate.isTrue;

/**
 * Resource that represents the {@link User} private funcionalities
 *
 * @author Jesús Álvarez Casanova
 * @author Adolfo Álvarez López
 *
 */
@Path("user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

	@Context
	private UriInfo uriInfo;

	@Inject
	private EventEJB eventEJB;

	@Inject
	private UserAuthorizationEJB auth;

	/**
	 * Returns the {@link Event} joined by the current {@link User} sorted by
	 * descending Date
	 *
	 * @param userLogin
	 *            Login of the {@link User}
	 * @param start
	 *            Index of the list of {@link Event} where you want to start the
	 *            list. If not set, it will be defaulted to the first element
	 * @param count
	 *            Number of {@link Event} asked for retrieved. If not set, it
	 *            will be defaulted to twenty
	 * @return An {@code OK} HTTP response containing the list of events or a
	 *         response with no content if the login doesn't match with the
	 *         logged user
	 */
	@GET
	@Path("{login}/joined")
	public Response getEventsJoinedByUser(
			@PathParam("login") final String userLogin,
			@QueryParam("start") @DefaultValue("0") final int start,
			@QueryParam("count") @DefaultValue("20") final int count) {
		isTrue(start >= 0, "The start number must be greater than zero");
		isTrue(count >= 0, "Number of events must be non-negative");

		if (userLogin.equals(auth.getCurrentUser().getLogin()))
            return Response.ok(eventEJB.getAttendingEvents(start, count))
					.build();
        else
            return Response.status(UNAUTHORIZED).build();
	}

	/**
	 * Returns the {@link Event} created by the current {@link User}
	 *
	 * @param userLogin
	 *            Login of the {@link User}
	 * @return An {@code OK} HTTP response containing the list of events or a
	 *         response with no content if the login doesn't match with the
	 *         logged user
	 */
	@GET
	@Path("{login}/created")
	public Response getEventsCreatedByUser(
			@PathParam("login") final String userLogin) {
		if (userLogin.equals(auth.getCurrentUser().getLogin()))
            return Response.ok(eventEJB.getEventsOwnedByCurrentUser()).build();
        else
            return Response.status(UNAUTHORIZED).build();
	}

}
