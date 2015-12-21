package es.uvigo.esei.dgss.letta.rest.util.mappers;

import javax.ejb.EJBAccessException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.status;

@Provider
public class EJBAccessExceptionMapper implements ExceptionMapper<EJBAccessException> {

    @Override
    public Response toResponse(final EJBAccessException eae) {
        return status(Status.UNAUTHORIZED).type(MediaType.TEXT_PLAIN)
              .entity("You are not allowed to perform the requested action")
              .build();
    }

}
