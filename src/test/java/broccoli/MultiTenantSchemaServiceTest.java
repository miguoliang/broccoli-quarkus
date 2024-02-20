package broccoli;

import broccoli.persistence.MultiTenantSchemaService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class MultiTenantSchemaServiceTest {

  @Inject
  MultiTenantSchemaService multiTenantSchemaService;

  @Test
  void testCreateSchema() throws SQLException {

    final var databaseConnectionInfo = multiTenantSchemaService.createSchema("test");
    final var connection = DriverManager.getConnection(databaseConnectionInfo.url(),
        databaseConnectionInfo.username(), databaseConnectionInfo.password());
    final String query = "SELECT * FROM INFORMATION_SCHEMA.SCHEMATA";
    final var preparedStatement = connection.prepareStatement(query);
    final var resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      System.out.printf(
          "%s%n", resultSet.getString("SCHEMA_NAME")
      );
    }
    resultSet.close();
    preparedStatement.close();
    connection.close();
  }
}
