package broccoli.persistence;

import io.quarkus.arc.Unremovable;
import io.quarkus.hibernate.orm.runtime.tenant.TenantResolver;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Unremovable
@RequestScoped
public class MultiTenantResolver implements TenantResolver {

  private final RoutingContext context;

  @Inject
  public MultiTenantResolver(RoutingContext context) {
    this.context = context;
  }

  @Override
  public String getDefaultTenantId() {
    return "default";
  }

  @Override
  public String resolveTenantId() {
    // OIDC TenantResolver has already calculated the tenant id and saved it as a RoutingContext `tenantId` attribute:
    return context.request().getHeader("X-Tenant-Id");
  }
}