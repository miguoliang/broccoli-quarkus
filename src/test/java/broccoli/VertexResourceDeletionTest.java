package broccoli;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import broccoli.common.JvmResourceService;
import broccoli.common.ResourceService;
import broccoli.common.ResourceTest;
import broccoli.resource.VertexResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

@QuarkusTest
@TestHTTPEndpoint(VertexResource.class)
class VertexResourceDeletionTest extends ResourceTest {

  @Inject
  JvmResourceService resourceService;

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

    final var vertex = getResourceService().createVertex(testInfo.getDisplayName(), "test");

    given()
        .when()
        .delete("/" + vertex.getId())
        .then()
        .statusCode(Response.Status.NO_CONTENT.getStatusCode());

    assertThat(getResourceService().vertexExists(vertex.getName(), vertex.getType()), is(false));
  }

  @Override
  protected ResourceService getResourceService() {
    return resourceService;
  }
}
