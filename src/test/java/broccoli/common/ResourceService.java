package broccoli.common;

import broccoli.client.EdgeClient;
import broccoli.client.VertexClient;
import broccoli.dto.request.CreateEdgeRequest;
import broccoli.dto.request.CreateVertexRequest;
import broccoli.dto.response.GetVertexResponse;
import broccoli.dto.response.QueryVertexPropertyResponse;
import broccoli.persistence.entity.Vertex;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * A service that interacts with the native resource.
 */
@Singleton
public final class ResourceService {

  @RestClient
  VertexClient vertexClient;

  @RestClient
  EdgeClient edgeClient;

  /**
   * Creates a vertex.
   *
   * @param name vertex name
   * @param type vertex type
   * @return created vertex
   */
  public Vertex createVertex(String name, String type) {
    try (final var response = vertexClient.createVertex(new CreateVertexRequest(name, type))) {
      return response.readEntity(Vertex.class);
    }
  }

  /**
   * Checks if a vertex exists.
   *
   * @param name vertex name
   * @param type vertex type
   * @return true if the vertex exists, false otherwise
   * @throws NoSuchAlgorithmException if the vertex id cannot be generated
   */
  public boolean vertexExists(String name, String type) throws NoSuchAlgorithmException {
    final var id = Vertex.getId(name, type);
    try (final var response = vertexClient.getVertex(id)) {
      return response.getStatus() == Response.Status.OK.getStatusCode();
    } catch (final WebApplicationException e) {
      if (e.getResponse().getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
        return false;
      }
      throw e;
    }
  }

  /**
   * Checks if an edge exists.
   *
   * @param inVertexId  incoming vertex id
   * @param outVertexId outgoing vertex id
   * @param name        edge name
   * @param scope       edge scope
   * @return true if the edge exists, false otherwise
   */
  public boolean edgeExists(String inVertexId, String outVertexId, String name, String scope) {
    return edgeClient.searchEdges(Set.of(inVertexId, outVertexId),
        Set.of(name), Set.of(scope), new Pageable()).getTotalElements() > 0;
  }

  /**
   * Creates an edge.
   *
   * @param inVertexId  incoming vertex id
   * @param outVertexId outgoing vertex id
   * @param name        edge name
   * @param scope       edge scope
   */
  public void createEdge(String inVertexId, String outVertexId, String name, String scope) {
    edgeClient.createEdge(new CreateEdgeRequest(inVertexId, outVertexId, name, scope));
  }

  /**
   * Gets a vertex.
   *
   * @param name vertex name
   * @param type vertex type
   * @return vertex
   * @throws NoSuchAlgorithmException if the vertex id cannot be generated
   */
  public GetVertexResponse getVertex(String name, String type) throws NoSuchAlgorithmException {
    final var id = Vertex.getId(name, type);
    try (final var response = vertexClient.getVertex(id)) {
      return response.readEntity(GetVertexResponse.class);
    }
  }

  /**
   * Gets a vertex property.
   *
   * @param vertexId vertex id
   * @param scope    scope
   * @param key      key
   * @return vertex property
   */
  public QueryVertexPropertyResponse getVertexProperty(String vertexId, String scope, String key) {

    var page = vertexClient.getProperties(vertexId, scope, key, new Pageable());
    while (page.getPageNumber() < page.getTotalPages()) {
      for (final var property : page.getContent()) {
        if (property.key().equals(key) && property.scope().equals(scope)) {
          return property;
        }
      }
      if (page.getPageNumber() >= page.getTotalPages() - 1) {
        break;
      }
      page = vertexClient.getProperties(vertexId, scope, key,
          new Pageable(page.getPageNumber() + 1, page.getPageSize()));
    }
    return null;
  }
}
