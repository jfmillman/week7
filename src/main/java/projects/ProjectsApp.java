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
	//WEEK 10: Added #2 - List projects
	//@formatter:off
	private List <String> operations = List.of(
	 "1) Add a project",
	 "2) List projects",
	 "3) Select a project"
	
			);
	//@formatter:on

	//this method processes the menu
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ProjectsApp().processUserSelections();
	
		
	}
//displays the menu selections, gets a selection from the user, and acts on the selection. Try catch in case of exception
	//WEEK 10: added case 2 for listProjects and case 3 for selectProject
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
