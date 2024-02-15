package broccoli.persistence.entity;

import broccoli.common.DigestUtils;
import broccoli.common.StringUtils;
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
import java.util.List;
import java.util.Set;

/**
 * The {@link Vertex} entity.
 */
@Entity
@Table(name = "vertex")
public class Vertex {
  @Id
  @Column(name = "id", nullable = false)
  public String id;

  @OneToMany(mappedBy = "inVertex", cascade = CascadeType.ALL, orphanRemoval = true)
  public Set<Edge> outEdges = new LinkedHashSet<>();

  @OneToMany(mappedBy = "outVertex", cascade = CascadeType.ALL, orphanRemoval = true)
  public Set<Edge> inEdges = new LinkedHashSet<>();

  @OneToMany(mappedBy = "vertex", cascade = CascadeType.ALL, orphanRemoval = true)
  public Set<VertexProperty> properties = new LinkedHashSet<>();

  @Column(name = "name", nullable = false)
  public String name;

  @Column(name = "type", nullable = false)
  public String type;

  @Embedded
  public GeneralColumns generalColumns;

  @Version
  @Column(name = "version", nullable = false)
  public Integer version = 0;

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
   * Check if a property exists.
   *
   * @param scope scope
   * @param key   key
   * @return true if exists
   */
  public boolean hasProperty(String scope, String key) {
    return properties.stream()
        .anyMatch(p -> p.scope.equals(scope) && p.key.equals(key));
  }

  /**
   * Get property values.
   *
   * @param scope scope
   * @param key   key
   * @return property values
   */
  public List<String> getProperty(String scope, String key) {
    return properties.stream()
        .filter(p -> p.scope.equals(scope) && p.key.equals(key))
        .sorted((a, b) -> b.generalColumns.dateUpdated
            .compareTo(a.generalColumns.dateUpdated))
        .map(it -> it.value)
        .toList();
  }
}
