package broccoli.persistence.repository;

import broccoli.persistence.entity.Vertex;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VertexRepository implements PanacheRepositoryBase<Vertex, String> {
}
