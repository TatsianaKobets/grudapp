package org.example.grudapp.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

public class DatabaseManager {

  private static final String DB_URL = "db.url";
  private static final String DB_USERNAME = "db.username";
  private static final String DB_PASSWORD = "db.password";
  private static final String DB_DEFAULT_SCHEMA = "db.defaultSchema";

  public static Connection getConnection() throws SQLException {
    Properties properties = ConfigLoader.loadConfig();
    if (properties == null) {
      throw new SQLException("Unable to load config");
    }

    String url = properties.getProperty(DB_URL);
    String username = properties.getProperty(DB_USERNAME);
    String password = properties.getProperty(DB_PASSWORD);

    return DriverManager.getConnection(url, username, password);
  }

  public static void migrate() throws SQLException, LiquibaseException {
    Connection connection = null;
    try {
      connection = getConnection();
      try (Statement statement = connection.createStatement()) {
        statement.execute("CREATE SCHEMA IF NOT EXISTS postgres_schema");

        Database database = DatabaseFactory.getInstance()
            .findCorrectDatabaseImplementation(new JdbcConnection(connection));
        database.setDefaultSchemaName(ConfigLoader.loadConfig().getProperty(DB_DEFAULT_SCHEMA));

        Liquibase liquibase = new Liquibase("db/changelog/changelog.xml",
            new ClassLoaderResourceAccessor(), database);
        liquibase.update();
        System.out.println("Migration is completed successfully");
      }
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          System.out.println("Error closing connection: " + e.getMessage());
        }
      }
    }
  }
}
