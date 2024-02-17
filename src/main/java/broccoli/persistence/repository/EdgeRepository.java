package broccoli.persistence.repository;

import broccoli.persistence.entity.Edge;
import broccoli.persistence.entity.EdgeId;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * The {@link EdgeRepository}.
 */
@ApplicationScoped
public class EdgeRepository implements PanacheRepositoryBase<Edge, EdgeId> {
}
