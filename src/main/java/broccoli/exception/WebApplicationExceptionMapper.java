package broccoli.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

/**
 * Maps {@link WebApplicationException} to a JSON response.
 */
@Provider
@Slf4j
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

  @Override
  public Response toResponse(WebApplicationException e) {
    final var errorId = UUID.randomUUID().toString();
    final var errorMessage = new ErrorResponse.ErrorMessage(e.getMessage());
    final var errorResponse = new ErrorResponse(errorId, errorMessage);
    return Response.status(e.getResponse().getStatus()).entity(errorResponse).build();
  }
}
