package com.bankzecure.webapp.repository;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement; // hinzugefügt
import java.sql.ResultSet;
import java.sql.SQLException;
import com.bankzecure.webapp.entity.*;
import com.bankzecure.webapp.JdbcUtils;

public class CustomerRepository {
  private final static String DB_URL = "jdbc:mysql://localhost:3306/springboot_bankzecure?serverTimezone=GMT";
	private final static String DB_USERNAME = "bankzecure";
	private final static String DB_PASSWORD = "Ultr4B4nk@L0nd0n";

  public Customer findByIdentifierAndPassword(final String identifier, final String password) {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    try {
      connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

      final String query = "SELECT * FROM customer WHERE identifier = ? AND password = ?;"; // '" + identifier + "' '" + password + "'


      System.out.println(query);
      statement = connection.prepareStatement(query); //statement = connection.createStatement();


      System.out.println(statement);
      statement.setString(1, identifier);
      statement.setString(2, password);
      System.out.println(statement);

      resultSet = statement.executeQuery(); //       resultSet = statement.executeQuery(query);

      Customer customer = null;

      if (resultSet.next()) {
        final int id = resultSet.getInt("id");
        final String identifierInDb = resultSet.getString("identifier");
        final String firstName = resultSet.getString("first_name");
        final String lastName = resultSet.getString("last_name");
        final String email = resultSet.getString("email");
        customer = new Customer(id, identifierInDb, firstName, lastName, email);
      }
      return customer;
    } catch (final SQLException e) {
      e.printStackTrace();
    } finally {
      JdbcUtils.closeResultSet(resultSet);
      JdbcUtils.closeStatement(statement);
      JdbcUtils.closeConnection(connection);
    }
    return null;
  }

  //-------------------

  public Customer update(String identifier, String newEmail, String newPassword) {

    Connection connection = null;
    PreparedStatement statement = null; // Statement statement = null;
    PreparedStatement statement1 = null; // Statement statement = null;
    PreparedStatement statement2 = null; // Statement statement = null;
    PreparedStatement statement3 = null; // Statement statement = null;
    ResultSet resultSet = null;
    Customer customer = null;
    String emailquery = "UPDATE customer SET email = ?";

  try {
    connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

      if (newPassword != "") {
        statement = connection.prepareStatement("UPDATE customer SET email = ?" + ",password = ?" + " WHERE identifier = ?");
        statement.setString(1, newEmail);
        statement.setString(2, newPassword);
        statement.setString(3, identifier);
        statement.executeUpdate();

      } else {
        statement = connection.prepareStatement("UPDATE customer SET email = ?" + " WHERE identifier = ?");
        statement.setString(1, newEmail);
        statement.setString(2, identifier);
      }
        statement.executeUpdate();

        JdbcUtils.closeStatement(statement);
        JdbcUtils.closeConnection(connection);

        connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);


      statement = connection.prepareStatement("Select * from customer where identifier = ?");
      statement.setString(1, identifier);

            resultSet = statement.executeQuery();

        if (resultSet.next()) {
          final int id = resultSet.getInt("id");
          final String identifierInDb = resultSet.getString("identifier");
          final String firstName = resultSet.getString("first_name");
          final String lastName = resultSet.getString("last_name");
          final String email = resultSet.getString("email");
          customer = new Customer(id, identifierInDb, firstName, lastName, email);
        }
        return customer;
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
   	    JdbcUtils.closeStatement(statement);
   	    JdbcUtils.closeConnection(connection);
    }
    return null;
}
}
