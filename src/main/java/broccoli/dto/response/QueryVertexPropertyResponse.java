package broccoli.dto.response;

import broccoli.persistence.entity.VertexProperty;

/**
 * Response for a vertex property query.
 *
 * @param vertexId vertex id
 * @param scope    scope
 * @param key      key
 * @param value    value
 */
public record QueryVertexPropertyResponse(String vertexId, String scope, String key, String value) {

  /**
   * Creates a response from a vertex property.
   *
   * @param vertexProperty vertex property
   * @return {@link QueryVertexPropertyResponse}
   */
  public static QueryVertexPropertyResponse of(VertexProperty vertexProperty) {
    return new QueryVertexPropertyResponse(
        vertexProperty.getVertex().getId(),
        vertexProperty.getScope(),
        vertexProperty.getKey(),
        vertexProperty.getValue());
  }
}
