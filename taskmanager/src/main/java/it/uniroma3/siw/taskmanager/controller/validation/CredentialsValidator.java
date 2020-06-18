package it.uniroma3.siw.taskmanager.controller.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.taskmanager.model.Credentials;
import it.uniroma3.siw.taskmanager.model.User;
import it.uniroma3.siw.taskmanager.service.CredentialsService;

@Component
public class CredentialsValidator implements Validator{

	 final Integer MAX_USERNAME_LENGTH=20;
	 final Integer MIN_USERNAME_LENGTH=4;
	 final Integer MAX_PASSWORD_LENGTH=20;
	 final Integer MIN_PASSWORD_LENGTH=6;
	
	@Autowired
	CredentialsService credentialsService;

	@Override
	public void validate(Object o, Errors errors) {
    Credentials credentials = (Credentials)o;
    String username = credentials.getUsername().trim();
    String password = credentials.getPassword().trim();
    if(username.trim().isEmpty()) 
    	errors.rejectValue("username", "required");
    	else
    		if(username.length()> MAX_USERNAME_LENGTH || username.length()< MIN_USERNAME_LENGTH)
    			errors.rejectValue("username", "size");
    		else if (this.credentialsService.getCredentials(username) != null)
    			errors.rejectValue("username", "duplicate");
    if(password.trim().isEmpty()) 
    	errors.rejectValue("password", "required");
    	else
    		if(password.length()> MAX_PASSWORD_LENGTH || password.length()< MIN_PASSWORD_LENGTH)
    			errors.rejectValue("password", "size");
	}
    
 		
	
	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}
	public void validateSharing(Credentials sharerUserCredentials, Credentials sharedCredentials, Errors errors) {
		String username = sharedCredentials.getUsername();
		if(username.trim().isEmpty()) 
			errors.rejectValue("username", "required");
		else if(username.trim().length() < MIN_USERNAME_LENGTH || username.trim().length() > MAX_USERNAME_LENGTH)
			errors.rejectValue("username", "size");
		else if(credentialsService.getCredentials(username) == null)
			errors.rejectValue("username", "errorExist");
		else if(credentialsService.getUserByUsername(username).equals(sharerUserCredentials.getUser()))
			errors.rejectValue("username", "sameAsOwner");
	}

}


	
	


