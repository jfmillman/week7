package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;



public class ProjectsApp {
	private Scanner scanner = new Scanner(System.in);
	private ProjectService projectService = new ProjectService();
	private Project curProject;

	//this will be printed in the console so the user knows what selection to make
	//WEEK 10: Added #2 - List projects and #3 - Select a project
	//WEEK 11: Added #4 = Update project details and #5 = Delete a project
	//@formatter:off
	private List <String> operations = List.of(
	 "1) Add a project",
	 "2) List projects",
	 "3) Select a project", 
	 "4) Update project deatails",
	 "5) Delete a project"
	
			);
	//@formatter:on

	//this method processes the menu
	public static void main(String[] args) {
		new ProjectsApp().processUserSelections();
	
		
	}
//displays the menu selections, gets a selection from the user, and acts on the selection. Try catch in case of exception
	//WEEK 10: added case 2 for listProjects and case 3 for selectProject
	//WEEK 11: added case 4 for updateProjectDetails and case 5 for deleteProject
	private void processUserSelections() {
		// TODO Auto-generated method stub
		boolean done = false;
		
		while(!done) {
			
			try {
				int selection = getUserSelection();
				switch(selection) {
				case -1:
					done = exitMenu();
					break;
					case 1:
						createProject();
						break;
					case 2:
						listProjects();
						break;
					case 3:
						selectProject();
						break;
					case 4:
						updateProjectDetails();
						break;
					case 5:
						deleteProejct();
						break;
					default: 
						System.out.println("\n" + selection + " is not valid. Try again.");
						break;
				}
			} 
			catch(Exception e) {
				System.out.println("\nError: " + e.toString() + " Try again.");
			}
		}
	}

	//WEEK 11: first call method listProjects, then prompt the user to enter the project ID. Then call the deleteProject method to delete the 
	//project based on the ID the user enters. Lastly we have a check to see if the project ID in the current project is the same as the ID entered by the user
	private void deleteProejct() {
		listProjects();
		
		Integer projectId = getIntInput("Enter the ID of the project to delete");
		
		projectService.deleteProject(projectId);
		System.out.println("Project " + projectId + " was deleted successfully.");
		
		if (Objects.nonNull(curProject) && curProject.getProjectId().equals(projectId)) {
			curProject = null;
		}
	
}
	//WEEK 11: checked to see if project is null. If is, prompts the user to select a project. If not null, it will print a message asking for projectName, estimatedHours,
	//actualHours, difficulty, and notes. Then I created a new project object. if the value is not null, the new input value is added to the project object. If null,
	//the value from curProject is added.
	private void updateProjectDetails() {
		if(Objects.isNull(curProject)) {
			System.out.println("\nPlease select a project");
			return;
		}
		
		String projectName = getStringInput("Enter the project name [" + curProject.getProjectName() + "]");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours [" + curProject.getEstimatedHours() + "]");
		BigDecimal actualHours = getDecimalInput ("Enter the actual hours [" + curProject.getActualHours() + "]");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5) [" + curProject.getDifficulty() + "]");
		String notes = getStringInput("Enter the project notes [" + curProject.getNotes() + "]");
		
		Project project = new Project();
		
		project.setProjectId(curProject.getProjectId());
		project.setProjectName(Objects.isNull(projectName) ? curProject.getProjectName() : projectName);
		project.setEstimatedHours(Objects.isNull(estimatedHours) ? curProject.getEstimatedHours() : estimatedHours);
		project.setActualHours(Objects.isNull(actualHours) ? curProject.getActualHours() : actualHours);
		project.setDifficulty(Objects.isNull(difficulty) ? curProject.getDifficulty() : difficulty);
		project.setNotes(Objects.isNull(notes) ? curProject.getNotes() : notes);
		
		projectService.modifyProjectDetails(project);
		
		curProject = projectService.fetchProjectById(curProject.getProjectId());
	
}
	//WEEK 10: lists the project IDs and names so the user can select a project ID. Once the ID is entered, the project details will be returned.
	private void selectProject() {
	  listProjects();
	  Integer projectId = getIntInput ("Enter a project ID to select a project");
	  
	  //Unselects the current project
	  curProject = null;
	  
	  //thows an exception if an invalid project id is entered
	  curProject = projectService.fetchProjectById(projectId);
	  
	
}
	//WEEK 10: listProjects method lists out the project with the name and ID, : and and indent for each line
	private void listProjects() {
		List<Project> projects = projectService.fetchAllProjects();
		
		System.out.println("\nProjects:");
		
		projects.forEach(project -> System.out.println("   " + project.getProjectId() + ": " + project.getProjectName()));
		
		
	}
	//gathers project details from user
	private void createProject() {
		String projectName = getStringInput ("Enter the project name");
		BigDecimal estimatedHours = getDecimalInput ("Enter the estimated hours");
		BigDecimal actualHours = getDecimalInput ("Enter the actual hours");
		Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
		String notes = getStringInput ("Enter the project notes");
		
		Project project = new Project ();
		
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
		
		Project dbProject = projectService.addProject(project);
		System.out.println("You have successfully created project: " + dbProject);
		
		
	}
//BigDecimal method with a try catch in case an invalid entry is input
	private BigDecimal getDecimalInput(String string) {
		String input = getStringInput(string);
		
		if(Objects.isNull(input)) {
		return null;
	}
		try {
			return new BigDecimal(input).setScale(2);
		}
		catch(NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number.");
		}
	}
//this allows the user to exit the menu
	private boolean exitMenu() {
		System.out.println("\nExiting the menu!");
		return true;
	}

	//prints the operations and accepts user input as an Integer
	private int getUserSelection() {
		printOperations();
		
		Integer input = getIntInput("Enter a menu selection");
		
		return Objects.isNull(input) ? -1 : input;
	}
//returns the users menu selection. Try catch in case the input is null.
	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);
		
		if(Objects.isNull(input)) {
		return null;
	}
		try {
			return Integer.valueOf(input);
		}
		catch(NumberFormatException e) {
			throw new DbException(input + " is not a valid number.");
		}
	}
//prints the prompt and gets input from user
	private String getStringInput(String prompt) {
		System.out.println(prompt + ": ");
		String input = scanner.nextLine();
		
		return input.isBlank() ? null : input.trim();
	}

	//prints each available selection on a separate line in the console
	//WEEK 10: Prints the current project when the available menu selections are displayed
	private void printOperations() {
		System.out.println("\nThese are the available selections. Press the Enter Key to quit:");
		operations.forEach(line -> System.out.println("  " + line));
		
	//WEEK 10 addition for printOperations
		if (Objects.isNull(curProject)) {
			System.out.println("\nYou are not working with a project.");
		}
		else {
			System.out.println("\nYou are working with project: " + curProject);
		}
		
	}

}
