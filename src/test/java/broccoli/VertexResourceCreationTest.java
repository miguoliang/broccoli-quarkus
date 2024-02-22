package broccoli;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import broccoli.common.ResourceService;
import broccoli.dto.request.CreateVertexRequest;
import broccoli.persistence.MultiTenantSchemaService;
import broccoli.persistence.entity.Vertex;
import broccoli.resource.VertexResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

@QuarkusTest
@TestHTTPEndpoint(VertexResource.class)
@QuarkusTestResource(H2DatabaseTestResource.class)
class VertexResourceCreationTest {

  @Inject
  MultiTenantSchemaService multiTenantSchemaService;

  @Inject
  ResourceService resourceService;

  @BeforeEach
  void setUp() {
    multiTenantSchemaService.createSchema("default");
  }

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
        .post()
        .then()
        .statusCode(Response.Status.CREATED.getStatusCode())
        .body("id", is(id))
        .body("name", is(name))
        .body("type", is(type));

    final var vertex = resourceService.getVertex(name, type);
    assertThat(vertex, notNullValue());
    assertThat(vertex.id(), is(id));
    assertThat(vertex.name(), is(name));
    assertThat(vertex.type(), is(type));
  }

  @Test
  void shouldReturnConflict_WhenVertexAlreadyExists(TestInfo testInfo) {
    final var name = testInfo.getDisplayName();
    final var type = "test";
    resourceService.createVertex(name, type);

    given()
        .when()
        .body(new CreateVertexRequest(name, type))
        .contentType(MediaType.APPLICATION_JSON)
        .post()
        .then()
        .statusCode(Response.Status.CONFLICT.getStatusCode());
  }

  @Test
  void shouldFail_WhenBadParameters() {
    given()
        .when()
        .body(new CreateVertexRequest(null, null))
        .contentType(MediaType.APPLICATION_JSON)
        .post()
        .then()
        .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
  }
}