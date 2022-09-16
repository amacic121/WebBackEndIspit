package amacic.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundException extends RuntimeException implements ExceptionMapper<NotFoundException> {

    private static final String DEFAULT_ERROR = "Not found";

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
        System.out.println(message);
    }

    @Override
    public Response toResponse(NotFoundException exception) {
        return Response.status(404).entity(exception.getMessage())
                .type("text/plain").build();
    }
}
