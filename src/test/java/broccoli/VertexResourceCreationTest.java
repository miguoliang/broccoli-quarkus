package broccoli;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

import broccoli.dto.request.CreateVertexRequest;
import broccoli.persistence.entity.Vertex;
import broccoli.persistence.repository.VertexRepository;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

@QuarkusTest
class VertexResourceCreationTest {

  @Inject
  VertexRepository vertexRepository;

  @Test
  void shouldReturnCreated_WhenVertexDoesNotExist(TestInfo testInfo)
      throws NoSuchAlgorithmException {

    final var name = testInfo.getDisplayName();
    final var type = "test";
    final var id = Vertex.getId(name, type);

    given()
        .when()
        .body(new CreateVertexRequest(name, type))
        .contentType(ContentType.JSON)
        .post("/vertex")
        .then()
        .body("id", is(id))
        .statusCode(HttpStatus.SC_CREATED);

    final var vertex = vertexRepository.findById(id);
    assertThat(vertex, notNullValue());
    assertThat(vertex.id, is(id));
    assertThat(vertex.name, is(name));
    assertThat(vertex.type, is(type));
    assertThat(vertex.version, is(0));
    assertThat(vertex.generalColumns.dateCreated, lessThanOrEqualTo(LocalDateTime.now()));
    assertThat(vertex.generalColumns.dateUpdated, lessThanOrEqualTo(LocalDateTime.now()));
  }

  @Test
  void shouldReturnConflict_WhenVertexAlreadyExists(TestInfo testInfo)
      throws NoSuchAlgorithmException {
    final var name = testInfo.getDisplayName();
    final var type = "test";
    final var id = Vertex.getId(name, type);

    final var vertex = new Vertex();
    vertex.id = id;
    vertex.name = name;
    vertex.type = type;
    QuarkusTransaction.requiringNew().run(() -> vertexRepository.persist(vertex));

    given()
        .when()
        .body(new CreateVertexRequest(name, type))
        .contentType(ContentType.JSON)
        .post("/vertex")
        .then()
        .statusCode(Response.Status.CONFLICT.getStatusCode());
  }

  @Test
  void shouldFail_WhenBadParameters() {
    given()
        .when()
        .body(new CreateVertexRequest(null, null))
        .contentType(ContentType.JSON)
        .post("/vertex")
        .then()
        .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
  }
}