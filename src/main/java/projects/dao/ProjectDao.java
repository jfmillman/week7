package projects.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import projects.entity.Category;
import projects.entity.Material;
import projects.entity.Project;
import projects.entity.Step;
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

//WEEK 10: Retrieves all the projects from the database with try/catch to catch exceptions
	public List<Project> fetchAllProjects() {
		String sql = "SELECT * FROM " + PROJECT_TABLE + " ORDER BY project_name";
		
		try(Connection conn = DbConnection.getConnection()) {
			startTransaction(conn);
		
			try (PreparedStatement stmt = conn.prepareStatement(sql)){
				try (ResultSet rs = stmt.executeQuery()) {
					List<Project> projects = new LinkedList<>();
					
					while (rs.next()) {
						projects.add(extract(rs, Project.class));
					}
					
					return projects;
				}
			}
			
			catch (Exception e) {
				rollbackTransaction(conn);
				throw new DbException(e);
			}
			
			} catch (SQLException e) {
			throw new DbException(e);
		}
		
	}

	//WEEK 10: retrieves a project row and all associated project rows (materials, steps, categories)
	public Optional<Project> fetchProjectById(Integer projectId) {
		String sql = "SELECT * FROM " + PROJECT_TABLE + " WHERE project_id = ? ";
		
		try(Connection conn = DbConnection.getConnection()) {
			startTransaction(conn);
			
			try {
				Project project = null;
				
				try(PreparedStatement stmt = conn.prepareStatement(sql)) {
					setParameter(stmt, 1, projectId, Integer.class);
					
					try (ResultSet rs = stmt.executeQuery()) {
						if (rs.next()) {
							project = extract(rs, Project.class);
						}
					}
				}
				
				if (Objects.nonNull(project)) {
					project.getMaterials().addAll(fetchMaterialsForProject(conn, projectId));
					project.getSteps().addAll(fetchStepsForProject(conn, projectId));
					project.getCategories().addAll(fetchCategoriesFromProject(conn, projectId));
				}
				
				commitTransaction(conn);
				
				return Optional.ofNullable(project);
			}
			
			catch (Exception e) {
				rollbackTransaction(conn);
				throw new DbException(e);
			}
			
			} 
			catch (SQLException e) {
			throw new DbException(e);
	}

	}

	//WEEK 10: returns Materials as a list
	private List<Material> fetchMaterialsForProject(Connection conn, Integer projectId) throws SQLException{
		//@formatter: off
		String sql = "SELECT * FROM " + MATERIAL_TABLE
				+ " WHERE project_id = ?";
		//@formatter: on
				
				try(PreparedStatement stmt = conn.prepareStatement(sql)) {
					setParameter (stmt, 1, projectId, Integer.class);
					
					try (ResultSet rs = stmt.executeQuery()) {
						List<Material> materials = new LinkedList<Material>();
						
						while (rs.next()) {
								materials.add(extract(rs, Material.class));
						}
						return materials;
					}
				}
	}

	//WEEK 10: returns Steps as a List
	private List<Step> fetchStepsForProject(Connection conn, Integer projectId) throws SQLException {
		//@formatter: off
		String sql = "SELECT * FROM " + STEP_TABLE
				+ " WHERE project_id = ?";
		//@formatter: on
		
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			setParameter(stmt, 1, projectId, Integer.class);
			
			try (ResultSet rs = stmt.executeQuery()) {
				List<Step> steps = new LinkedList<Step> ();
				
				while (rs.next() ) {
					steps.add(extract(rs, Step.class));
				}
				
				
				return steps;
			}
		}
	}

	//WEEK 10: returns Categories as a List
	private List<Category> fetchCategoriesFromProject(Connection conn, Integer projectId) throws SQLException {
		//@formatter: off
		String sql = ""
				+ "SELECT c.* FROM " + CATEGORY_TABLE + " c "
				+ "JOIN " + PROJECT_CATEGORY_TABLE + " pc USING (category_id) "
				+ "WHERE project_id = ?";
		//@formatter: on
		
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			setParameter(stmt, 1, projectId, Integer.class);
			
			try (ResultSet rs = stmt.executeQuery()) {
				List<Category> categories = new LinkedList<> ();
				
				while(rs.next() ) {
					categories.add(extract(rs, Category.class));
				}
				
				return categories;
			}
		}
	}
}
