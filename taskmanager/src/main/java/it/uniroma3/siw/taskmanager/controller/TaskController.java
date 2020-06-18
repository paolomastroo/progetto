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
import it.uniroma3.siw.taskmanager.controller.validation.ProjectValidator;
import it.uniroma3.siw.taskmanager.controller.validation.TaskValidator;
import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.ProjectService;
import it.uniroma3.siw.taskmanager.service.TaskService;

@Controller
public class TaskController {

	
	@Autowired
	TaskService taskService;
	
	@Autowired
	SessionData sessionData;

	@Autowired
	ProjectService projectService;
	
	@Autowired
	ProjectValidator projectValidator;
	
	@Autowired
	TaskValidator taskValidator;

	
	@RequestMapping(value= {"/project/{projectId}/addtask"}, method=RequestMethod.GET)
	public String createTaskForm(Model model, @PathVariable Long projectId) {
		Project project = this.projectService.getProject(projectId);
		model.addAttribute("project", project);
		model.addAttribute("taskForm", new Task());
		return "addTask";
	}
	
	@RequestMapping (value= {"/project/{projectId}/addtask"}, method=RequestMethod.POST)
	public String createTask(@Valid @ModelAttribute("taskForm") Task task, Long projectId,
			BindingResult projectBindingResult,
			BindingResult taskBindingResult,
			Model model) {
		
		Project project = this.projectService.getProject(projectId);
		projectValidator.validate(project, projectBindingResult);
		taskValidator.validate(task, taskBindingResult);
		if(!projectBindingResult.hasErrors() && !taskBindingResult.hasErrors()) {
			List<Task> tasks = project.getTasks();
			tasks.add(task);
			project.setTasks(tasks);
			this.taskService.saveTask(task);
			this.projectService.saveProject(project);
			return "redirect:/project/"+ project.getId();
		}
		model.addAttribute("project", project);
		model.addAttribute("taskForm", new Task());
		return "addTask";
		
	}
	
}
