package broccoli;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import broccoli.common.ResourceService;
import broccoli.dto.request.SetVertexPropertyRequest;
import broccoli.persistence.entity.Vertex;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

@QuarkusTest
class VertexResourcePropertyCreationTest {

  @Inject
  ResourceService resourceService;

  @Test
  void shouldSavePropertyValue_IfPropertyDoesNotExist(TestInfo testInfo)
      throws NoSuchAlgorithmException {

    final var name = testInfo.getDisplayName();
    final var type = "test";
    final var id = Vertex.getId(name, type);
    resourceService.createVertex(name, type);

    final var scope = "default";
    final var key = "key";
    final var value = "value";

    given()
        .when()
        .body(new SetVertexPropertyRequest(scope, key, "value"))
        .contentType(MediaType.APPLICATION_JSON)
        .post("/vertex/" + id + "/property")
        .then()
        .statusCode(Response.Status.NO_CONTENT.getStatusCode());

    final var vertex = resourceService.getVertex(name, type);
    assertThat(vertex.getProperty(scope, key), equalTo(value));
  }
}
