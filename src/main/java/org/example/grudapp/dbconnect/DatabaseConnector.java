package org.example.grudapp.dbconnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
  private static final String DB_URL = "jdbc:postgresql://localhost:5432/mydb";
  private static final String DB_USERNAME = "user";
  private static final String DB_PASSWORD = "password";

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
  }
}
