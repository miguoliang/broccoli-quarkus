package broccoli;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import broccoli.common.ResourceService;
import broccoli.resource.EdgeResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

@QuarkusTest
@TestHTTPEndpoint(EdgeResource.class)
class EdgeResourceQueryTest {

  @Inject
  ResourceService resourceService;

  @Test
  void shouldReturnBadRequest_WithoutParameters() {

    given()
        .when()
        .get()
        .then()
        .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
  }

  @Test
  void shouldReturnOk_WithValidParameters(TestInfo testInfo) {

    final var inVertex =
        resourceService.createVertex(testInfo.getDisplayName() + "in", "type");
    final var outVertex =
        resourceService.createVertex(testInfo.getDisplayName() + "out", "type");
    final var name = "associated";
    final var scope = "default";
    resourceService.createEdge(inVertex.getId(), outVertex.getId(), name, scope);

    final var params = String.format("?vid=%s&vid=%s&name=%s&scope=%s",
        inVertex.getId(), outVertex.getId(), name, scope);

    given()
        .when()
        .get("?" + params)
        .then()
        .body("content.size()", is(1))
        .body("content[0].inVertexId", is(inVertex.getId()))
        .body("content[0].inVertexName", is(inVertex.getName()))
        .body("content[0].inVertexType", is(inVertex.getType()))
        .body("content[0].outVertexId", is(outVertex.getId()))
        .body("content[0].outVertexName", is(outVertex.getName()))
        .body("content[0].outVertexType", is(outVertex.getType()))
        .body("content[0].name", is(name))
        .body("content[0].scope", is(scope))
        .body("totalElements", is(1))
        .body("totalPages", is(1))
        .body("pageSize", is(20))
        .body("pageNumber", is(0))
        .statusCode(Response.Status.OK.getStatusCode());
  }
}
