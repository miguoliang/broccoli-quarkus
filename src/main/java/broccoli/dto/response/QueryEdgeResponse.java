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
    final var inVertex = edge.getInVertex();
    final var outVertex = edge.getOutVertex();
    return new QueryEdgeResponse(inVertex.getId(), inVertex.getName(), inVertex.getType(),
        outVertex.getId(), outVertex.getName(), outVertex.getType(), edge.getName(),
        edge.getScope());
  }
}
