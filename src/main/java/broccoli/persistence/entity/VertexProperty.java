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
 * The {@link VertexProperty}.
 */
@Entity
@IdClass(VertexPropertyId.class)
@Table(name = "vertex_property")
public class VertexProperty {

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "vertex_id")
  private Vertex vertex;

  @Id
  @Column(name = "scope", nullable = false)
  private String scope = "default";

  @Id
  @Column(name = "property_key", nullable = false)
  private String key;

  @Column(name = "property_value", nullable = false)
  private String value = "";

  @Embedded
  private GeneralColumns generalColumns;

  @Version
  @Column(name = "version", nullable = false)
  private Integer version = 0;

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

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
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
