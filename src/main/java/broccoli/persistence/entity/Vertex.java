package broccoli.persistence.entity;

import broccoli.common.DigestUtils;
import broccoli.common.StringUtils;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**
 * The {@link Vertex} entity.
 */
@Getter
@Setter
@Entity
@Table(name = "vertex")
public class Vertex extends PanacheEntityBase {
  @Id
  @Column(name = "id", nullable = false)
  private String id;

  @OneToMany(mappedBy = "inVertex", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Edge> outEdges = new LinkedHashSet<>();

  @OneToMany(mappedBy = "outVertex", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Edge> inEdges = new LinkedHashSet<>();

  @OneToMany(mappedBy = "vertex", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<VertexProperty> properties = new LinkedHashSet<>();

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "type", nullable = false)
  private String type;

  @Embedded
  private GeneralColumns generalColumns;

  @Version
  @Column(name = "version", nullable = false)
  private Integer version = 0;

  /**
   * Calculate vertex id.
   *
   * @param name Vertex name
   * @param type Vertex type
   * @return Vertex id
   */
  public static String getId(String name, String type) throws NoSuchAlgorithmException {
    return DigestUtils.sha512Hex(
        StringUtils.defaultString(type) + ":" + StringUtils.defaultString(name));
  }

  /**
   * Set vertex name.
   *
   * @param name Vertex name
   */
  public void setName(String name) throws NoSuchAlgorithmException {
    this.name = name;
    this.id = getId(name, type);
  }

  /**
   * Set vertex type.
   *
   * @param type Vertex type
   */
  public void setType(String type) throws NoSuchAlgorithmException {
    this.type = type;
    this.id = getId(name, type);
  }

  /**
   * Get property.
   *
   * @param scope scope
   * @param key   key
   * @return the value of the property
   */
  public String getProperty(String scope, String key) {
    return properties.stream()
        .filter(property -> property.getScope().equals(scope) && property.getKey().equals(key))
        .map(VertexProperty::getValue)
        .findFirst()
        .orElse(null);
  }
}
