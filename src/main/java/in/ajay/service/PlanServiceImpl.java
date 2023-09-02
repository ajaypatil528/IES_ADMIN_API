package in.ajay.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.ajay.binding.PlanForm;
import in.ajay.entity.EligEntity;
import in.ajay.entity.PlanEntity;
import in.ajay.repo.EligRepo;
import in.ajay.repo.PlanRepo;
import in.ajay.utils.EmailUtils;

@Service
public class PlanServiceImpl implements PlanService {
	
	@Autowired
	private EmailUtils emailUtils;
	
	@Autowired
	private PlanRepo planRepo;
	
	@Autowired
	private EligRepo eligRepo; 

	@Override
	public boolean createPlan(PlanForm planForm) {
		
		PlanEntity planEntity = new PlanEntity();
		BeanUtils.copyProperties(planForm, planEntity);
		
		planEntity.getPlanCategory();
		planEntity.getPlanName();
		planEntity.getPlanStartDate();
		planEntity.getPlanEndDate();
		
		planRepo.save(planEntity);
		
		return true;
		
	}

	@Override
	public List<PlanForm> fetchPlans() {
		
		List<PlanEntity> planEntities = planRepo.findAll();
		List<PlanForm> plans = new ArrayList<>();
		
		for (PlanEntity planEntity : planEntities) {
			PlanForm plan = new PlanForm();
			BeanUtils.copyProperties(planEntity, plan);
			plans.add(plan);
		}
		return plans;
	}

	@Override
	public PlanForm getPlanById(Integer planId) {
		
		Optional<PlanEntity> optional = planRepo.findById(planId);
		if(optional.isPresent()) {
			PlanEntity planEntity = optional.get();
			PlanForm plan = new PlanForm();
			BeanUtils.copyProperties(planEntity, plan);
			return plan;
		}
		return null;
	}

	@Override
	public String changePlanStatus(Integer edgTraceId, String status) {
			
		Integer cnt = eligRepo.updatePlanStatus(edgTraceId, status);
		if(cnt > 0) {
			return "Status Chnaged";
		}
		return "Failed to change";
	}

}
