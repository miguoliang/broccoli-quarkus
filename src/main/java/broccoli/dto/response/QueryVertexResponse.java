package broccoli.dto.response;

import broccoli.persistence.entity.Vertex;

/**
 * The {@link QueryVertexResponse} class.
 */

public record QueryVertexResponse(String id, String name, String type) {

  public static QueryVertexResponse of(Vertex vertex) {
    return new QueryVertexResponse(vertex.id, vertex.name, vertex.type);
  }
}
