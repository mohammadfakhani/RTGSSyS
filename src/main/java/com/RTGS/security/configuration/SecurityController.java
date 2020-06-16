package com.RTGS.security.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.RTGS.security.users.Token;
import com.RTGS.security.users.UserService;

@RestController
public class SecurityController {

	@Autowired 
	private UserService userService;  
    
    @RequestMapping(method = RequestMethod.GET , value = "/tsa")
    public ModelAndView authunticateUser() {
    	ModelAndView mav = new ModelAndView("Login/twoStepAuth");
    	mav.addObject("obj", new Token());
    	return mav ; 
    }
    
    
    @RequestMapping(method = RequestMethod.POST , value = "/tsa/otp")
    public ModelAndView confirmUserToken(@ModelAttribute Token token) {
    	boolean result = userService.validateUserToken(token.getToken());
    	if(result) {//token is true
    		ModelAndView mav = new ModelAndView("Login/welcome");
        	return mav ; 
    	} else {//redirect to tsa 
    		return authunticateUser();
    	}
    }
    
}
