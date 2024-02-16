package broccoli.persistence.entity;

import java.io.Serializable;

/**
 * The {@link VertexPropertyId}.
 */
public class VertexPropertyId implements Serializable {

  private Vertex vertex;
  private String scope;
  private String key;

  public Vertex getVertex() {
    return vertex;
  }

  public void setVertex(Vertex vertex) {
    this.vertex = vertex;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    VertexPropertyId that = (VertexPropertyId) o;

    if (!vertex.equals(that.vertex)) {
      return false;
    }
    if (!scope.equals(that.scope)) {
      return false;
    }
    return key.equals(that.key);
  }

  @Override
  public int hashCode() {
    int result = vertex.hashCode();
    result = 31 * result + scope.hashCode();
    result = 31 * result + key.hashCode();
    return result;
  }
}
