package broccoli.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Convert validation exceptions to HTTP 400 Bad Request.
 */
@Provider
public class ConstraintViolationExceptionMapper
    implements ExceptionMapper<ConstraintViolationException> {

  @Override
  public Response toResponse(ConstraintViolationException e) {
    final var errorMessages = e.getConstraintViolations().stream()
        .map(constraintViolation -> new ErrorResponse.ErrorMessage(
            constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage()))
        .toList();
    return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(errorMessages))
        .build();
  }

}
