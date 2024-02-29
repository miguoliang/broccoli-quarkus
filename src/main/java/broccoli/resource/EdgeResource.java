package broccoli.resource;

import broccoli.common.Page;
import broccoli.common.Pageable;
import broccoli.dto.request.CreateEdgeRequest;
import broccoli.dto.response.QueryEdgeResponse;
import broccoli.persistence.entity.Edge;
import broccoli.persistence.entity.Vertex;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.BeanParam;
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
@SuppressWarnings("java:S3252")
public class EdgeResource {

  /**
   * Search edges.
   *
   * @param vid      vertex id collection
   * @param name     edge name
   * @param scope    edge scope
   * @param pageable page
   * @return page of edges
   */
  @GET
  @Transactional
  public Page<QueryEdgeResponse> searchEdges(@QueryParam("vid") @NotEmpty Set<String> vid,
                                             @QueryParam("name") @NotEmpty Set<String> name,
                                             @QueryParam("scope") @NotEmpty Set<String> scope,
                                             @BeanParam Pageable pageable) {
    final var index = pageable.getPage();
    final var size = pageable.getSize();
    final var query = "name in ?2 and scope in ?3 and inVertex.id in ?1 or outVertex.id in ?1";
    final var content = Edge
        .<Edge>find(query, vid, name, scope)
        .page(index, size)
        .stream()
        .map(QueryEdgeResponse::of)
        .toList();
    final var count = Edge.count(query, vid, name, scope);
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

    final var inVertex = Vertex.<Vertex>findByIdOptional(request.inVertexId())
        .orElseThrow(() -> new NotFoundException("Incoming vertex not found"));
    final var outVertex = Vertex.<Vertex>findByIdOptional(request.outVertexId())
        .orElseThrow(() -> new NotFoundException("Outgoing vertex not found"));

    if (Edge
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
    Edge.persist(edge);
    return Response.noContent().build();
  }
}
