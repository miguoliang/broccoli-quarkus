package broccoli.persistence.entity;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * The {@link EdgeId}.
 */
@Setter
@Getter
@EqualsAndHashCode
public class EdgeId implements Serializable {

  private Vertex inVertex;
  private Vertex outVertex;
  private String name;
  private String scope;
}
