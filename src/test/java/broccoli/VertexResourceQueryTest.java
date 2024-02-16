package broccoli;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import broccoli.persistence.entity.Vertex;
import broccoli.persistence.repository.VertexRepository;
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
  VertexRepository vertexRepository;

  @Test
  void getById_ShouldReturnFound(TestInfo testInfo) throws NoSuchAlgorithmException {

    final var name = testInfo.getDisplayName();
    final var type = "type";
    final var vertex = new Vertex();
    vertex.setName(name);
    vertex.setType(type);

    QuarkusTransaction.requiringNew().run(() ->
        vertexRepository.persist(vertex));

    given()
        .when().get("/vertex/" + vertex.getId())
        .then()
        .statusCode(Response.Status.OK.getStatusCode())
        .body("id", is(vertex.getId()))
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
        final var vertex = new Vertex();
        vertex.setName(testInfo.getDisplayName() + i);
        vertex.setType("type");
        vertexRepository.persist(vertex);
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

}