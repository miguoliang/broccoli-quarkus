package broccoli.dto.response;

import broccoli.persistence.entity.Edge;

/**
 * The {@link QueryEdgeResponse} class.
 */
public record QueryEdgeResponse(String inVertexId, String inVertexName, String inVertexType,
                                String outVertexId, String outVertexName, String outVertexType,
                                String name, String scope) {
  /**
   * Convert to {@link QueryEdgeResponse}.
   *
   * @param edge edge
   * @return {@link QueryEdgeResponse}
   */
  public static QueryEdgeResponse of(Edge edge) {
    final var inVertex = edge.inVertex;
    final var outVertex = edge.outVertex;
    return new QueryEdgeResponse(inVertex.id, inVertex.name, inVertex.type,
        outVertex.id, outVertex.name, outVertex.type, edge.name,
        edge.scope);
  }
}
