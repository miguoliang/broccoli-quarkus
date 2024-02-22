package broccoli;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import broccoli.common.ResourceService;
import broccoli.dto.request.SetVertexPropertyRequest;
import broccoli.persistence.entity.Vertex;
import broccoli.resource.VertexResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

@QuarkusTest
@TestHTTPEndpoint(VertexResource.class)
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
        .post("/" + id + "/property")
        .then()
        .statusCode(Response.Status.NO_CONTENT.getStatusCode());

    final var property = resourceService.getVertexProperty(id, scope, key);
    assertThat(property, notNullValue());
    assertThat(property.vertexId(), is(id));
    assertThat(property.key(), is(key));
    assertThat(property.scope(), is(scope));
    assertThat(property.value(), is(value));
  }
}
