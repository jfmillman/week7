package projects.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import projects.exception.DbException;

//these are the constants
public class DbConnection {
	private static final String SCHEMA = "projects";
	private static final String USER = "projects";
	private static final String PASSWORD = "projects";
	private static final String HOST = "localhost";
	private static final int PORT = 3306;

	//this is the method that will return the MySQL connection URL
	public static Connection getConnection() {
		String url = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s&useSSL=false", HOST, PORT, SCHEMA, USER, PASSWORD);

System.out.println("Connecting with url=" + url);
		//this is how you create a connection, by using Driver Manager, along with a try catch block to catch the SQL Exception
		try {
			Connection conn = DriverManager.getConnection(url);
			
			System.out.println("Successfully obtained connection");
			return conn;
			//this is where we return the connection
		} catch (SQLException e) {
			System.out.println("Connection failed");
			throw new DbException(e);
		//	If we get an exception it will throw a catch block and will throw the exception that was entered in the catch block.
			
		}
	}
}
