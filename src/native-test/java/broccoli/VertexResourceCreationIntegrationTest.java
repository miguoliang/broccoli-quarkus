package broccoli;

import broccoli.common.NativeResourceService;
import broccoli.common.ResourceService;
import io.quarkus.test.junit.QuarkusIntegrationTest;

@QuarkusIntegrationTest
class VertexResourceCreationIntegrationTest extends VertexResourceCreationTest {
  // Execute the same tests but in packaged mode.

  NativeResourceService resourceService = new NativeResourceService();

  @Override
  protected ResourceService getResourceService() {
    return resourceService;
  }
}
