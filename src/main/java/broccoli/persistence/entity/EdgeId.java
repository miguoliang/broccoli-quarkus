package broccoli.persistence.entity;

import java.io.Serializable;

/**
 * The {@link EdgeId}.
 */
public class EdgeId implements Serializable {

  private Vertex inVertex;
  private Vertex outVertex;
  private String name;
  private String scope;

  public Vertex getInVertex() {
    return inVertex;
  }

  public void setInVertex(Vertex inVertex) {
    this.inVertex = inVertex;
  }

  public Vertex getOutVertex() {
    return outVertex;
  }

  public void setOutVertex(Vertex outVertex) {
    this.outVertex = outVertex;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    EdgeId edgeId = (EdgeId) o;

    if (!inVertex.equals(edgeId.inVertex)) {
      return false;
    }
    if (!outVertex.equals(edgeId.outVertex)) {
      return false;
    }
    if (!name.equals(edgeId.name)) {
      return false;
    }
    return scope.equals(edgeId.scope);
  }

  @Override
  public int hashCode() {
    int result = inVertex.hashCode();
    result = 31 * result + outVertex.hashCode();
    result = 31 * result + name.hashCode();
    result = 31 * result + scope.hashCode();
    return result;
  }
}
