package broccoli.persistence.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

/**
 * The {@link VertexProperty}.
 */
@Setter
@Getter
@Entity
@Table(name = "vertex_property", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"vertex_id", "scope", "property_key"})
})
public class VertexProperty extends PanacheEntity {

  @ManyToOne(optional = false)
  @JoinColumn(name = "vertex_id")
  private Vertex vertex;

  @Column(name = "scope", nullable = false)
  private String scope = "default";

  @Column(name = "property_key", nullable = false)
  private String key;

  @Column(name = "property_value", nullable = false)
  private String value = "";

  @Embedded
  private GeneralColumns generalColumns;

  @Version
  @Column(name = "version", nullable = false)
  private Integer version = 0;

}
