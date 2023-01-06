package projects;

import java.sql.Connection;

import projects.dao.DbConnection;

public class ProjectsApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Connection conn = DbConnection.getConnection();
		//this is how we test that we have a connection
	}

}
