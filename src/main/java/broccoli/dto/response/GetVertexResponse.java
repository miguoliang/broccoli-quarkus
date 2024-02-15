package broccoli.dto.response;

import broccoli.persistence.entity.Vertex;

/**
 * The {@link GetVertexResponse} class.
 *
 * @param id   Vertex id
 * @param name Vertex name
 * @param type Vertex type
 */
public record GetVertexResponse(String id, String name, String type) {

  public static GetVertexResponse of(Vertex vertex) {
    return new GetVertexResponse(vertex.id, vertex.name, vertex.type);
  }
}
