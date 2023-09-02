package in.ajay.service;

import java.util.List;

import org.springframework.stereotype.Service;

import in.ajay.binding.PlanForm;

@Service
public interface PlanService {

	public boolean createPlan(PlanForm planForm);
	
	public List<PlanForm> fetchPlans();
	
	public PlanForm getPlanById(Integer planId);
	
	public String changePlanStatus(Integer planId, String status);
}
