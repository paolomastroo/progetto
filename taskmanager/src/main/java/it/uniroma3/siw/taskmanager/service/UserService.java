package it.uniroma3.siw.taskmanager.service;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.taskmanager.model.Project;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.repository.ProjectRepository;
import it.uniroma3.siw.taskmanager.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	protected UserRepository userRepository;
	
	@Autowired
	protected ProjectRepository projectRepository;
	
	@Transactional
	public User getUser(long id) {
		Optional<User> result = this.userRepository.findById(id);
		return result.orElse(null);
	}
	
	
	
	
	@Transactional
	public User saveUser(User user) {
		return this.userRepository.save(user);
	}
	
	@Transactional
	public List<User> findAllUsers(){
		List<User> result = new ArrayList<>();
		Iterable<User> iterable = this.userRepository.findAll();
		for(User u : iterable) {
			result.add(u);
			}
		return result;
	}
	@Transactional
	public List<User> getMembers(Project project){
	return project.getMembers();
}
	@Transactional
	public List<Project> findAllProjects(long id){
		List<Project> result = this.getUser(id).getOwnedProjects();
		return result;
	}


}
