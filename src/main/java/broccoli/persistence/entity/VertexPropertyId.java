package broccoli.persistence.entity;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * The {@link VertexPropertyId}.
 */
@Setter
@Getter
@EqualsAndHashCode
public class VertexPropertyId implements Serializable {

  private Vertex vertex;
  private String scope;
  private String key;
}
