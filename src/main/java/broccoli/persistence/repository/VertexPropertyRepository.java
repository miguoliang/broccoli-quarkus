package broccoli.persistence.repository;

import broccoli.persistence.entity.VertexProperty;
import broccoli.persistence.entity.VertexPropertyId;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * The {@link VertexPropertyRepository}.
 */
@ApplicationScoped
public class VertexPropertyRepository
    implements PanacheRepositoryBase<VertexProperty, VertexPropertyId> {
}
