package projects.service;

import java.util.List;
import java.util.NoSuchElementException;


import projects.dao.ProjectDao;
import projects.entity.Project;
import projects.exception.DbException;


public class ProjectService {

	//this method calls the DAO class to insert a project row and returns the project object with the newly generated primary key value
	private ProjectDao projectDao = new ProjectDao();

	public Project addProject(Project project) {
		return projectDao.insertProject(project);
	}

	//WEEK 10: this returns the results of the method call to the DAO class.
	public List<Project> fetchAllProjects() {
		return projectDao.fetchAllProjects();
	}

	//WEEK 10: Calls the DAO to retrieve a single Project object with all details, including materials, steps, and categories
	public Project fetchProjectById(Integer projectId) {
		return projectDao.fetchProjectById(projectId).orElseThrow(
				() -> new NoSuchElementException("Project with project ID=" + projectId + "does not exist."));
		
	}

	//WEEK 11: the Dao method returns a boolean that indicated whether the update was successful. If false, an exception will be thrown.
	public void modifyProjectDetails(Project project) {
		if (!projectDao.modifyProjectDetails(project)) {
			throw new DbException ("Project with ID=" + project.getProjectId() + " does not exist.");
		}
		
	}

	//WEEK 11: call on the delete project method in project Dao to check the boolean return value. if the return is false (aka the project ID is invalid)
	//a DbException is thrown
	public void deleteProject(Integer projectId) {
		if (!projectDao.deleteProject(projectId)) {
			throw new DbException("Project with ID=" + projectId + " does not exist.");
		}
		
	}

}
