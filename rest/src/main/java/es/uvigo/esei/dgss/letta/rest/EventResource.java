package es.uvigo.esei.dgss.letta.rest;

import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.apache.commons.lang3.Validate.isTrue;

import java.net.URI;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
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
import es.uvigo.esei.dgss.letta.domain.entities.Event.Category;
import es.uvigo.esei.dgss.letta.service.EventEJB;
import es.uvigo.esei.dgss.letta.service.util.exceptions.EventAlredyJoinedException;
import es.uvigo.esei.dgss.letta.service.util.exceptions.EventIsCancelledException;
import es.uvigo.esei.dgss.letta.service.util.exceptions.UserNotAuthorizedException;

/**
 * Resource that represents the {@link Event Events} in the application.
 *
 * @author Alberto Gutiérrez Jácome
 * @author Alberto Pardellas Soto
 * @author Jesús Álvarez Casanova
 * @author Adolfo Álvarez López
 */
@Path("event")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventResource {

    @EJB
    private EventEJB events;

    @Context
    private UriInfo uri;

    private URI getEventURI(final int id) {
        return uri.getAbsolutePathBuilder().path("" + id).build();
    }

    @GET
    @Path("{id: \\d+}")
    public Response get(@PathParam("id") final int id) {
        return events.get(id) // .fold(status(NOT_FOUND), status(OK)::entity)
              .map(status(OK)::entity)
              .orElse(status(NOT_FOUND))
              .build();
    }

    /**
     * Returns the list of {@link Event Events} stored in the application,
     * sorted by ascending date and paginated.
     *
     * @param page Page number to retrieve, counting from 1. If not set, it will
     *        be defaulted to the first page.
     * @param size Number of events to retrieve per page. If not set, it will be
     *        defaulted to twenty.
     *
     * @return An {@code OK} HTTP response containing the list of events in the
     *         requested page.
     *
     * @throws IllegalArgumentException If either the page number is less than
     *         one or the page size is less than zero.
     */
    @GET
    public Response list(
        @QueryParam("page") @DefaultValue("1")  final int page,
        @QueryParam("size") @DefaultValue("20") final int size
    ) throws IllegalArgumentException {
        isTrue(page >= 1, "Page number must be greater than zero");
        isTrue(size >= 0, "Page size must be non-negative");

        final int start = (page - 1) * size;
        return status(OK).entity(events.listByDate(start, size)).build();
    }

    /**
     * Returns the list of {@link Event Events} that are currently highlighted.
     *
     * @return A {@code OK} HTTP response containing the list of the events in
     *         the application that are currently highlighted.
     */
    @GET
    @Path("highlighted")
    public Response highlighted() {
        return status(OK).entity(events.listHighlighted()).build();
    }

    /**
     * Returns the list of {@link Event Events} matching a given search query.
     * The search will be performed in both the event's {@link Event#getTitle()
     * title} and the {@link Event#getSummary() summary}.
     *
     * @param query The search query (title/summary terms) to perform. If not
     *        set, it will be defaulted to an empty search query, thus
     *        supposedly returning all the events (equivalent to
     *        {@link #list(int, int)}).
     * @param page Page number to retrieve, counting from 1. If not set, it will
     *        be defaulted to the first page.
     * @param size Number of events to retrieve per page. If not set, it will be
     *        defaulted to twenty.
     *
     * @return An {@code OK} HTTP response containing the list of events that
     *         result from executing a search with the specified query in the
     *         requested page.
     *
     * @throws IllegalArgumentException If either the page number is less than
     *         one or the page size is less than zero.
     */
    @GET
    @Path("search")
    public Response search(
        @QueryParam("query") @DefaultValue("")   final String query,
        @QueryParam("page")  @DefaultValue("1")  final int    page,
        @QueryParam("size")  @DefaultValue("20") final int    size
    ) throws IllegalArgumentException {
        isTrue(page >= 1, "Page number must be greater than zero");
        isTrue(size >= 0, "Page size must be non-negative");

        final int start = (page - 1) * size;
        return status(OK).entity(events.search(query, start, size)).build();
    }

    @POST
    public Response create(
        final Event event
    ) throws IllegalArgumentException, SecurityException {
        final int id = events.createEvent(event).getId();
        return status(CREATED).location(getEventURI(id)).build();
    }

    @PUT
    @Path("{id: \\d+}")
    public Response update(
        @PathParam("id") final int id, final Event event
    ) throws IllegalArgumentException, SecurityException {
        isTrue(event.getId() == id, "Given Event does not match path's ID");

        try {
            events.modifyEvent(event);
            return status(OK).location(getEventURI(event.getId())).build();
        } catch (final UserNotAuthorizedException e) {
            throw new SecurityException(e.getMessage());
        }
    }

    @GET
    @Path("{id: \\d+}/attendees")
    public Response getAttendees(@PathParam("id") final int id) {
        return events.getWithAttendees(id)
              .map(e -> status(OK).entity(e.getAttendees()))
              .orElse(status(NOT_FOUND))
              .build();
    }

    @POST
    @Path("{id: \\d+}/attendees")
    public Response attendToEvent(
        @PathParam("id") final int id
    ) throws SecurityException, IllegalArgumentException {
        try {
            events.attendToEvent(id);
            return status(NO_CONTENT).build();
        } catch (final EventAlredyJoinedException | EventIsCancelledException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    @GET
    @Path("advanced_search")
    public Response advanced_search(
        @QueryParam("category") @DefaultValue("")   final String category,
        @QueryParam("query") @DefaultValue("")   final String query,
        @QueryParam("state") @DefaultValue("")   final String state,
        @QueryParam("page")  @DefaultValue("1")  final int    page,
        @QueryParam("size")  @DefaultValue("20") final int    size
    ) throws IllegalArgumentException {
        isTrue(page >= 1, "Page number must be greater than zero");
        isTrue(size >= 0, "Page size must be non-negative");
        boolean aux=false;
        if(state=="TRUE"){
        	aux=true;
        }else{
        	aux=false;
        }
        Category cat =null;
        Category nova= cat.valueOf("category");
        final int start = (page - 1) * size;
        List<Event> rlist= events.advanced_search(nova, query, aux, start, size);
        System.out.println("O resultado e "+rlist.toString());
        return status(OK).entity(rlist).build();
    }
	/**
	 * 
	 * Returns the {@link Event} information
	 * 
	 * @param eventId
	 *            indicates the Event id
	 * @return the {@link Event} information
	 * @throws IllegalArgumentException
	 *             if the {@link Event} is not found
	 */
	@GET
	@Path("{id}")
	public Response getEventInfo(@PathParam("id") int eventId)
			throws IllegalArgumentException {
		final Event event = events.getEvent(eventId);

		if (event == null)
			throw new IllegalArgumentException("Event not found: " + eventId);
		else
			return Response.ok(event).build();
	}

}
