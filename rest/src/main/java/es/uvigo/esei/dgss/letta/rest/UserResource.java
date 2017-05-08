package es.uvigo.esei.dgss.letta.rest;

import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;
import static org.apache.commons.lang3.Validate.isTrue;

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
import es.uvigo.esei.dgss.letta.service.UserEJB;
import es.uvigo.esei.dgss.letta.service.util.exceptions.UserNotAuthorizedException;

/**
 * Resource that represents the {@link User} private funcionalities
 *
 * @author Jesús Álvarez Casanova
 * @author Adolfo Álvarez López
 * @author world1mehdi
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
	private UserEJB userEJB;

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

	/**
	 * Sends friend requests
	 *
	 * @param userLogin
	 * 				the login if {@link User}
	 * @param friendLogin
	 * 				the login of friend's {@link User}
	 * @return An {@code OK} HTTP response
	 * @throws IllegalArgumentException if the provided parameters are not valid.
	 * @throws SecurityException if the user does not have permission.
	 */
	@POST
	@Path("{login}/sendRequest/{friendLogin}")
    public Response sendFriendRequest(
    		@PathParam("login") final String userLogin,
    		@PathParam("friendLogin") final String friendLogin
    ) throws IllegalArgumentException, SecurityException {
		if (userLogin.equals(auth.getCurrentUser().getLogin())){
			userEJB.sendRequest(friendLogin);
            return Response.status(OK).build();
		} else
            return Response.status(UNAUTHORIZED).build();
    }

	/**
	 * Cancels friend requests
	 *
	 * @param userLogin
	 * 				the login if {@link User}
	 * @param friendLogin
	 * 				the login of friend's {@link User}
	 * @return An {@code OK} HTTP response
	 * @throws IllegalArgumentException if the provided parameters are not valid.
	 * @throws SecurityException if the user does not have permission.
	 */
	@POST
	@Path("{login}/cancelRequest/{friendLogin}")
    public Response cancelFriendRequest(
    		@PathParam("login") final String userLogin,
    		@PathParam("friendLogin") final String friendLogin
    ) throws IllegalArgumentException, SecurityException {
		if (userLogin.equals(auth.getCurrentUser().getLogin())){
			userEJB.cancelFriendship(friendLogin);
            return Response.status(OK).build();
		} else
            return Response.status(UNAUTHORIZED).build();
    }

	/**
	 * Accepts friend requests
	 *
	 * @param userLogin
	 * 				the login if {@link User}
	 * @param friendLogin
	 * 				the login of friend's {@link User}
	 * @return An {@code OK} HTTP response
	 * @throws IllegalArgumentException if the provided parameters are not valid.
	 * @throws SecurityException if the user does not have permission.
	 */

	@PUT
	@Path("{login}/acceptRequest/{friendLogin}")
    public Response acceptFriendRequest(
    		@PathParam("login") final String userLogin,
    		@PathParam("friendLogin") final String friendLogin
    ) throws IllegalArgumentException, SecurityException {
		if (userLogin.equals(auth.getCurrentUser().getLogin())){
			userEJB.acceptOrRejectFriendRequest(userLogin, true);
            return Response.status(OK).build();
		} else
            return Response.status(UNAUTHORIZED).build();
    }

	/**
	 * Rejects friend requests
	 *
	 * @param userLogin
	 * 				the login if {@link User}
	 * @param friendLogin
	 * 				the login of friend's {@link User}
	 * @return An {@code OK} HTTP response
	 * @throws IllegalArgumentException if the provided parameters are not valid.
	 * @throws SecurityException if the user does not have permission.
	 */
	@PUT
	@Path("{login}/rejectRequest/{friendLogin}")
    public Response rejectFriendRequest(
    		@PathParam("login") final String userLogin,
    		@PathParam("friendLogin") final String friendLogin
    ) throws IllegalArgumentException, SecurityException {
		if (userLogin.equals(auth.getCurrentUser().getLogin())){
			userEJB.acceptOrRejectFriendRequest(friendLogin, false);
            return Response.status(OK).build();
		} else
            return Response.status(UNAUTHORIZED).build();
    }

	/**
	 * Remove friendship
	 *
	 * @param userLogin
	 * 				the login if {@link User}
	 * @param friendLogin
	 * 				the login of friend's {@link User}
	 * @return An {@code OK} HTTP response
	 * @throws IllegalArgumentException if the provided parameters are not valid.
	 * @throws SecurityException if the user does not have permission.
	 */
	@DELETE
	@Path("{login}/removeFriendship/{friendLogin}")
    public Response removeFriendship(
    		@PathParam("login") final String userLogin,
    		@PathParam("friendLogin") final String friendLogin
    ) throws IllegalArgumentException, SecurityException {
		if (userLogin.equals(auth.getCurrentUser().getLogin())){
			userEJB.removeFriendship(friendLogin);
            return Response.status(OK).build();
		} else
            return Response.status(UNAUTHORIZED).build();
    }


	@GET
	@Path("{login}/searchUser/{keyword}")
	public Response searchForAnUser(
			@PathParam("login") final String userLogin,
			@PathParam("keyword") final String keyword) {
		if (userLogin.equals(auth.getCurrentUser().getLogin()))
            return Response.ok(userEJB.searchUser(keyword)).build();
        else
            return Response.status(UNAUTHORIZED).build();
	}

}
