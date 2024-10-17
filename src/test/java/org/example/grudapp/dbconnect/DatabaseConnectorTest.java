package org.example.grudapp.dbconnect;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;

import static org.junit.Assert.*;

public class DatabaseConnectorTest {

  @Test
  public void testGetConnection() throws SQLException {
    // Arrange
    // No setup needed, we're testing the connection method directly

    // Act
    Connection connection = DatabaseConnector.getConnection();

    // Assert
    assertNotNull(connection);
    assertTrue(connection.isValid(5)); // Check if the connection is valid
  }
}
