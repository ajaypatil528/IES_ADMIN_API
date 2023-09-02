package in.ajay.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.ajay.binding.PlanForm;
import in.ajay.service.PlanService;

@RestController
public class PlanRestController {

	@Autowired
	private PlanService planService;
	
	@PostMapping("/plan")
	public ResponseEntity<String> createPlan(@RequestBody PlanForm planForm){
		boolean status = planService.createPlan(planForm);
		if(status) {
			return new ResponseEntity<String>("Plan is created", HttpStatus.CREATED);
		}else {
			return new ResponseEntity<String>("Plan Creation failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/plans")
	public ResponseEntity<List<PlanForm>> getPlans(){
		List<PlanForm> fetchPlans = planService.fetchPlans();
		return new ResponseEntity(fetchPlans, HttpStatus.OK);
	}
	
	@GetMapping("/plan/{planId}")
	public ResponseEntity<PlanForm> getPlan(@PathVariable("planId") Integer planId){
		PlanForm planById = planService.getPlanById(planId);
		return new ResponseEntity<PlanForm>(planById, HttpStatus.OK);
	}
	
	@PutMapping("/plan/{planId}/{status}")
	public ResponseEntity<List<PlanForm>> updatePlan(@PathVariable("planId") Integer planId, 
													 @PathVariable("status") String status){
		planService.changePlanStatus(planId, status);
		List<PlanForm> fetchPlans = planService.fetchPlans();
		return new ResponseEntity(fetchPlans, HttpStatus.OK);
	}

}
