package com.RTGS.security.users;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.RTGS.MasterService;


@RestController
public class UserController extends MasterService {

	@Autowired
	UserService userService ; 
	
	public UserController() { 
	}
	
	///all users ///
	@RequestMapping(method = RequestMethod.GET , value = "/adminstration/users/all")
	public ModelAndView getAllUsers() {  
		ModelAndView mav = new ModelAndView("User/AllUsers");
		List<RTGSUser> usersList =  this.userService.getAllUsers() ; 
		mav.addObject("userslist",usersList);
		return mav ; 
	}
		

	@RequestMapping(method = RequestMethod.GET , value = "/adminstration/users/active")
	public ModelAndView getActiveUsers() {
		ModelAndView mav = new ModelAndView("User/activeUsers");
		mav.addObject("userslist",this.userService.getActiveUsers());
		return mav ; 
	}
	
	@RequestMapping(method = RequestMethod.POST , value = "/adminstration/users/user/deactivate/{userid}")
	public void deActivateUser(@PathVariable int userid , HttpServletResponse response) throws IOException {
		this.userService.deActivateUser(userid);
		response.sendRedirect("/adminstration/users/active");
	}
	
	@RequestMapping(method = RequestMethod.GET , value = "/adminstration/users/nonactive")
	public ModelAndView getNonActiveUsers() {
		ModelAndView mav = new ModelAndView("User/nonActiveUsers");
		mav.addObject("userslist",this.userService.getNonActiveUsers());
		return mav ; 
	}
	
	@RequestMapping(method = RequestMethod.POST , value = "/adminstration/users/user/activate/{userid}")
	public void activateUser(@PathVariable int userid , HttpServletResponse response) throws IOException {
		this.userService.activateUser(userid);
		response.sendRedirect("/adminstration/users/nonactive");
	}
	
	
	@RequestMapping(method = RequestMethod.GET , value = "/adminstration/users/inject")
	public void injectUser() {
		this.userService.injectUsers();
	}
	
	@RequestMapping(method = RequestMethod.GET , value = "/test")
	public void test() {
		if(super.get_current_User() == null ) {
			System.out.println("null user ");
		}
		else 
			System.out.println("user detected with :"+super.get_current_User().getUsername());
	}
	
	

	
}