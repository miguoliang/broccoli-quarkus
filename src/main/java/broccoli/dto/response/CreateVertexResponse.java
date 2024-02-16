package broccoli.dto.response;

import broccoli.persistence.entity.Vertex;

/**
 * The {@link CreateVertexResponse} class.
 *
 * @param id   Vertex id
 * @param name Vertex name
 * @param type Vertex type
 */
public record CreateVertexResponse(String id, String name, String type) {

  public static CreateVertexResponse of(Vertex vertex) {
    return new CreateVertexResponse(vertex.getId(), vertex.getName(), vertex.getType());
  }
}
