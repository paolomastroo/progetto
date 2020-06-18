package it.uniroma3.siw.taskmanager.service;

import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.taskmanager.model.Task;
import it.uniroma3.siw.taskmanager.repository.TaskRepository;

@Service
public class TaskService {

	@Autowired
	protected TaskRepository taskRepository;
	
	/**
	 * metodo che mi prende il task dal DB sulla base del suo id
	 */
	
	@Transactional
	public Task getTask(long id){
		Optional<Task> result = this.taskRepository.findById(id);
		return result.orElse(null);
		
	}
	/**
	 * questo metodo mi salva il task nel DB
	 * @param task
	 * @return
	 */
	@Transactional
	public Task saveTask(Task task) {
		return this.taskRepository.save(task);
	}
	/**
	 * metodo che salva il task nel DB quando è completo
	 * @param task
	 * @return
	 */
	@Transactional
	public Task setCompleted(Task task) {
		task.setCompleted(true);
		return this.taskRepository.save(task);
	}
	/**
	 * metodo che mi elimina un task dal DB
	 * @param task
	 */
	@Transactional
	public void deleteTask(Task task) {
		this.taskRepository.delete(task);
	}
	
}
