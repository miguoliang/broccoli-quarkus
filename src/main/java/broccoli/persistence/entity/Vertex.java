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
import java.util.Set;

/**
 * The {@link Vertex} entity.
 */
@Entity
@Table(name = "vertex")
public class Vertex {
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

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Set<Edge> getOutEdges() {
    return outEdges;
  }

  public void setOutEdges(Set<Edge> outEdges) {
    this.outEdges = outEdges;
  }

  public Set<Edge> getInEdges() {
    return inEdges;
  }

  public void setInEdges(Set<Edge> inEdges) {
    this.inEdges = inEdges;
  }

  public Set<VertexProperty> getProperties() {
    return properties;
  }

  public void setProperties(Set<VertexProperty> properties) {
    this.properties = properties;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
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

  public String getProperty(String scope, String key) {
    return properties.stream()
        .filter(property -> property.getScope().equals(scope) && property.getKey().equals(key))
        .map(VertexProperty::getValue)
        .findFirst()
        .orElse(null);
  }
}
