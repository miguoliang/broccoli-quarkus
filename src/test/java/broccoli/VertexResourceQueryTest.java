package broccoli;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import broccoli.common.JvmResourceService;
import broccoli.common.ResourceService;
import broccoli.common.ResourceTest;
import broccoli.persistence.entity.Vertex;
import broccoli.resource.VertexResource;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

@QuarkusTest
@TestHTTPEndpoint(VertexResource.class)
class VertexResourceQueryTest extends ResourceTest {

  @Inject
  JvmResourceService resourceService;

  @Test
  void getById_ShouldReturnFound(TestInfo testInfo) throws NoSuchAlgorithmException {

    final var name = testInfo.getDisplayName();
    final var type = "type";
    final var id = Vertex.getId(name, type);
    getResourceService().createVertex(name, type);

    given()
        .when().get("/" + id)
        .then()
        .statusCode(Response.Status.OK.getStatusCode())
        .body("id", is(id))
        .body("name", is(name))
        .body("type", is(type));
  }

  @Test
  void getById_ShouldReturnNotFound() {

    given()
        .when().get("/vertex/1")
        .then()
        .statusCode(Response.Status.NOT_FOUND.getStatusCode());
  }

  @Test
  void query_ShouldReturnFoundWithoutParameters(TestInfo testInfo) {

    QuarkusTransaction.requiringNew().run(() -> IntStream.range(0, 50).forEach(i -> {
      try {
        final var name = testInfo.getDisplayName() + i;
        final var type = "type";
        getResourceService().createVertex(name, type);
      } catch (Exception e) {
        assert false;
      }
    }));

    given()
        .when()
        .get()
        .then()
        .statusCode(Response.Status.OK.getStatusCode())
        .body("content.size()", is(20))
        .body("totalElements", greaterThanOrEqualTo(50))
        .body("totalPages", greaterThanOrEqualTo(3))
        .body("pageNumber", greaterThanOrEqualTo(0))
        .body("pageSize", greaterThanOrEqualTo(20));
  }

  @Test
  void query_ShouldReturnFoundWithQ(TestInfo testInfo) throws NoSuchAlgorithmException {

    final var name = testInfo.getDisplayName();
    final var type = "type";
    getResourceService().createVertex(name, type);

    given()
        .when().get("?q=" + name)
        .then()
        .statusCode(Response.Status.OK.getStatusCode())
        .body("content.size()", is(1))
        .body("totalElements", is(1))
        .body("totalPages", is(1))
        .body("pageNumber", is(0))
        .body("pageSize", is(20));
  }

  @Override
  protected ResourceService getResourceService() {
    return resourceService;
  }
}