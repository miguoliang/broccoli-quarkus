package broccoli.resource;

import broccoli.dto.response.Page;
import broccoli.dto.response.QueryEdgeResponse;
import broccoli.persistence.entity.Edge;
import broccoli.persistence.entity.Vertex;
import broccoli.persistence.repository.EdgeRepository;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.annotation.Nullable;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.util.Set;

@Path("/edge")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EdgeResource {

  private final EdgeRepository edgeRepository;

  @Inject
  public EdgeResource(EdgeRepository edgeRepository) {
    this.edgeRepository = edgeRepository;
  }

  @GET
  @Transactional
  public Page<QueryEdgeResponse> searchEdges(@QueryParam("vid") @NotEmpty Set<String> vid,
                                             @QueryParam("name") @NotEmpty Set<String> name,
                                             @QueryParam("scope") @NotEmpty Set<String> scope,
                                             @QueryParam("page") @Nullable Integer pageNumber,
                                             @QueryParam("size") @Nullable Integer pageSize) {
    final var index = pageNumber == null ? 0 : pageNumber;
    final var size = pageSize == null ? 20 : pageSize;
    final var content = edgeRepository
        .find("inVertex.id in ?1 and name in ?2 and scope in ?3", vid, name, scope)
        .page(index, size)
        .stream()
        .map(QueryEdgeResponse::of)
        .toList();
    final var count =
        PanacheEntityBase.count("inVertex.id in ?1 and name in ?2 and scope in ?3", vid, name,
            scope);
    return new Page<>(content, index, size, count);
  }

  @POST
  @Transactional
  public QueryEdgeResponse createEdge(QueryEdgeResponse request) {
    final var edge = new Edge();
    edge.inVertex = new Vertex();
    edge.inVertex.id = request.inVertexId();
    edge.outVertex = new Vertex();
    edge.outVertex.id = request.outVertexId();
    edge.name = request.name();
    edge.scope = request.scope();
    edgeRepository.persist(edge);
    return QueryEdgeResponse.of(edge);
  }
}
