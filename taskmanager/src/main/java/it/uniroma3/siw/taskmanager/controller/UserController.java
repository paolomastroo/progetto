package it.uniroma3.siw.taskmanager.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.taskmanager.controller.session.SessionData;
import it.uniroma3.siw.taskmanager.controller.validation.UserValidator;
import it.uniroma3.siw.taskmanager.model.Credentials;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.CredentialsService;
import it.uniroma3.siw.taskmanager.service.UserService;

@Controller
public class UserController {

	@Autowired
	SessionData sessionData;
	
	@Autowired
	CredentialsService credentialsService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserValidator userValidator;
	
	public UserController() {
		
	}
	
	@RequestMapping(value= {"/home"}, method= RequestMethod.GET)
	public String home(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("user", loggedUser);
		return "home";
	}
	@RequestMapping(value= {"/admin"}, method= RequestMethod.GET)
	public String admin(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("user", loggedUser);
		return "admin";
	}
	
	@RequestMapping(value={"/users/me"}, method = RequestMethod.GET)
    public String me(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		Credentials credentials = sessionData.getLoggedCredentials();
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("credentials", credentials);
		
		return "userProfile";
	}
	@RequestMapping(value= {"/admin/users"}, method = RequestMethod.GET)
	public String userList(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Credentials> allCredentials = this.credentialsService.getAllCredentials();
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("credentialsList", allCredentials);
		
		return "allUser";
	}
	@RequestMapping(value= {"/admin/users/{username}/delete"}, method=RequestMethod.POST)
	public String removeUser(Model model, @PathVariable String username) {
		this.credentialsService.deleteCredentials(username);
		return"redirect:/admin/users";
	}
	
	@RequestMapping(value = {"/projects/me"}, method=RequestMethod.GET)
	public String projectList(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Project> myOwnedProjects = this.userService.findAllProjects(loggedUser.getId());
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("myOwnedProjects", myOwnedProjects);
		return "myOwnedProjects";
		
	}
	@RequestMapping(value= {"/users/me/update"}, method=RequestMethod.GET)
	public String updateProfile(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
		return "updateProfile";
	}
	@RequestMapping(value= {"/users/me/update"}, method=RequestMethod.POST)
	public String updateProfile(@Valid @ModelAttribute("User") User user,
			BindingResult userBindingResult,
			Model model) {
		this.userValidator.validate(user, userBindingResult);
		if(!userBindingResult.hasErrors()) {
			this.userService.saveUser(user);
		return "updateProfileSuccessful";
	}
		return "updateProfile";
	}
	
}
