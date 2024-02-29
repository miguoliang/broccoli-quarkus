package broccoli;

import static io.restassured.RestAssured.given;

import broccoli.dto.request.SetVertexPropertyRequest;
import broccoli.persistence.entity.Vertex;
import broccoli.resource.VertexResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

@QuarkusTest
@TestHTTPEndpoint(VertexResource.class)
class VertexResourcePropertyQueryTest {

  @Test
  void shouldFailToSavePropertyValue_IfVertexDoesNotExist(TestInfo testInfo)
      throws NoSuchAlgorithmException {

    final var name = testInfo.getDisplayName();
    final var type = "test";
    final var id = Vertex.getId(name, type);

    final var scope = "default";
    final var key = "key";
    final var value = "value";

    given()
        .when()
        .body(new SetVertexPropertyRequest(scope, key, value))
        .contentType(MediaType.APPLICATION_JSON)
        .get("/" + id + "/property")
        .then()
        .statusCode(Response.Status.NOT_FOUND.getStatusCode());
  }
}
