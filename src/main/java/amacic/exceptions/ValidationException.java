package amacic.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ValidationException extends RuntimeException implements ExceptionMapper<ValidationException> {

    private static final String DEFAULT_ERROR = "Validation error";

    public ValidationException() {
        super(DEFAULT_ERROR);
    }

    public ValidationException(String message) {
        super(message);
        System.out.println(message);
    }

    @Override
    public Response toResponse(ValidationException exception) {
        return Response.status(400).entity(exception.getMessage())
                .type("text/plain").build();
    }
}
