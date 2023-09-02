package in.ajay.service;


import org.springframework.stereotype.Service;

import in.ajay.binding.DashboardCard;
import in.ajay.binding.LoginForm;
import in.ajay.binding.UserAccountForm;

@Service 
public interface UserService {

	public String loginForm(LoginForm loginForm);
	
	public boolean recoverPassword(String email);
	
	public DashboardCard fetchDashboardInfo();
	
	public UserAccountForm getUserByEmail(String email);
}
