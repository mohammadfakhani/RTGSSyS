package com.RTGS.Aspect.Model;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.RTGS.Aspect.Model.exceptions.UnAuthenticatedException;
import com.RTGS.security.users.User;
import com.RTGS.security.users.UserRepository;

@Aspect
@Component
@EnableAspectJAutoProxy
public class AspectModel {
	
	@Autowired
	private UserRepository userRepo ; 

	
	
	@Before("execution(* com.RTGS.Settlement.ChaqueController..*(..)))")
	public void secureUserService(JoinPoint  proceedingJoinPoint)  {
			System.out.println("intercepting user Controller methods ");
			printFunctionCallInfo(proceedingJoinPoint);
			User user = get_current_User();   
			if(!checkUserTokenState(user)) {
				throw new UnAuthenticatedException();
			}
	}
	
	
	
	private User get_current_User() {
		String username ; 
    	 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        Object principal =  auth.getPrincipal();
	        if(principal instanceof UserDetails) {
	        	 username = ((UserDetails) principal).getUsername() ; 
		         for(User user : this.userRepo.findAll()) {
		 			if(user.getUsername().equalsIgnoreCase(username)) {
		 				return user ; 
		 			}
		 		}
	        }
	        else {
	        	System.out.println((String)principal + "attempted route access ");
	        	System.out.println("Access intercepted from Aspect");
	        	throw new UnAuthenticatedException();
	        }
	         return null  ; 
    }
	
	private boolean checkUserTokenState(User user ) {
		if(user.isTokenEntered()) {
			return true ;
		}
		return false ; 
	}
	
	private String printFunctionCallInfo(JoinPoint  proceedingJoinPoint) {
		MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        String[] param = methodSignature.getParameterNames();
       String out = "excution request for : " + className + "." + methodName ; 
        System.out.println("excution request for : " + className + "." + methodName );
        for(String parameter : param ) {
        	out+="with parameter : "+parameter ; 
        	System.out.println("with parameter : "+parameter);
        }
        return out ; 
	}
	
}
