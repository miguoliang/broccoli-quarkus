package broccoli.persistence;

import io.quarkus.hibernate.orm.runtime.tenant.TenantResolver;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Custom tenant resolver.
 */
@ApplicationScoped
public class CustomTenantResolver implements TenantResolver {

  private final RoutingContext routingContext;

  @Inject
  public CustomTenantResolver(RoutingContext routingContext) {
    this.routingContext = routingContext;
  }

  @Override
  public String getDefaultTenantId() {
    return "default";
  }

  @Override
  public String resolveTenantId() {

    final var tenantId = routingContext.request().getHeader("X-Tenant-Id");
    return tenantId != null ? tenantId : getDefaultTenantId();
  }
}
