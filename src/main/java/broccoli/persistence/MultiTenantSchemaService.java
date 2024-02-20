package broccoli.persistence;

import broccoli.common.StringUtils;
import jakarta.enterprise.context.ApplicationScoped;
import org.flywaydb.core.Flyway;

@ApplicationScoped
public class MultiTenantSchemaService {

  public DatabaseConnectionInfo createSchema(String tenantId) {

    final var schemaName = "schema_" + tenantId;
    final var jdbcUrl = "jdbc:postgresql://localhost:5432/mydb";
    final var username = "myuser";
    final var password = "mypassword";
//    final var username = generateUsername();
//    final var password = generatePassword();
    final var flyway = Flyway.configure()
        .dataSource(jdbcUrl, username, password)
        .schemas(schemaName)
        .load();
    flyway.migrate();
    return new DatabaseConnectionInfo(jdbcUrl, username, password);
  }

  public record DatabaseConnectionInfo(String url, String username, String password) {
  }

  private static String generateUsername() {
    return "user_" + StringUtils.generateRandomString(8);
  }

  private static String generatePassword() {
    return StringUtils.generateRandomString(8) + "!0A";
  }
}
