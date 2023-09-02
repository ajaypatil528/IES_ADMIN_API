package in.ajay.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.ajay.binding.DashboardCard;
import in.ajay.binding.LoginForm;
import in.ajay.binding.UserAccountForm;
import in.ajay.service.AccountService;
import in.ajay.service.UserService;

@RestController
public class UserRestController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AccountService accService;
	
	@PostMapping("/login")
	public String login(@RequestBody LoginForm loginForm) {
		
		String status = userService.loginForm(loginForm);
		
		if(status.equals("success")) {
			return "redirect:/dashboard?email="+loginForm.getEmail();
		}else{
			return status;
		}
	}
	
	@GetMapping("/dashboard")
	public ResponseEntity<DashboardCard> buildDashboard(@RequestParam("email") String email){
		UserAccountForm user = userService.getUserByEmail(email);
		DashboardCard dashboardCard = userService.fetchDashboardInfo();
		dashboardCard.setUser(user);
		return new ResponseEntity<DashboardCard>(dashboardCard, HttpStatus.OK);
	}
	
}
