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
 * The {@link Edge} entity.
 */
@Setter
@Getter
@Entity
@Table(name = "edge", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"in_vertex_id", "out_vertex_id", "name", "scope"})
})
public class Edge extends PanacheEntity {

  @ManyToOne(optional = false)
  @JoinColumn(name = "in_vertex_id")
  private Vertex inVertex;

  @ManyToOne(optional = false)
  @JoinColumn(name = "out_vertex_id")
  private Vertex outVertex;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "scope", nullable = false)
  private String scope = "default";

  @Embedded
  private GeneralColumns generalColumns;

  @Version
  @Column(name = "version", nullable = false)
  private Integer version = 0;

}
