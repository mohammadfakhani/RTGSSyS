package com.RTGS.Settlement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
		mav.addObject("check", new Chaque());
		mav.addObject("banksList", facade.getBanksList());
		return mav ; 
	}
	
	@RequestMapping(method = RequestMethod.POST , value = "/settlements/checks/add")
	public ModelAndView addNewCheckResponse(@ModelAttribute Chaque chaque) {
		chaque = extractBankBranch(chaque);
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
	
	
	
	@RequestMapping(method = RequestMethod.POST , value = "/settlements/Search")
	public ModelAndView searchCheckByCheckNumber(@RequestParam("search") String checkNumber) {
		ModelAndView mav = new ModelAndView("settlements/searchCheck");
		List<Chaque> checksList = facade.getChaqueService().findByCheckID(Integer.valueOf(checkNumber));
		List<Chaque> filteredList = new ArrayList<Chaque>();
		RTGSUser user = facade.getMasterService().get_current_User();
		for(Chaque check : checksList){
			if(check.getSecondBankName().equalsIgnoreCase(user.getBankName()))
				if(check.getSecondBranchName().equalsIgnoreCase(user.getBranchName())) {
					filteredList.add(check);
				}
		}
		mav.addObject("checksList", filteredList);
		return mav ; 
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

	
	
	
	
	
	private Chaque extractBankBranch(Chaque check) {
		String bankName ="";
		String branchName ="";
		boolean dot = false ; 
		for(Character c : check.getFirstBranchName().toCharArray()) {
			if(c.equals('.')){
				dot = true ;	
				continue;
			}
			if(!dot) {
				bankName +=c ; 
			}else {
				branchName+=c ; 
			}
		}
		check.setFirstBankName(bankName);
		check.setFirstBranchName(branchName);
		return check ;
	}
	
	
}
