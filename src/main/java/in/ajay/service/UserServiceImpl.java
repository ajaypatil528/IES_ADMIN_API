package in.ajay.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.ajay.binding.DashboardCard;
import in.ajay.binding.LoginForm;
import in.ajay.binding.UserAccountForm;
import in.ajay.constants.AppConstants;
import in.ajay.entity.EligEntity;
import in.ajay.entity.UserEntity;
import in.ajay.repo.EligRepo;
import in.ajay.repo.PlanRepo;
import in.ajay.repo.UserRepo;
import in.ajay.utils.EmailUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private EmailUtils emailUtils;

	@Autowired
	private PlanRepo planRepo;

	@Autowired
	private EligRepo eligRepo;

	@Override
	public String loginForm(LoginForm loginForm) {

		UserEntity entity = userRepo.findByEmailAndPwd(loginForm.getEmail(), loginForm.getPasswd());

		if (entity == null) {
			return AppConstants.INVALID_CRED;
		}

		if (AppConstants.Y_STR.equals(entity.getActiveSw()) && AppConstants.UNLOCKED.equals(entity.getAccStatus())) {
			return AppConstants.SUCCESS;
		} else {
			return AppConstants.ACC_LOCKED;
		}

	}

	@Override
	public boolean recoverPassword(String email) {

		UserEntity userEntity = userRepo.findByEmail(email);
		if (userEntity == null) {
			return false;
		} else {
			String subject = AppConstants.RECOVER_PWD;
			String body = readEmailBody(AppConstants.FORGOT_PWD_BODY_FILE, userEntity);
			return emailUtils.sendEmail(subject, body, email);
		}

	}

	@Override
	public DashboardCard fetchDashboardInfo() {

		long plansCount = planRepo.count();

		List<EligEntity> eliList = eligRepo.findAll();

		Long approvedCnt = eliList.stream().filter(ed -> ed.getPlanStatus().equals(AppConstants.AP)).count();

		Long deniedCnt = eliList.stream().filter(ed -> ed.getPlanStatus().equals(AppConstants.DN)).count();

		Double total = eliList.stream().mapToDouble(ed -> ed.getBenefitAmt()).sum();

		DashboardCard card = new DashboardCard();

		card.setPlansCnt(plansCount);
		card.setDeniedCnt(deniedCnt);
		card.setApprovedCnt(approvedCnt);
		card.setBeniftAmtGiven(total);

		return card;
	}

	@Override
	public UserAccountForm getUserByEmail(String email) {
		UserEntity userEntity = userRepo.findByEmail(email);
		UserAccountForm user = new UserAccountForm();
		BeanUtils.copyProperties(userEntity, user);
		return user;
	}
	
	private String readEmailBody(String filename, UserEntity user) {
		StringBuilder sb = new StringBuilder();
		
		try (Stream<String> lines = Files.lines(Paths.get(filename))) {
			lines.forEach(line -> {
				   line = line.replace(AppConstants.FNAME, user.getFullName());
				   line = line.replace(AppConstants.PWD, user.getPwd());
				   line = line.replace(AppConstants.EMAIL, user.getEmail());
				   sb.append(line);
			});
			
		}catch (Exception e){
			
		}
		return sb.toString();
	}

}
