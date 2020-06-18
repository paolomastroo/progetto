package it.uniroma3.siw.taskmanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.taskmanager.model.Credentials;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.repository.CredentialsRepository;

@Service
public class CredentialsService {

	@Autowired
	protected CredentialsRepository credentialsRepository;

	@Autowired
	protected PasswordEncoder passwordEncoder;

 @Transactional
 public Credentials getCredentials(long id) {
	 Optional<Credentials> result = this.credentialsRepository.findById(id);
	 return result.orElse(null);
 }
 
 @Transactional
 public Credentials getCredentials(String username) {
	 Optional<Credentials> result = this.credentialsRepository.findByUsername(username);
	 return result.orElse(null);
 }
 @Transactional
 public Credentials saveCredentials(Credentials credentials) {
	 credentials.setRole(Credentials.DEFAUL_ROLE);
	 credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
	 return this.credentialsRepository.save(credentials);
 }
 @Transactional
 public List<Credentials> getAllCredentials(){
	 List<Credentials> result = new ArrayList<>();
	 Iterable<Credentials> it = this.credentialsRepository.findAll();
	 for(Credentials c : it) {
		 result.add(c);
	 }
	 return result;
 }
 @Transactional
 public void deleteCredentials(String username) {
	 this.credentialsRepository.deleteAll();
	 
 }

	@Transactional
	public User getUserByUsername(String username) {
		return this.getCredentials(username).getUser();
	}
 
 
 
}