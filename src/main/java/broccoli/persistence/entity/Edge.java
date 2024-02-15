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
  public Vertex inVertex;

  @Id
  @ManyToOne(optional = false)
  @JoinColumn(name = "out_vertex_id")
  public Vertex outVertex;

  @Id
  @Column(name = "name", nullable = false)
  public String name;

  @Id
  @Column(name = "scope", nullable = false)
  public String scope = "default";

  @Embedded
  public GeneralColumns generalColumns;

  @Version
  @Column(name = "version", nullable = false)
  public Integer version = 0;
}
