package broccoli.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;

/**
 * Service to create a schema for a tenant.=
 */
@ApplicationScoped
public class MultiTenantSchemaService {

  private final DataSource defaultDataSource;

  /**
   * Constructor.
   *
   * @param defaultDataSource default data source
   */
  @Inject
  public MultiTenantSchemaService(DataSource defaultDataSource) {
    this.defaultDataSource = defaultDataSource;
  }

  /**
   * Create a schema for a tenant.
   *
   * @param tenantId tenant id
   */
  public void createSchema(String tenantId) {

    final var flyway = Flyway.configure()
        .dataSource(defaultDataSource)
        .schemas(tenantId)
        .load();
    flyway.migrate();
  }
}
