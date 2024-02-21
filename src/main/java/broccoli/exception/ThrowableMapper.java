package broccoli.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

/**
 * ThrowableMapper.
 */
@Provider
@Slf4j
public class ThrowableMapper implements ExceptionMapper<Throwable> {

  @Override
  public Response toResponse(Throwable e) {
    String errorId = UUID.randomUUID().toString();
    log.error("errorId[{}]", errorId, e);
    final var errorMessage = new ErrorResponse.ErrorMessage(e.getMessage());
    final var errorResponse = new ErrorResponse(errorId, errorMessage);
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
  }

}
