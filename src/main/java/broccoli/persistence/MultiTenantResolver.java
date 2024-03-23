package broccoli.persistence;

import io.quarkus.hibernate.orm.PersistenceUnitExtension;
import io.quarkus.hibernate.orm.runtime.tenant.TenantResolver;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

/**
 * Custom tenant resolver that resolves the tenant based on the path.
 */
@RequestScoped
@PersistenceUnitExtension
public class MultiTenantResolver implements TenantResolver {

  private final RoutingContext context;

  @Inject
  public MultiTenantResolver(RoutingContext context) {
    this.context = context;
  }

  @Override
  public String getDefaultTenantId() {
    return "PUBLIC";
  }

  @Override
  public String resolveTenantId() {
    final var tenantId = context.request().getHeader("X-Tenant-Id");
    return tenantId != null ? tenantId : getDefaultTenantId();
  }
}