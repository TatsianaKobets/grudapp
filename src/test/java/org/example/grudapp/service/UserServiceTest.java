package org.example.grudapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.example.grudapp.PostgresContainerTest;
import org.example.grudapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.junit.jupiter.TestcontainersExtension;

@ExtendWith(TestcontainersExtension.class)
class UserServiceTest extends PostgresContainerTest {

  private UserService userService;

  @BeforeEach
  void setUp() {
    userService = new UserService();
  }

  @Test
  void testGetConnection() throws SQLException {
    Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
        "postgres", "password");
    userService.setConnection(connection);
    Connection result = userService.getConnection();

    assertEquals(connection, result);
  }

  @Test
  void registerUser_success() {
    String email = "test@example.com";
    String password = "testpassword";
    String username = "testuser";

    User user = userService.registerUser(email, password, username);

    assertEquals(user.getId(), userService.getUserByEmail(email).getId());
    assertEquals(user.getEmail(), userService.getUserByEmail(email).getEmail());
    assertEquals(user.getName(), userService.getUserByEmail(email).getName());
    assertEquals(user.getRole(), userService.getUserByEmail(email).getRole());
  }

  @Test
  void registerUser_duplicateEmail() {
    String email = "test@example.com";
    String password = "testpassword";
    String username = "testuser";

    userService.registerUser(email, password, username);

    User user = userService.registerUser(email, password, username);

    assertNull(user);
  }
}
/*
  @Container
  public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
      "postgres:latest")
      .withDatabaseName("postgres")
      .withUsername("postgres")
      .withPassword("password");

  private UserService userService;
  private Connection connection;

  @BeforeEach
  public void setUp() throws SQLException {
    // Устанавливаем соединение с контейнером PostgreSQL connection = DriverManager.getConnection(postgresContainer.getJdbcUrl(),
    postgresContainer.getUsername(), postgresContainer.getPassword());
    connection = DriverManager.getConnection(postgresContainer.getJdbcUrl(), postgresContainer.getUsername(),
        postgresContainer.getPassword());
    userService = new UserService();
    userService.setConnection(connection);
    // Создаем необходимую схему и таблицы
    createSchemaAndTables();
  }

  private void createSchemaAndTables() throws SQLException {
    // Создаем схему и таблицы, необходимые для тестов String sql = "CREATE SCHEMA IF NOT EXISTS postgres_schema; " +
    "CREATE TABLE IF NOT EXISTS postgres_schema.users (" +
        "id SERIAL PRIMARY KEY, " +
        "email VARCHAR(255) NOT NULL UNIQUE, " +
        "password VARCHAR(255) NOT NULL, " +
        "name VARCHAR(255) NOT NULL, " +
        "role VARCHAR(50) NOT NULL);";
    try (var stmt = connection.createStatement()) {
      stmt.execute(sql);
    }
  }

  @AfterEach
  public void tearDown() throws SQLException {
    // Закрываем соединение после теста if (connection != null) {
    connection.close();
  }
}

@Test
public void testRegisterUser() {
  // Проверяем регистрацию нового пользователя int userId = userService.registerUser ("test@example.com", "password123", "Test User");
  Assertions.assertTrue(userId > 0, "Пользователь должен быть зарегистрирован");
  // Проверяем, что пользователь зарегистрирован в базе данных
  User user = userService.getUser ByEmail("test@example.com");
  Assertions.assertNotNull(user, "Пользователь должен существовать в базе данных");
  Assertions.assertEquals("Test User", user.getName(), "Имя пользователя должно совпадать");
}

@Test
public void testAuthenticateUser() {
  // Сначала регистрируем пользователя userService.registerUser ("test@example.com", "password123", "Test User");

  // Проверяем аутентификацию User authenticatedUser  = userService.authenticateUser ("test@example.com", "password123");
  Assertions.assertNotNull(authenticatedUser, "Пользователь должен быть аутентифицирован");
  Assertions.assertEquals("Test User", authenticatedUser.getName(),
      "Имя аутентифицированного пользователя должно совпадать");
}

@Test
public void testDeleteUser() {
  // Регистрируем пользователя userService.registerUser ("test@example.com", "password123", "Test User");

  // Удаляем пользователя
  userService.deleteUser("test@example.com");

  // Проверяем, что пользователь был удален User user = userService.getUser ByEmail("test@example.com");
  Assertions.assertNull(user, "Пользователь должен быть удален из базы данных");
}

}*/