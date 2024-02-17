package broccoli;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import broccoli.common.ResourceService;
import broccoli.persistence.entity.Vertex;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

@QuarkusTest
class VertexResourceQueryTest {

  @Inject
  ResourceService resourceService;

  @Test
  void getById_ShouldReturnFound(TestInfo testInfo) throws NoSuchAlgorithmException {

    final var name = testInfo.getDisplayName();
    final var type = "type";
    final var id = Vertex.getId(name, type);
    resourceService.createVertex(name, type);

    given()
        .when().get("/vertex/" + id)
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
        resourceService.createVertex(name, type);
      } catch (Exception e) {
        assert false;
      }
    }));

    given()
        .when().get("/vertex")
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
    resourceService.createVertex(name, type);

    given()
        .when().get("/vertex?q=" + name)
        .then()
        .statusCode(Response.Status.OK.getStatusCode())
        .body("content.size()", is(1))
        .body("totalElements", is(1))
        .body("totalPages", is(1))
        .body("pageNumber", is(0))
        .body("pageSize", is(20));
  }

  @Test
  void query_ShouldReturnFoundWithPaginationOnly(TestInfo testInfo) {

    QuarkusTransaction.requiringNew().run(() -> IntStream.range(0, 50).forEach(i -> {
      try {
        final var name = testInfo.getDisplayName() + i;
        final var type = "type";
        resourceService.createVertex(name, type);
      } catch (Exception e) {
        assert false;
      }
    }));

    given()
        .when().get("/vertex?page=1&size=10")
        .then()
        .statusCode(Response.Status.OK.getStatusCode())
        .body("content.size()", is(10))
        .body("totalElements", greaterThanOrEqualTo(50))
        .body("totalPages", greaterThanOrEqualTo(5))
        .body("pageNumber", is(1))
        .body("pageSize", is(10));
  }

}