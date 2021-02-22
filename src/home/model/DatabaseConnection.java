package home.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection
{
  public static Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(
        "jdbc:sqlserver://localhost:1433;databaseName=CBTSystem;integratedSecurity=true");
  }
}
