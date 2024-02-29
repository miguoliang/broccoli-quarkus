package broccoli.resource;

import broccoli.common.Page;
import broccoli.common.Pageable;
import broccoli.common.StringUtils;
import broccoli.dto.request.CreateVertexRequest;
import broccoli.dto.request.SetVertexPropertyRequest;
import broccoli.dto.response.CreateVertexResponse;
import broccoli.dto.response.GetVertexResponse;
import broccoli.dto.response.QueryVertexPropertyResponse;
import broccoli.dto.response.QueryVertexResponse;
import broccoli.persistence.entity.Vertex;
import broccoli.persistence.entity.VertexProperty;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.security.NoSuchAlgorithmException;

/**
 * Vertex resource endpoints.
 */
@Path("/vertex")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SuppressWarnings("java:S3252")
public class VertexResource {

  /**
   * Create a vertex.
   *
   * @param request create vertex request
   * @return response
   * @throws NoSuchAlgorithmException no such algorithm exception
   */
  @POST
  @Transactional
  public Response createVertex(@Valid CreateVertexRequest request) throws NoSuchAlgorithmException {
    final var vertex = request.toEntity();
    if (Vertex.findById(vertex.getId()) != null) {
      return Response.status(Response.Status.CONFLICT).build();
    }
    Vertex.persist(vertex);
    return Response.created(URI.create("/vertex/" + vertex.getId()))
        .entity(CreateVertexResponse.of(vertex)).build();
  }

  /**
   * Get a vertex by id.
   *
   * @param id vertex id
   * @return response
   */
  @GET
  @Path("/{id}")
  public GetVertexResponse getVertex(@PathParam("id") @NotBlank String id) {
    final var vertex = Vertex.<Vertex>findById(id);
    if (vertex == null) {
      throw new NotFoundException();
    }
    return GetVertexResponse.of(vertex);
  }

  /**
   * Search vertices.
   *
   * @param q        query
   * @param pageable pageable
   * @return response
   */
  @GET
  public Page<QueryVertexResponse> searchVertices(@QueryParam("q") String q,
                                                  @BeanParam Pageable pageable) {
    final var index = pageable.getPage();
    final var size = pageable.getSize();

    if (StringUtils.isBlank(q)) {
      final var content = Vertex
          .<Vertex>findAll()
          .page(index, size)
          .stream()
          .map(QueryVertexResponse::of)
          .toList();
      final var count = Vertex.count();
      return new Page<>(content, index, size, count);
    }

    final var query = "name like ?1";
    final var pattern = StringUtils.join("%", q, "%");
    final var content = Vertex
        .<Vertex>find(query, pattern)
        .page(index, size)
        .stream()
        .map(QueryVertexResponse::of)
        .toList();
    final var count = Vertex.count(query, pattern);
    return new Page<>(content, index, size, count);
  }

  /**
   * Delete a vertex by id.
   *
   * @param id vertex id
   * @return response
   */
  @DELETE
  @Path("/{id}")
  @Transactional
  public Response deleteVertex(@PathParam("id") @NotBlank String id) {
    Vertex.<Vertex>findByIdOptional(id).ifPresent(Vertex::delete);
    return Response.noContent().build();
  }

  /**
   * Set a property to a vertex.
   *
   * @param id      vertex id
   * @param request set vertex property request
   * @return response
   */
  @POST
  @Path("/{id}/property")
  @Transactional
  public Response setProperty(@PathParam("id") @NotBlank String id,
                              @Valid SetVertexPropertyRequest request) {
    final var vertex = Vertex.<Vertex>findById(id);
    if (vertex == null) {
      throw new NotFoundException("Vertex not found.");
    }
    Vertex.persist(request.toEntity(vertex));
    return Response.noContent().build();
  }

  /**
   * Get properties of a vertex.
   *
   * @param id       vertex id
   * @param scope    scope
   * @param pageable pageable
   * @return response
   */
  @GET
  @Path("/{id}/property")
  public Page<QueryVertexPropertyResponse> getProperties(@PathParam("id") @NotBlank String id,
                                                         @QueryParam("scope")
                                                         @DefaultValue("default") String scope,
                                                         @BeanParam Pageable pageable) {
    if (Vertex.findById(id) == null) {
      throw new NotFoundException("Vertex not found.");
    }
    final var properties = VertexProperty
        .<VertexProperty>find("vertex.id = ?1 and scope = ?2", id, scope)
        .page(pageable.getPage(), pageable.getSize())
        .stream()
        .map(QueryVertexPropertyResponse::of)
        .toList();
    final var total = VertexProperty.count("vertex.id = ?1 and scope = ?2", id, scope);
    return new Page<>(properties, pageable.getPage(), pageable.getSize(), total);
  }
}
