package com.RTGS;


import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.RTGS.security.users.RTGSUser;
import com.RTGS.security.users.UserRepository;


@Service
public class MasterService {

	@Autowired
	protected UserRepository userRepo; 

	
	public MasterService(){
	 
	}
	
	public RTGSUser get_current_User() {
		String username ; 
    	 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        Object principal =  auth.getPrincipal();
	        if(principal instanceof UserDetails) {
	        	 username = ((UserDetails) principal).getUsername() ; 
		         for(RTGSUser user : this.userRepo.findAll()) {
		 			if(user.getUsername().equalsIgnoreCase(username)) {
		 				return user ; 
		 			}
		 		}
	        }
	         return null  ; 
    }
	
	public static LocalDateTime getCurrDateTime() {
		   //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		   LocalDateTime now = LocalDateTime.now();  
		   //System.out.println("current request time :"+dtf.format(now));
		   return now ; 
	}
	
	public static LocalDate getCurrDate() {
		   //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		   LocalDate now = LocalDate.now();  
		   //System.out.println("current request time :"+dtf.format(now));
		   return now ; 
	}
	
	public static String getDateAsString() {
		LocalDate now = LocalDate.now();  
		return now.toString();
	}
	
	public static String getDateTimeAsString() {
		LocalDateTime now = LocalDateTime.now();  
		return now.toString();
	}
	
	
	public static String getYearFromStringDate(String date) {
		LocalDate desiredDate = LocalDate.parse(date);
		int year = desiredDate.getYear(); 
		return String.valueOf(year) ; 
	}
	
	public static String getMonthFromStringDate(String date) {
		LocalDate desiredDate = LocalDate.parse(date);
		int month = desiredDate.getMonthValue(); 
		return String.valueOf(month) ; 
	}
	
	
	public static ModelAndView sendGeneralError(String errormsg){
		ModelAndView mav = new ModelAndView("Errors/generalError");
		mav.addObject("msg", errormsg);
		return mav ; 
	}
	
	
	public static ModelAndView sendSuccessMsg(String msg){
		ModelAndView mav = new ModelAndView("success/sucMsg");
		mav.addObject("msg", msg);
		return mav ; 
	}

	public static void addSequesnceVaraibles(ModelAndView mav,int index) {
		index++ ;
    	mav.addObject("nxt",index);
    	int prev = index - 2 ;
    	if(prev < 0 ) {
    		prev = 0 ; 
    	}
    	mav.addObject("prev",prev);
	}

	
}
