package org.example.grudapp;

import org.junit.jupiter.api.AfterAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class PostgresContainerTest {
  protected static final String POSTGRES_IMAGE = "postgres:latest";
  protected static final String POSTGRES_USER = "postgres";
  protected static final String POSTGRES_PASSWORD = "password";
  protected static final String POSTGRES_DB = "postgres";

  protected static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
      .withUsername(POSTGRES_USER)
      .withPassword(POSTGRES_PASSWORD)
      .withDatabaseName(POSTGRES_DB);

  static {
    postgreSQLContainer.start();
  }

  @AfterAll
  static void stopContainers() {
    postgreSQLContainer.stop();
  }
}
