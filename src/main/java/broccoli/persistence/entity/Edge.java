package broccoli.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

/**
 * The {@link Edge} entity.
 */
@Entity
@Table(name = "edge")
@IdClass(EdgeId.class)
public class Edge {

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "in_vertex_id")
  private Vertex inVertex;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "out_vertex_id")
  private Vertex outVertex;

  @Id
  @Column(name = "name", nullable = false)
  private String name;

  @Id
  @Column(name = "scope", nullable = false)
  private String scope = "default";

  @Embedded
  private GeneralColumns generalColumns;

  @Version
  @Column(name = "version", nullable = false)
  private Integer version = 0;

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

  public GeneralColumns getGeneralColumns() {
    return generalColumns;
  }

  public void setGeneralColumns(GeneralColumns generalColumns) {
    this.generalColumns = generalColumns;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }
}
