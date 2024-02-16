package broccoli.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * The {@link GeneralColumns} entity.
 */
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

  public LocalDateTime getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(LocalDateTime dateCreated) {
    this.dateCreated = dateCreated;
  }

  public LocalDateTime getDateUpdated() {
    return dateUpdated;
  }

  public void setDateUpdated(LocalDateTime dateUpdated) {
    this.dateUpdated = dateUpdated;
  }
}
