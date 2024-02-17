package broccoli.common;

import broccoli.persistence.entity.Edge;
import broccoli.persistence.entity.EdgeId;
import broccoli.persistence.entity.Vertex;
import broccoli.persistence.repository.EdgeRepository;
import broccoli.persistence.repository.VertexRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import java.security.NoSuchAlgorithmException;

/**
 * The {@link ResourceService}.
 */
@Singleton
public final class ResourceService {

  private final EdgeRepository edgeRepository;

  private final VertexRepository vertexRepository;

  @Inject
  public ResourceService(EdgeRepository edgeRepository, VertexRepository vertexRepository) {
    this.edgeRepository = edgeRepository;
    this.vertexRepository = vertexRepository;
  }

  /**
   * Create a vertex.
   *
   * @param name Vertex name
   * @param type Vertex type
   */
  @Transactional
  public Vertex createVertex(String name, String type) throws NoSuchAlgorithmException {
    final var vertex = new Vertex();
    vertex.setName(name);
    vertex.setType(type);
    vertexRepository.persistAndFlush(vertex);
    return vertex;
  }

  public boolean vertexExists(String name, String type) throws NoSuchAlgorithmException {
    return vertexRepository.findByIdOptional(Vertex.getId(name, type)).isPresent();
  }

  /**
   * If an edge exists.
   *
   * @param inVertexId  in-coming vertex id
   * @param outVertexId out-coming vertex id
   * @param name        edge name
   * @param scope       edge scope
   */
  public boolean edgeExists(String inVertexId, String outVertexId, String name, String scope) {
    final var inVertex = vertexRepository.findByIdOptional(inVertexId).orElseThrow();
    final var outVertex = vertexRepository.findByIdOptional(outVertexId).orElseThrow();
    final var edgeId = new EdgeId();
    edgeId.setInVertex(inVertex);
    edgeId.setOutVertex(outVertex);
    edgeId.setName(name);
    edgeId.setScope(scope);
    return edgeRepository.findByIdOptional(edgeId).isPresent();
  }

  /**
   * Create an edge.
   *
   * @param inVertexId  in-coming vertex id
   * @param outVertexId out-coming vertex id
   * @param name        edge name
   * @param scope       edge scope
   */
  @Transactional
  public void createEdge(String inVertexId, String outVertexId, String name, String scope) {
    final var inVertex = vertexRepository.findByIdOptional(inVertexId).orElseThrow();
    final var outVertex = vertexRepository.findByIdOptional(outVertexId).orElseThrow();
    final var edge = new Edge();
    edge.setInVertex(inVertex);
    edge.setOutVertex(outVertex);
    edge.setName(name);
    edge.setScope(scope);
    edgeRepository.persistAndFlush(edge);
  }

  /**
   * Get a vertex.
   *
   * @param name Vertex name
   * @param type Vertex type
   * @return Vertex
   */
  public Vertex getVertex(String name, String type) throws NoSuchAlgorithmException {
    return vertexRepository.findByIdOptional(Vertex.getId(name, type)).orElseThrow();
  }
}
