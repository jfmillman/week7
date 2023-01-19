package projects.service;

import projects.dao.ProjectDao;
import projects.entity.Project;


public class ProjectService {

	//this method calls the DAO class to insert a project row and returns the project object with the newly generated primary key value
	private ProjectDao projectDao = new ProjectDao();

	public Project addProject(Project project) {
		return projectDao.insertProject(project);
	}

}
