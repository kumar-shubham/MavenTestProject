package com.pisight.pimoney1.beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBAgent {
	
	private Connection connect = null;
	  private Statement statement = null;
	  private PreparedStatement preparedStatement = null;
	  private ResultSet resultSet = null;
	  
	  

	  public void readDataBase() throws Exception {
	    try {
	      // This will load the MySQL driver, each DB has its own driver
	      Class.forName("com.mysql.jdbc.Driver");
	      // Setup the connection with the DB
	      connect = DriverManager
	          .getConnection("jdbc:mysql://localhost/person?"
	              + "user=sqluser&password=sqluserpw");

	      // Statements allow to issue SQL queries to the database
	      statement = connect.createStatement();
	      // Result set get the result of the SQL query
	      resultSet = statement
	          .executeQuery("select * from person.detail");
	      writeResultSet(resultSet);

	      // PreparedStatements can use variables and are more efficient
	      preparedStatement = connect
	          .prepareStatement("insert into  person.detail values (default, ?, ?, ?)");
	     
	      // Parameters start with 1
	      preparedStatement.setString(1, "kunal");
	      preparedStatement.setInt(2, 25);
	      preparedStatement.setString(3, "Kharghar");
	      preparedStatement.executeUpdate();

	      preparedStatement = connect
	          .prepareStatement("SELECT name, age, address from person.detail");
	      resultSet = preparedStatement.executeQuery();
	      writeResultSet(resultSet);

	      // Remove again the insert comment
	      preparedStatement = connect
	      .prepareStatement("delete from person.detail where name= ? ; ");
	      preparedStatement.setString(1, "kumar");
	      preparedStatement.executeUpdate();
	      
	      resultSet = statement
	      .executeQuery("select * from person.detail");
	      writeMetaData(resultSet);
	      
	    } catch (Exception e) {
	      throw e;
	    }

	  }
	  
	  public List<Person> getPersonList() throws SQLException{
		  preparedStatement = connect
		          .prepareStatement("SELECT name, age, address from person.detail");
		  
		  resultSet = preparedStatement.executeQuery();
		  
		  return writeResultSet(resultSet);
	  }

	  private void writeMetaData(ResultSet resultSet) throws SQLException {
	    //   Now get some metadata from the database
	    // Result set get the result of the SQL query
	    
	    System.out.println("The columns in the table are: ");
	    
	    System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
	    for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
	      System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
	    }
	  }

	  private List<Person> writeResultSet(ResultSet resultSet) throws SQLException {
	    // ResultSet is initially before the first data set
		  List<Person> lop = new ArrayList<Person>();
		  while (resultSet.next()) {
	      // It is possible to get the columns via name
	      // also possible to get the columns via the column number
	      // which starts at 1
	      // e.g. resultSet.getSTring(2);
	      String name = resultSet.getString("name");
	      String age = resultSet.getString("age");
	      String address	 = resultSet.getString("address");
	      Person p = new Person(name, Integer.parseInt(age), address);
	      lop.add(p);
	      System.out.println("Name: " + name);
	      System.out.println("Age: " + age);
	      System.out.println("Address: " + address);
	    }
		  
		  return lop;
	  }

	  // You need to close the resultSet
	  public void close() {
	    try {
	      if (resultSet != null) {
	        resultSet.close();
	      }

	      if (statement != null) {
	        statement.close();
	      }

	      if (connect != null) {
	        connect.close();
	      }
	    } catch (Exception e) {

	    }
	  }

}
