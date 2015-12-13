package es.uvigo.esei.dgss.letta.rest.util.mappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.status;

@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    @Override
    public Response toResponse(final IllegalArgumentException iae) {
        return status(Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN)
              .entity(iae.getMessage()).build();
    }

}
