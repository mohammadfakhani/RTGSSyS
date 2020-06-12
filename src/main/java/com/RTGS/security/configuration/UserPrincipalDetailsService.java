package com.RTGS.security.configuration;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.RTGS.MasterService;
import com.RTGS.security.users.User;
import com.RTGS.security.users.UserRepository;


@Service
public class UserPrincipalDetailsService extends MasterService implements UserDetailsService {

	private UserRepository userRepository ; 
	
	public UserPrincipalDetailsService(UserRepository userRepository ){
		this.userRepository = userRepository ; 
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.userRepository.findByUsername(username) ;
		if(user == null ) {
			user = new User();
		}else {
			//super.activityService.addActivityLog(user.getId(),user.getUsername() , "Login");
		}
		UserPrincipal userPrincipal = new UserPrincipal(user) ;  
		
		return userPrincipal;
	}

	
	
}
