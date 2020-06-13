package com.RTGS.Settlement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ChaqueController {

	@Autowired 
	private ChaqueService chaqueService ; 
	
	
	
	@RequestMapping(method = RequestMethod.GET , value = "/settlements/checks/add")
	public ModelAndView addNewCheckRequest() {
		ModelAndView mav = new ModelAndView("settlements/add");
		mav.addObject("check", new Chaque());
		return mav ; 
	}
	
	@RequestMapping(method = RequestMethod.POST , value = "/settlements/checks/add")
	public ModelAndView addNewCheckResponse(@ModelAttribute Chaque chaque) {
		//this.chaqueService.addCheck(chaque);
		System.out.println(chaque.getFirstBankName());
		System.out.println(chaque);
		return this.success("check added successfully");
	}
	
	private ModelAndView success(String msg ) {
		ModelAndView mav = new ModelAndView("settlements/success");
		mav.addObject("msg",msg);
		return mav ; 
	}
	
}
