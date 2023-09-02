package in.ajay.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.ajay.entity.UserEntity;

public interface UserRepo extends JpaRepository<UserEntity, Integer>{

	
	@Query("UPDATE UserEntity set accStatus=:status where userId=:userId")
	public Integer updateAccStatus(Integer userId, String status);

	public UserEntity findByEmail(String email);

	public UserEntity findByEmailAndPwd(String email, String pwd);
}
