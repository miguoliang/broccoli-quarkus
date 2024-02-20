package broccoli.client;

import broccoli.common.Page;
import broccoli.common.Pageable;
import broccoli.dto.request.CreateEdgeRequest;
import broccoli.dto.response.QueryEdgeResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.util.Set;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * Edge client.
 */
@Path("/edge")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RegisterRestClient(configKey = "edge-client")
public interface EdgeClient {

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
  Page<QueryEdgeResponse> searchEdges(@QueryParam("vid") @NotEmpty Set<String> vid,
                                      @QueryParam("name") @NotEmpty Set<String> name,
                                      @QueryParam("scope") @NotEmpty Set<String> scope,
                                      @BeanParam Pageable pageable);

  /**
   * Create an edge.
   *
   * @param request create edge request
   */
  @POST
  void createEdge(CreateEdgeRequest request);
}
