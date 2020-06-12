package com.RTGS;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {

	 @RequestMapping(method = RequestMethod.GET , value = "/login")
	    public ModelAndView login() {
	    	ModelAndView mav = new ModelAndView("Login/login");
	    	return mav; 
	    }
	 
	   @RequestMapping(method = RequestMethod.GET , value = "/index")
	    public ModelAndView index() {
	    	ModelAndView mav = new ModelAndView("Login/index");
	    	return mav; 
	    }
	    

}
