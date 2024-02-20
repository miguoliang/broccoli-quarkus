package broccoli.common;

import broccoli.persistence.entity.Vertex;
import java.security.NoSuchAlgorithmException;

/**
 * The {@link ResourceService}.
 */
public interface ResourceService {

  /**
   * Create a vertex.
   *
   * @param name Vertex name
   * @param type Vertex type
   */
  Vertex createVertex(String name, String type) throws NoSuchAlgorithmException;

  /**
   * If a vertex exists.
   *
   * @param name Vertex name
   * @param type Vertex type
   */
  boolean vertexExists(String name, String type) throws NoSuchAlgorithmException;

  /**
   * If an edge exists.
   *
   * @param inVertexId  in-coming vertex id
   * @param outVertexId out-coming vertex id
   * @param name        edge name
   * @param scope       edge scope
   */
  boolean edgeExists(String inVertexId, String outVertexId, String name, String scope);

  /**
   * Create an edge.
   *
   * @param inVertexId  in-coming vertex id
   * @param outVertexId out-coming vertex id
   * @param name        edge name
   * @param scope       edge scope
   */
  void createEdge(String inVertexId, String outVertexId, String name, String scope);

  /**
   * Get a vertex.
   *
   * @param name Vertex name
   * @param type Vertex type
   * @return Vertex
   */
  Vertex getVertex(String name, String type) throws NoSuchAlgorithmException;
}
