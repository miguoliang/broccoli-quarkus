package broccoli.client;

import broccoli.common.Page;
import broccoli.common.Pageable;
import broccoli.dto.request.CreateVertexRequest;
import broccoli.dto.request.SetVertexPropertyRequest;
import broccoli.dto.response.QueryVertexResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * Vertex client.
 */
@Path("/vertex")
@RegisterRestClient
public interface VertexClient {

  /**
   * Create a vertex.
   *
   * @param request create vertex request
   */
  @POST
  Response createVertex(@Valid CreateVertexRequest request);

  /**
   * Get a vertex by id.
   *
   * @param id vertex id
   * @return response
   */
  @GET
  @Path("/{id}")
  Response getVertex(@PathParam("id") @NotBlank String id);

  /**
   * Search vertices.
   *
   * @param q        query
   * @param pageable pageable
   * @return response
   */
  @GET
  Page<QueryVertexResponse> searchVertices(@QueryParam("q") String q,
                                           @BeanParam Pageable pageable);

  /**
   * Delete a vertex by id.
   *
   * @param id vertex id
   */
  @DELETE
  @Path("/{id}")
  void deleteVertex(@PathParam("id") @NotBlank String id);

  /**
   * Set a property to a vertex.
   *
   * @param id      vertex id
   * @param request set vertex property request
   */
  @POST
  @Path("/{id}/property")
  void setProperty(@PathParam("id") @NotBlank String id,
                   @Valid SetVertexPropertyRequest request);
}
