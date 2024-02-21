package broccoli;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import broccoli.persistence.MultiTenantSchemaService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class MultiTenantSchemaServiceTest {

  @Inject
  MultiTenantSchemaService multiTenantSchemaService;

  @Inject
  DataSource defaultDataSource;

  @Test
  void testCreateSchema() throws SQLException {

    multiTenantSchemaService.createSchema("test");
    final var connection = defaultDataSource.getConnection();
    final String query = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '%s'"
        .formatted("schema_test");
    final var preparedStatement = connection.prepareStatement(query);
    final var resultSet = preparedStatement.executeQuery();
    resultSet.next();
    final var count = resultSet.getInt(1);
    resultSet.close();
    preparedStatement.close();
    connection.close();
    assertThat(count, is(1));
  }
}
