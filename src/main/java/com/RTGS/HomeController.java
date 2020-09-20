package com.RTGS;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {
	 	
	 	@RequestMapping(method = RequestMethod.GET , value = "/index")
	    public ModelAndView welcome() {
	    	ModelAndView mav = new ModelAndView("Login/welcome");
	    	return mav; 
	    }
	 	
	 	@RequestMapping(method = RequestMethod.GET , value = "/")
	    public ModelAndView welcomeIndex() {
	    	ModelAndView mav = new ModelAndView("Login/welcome");
	    	return mav; 
	    }
   
}
