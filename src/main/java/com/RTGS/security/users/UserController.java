package com.RTGS.security.users;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.RTGS.MasterService;


@RestController
public class UserController {

	@Autowired
	UserService userService ; 
	
	public UserController() { 
	}
	
	///all users ///
	@RequestMapping(method = RequestMethod.GET , value = "/adminstration/users/all")
	public ModelAndView getAllUsers() {  
		ModelAndView mav = new ModelAndView("User/AllUsers");
		List<User> usersList =  this.userService.getAllUsers() ; 
		mav.addObject("userslist",usersList);
		return mav ; 
	}
	
	///add new user ///
	@RequestMapping(method = RequestMethod.GET , value="/adminstration/users/adduser")
	public ModelAndView addUser() {
		ModelAndView mav = new ModelAndView("User/AddUser");
		mav.addObject("user",new User());
		return mav; 
	}
	
	//add user .//
	@RequestMapping(method = RequestMethod.POST , value="/adminstration/users/adduser")
	public ModelAndView addUser(@ModelAttribute User user)  {
		String response = this.userService.addUser(user); 
		if(response.equalsIgnoreCase("ok")) {
			return MasterService.sendSuccessMsg("تمت عملية إضافة مستخدم بنجاح"); 
		}else {
			return MasterService.sendGeneralError(response); 
		}
	}
	
	
	///update user ///	
	@RequestMapping(method = RequestMethod.GET , value="/adminstration/users/update/{id}")
	public ModelAndView updateUser(@PathVariable int id ) throws IOException {
		ModelAndView mav = new ModelAndView("User/update");
		User user = this.userService.getUserByID(id);
		mav.addObject("user",user);
		return mav ; 
	
	}
	
	@RequestMapping(method = RequestMethod.POST , value="/adminstration/users/update")
	public ModelAndView updateUser(@ModelAttribute User user) {
		String response = this.userService.updateUser(user);
		if(response.equalsIgnoreCase("ok")) {
			return MasterService.sendSuccessMsg("تم تعديل المستخدم بنجاح"); 
		}else {
			return MasterService.sendGeneralError(response);
		}
	}
	
	
	///delete User ///
	@Transactional
	@RequestMapping(method = RequestMethod.POST , value="/adminstration/users/delete/{userid}")
	public ModelAndView deleteUser(@PathVariable int userid){
		this.userService.deleteUser(this.userService.getUserByID(userid));
		return MasterService.sendSuccessMsg("تم حذف المستخدم بنجاح");
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
	
}