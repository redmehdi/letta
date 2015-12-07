package es.uvigo.esei.dgss.letta.rest;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.uvigo.esei.dgss.letta.domain.entities.Event;
import es.uvigo.esei.dgss.letta.service.EventEJB;

import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.OK;

import static org.apache.commons.lang3.Validate.isTrue;

/**
 * Resource that represents the {@link Event Events} in the application.
 *
 * @author Alberto Gutiérrez Jácome
 * @author Alberto Pardellas Soto
 */
@Path("public/event")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventResource {

    @EJB
    private EventEJB events;

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
     * title} and the {@link Event#getShortDescription() summary}.
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
    public Response seach(
        @QueryParam("query") @DefaultValue("")   final String query,
        @QueryParam("page")  @DefaultValue("1")  final int    page,
        @QueryParam("size")  @DefaultValue("20") final int    size
    ) throws IllegalArgumentException {
        isTrue(page >= 1, "Page number must be greater than zero");
        isTrue(size >= 0, "Page size must be non-negative");

        final int start = (page - 1) * size;
        return status(OK).entity(events.search(query, start, size)).build();
    }

}
