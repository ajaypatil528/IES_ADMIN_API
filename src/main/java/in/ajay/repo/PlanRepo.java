package in.ajay.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.ajay.entity.PlanEntity;

public interface PlanRepo extends JpaRepository<PlanEntity, Integer> {

	
}
