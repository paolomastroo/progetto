package it.uniroma3.siw.taskmanager.service;



import java.util.List;
import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.repository.ProjectRepository;

@Service
public class ProjectService {

	@Autowired
	protected ProjectRepository projectRepository;
	
	
	/**
	 * metodo che prende il Project dal DB in base all'ID.
	 * @param id per prendere il Poject dal DB
	 * @return il Project da prendere
	 */
	@Transactional
	public Project getProject(long id) {
		Project result = this.projectRepository.findById(id).get();
		return result;
	}
	/**
	 * salva il Project nel DB
	 */
	@Transactional
	public Project saveProject(Project project) {
		return this.projectRepository.save(project);
	}
	/**
	 * Elimina il progetto dal DB
	 * @param project da eliminare
	 */
	@Transactional
	public void deleteProject(Project project) {
		this.projectRepository.delete(project);
	}
	/**
	 * salva un progetto condiviso con uno specifico User
	 * @param project
	 * @param user
	 * @return
	 */
	@Transactional
	public Project shareProjectWithUser(Project project,User user) {
		project.addMember(user);
		return this.projectRepository.save(project);
	}
	
	@Transactional
	public List<Project> retrieveProjectsOwnedBy(User user){
		return user.getOwnedProjects();
	}
	@Transactional
	public List<Project> retrieveProjectsSharedWith(User user){
		return projectRepository.findByMembers(user);
	}
	

	
}
