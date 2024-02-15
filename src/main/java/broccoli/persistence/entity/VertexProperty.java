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
  public Vertex vertex;

  @Id
  @Column(name = "scope", nullable = false)
  public String scope = "default";

  @Id
  @Column(name = "property_key", nullable = false)
  public String key;

  @Column(name = "property_value", nullable = false)
  public String value = "";

  @Embedded
  public GeneralColumns generalColumns;

  @Version
  @Column(name = "version", nullable = false)
  public Integer version = 0;
}
