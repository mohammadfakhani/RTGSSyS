package com.RTGS.Settlement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.RTGS.Facade.Facade;
import com.RTGS.security.users.RTGSUser;

@RestController
public class ChaqueController {

	@Autowired
	private Facade facade;
	
	@RequestMapping(method = RequestMethod.GET ,value = "/settlements/checks/all")
	public ModelAndView getChecks(@Param(value ="index") int index) {
		ModelAndView mav = new ModelAndView("settlements/allChecks");
		List<Chaque> allChecksList = facade.getAllChecks(index);
		mav.addObject("checksList",allChecksList);
		mav = facade.addSequenceVariable(index,mav,allChecksList);
		return mav ; 
	}
	
	/*
	 
	@RequestMapping(method = RequestMethod.GET , value = "/settlements/test/inject")
	public ModelAndView injectTestData() {
		this.chaqueService.injectData() ; 
		return this.success("test data added ");
	}
	*/
	
	@RequestMapping(method = RequestMethod.GET , value = "/settlements/test/load")
	public String stresTest() {
		facade.stressTest() ;
		return "succ";
	}
	
	
	
	@RequestMapping(method = RequestMethod.GET , value = "/settlements/test/sendhold")
	public ModelAndView sendTestData() {
		facade.sendOnHoldChecks(); 
		return this.success("test data sended ");
	}
	
	@RequestMapping(method = RequestMethod.GET , value = "/settlements/checks/add")
	public ModelAndView addNewCheckRequest() {
		ModelAndView mav = new ModelAndView("settlements/add");
		List<RTGSUser> usersList = facade.getUserService().getAllUsers(); 
		List<RTGSUser> usersFilteredList = new ArrayList<RTGSUser>() ; 
		for(RTGSUser user :usersList) {
			if(!user.getUsername().equalsIgnoreCase("admin")) {
				usersFilteredList.add(user);
			}
		}
		mav.addObject("banksList", usersFilteredList);
		mav.addObject("check", new Chaque());
		return mav ; 
	}
	
	@RequestMapping(method = RequestMethod.POST , value = "/settlements/checks/add")
	public ModelAndView addNewCheckResponse(@ModelAttribute Chaque chaque) {
		String result = facade.addCheck(chaque);
		if(!result.equalsIgnoreCase("ok")) {
			return failView(result); 
		}else {
		return this.success("تمت إضافة الشيك بنجاح");
		}
	}
	
	
	@RequestMapping(method = RequestMethod.GET , value = "/settlements/reports")
	public ModelAndView getSettlementReports() {
		ModelAndView mav = new ModelAndView("settlements/allreports");
		mav.addObject("reportsList",facade.getAllSettlementReports());
		return mav; 
	}
	
	@RequestMapping(method = RequestMethod.GET , value = "/settlements/reports/checks/{id}")
	public ModelAndView getSettlementReportsFromCheck(@PathVariable int id ) {
		ModelAndView mav = new ModelAndView("settlements/settledChecksList");
		mav.addObject("checksList", facade.getUserChecksFromReportID(id));
		return mav ; 
	}
	
	@RequestMapping(method = RequestMethod.GET , value = "/settlements/reports/settlementReport/{id}")
	public ModelAndView getSettlementCheck(@PathVariable int id ) {
		ModelAndView mav = new ModelAndView("settlements/SettledReportsList");
		mav.addObject("reportsList", facade.getUserSettledChecksFromReportID(id));
		return mav; 
	}
	
	
	
	private ModelAndView success(String msg ) {
		ModelAndView mav = new ModelAndView("settlements/success");
		mav.addObject("msg",msg);
		return mav ; 
	}
	
	private ModelAndView failView(String result) {
		System.out.println("res : "+result);
		ModelAndView  mav = new ModelAndView("settlements/fail");
		mav.addObject("msg", result);
		return mav ; 
	}

	
	
	
}
