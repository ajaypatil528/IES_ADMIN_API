package in.ajay.service;

import java.util.List;

import org.springframework.stereotype.Service;

import in.ajay.binding.UnlockAccountForm;
import in.ajay.binding.UserAccountForm;

@Service
public interface AccountService {

	public boolean createUserAccount(UserAccountForm accForm);
	
	public List<UserAccountForm> fetchUserAccounts();
	
	public UserAccountForm getUserAccById(Integer accId);
	
	public String changeAccStatus(Integer accId, String status);
	
	public String unlockUserAccount(UnlockAccountForm unlockAccForm);
}
