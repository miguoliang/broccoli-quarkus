package broccoli.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;

@ApplicationScoped
public class MultiTenantSchemaService {

  private final DataSource defaultDataSource;

  @Inject
  public MultiTenantSchemaService(DataSource defaultDataSource) {
    this.defaultDataSource = defaultDataSource;
  }

  public void createSchema(String tenantId) {

    final var schemaName = "schema_" + tenantId;
    final var flyway = Flyway.configure()
        .dataSource(defaultDataSource)
        .schemas(schemaName)
        .load();
    flyway.migrate();
  }
}
