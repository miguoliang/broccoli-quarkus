package broccoli;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import broccoli.common.ResourceService;
import broccoli.dto.request.CreateEdgeRequest;
import broccoli.persistence.MultiTenantSchemaService;
import broccoli.resource.EdgeResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

@QuarkusTest
@TestHTTPEndpoint(EdgeResource.class)
class EdgeResourceCreationTest {

  @Inject
  ResourceService resourceService;

  @Inject
  MultiTenantSchemaService multiTenantSchemaService;

  @BeforeEach
  void setUp() {
    multiTenantSchemaService.createSchema("default");
  }

  @Test
  void shouldReturnCreated_WhenEdgeDoesNotExists(TestInfo testInfo)
      throws NoSuchAlgorithmException {

    final var inVertex =
        resourceService.createVertex(testInfo.getDisplayName() + "in", "test");
    final var outVertex =
        resourceService.createVertex(testInfo.getDisplayName() + "out", "test");
    final var name = "associated";
    final var scope = "default";
    final var request = new CreateEdgeRequest(inVertex.getId(), outVertex.getId(), name, scope);

    given()
        .when()
        .contentType(MediaType.APPLICATION_JSON)
        .body(request)
        .post()
        .then()
        .statusCode(Response.Status.NO_CONTENT.getStatusCode());
  }

  @Test
  void shouldReturnConflict_WhenEdgeAlreadyExists(TestInfo testInfo)
      throws NoSuchAlgorithmException {

    final var inVertex =
        resourceService.createVertex(testInfo.getDisplayName() + "in", "test");
    final var outVertex =
        resourceService.createVertex(testInfo.getDisplayName() + "out", "test");
    final var name = "associated";
    final var scope = "default";
    resourceService.createEdge(inVertex.getId(), outVertex.getId(), name, scope);
    final var request = new CreateEdgeRequest(inVertex.getId(), outVertex.getId(), name, scope);

    given()
        .when()
        .contentType(MediaType.APPLICATION_JSON)
        .body(request)
        .post()
        .then()
        .statusCode(Response.Status.CONFLICT.getStatusCode());
  }

  @Test
  void shouldReturnNotFound_WhenAnyVertexDoesNotExist(TestInfo testInfo)
      throws NoSuchAlgorithmException {

    final var inVertex =
        resourceService.createVertex(testInfo.getDisplayName() + "in", "test");
    final var outVertex =
        resourceService.createVertex(testInfo.getDisplayName() + "out", "test");
    final var name = "associated";
    final var scope = "default";
    final var fakeId = "fakeId";

    given()
        .when()
        .contentType(MediaType.APPLICATION_JSON)
        .body(new CreateEdgeRequest(fakeId, outVertex.getId(), name, scope))
        .post()
        .then()
        .statusCode(Response.Status.NOT_FOUND.getStatusCode())
        .body("errors.size()", is(1))
        .body("errors[0].message", is("Incoming vertex not found"));

    given()
        .when()
        .body(new CreateEdgeRequest(inVertex.getId(), fakeId, name, scope))
        .contentType(MediaType.APPLICATION_JSON)
        .post()
        .then()
        .statusCode(Response.Status.NOT_FOUND.getStatusCode())
        .body("errors.size()", is(1))
        .body("errors[0].message", is("Outgoing vertex not found"));
  }
}
