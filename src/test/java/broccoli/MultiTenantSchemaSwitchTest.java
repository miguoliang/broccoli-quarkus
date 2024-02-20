package broccoli;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import broccoli.client.VertexClient;
import broccoli.dto.request.CreateVertexRequest;
import broccoli.persistence.MultiTenantSchemaService;
import broccoli.persistence.entity.Vertex;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MultiTenantSchemaSwitchTest {

  @Inject
  MultiTenantSchemaService multiTenantSchemaService;

  @Inject
  @RestClient
  VertexClient vertexClient;

  MultiTenantSchemaService.DatabaseConnectionInfo databaseConnectionInfo1;
  MultiTenantSchemaService.DatabaseConnectionInfo databaseConnectionInfo2;

  @BeforeAll
  public void setup() {

    databaseConnectionInfo1 = multiTenantSchemaService.createSchema("tenant1");
    databaseConnectionInfo2 = multiTenantSchemaService.createSchema("tenant2");
  }

  @Test
  void testDataIsIsolated() throws SQLException, NoSuchAlgorithmException {

    final var request1 = new CreateVertexRequest("vertex1", "test");
    final var request2 = new CreateVertexRequest("vertex2", "test");
    final var vertex1 = vertexClient.createVertex("tenant1", request1);
    final var vertex2 = vertexClient.createVertex("tenant2", request2);
    final var check1 = getVertex(databaseConnectionInfo1, vertex1.id());
    final var check2 = getVertex(databaseConnectionInfo2, vertex2.id());
    final var count1 = getVertexCount(databaseConnectionInfo1);
    final var count2 = getVertexCount(databaseConnectionInfo2);

    // assert check1 and check2
    assertThat(check1.getName(), is("vertex1"));
    assertThat(check1.getType(), is("test"));
    assertThat(check2.getName(), is("vertex2"));
    assertThat(check2.getType(), is("test"));

    // assert count1 and count2
    assertThat(count1, is(1));
    assertThat(count2, is(1));
  }

  private Vertex getVertex(
      final MultiTenantSchemaService.DatabaseConnectionInfo databaseConnectionInfo,
      final String id) throws SQLException, NoSuchAlgorithmException {

    final var connection = DriverManager.getConnection(databaseConnectionInfo.url(),
        databaseConnectionInfo.username(),
        databaseConnectionInfo.password());
    final var resultSet = connection.createStatement()
        .executeQuery("SELECT * FROM vertex WHERE id = '%s'".formatted(id));
    connection.createStatement()
        .executeQuery("SELECT * FROM vertex WHERE id = '%s'".formatted(id)).next();
    final var name = resultSet.getString("name");
    final var type = resultSet.getString("type");
    final var vertex = new Vertex();
    vertex.setName(name);
    vertex.setType(type);
    return vertex;
  }

  private int getVertexCount(
      final MultiTenantSchemaService.DatabaseConnectionInfo databaseConnectionInfo) throws SQLException {

    final var connection = DriverManager.getConnection(databaseConnectionInfo.url(),
        databaseConnectionInfo.username(),
        databaseConnectionInfo.password());
    final var resultSet = connection.createStatement()
        .executeQuery("SELECT COUNT(*) FROM vertex");
    resultSet.next();
    return resultSet.getInt(1);
  }
}
