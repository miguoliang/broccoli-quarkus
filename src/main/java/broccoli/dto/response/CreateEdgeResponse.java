package broccoli.dto.response;

import broccoli.persistence.entity.Edge;

/**
 * The {@link CreateEdgeResponse} class.
 */

public record CreateEdgeResponse(String inVertexId, String inVertexName, String inVertexType,
                                 String outVertexId, String outVertexName, String outVertexType,
                                 String name, String scope) {

  /**
   * The {@link CreateEdgeResponse} of.
   *
   * @param edge edge
   * @return {@link CreateEdgeResponse}
   */
  public static CreateEdgeResponse of(Edge edge) {
    final var inVertex = edge.inVertex;
    final var outVertex = edge.outVertex;
    return new CreateEdgeResponse(inVertex.id, inVertex.name, inVertex.type,
        outVertex.id, outVertex.name, outVertex.type, edge.name,
        edge.scope);
  }
}
