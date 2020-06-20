package com.RTGS.Settlement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.RTGS.MasterService;

@RestController
public class ChaqueController {

	@Autowired 
	private ChaqueService chaqueService ; 
	
	
	@RequestMapping(method = RequestMethod.GET ,value = "/settlements/checks/all")
	public ModelAndView getChecks(@Param(value ="index") int index) {
		ModelAndView mav = new ModelAndView("settlements/allChecks");
		List<Chaque> allChecksList = this.chaqueService.getAllChecks(index);
		mav.addObject("checksList",allChecksList);
		if(allChecksList.size() > 0 ) {
			MasterService.addSequesnceVaraibles(mav, index);
		}else {
			MasterService.addSequesnceVaraibles(mav, -1);
		}
		return mav ; 
	}
	
	@RequestMapping(method = RequestMethod.GET , value = "/settlements/test/inject")
	public ModelAndView injectTestData() {
		this.chaqueService.injectData() ; 
		return this.success("test data added ");
	}
	
	@RequestMapping(method = RequestMethod.GET , value = "/settlements/checks/add")
	public ModelAndView addNewCheckRequest() {
		ModelAndView mav = new ModelAndView("settlements/add");
		mav.addObject("check", new Chaque());
		return mav ; 
	}
	
	@RequestMapping(method = RequestMethod.POST , value = "/settlements/checks/add")
	public ModelAndView addNewCheckResponse(@ModelAttribute Chaque chaque) {
		String result = this.chaqueService.addCheck(chaque);
		if(!result.equalsIgnoreCase("ok")) {
			return failView(result); 
		}else {
			//send to msgQ 
		return this.success("تمت إضافة الشيك بنجاح");
		}
	}
	
	
	@RequestMapping(method = RequestMethod.GET , value = "/settlements/reports")
	public ModelAndView getSettlementReports() {
		ModelAndView mav = new ModelAndView("settlements/allreports");
		
		return null ; 
	}
	
	@RequestMapping(method = RequestMethod.GET , value = "/settlements/reports/checks/{id}")
	public ModelAndView getSettlementReportsFromCheck(@PathVariable int id ) {
		ModelAndView mav = new ModelAndView("settlements/allreports");
		
		return null ; 
	}
	
	@RequestMapping(method = RequestMethod.GET , value = "/settlements/reports/settlementReport/{id{")
	public ModelAndView getSettlementCheck(@PathVariable int id ) {
		ModelAndView mav = new ModelAndView("settlements/allreports");
		
		return null ; 
	}
	
	
	
	private ModelAndView success(String msg ) {
		ModelAndView mav = new ModelAndView("settlements/success");
		mav.addObject("msg",msg);
		return mav ; 
	}
	
	private ModelAndView failView(String result) {
		ModelAndView  mav = new ModelAndView("settlements/fail");
		mav.addObject("msg", result);
		return mav ; 
	}

	
	
	
}
