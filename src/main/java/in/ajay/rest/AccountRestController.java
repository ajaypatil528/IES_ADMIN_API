package in.ajay.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.ajay.binding.UserAccountForm;
import in.ajay.service.AccountService;

@RestController
public class AccountRestController {
	
	private Logger logger = LoggerFactory.getLogger(AccountRestController.class);
	
	@Autowired
	private AccountService accService;
	
	@PostMapping("/user")
	public ResponseEntity<String> createAccount(@RequestBody UserAccountForm userAccForm){
		
		logger.debug("Account creation process started...");
		
		boolean status = accService.createUserAccount(userAccForm);
		
		logger.debug("Account creation process completed...");
		
		if(status) {
			
			logger.info("Account created successfully...");
			return new ResponseEntity<String>("Account created", 
													   HttpStatus.CREATED);  //201
		}else {
			logger.info("Account creation failed...");
			return new ResponseEntity<String>("Account Creation Failed", 
													   HttpStatus.INTERNAL_SERVER_ERROR); //500
		}
	}
	
	@GetMapping("/users")
	public ResponseEntity<List<UserAccountForm>> getUsers(){
		logger.debug("Fetching User Accounts process started...");
		List<UserAccountForm> userAccForms = accService.fetchUserAccounts();
		logger.debug("Fetching User Accounts process completed...");
		logger.info("User Accounts fetched successfully...");
		return new ResponseEntity(userAccForms, HttpStatus.OK); 
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<UserAccountForm> getUser(@PathVariable("userId") Integer userId){
		UserAccountForm userAccById = accService.getUserAccById(userId);
		logger.info("User Accounts Fetched successfully...");
		return new ResponseEntity<UserAccountForm>(userAccById, HttpStatus.OK);
	}
	
	@PutMapping("/user/{userId}/{status}")
	public ResponseEntity<List<UserAccountForm>> updateUserAcc(@PathVariable("userId") Integer userId,
															   @PathVariable("status") String status){
		logger.debug("User account update process started...");
		accService.changeAccStatus(userId, status);
		logger.debug("User account update process completed...");
		logger.info("User account status updated successfully...");
		List<UserAccountForm> userAccForms = accService.fetchUserAccounts();
		return new ResponseEntity(userAccForms, HttpStatus.OK); 
	}
	
}
