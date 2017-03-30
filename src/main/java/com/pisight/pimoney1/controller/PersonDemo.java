package com.pisight.pimoney1.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.pisight.pimoney1.beans.Person;

@RestController
public class PersonDemo {

	private Connection connect = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;


	private void setDBConnection(){

		try {
			Class.forName("com.mysql.jdbc.Driver");
		      // Setup the connection with the DB
		      connect = DriverManager
		          .getConnection("jdbc:mysql://localhost/person?"
		              + "user=sqluser&password=sqluserpw");

			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		} catch (SQLException ex) {
			System.out.println("#################################################################################");
			System.err.println(ex);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Person> getPersonList() throws SQLException{
		
		List<Person> result = null;
		try{
			setDBConnection();
			preparedStatement = connect
					.prepareStatement("SELECT name, age, address from person.detail");


			System.out.println("**********************************************************");
			resultSet = preparedStatement.executeQuery();
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			result = writeResultSet(resultSet);
		}
		catch (Exception e){
			System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
			System.err.println(e);
		}
		finally{
			connect.close();
		}


		return result;
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

}
