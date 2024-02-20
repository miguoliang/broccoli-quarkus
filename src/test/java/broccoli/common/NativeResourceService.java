package broccoli.common;

import broccoli.client.EdgeClient;
import broccoli.client.VertexClient;
import broccoli.dto.request.CreateEdgeRequest;
import broccoli.dto.request.CreateVertexRequest;
import broccoli.persistence.entity.Vertex;
import jakarta.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * A service that interacts with the native resource.
 */
public final class NativeResourceService implements ResourceService {

  @RestClient
  VertexClient vertexClient;

  @RestClient
  EdgeClient edgeClient;

  @Override
  public Vertex createVertex(String name, String type) {
    try (final var response = vertexClient.createVertex(new CreateVertexRequest(name, type))) {
      return response.readEntity(Vertex.class);
    }
  }

  @Override
  public boolean vertexExists(String name, String type) throws NoSuchAlgorithmException {
    final var id = Vertex.getId(name, type);
    try (final var response = vertexClient.getVertex(id)) {
      return response.getStatus() == Response.Status.NOT_FOUND.getStatusCode();
    }
  }

  @Override
  public boolean edgeExists(String inVertexId, String outVertexId, String name, String scope) {
    return edgeClient.searchEdges(Set.of(inVertexId, outVertexId),
        Set.of(name), Set.of(scope), new Pageable()).getTotalElements() > 0;
  }

  @Override
  public void createEdge(String inVertexId, String outVertexId, String name, String scope) {
    edgeClient.createEdge(new CreateEdgeRequest(inVertexId, outVertexId, name, scope));
  }

  @Override
  public Vertex getVertex(String name, String type) throws NoSuchAlgorithmException {
    final var id = Vertex.getId(name, type);
    try (final var response = vertexClient.getVertex(id)) {
      return response.readEntity(Vertex.class);
    }
  }
}
