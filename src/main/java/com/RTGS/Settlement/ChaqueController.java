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
		String result = this.chaqueService.addCheck(chaque);
		if(!result.equalsIgnoreCase("ok")) {
			return failView(result); 
		}else {
			//send to msgQ 
		return this.success("تمت إضافة الشيك بنجاح");
		}
	}
	
	
	@RequestMapping(method = RequestMethod.GET , value = "/settlements/test")
	public void aspectTest() {
		System.out.println("done");
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
