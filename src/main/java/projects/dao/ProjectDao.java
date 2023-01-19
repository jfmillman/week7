package projects.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import projects.entity.Project;
import projects.exception.DbException;
import provided.util.DaoBase;


//extends DaoBase
public class ProjectDao extends DaoBase {
	//These are constraints
	private static final String CATEGORY_TABLE = "category";
	private static final String MATERIAL_TABLE = "material";
	private static final String PROJECT_TABLE = "project";
	private static final String PROJECT_CATEGORY_TABLE = "project_category";
	private static final String STEP_TABLE = "step";

	
	public Project insertProject(Project project) {
		//@formatter:off
		//SQL statement will insert values from Project object passed to the insertProject() method. ? is a placeholder value for parameters passed to PreparedStatement
				String sql = ""
					+ "INSERT INTO " + PROJECT_TABLE + " "
					+ "(project_name, estimated_hours, actual_hours, difficulty, notes) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)";
				
				//this is where we obtain a connection
				try(Connection conn = DbConnection.getConnection()) {
					startTransaction(conn); //transaction started
				
					
					//set project details as parameters	
				try(PreparedStatement stmt = conn.prepareStatement(sql))	 {
					setParameter(stmt, 1, project.getProjectName(), String.class);
					setParameter(stmt, 2, project.getEstimatedHours(), BigDecimal.class);
					setParameter(stmt, 3, project.getActualHours(), BigDecimal.class);
					setParameter(stmt, 4, project.getDifficulty(), Integer.class);
					setParameter(stmt, 5, project.getNotes(), String.class);
					
					//this saves project details
					stmt.executeUpdate();
					
					//obtains project ID
					Integer projectId = getLastInsertId(conn, PROJECT_TABLE);
					
					//commit transaction
					commitTransaction(conn);
					
					//sets project ID
					project.setProjectId(projectId);
					return project;
				}
				//catches exception
				catch(Exception e) {
					rollbackTransaction(conn); //transaction rolled back
					throw new DbException(e);
				}
				} catch (SQLException e) {
					throw new DbException(e);
				}
				
				
				//@formatter:on
	}

}
