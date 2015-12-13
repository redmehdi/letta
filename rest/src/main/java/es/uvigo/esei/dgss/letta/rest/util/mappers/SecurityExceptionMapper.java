package es.uvigo.esei.dgss.letta.rest.util.mappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.status;

@Provider
public class SecurityExceptionMapper implements ExceptionMapper<SecurityException> {

    @Override
    public Response toResponse(final SecurityException se) {
        return status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN)
              .entity(se.getMessage()).build();
    }

}
