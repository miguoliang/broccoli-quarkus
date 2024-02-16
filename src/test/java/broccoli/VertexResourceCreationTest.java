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
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
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
        .contentType(MediaType.APPLICATION_JSON)
        .post("/vertex")
        .then()
        .body("id", is(id))
        .statusCode(Response.Status.CREATED.getStatusCode());

    final var vertex = vertexRepository.findById(id);
    assertThat(vertex, notNullValue());
    assertThat(vertex.getId(), is(id));
    assertThat(vertex.getName(), is(name));
    assertThat(vertex.getType(), is(type));
    assertThat(vertex.getVersion(), is(0));
    assertThat(vertex.getGeneralColumns().getDateCreated(), lessThanOrEqualTo(LocalDateTime.now()));
    assertThat(vertex.getGeneralColumns().getDateUpdated(), lessThanOrEqualTo(LocalDateTime.now()));
  }

  @Test
  void shouldReturnConflict_WhenVertexAlreadyExists(TestInfo testInfo)
      throws NoSuchAlgorithmException {
    final var name = testInfo.getDisplayName();
    final var type = "test";
    final var id = Vertex.getId(name, type);

    final var vertex = new Vertex();
    vertex.setId(id);
    vertex.setName(name);
    vertex.setType(type);
    QuarkusTransaction.requiringNew().run(() -> vertexRepository.persist(vertex));

    given()
        .when()
        .body(new CreateVertexRequest(name, type))
        .contentType(MediaType.APPLICATION_JSON)
        .post("/vertex")
        .then()
        .statusCode(Response.Status.CONFLICT.getStatusCode());
  }

  @Test
  void shouldFail_WhenBadParameters() {
    given()
        .when()
        .body(new CreateVertexRequest(null, null))
        .contentType(MediaType.APPLICATION_JSON)
        .post("/vertex")
        .then()
        .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
  }
}