package it.uniroma3.siw.taskmanager.controller;

import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.taskmanager.controller.session.SessionData;
import it.uniroma3.siw.taskmanager.controller.validation.CredentialsValidator;
import it.uniroma3.siw.taskmanager.controller.validation.ProjectValidator;
import it.uniroma3.siw.taskmanager.model.Credentials;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.CredentialsService;
import it.uniroma3.siw.taskmanager.service.ProjectService;
import it.uniroma3.siw.taskmanager.service.UserService;

@Controller
public class ProjectController {

	@Autowired
	CredentialsService credentialsService;
	
	@Autowired
	ProjectService projectService;

	@Autowired
	UserService userService;

	@Autowired
	ProjectValidator projectValidator;

	@Autowired
	SessionData sessionData;
	
	@Autowired
	CredentialsValidator credentialsValidator;

	@RequestMapping(value="/projects", method=RequestMethod.GET)
	public String myOwnedProjects(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Project> projectsList = projectService.retrieveProjectsOwnedBy(loggedUser);
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("myOwnedProjects", projectsList);

		return"myOwnedProjects";

	}

	@RequestMapping(value= {"/projects/{projectId}"}, method = RequestMethod.GET)
	public String project(Model model, @PathVariable Long projectId) {
		Project project = projectService.getProject(projectId);
		User loggedUser = sessionData.getLoggedUser();
		if(project==null)
			return "redirect:/projects";
		List<User> members = userService.getMembers(project);
		if(!project.getOwner().equals(loggedUser) && !members.contains(loggedUser))
			return "redirect:/projects";

		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("project", project);
		model.addAttribute("members", members);

		return "project";

	}
	@RequestMapping (value= {"/projects/add"}, method = RequestMethod.GET)
	public String createProjectForm(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectForm", new Project());
		return "addProject";

	}

	@RequestMapping (value= {"/projects/add"}, method = RequestMethod.POST)
	public String createProject(@Valid @ModelAttribute("projectForm") Project project,
			BindingResult projectBindingResult,
			Model model) {
		User loggedUser= sessionData.getLoggedUser();
		List<Project> myOwnedProject = loggedUser.getOwnedProjects();
		projectValidator.validate(project, projectBindingResult);
		if(!projectBindingResult.hasErrors()) {
			project.setOwner(loggedUser);
			myOwnedProject.add(project);
			loggedUser.setOwnedProjects(myOwnedProject);
			this.projectService.saveProject(project);
			this.userService.saveUser(loggedUser);
			return"redirect:/projects/" + project.getId();
		}
		model.addAttribute("loggedUser", loggedUser);
		return "addProject";
	}

	@RequestMapping(value = {"/project/me/{projectId}/delete"}, method=RequestMethod.POST)
	public String deleteProject(Model model, @PathVariable Long projectId) {
		User loggedUser = sessionData.getLoggedUser();
		List<Project> myOwnedProject = loggedUser.getOwnedProjects();
		Iterable<Project> it = myOwnedProject;
		for(Project deletedProject : it) {
			if(deletedProject.getId()==projectId) {
				
				this.projectService.deleteProject(deletedProject);
			}
		}
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("myOwnedProject", myOwnedProject);
		return "myOwnedProjects";

	}
	@RequestMapping(value = {"/sharedProjectsWithMe"}, method = RequestMethod.GET)
	public String sharedProjectsWithMe(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Project> sharedProjectsList = projectService.retrieveProjectsSharedWith(loggedUser);
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("sharedProjectsList", sharedProjectsList);
		return "sharedProjectsWithMe";
	}
	
	@RequestMapping(value = {"projects/{projectId}/share"}, method = RequestMethod.POST)
	public String shareProject(@Valid @ModelAttribute("shareCredentialsForm") Credentials shareCredentialsForm,
			BindingResult shareCredentialsFormBindingResult,
			@PathVariable("projectId") Long projectId,
			Model model) {
		Project project = this.projectService.getProject(projectId);
		Credentials sharerCredentials = sessionData.getLoggedCredentials();
		if(project.getOwner().equals(sharerCredentials.getUser()) || sharerCredentials.getRole().equals("ADMIN")) {
			this.credentialsValidator.validateSharing(sharerCredentials, shareCredentialsForm, shareCredentialsFormBindingResult);
			if(!shareCredentialsFormBindingResult.hasErrors()) {
				User user2ShareWith = credentialsService.getUserByUsername(shareCredentialsForm.getUsername());
					this.projectService.shareProjectWithUser(project, user2ShareWith);
					return "redirect:/projects/"+projectId.toString();
			}
		}
		model.addAttribute("loggedCredentials", sharerCredentials);
		model.addAttribute("shareCredentialsForm", shareCredentialsForm);
		return "redirect:/projects/"+project.getId().toString();
	}
}


