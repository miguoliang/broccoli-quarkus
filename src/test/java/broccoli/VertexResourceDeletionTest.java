package broccoli;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import broccoli.common.ResourceService;
import broccoli.persistence.MultiTenantSchemaService;
import broccoli.resource.VertexResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

@QuarkusTest
@TestHTTPEndpoint(VertexResource.class)
class VertexResourceDeletionTest {

  @Inject
  ResourceService resourceService;

  @Inject
  MultiTenantSchemaService multiTenantSchemaService;

  @BeforeEach
  void setUp() {
    multiTenantSchemaService.createSchema("default");
  }

  @Test
  void shouldReturnNoContent_WhenVertexDoesNotExist(TestInfo testInfo) {

    given()
        .when()
        .delete("/" + testInfo.getDisplayName())
        .then()
        .statusCode(Response.Status.NO_CONTENT.getStatusCode());
  }

  @Test
  void shouldReturnNoContent_WhenVertexAlreadyExists(TestInfo testInfo)
      throws NoSuchAlgorithmException {

    final var vertex = resourceService.createVertex(testInfo.getDisplayName(), "test");

    given()
        .when()
        .delete("/" + vertex.getId())
        .then()
        .statusCode(Response.Status.NO_CONTENT.getStatusCode());

    assertThat(resourceService.vertexExists(vertex.getName(), vertex.getType()), is(false));
  }
}
