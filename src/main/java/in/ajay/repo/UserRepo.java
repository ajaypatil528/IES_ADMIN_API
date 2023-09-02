package in.ajay.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import in.ajay.entity.UserEntity;

public interface UserRepo extends JpaRepository<UserEntity, Integer>{

}
