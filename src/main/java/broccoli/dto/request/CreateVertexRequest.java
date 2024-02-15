package broccoli.dto.request;

import broccoli.persistence.entity.Vertex;
import jakarta.validation.constraints.NotBlank;
import java.security.NoSuchAlgorithmException;

/**
 * The {@link CreateVertexRequest} class.
 *
 * @param name Vertex name
 * @param type Vertex type
 */
public record CreateVertexRequest(@NotBlank String name, @NotBlank String type) {

  /**
   * Convert to {@link Vertex}.
   *
   * @return {@link Vertex}
   */
  public Vertex toEntity() throws NoSuchAlgorithmException {

    final var vertex = new Vertex();
    vertex.setName(name);
    vertex.setType(type);
    return vertex;
  }
}