package in.ajay.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.ajay.binding.UnlockAccountForm;
import in.ajay.binding.UserAccountForm;
import in.ajay.entity.UserEntity;
import in.ajay.repo.UserRepo;
import in.ajay.utils.EmailUtils;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private UserRepo userRepo;

	@Autowired
	private EmailUtils emailUtils;
	
	@Override
	public boolean createUserAccount(UserAccountForm accForm) {
		
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(accForm, userEntity);
		
		//set random password
		userEntity.setPwd(generatePwd());
		
		//set account status
		userEntity.setAccStatus("LOCKED");
		userEntity.setActiveSw("Y");
		userRepo.save(userEntity);
		
		// send email
		String subject = "User Registration";
		String body = readEmailBody("REG_EMAIL_BODY.txt", userEntity);
		return emailUtils.sendEmail(subject, body, accForm.getEmail());
		
	
	}

	private String readEmailBody(String filename, UserEntity userEntity) {
		
		StringBuilder sb = new StringBuilder();
		try (Stream<String> lines = Files.lines(Paths.get(filename))){
					lines.forEach(line ->{
						line = line.replace("${FNAME}", userEntity.getFullName());
						line = line.replace("${TEMP_PWD}", userEntity.getPwd());
						line = line.replace("${EMAIL}", userEntity.getEmail());
						sb.append(line);
					});
		}catch(Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	@Override
	public List<UserAccountForm> fetchUserAccounts() {


		List<UserEntity> userEntities = userRepo.findAll();
		
		List<UserAccountForm> users = new ArrayList<>();
		
		for (UserEntity userEntity : userEntities) {
			UserAccountForm user = new UserAccountForm();
			BeanUtils.copyProperties(userEntity, user);
			users.add(user);
		}
		return users;
	}

	@Override
	public UserAccountForm getUserAccById(Integer accId) {

	 Optional<UserEntity> optional = userRepo.findById(accId);
	 if(optional.isPresent()) {
		 UserEntity userEntity = optional.get();
		 UserAccountForm user = new UserAccountForm();
		 BeanUtils.copyProperties(userEntity, user);
		 return user;
	 }
		return null;
	}

	@Override
	public String changeAccStatus(Integer userId, String status) {
		
		int cnt = userRepo.updateAccStatus(userId, status);
		if(cnt > 0) {
			return "Status changed";
		}
		return "Failed to change";
	}

	@Override
	public String unlockUserAccount(UnlockAccountForm unlockAccForm) {
		
		UserEntity entity = userRepo.findByEmail(unlockAccForm.getEmail());
		entity.setPwd(unlockAccForm.getNewPsswd());
		entity.setAccStatus("UNLOCKED");
		
		userRepo.save(entity);
		return "Account Unlocked";
	}
	
	private String generatePwd() {
		
		String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
	    String numbers = "0123456789";

	    // combine all strings
	    String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;

	    // create random string builder
	    StringBuilder sb = new StringBuilder();

	    // create an object of Random class
	    Random random = new Random();

	    // specify length of random string
	    int length = 10;

	    for(int i = 0; i < length; i++) {

	      // generate random index number
	      int index = random.nextInt(alphaNumeric.length());

	      // get character specified by index
	      // from the string
	      char randomChar = alphaNumeric.charAt(index);

	      // append the character to string builder
	      sb.append(randomChar);
	    }

	    return sb.toString();
		
	}

}
