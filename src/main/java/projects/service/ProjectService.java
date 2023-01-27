package projects.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import projects.dao.ProjectDao;
import projects.entity.Project;


public class ProjectService {

	//this method calls the DAO class to insert a project row and returns the project object with the newly generated primary key value
	private ProjectDao projectDao = new ProjectDao();

	public Project addProject(Project project) {
		return projectDao.insertProject(project);
	}

	//WEEK 10: this returns the results o the method call to the DAO class.
	public List<Project> fetchAllProjects() {
		return projectDao.fetchAllProjects();
	}

	//WEEK 10: Calls the DAO to retrieve a single Project object with all details, including materials, steps, and categories
	public Project fetchProjectById(Integer projectId) {
		return projectDao.fetchProjectById(projectId).orElseThrow(
				() -> new NoSuchElementException("Project with project ID=" + projectId + "does not exist."));
		
	}

}
