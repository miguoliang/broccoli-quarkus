package broccoli.dto.request;

import broccoli.persistence.entity.Vertex;
import broccoli.persistence.entity.VertexProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * The {@link SetVertexPropertyRequest} class.
 */
public record SetVertexPropertyRequest(@NotBlank String scope, @NotBlank String key,
                                       @NotNull String value) {

  /**
   * Convert to {@link VertexProperty}.
   *
   * @param vertex vertex
   * @return {@link VertexProperty}
   */
  public VertexProperty toEntity(Vertex vertex) {
    final var vertexProperty = new VertexProperty();
    vertexProperty.setScope(scope);
    vertexProperty.setKey(key);
    vertexProperty.setValue(value);
    vertexProperty.setVertex(vertex);
    return vertexProperty;
  }
}
