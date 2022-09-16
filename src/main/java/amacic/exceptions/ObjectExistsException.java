package amacic.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ObjectExistsException extends RuntimeException implements ExceptionMapper<ObjectExistsException> {

    private static final String DEFAULT_ERROR = "Item already exists";

    public ObjectExistsException() {
        super(DEFAULT_ERROR);
    }

    public ObjectExistsException(String message) {
        super(message);
        System.out.println(message);
    }

    @Override
    public Response toResponse(ObjectExistsException exception) {
        return Response.status(409).entity(exception.getMessage())
                .type("text/plain").build();
    }
}
