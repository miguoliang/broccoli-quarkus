package broccoli.resource;

import broccoli.common.Page;
import broccoli.dto.request.CreateEdgeRequest;
import broccoli.dto.response.QueryEdgeResponse;
import broccoli.persistence.entity.Edge;
import broccoli.persistence.repository.EdgeRepository;
import broccoli.persistence.repository.VertexRepository;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.annotation.Nullable;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Set;

/**
 * Edge resource endpoints.
 */
@Path("/edge")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EdgeResource {

  private final EdgeRepository edgeRepository;

  private final VertexRepository vertexRepository;

  /**
   * Bean constructor.
   *
   * @param edgeRepository   The edge repository.
   * @param vertexRepository The vertex repository.
   */
  @Inject
  public EdgeResource(EdgeRepository edgeRepository, VertexRepository vertexRepository) {
    this.edgeRepository = edgeRepository;
    this.vertexRepository = vertexRepository;
  }

  /**
   * Search edges.
   *
   * @param vid        vertex id collection
   * @param name       edge name
   * @param scope      edge scope
   * @param pageNumber page number
   * @param pageSize   page size
   * @return page of edges
   */
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

  /**
   * Create an edge.
   *
   * @param request create edge request
   * @return response
   */
  @POST
  @Transactional
  public Response createEdge(CreateEdgeRequest request) {

    final var inVertex = vertexRepository.findByIdOptional(request.inVertexId())
        .orElseThrow(() -> new NotFoundException("Incoming vertex not found"));
    final var outVertex = vertexRepository.findByIdOptional(request.outVertexId())
        .orElseThrow(() -> new NotFoundException("Outgoing vertex not found"));

    if (edgeRepository
        .find("inVertex.id = ?1 and outVertex.id = ?2 and name = ?3 and scope = ?4",
            inVertex.getId(), outVertex.getId(), request.name(), request.scope())
        .firstResult() != null) {
      return Response.status(Response.Status.CONFLICT).build();
    }

    final var edge = new Edge();
    edge.setInVertex(inVertex);
    edge.setOutVertex(outVertex);
    edge.setName(request.name());
    edge.setScope(request.scope());
    edgeRepository.persist(edge);
    return Response.noContent().build();
  }
}
