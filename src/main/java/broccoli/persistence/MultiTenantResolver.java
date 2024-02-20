package broccoli.persistence;

import io.quarkus.hibernate.orm.runtime.tenant.TenantResolver;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom tenant resolver.
 */
@ApplicationScoped
@Slf4j
public class MultiTenantResolver implements TenantResolver {

  private final RoutingContext routingContext;

  @Inject
  public MultiTenantResolver(RoutingContext routingContext) {
    this.routingContext = routingContext;
  }

  @Override
  public String getDefaultTenantId() {
    return "default";
  }

  @Override
  public String resolveTenantId() {

    final var tenantId = routingContext.request().getHeader("X-Tenant-Id");
    log.info("Tenant id: {}, path: {}", tenantId, routingContext.request().path());
    return tenantId != null ? tenantId : getDefaultTenantId();
  }
}
