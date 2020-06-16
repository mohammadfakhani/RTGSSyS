package com.RTGS.security.configuration;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.RTGS.MasterService;
import com.RTGS.SendingOTP.Message;
import com.RTGS.SendingOTP.MessageFactory;
import com.RTGS.security.users.RTGSUser;
import com.RTGS.security.users.UserRepository;


@Service
public class UserPrincipalDetailsService extends MasterService implements UserDetailsService {

	private UserRepository userRepository ; 
	
	public UserPrincipalDetailsService(UserRepository userRepository ){
		this.userRepository = userRepository ; 
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		RTGSUser user = this.userRepository.findByUsername(username) ;
		if(user == null ) {
			user = new RTGSUser();
		}else {
			System.out.println("login success proceeding to two step Auth");
			try {
				 MessageFactory messageFactory = new MessageFactory();
			        Message message =null;
			        message =messageFactory.makeMessage("email",user.getEmail());
			        System.out.println("msg sent to "+message.getMessageReceiver()+" from : "+message.getMessageSender()+" with OTP : "+message.getOTP()+"\n");
			        //message.sendMessage();
			        user.setLastCode(message.getOTP());
			        user.setTokenEntered(false);
			        this.userRepo.save(user);
			}catch(Exception e ) {
				System.out.println("OPT attemp faild ");
			}
		}
		UserPrincipal userPrincipal = new UserPrincipal(user) ;  
		return userPrincipal;
	}

	
	
}
