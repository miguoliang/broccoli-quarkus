package broccoli.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.PathParam;

/**
 * The {@link DeleteVertexRequest} class.
 */
public record DeleteVertexRequest(@PathParam("id") @NotBlank String id) {
}
