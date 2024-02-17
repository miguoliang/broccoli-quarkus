package broccoli.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * The {@link GeneralColumns} entity.
 */
@Setter
@Getter
@Embeddable
public class GeneralColumns {

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "date_created")
  private LocalDateTime dateCreated = LocalDateTime.now();

  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "date_updated")
  private LocalDateTime dateUpdated = LocalDateTime.now();

}
