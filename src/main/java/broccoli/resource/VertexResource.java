package broccoli.resource;

import broccoli.common.StringUtils;
import broccoli.dto.request.CreateVertexRequest;
import broccoli.dto.request.SetVertexPropertyRequest;
import broccoli.dto.response.CreateVertexResponse;
import broccoli.dto.response.GetVertexResponse;
import broccoli.dto.response.Page;
import broccoli.dto.response.QueryVertexResponse;
import broccoli.persistence.repository.VertexPropertyRepository;
import broccoli.persistence.repository.VertexRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.security.NoSuchAlgorithmException;

@Path("/vertex")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VertexResource {

  private final VertexRepository vertexRepository;

  private final VertexPropertyRepository vertexPropertyRepository;

  @Inject
  public VertexResource(VertexRepository vertexRepository,
                        VertexPropertyRepository vertexPropertyRepository) {
    this.vertexRepository = vertexRepository;
    this.vertexPropertyRepository = vertexPropertyRepository;
  }

  @POST
  @Transactional
  public Response createVertex(@Valid CreateVertexRequest request) throws NoSuchAlgorithmException {
    final var vertex = request.toEntity();
    if (vertexRepository.findById(vertex.getId()) != null) {
      return Response.status(Response.Status.CONFLICT).build();
    }
    vertexRepository.persist(vertex);
    return Response.created(URI.create("/vertex/" + vertex.getId()))
        .entity(CreateVertexResponse.of(vertex)).build();
  }

  @GET
  @Path("/{id}")
  public GetVertexResponse getVertex(@PathParam("id") @NotBlank String id) {
    final var vertex = vertexRepository.findById(id);
    if (vertex == null) {
      throw new NotFoundException();
    }
    return GetVertexResponse.of(vertex);
  }

  @GET
  public Page<QueryVertexResponse> searchVertices(@QueryParam("q") String q,
                                                  @QueryParam("page") Integer pageNumber,
                                                  @QueryParam("size") Integer pageSize) {
    final var index = pageNumber == null ? 0 : pageNumber;
    final var size = pageSize == null ? 20 : pageSize;

    if (StringUtils.isBlank(q)) {
      final var content = vertexRepository
          .findAll()
          .page(index, size)
          .stream()
          .map(QueryVertexResponse::of)
          .toList();
      final var count = vertexRepository.count();
      return new Page<>(content, index, size, count);
    }

    final var query = "name like ?1";
    final var pattern = StringUtils.join("%", q, "%");
    final var content = vertexRepository
        .find(query, pattern)
        .page(index, size)
        .stream()
        .map(QueryVertexResponse::of)
        .toList();
    final var count = vertexRepository.count(query, pattern);
    return new Page<>(content, index, size, count);
  }

  @DELETE
  @Path("/{id}")
  @Transactional
  public Response deleteVertex(@PathParam("id") @NotBlank String id) {
    final var vertex = vertexRepository.findById(id);
    if (vertex == null) {
      throw new NotFoundException();
    }
    vertexRepository.delete(vertex);
    return Response.noContent().build();
  }

  @PUT
  @Path("/{id}/property")
  @Transactional
  public Response setProperty(@PathParam("id") @NotBlank String id,
                              SetVertexPropertyRequest request) {
    final var vertex =
        vertexRepository.findByIdOptional(id).orElseThrow(NotFoundException::new);
    vertexPropertyRepository.persist(request.toEntity(vertex));
    return Response.noContent().build();
  }
}
