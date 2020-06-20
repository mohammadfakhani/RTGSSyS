package com.RTGS.security.users;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.RTGS.MasterService;

@Service
public class UserService extends MasterService {

	@Autowired 
	UserRepository userRepository ; 	
	
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); ;

	public UserService() {

	}
	//
	
	//all Users// 
	public List<RTGSUser> getAllUsers() {
		return this.userRepository.findAll();
	}
	
	//find user by id // 
	@Transactional
	public RTGSUser getUserByID(int id ) {
		List<RTGSUser> allUsers = this.userRepository.findAll() ; 
		if(allUsers.isEmpty()) {
			System.out.println("empty UsersList ");
			return null ;  
		}
		for(RTGSUser user : allUsers) {
			if(user.getId() == id ){
				return user  ; 
			}
		}
		System.out.println("requested user not found ");
		return null ; 
	}
	
	//find User by userName 
	public RTGSUser getUserByUserName(String userName) {
		for(RTGSUser user : this.userRepository.findAll()) {
			if(user.getUsername().equalsIgnoreCase(userName)) {
				return user ; 
			}
		}
		return null ;
	}
	
	//add new user // 
	@Transactional
	public String addUser(RTGSUser user ) {
		user.flatUserDetailes();
		if(checkUserinforDuplication(user)) {
			return "User already exist in the system";
		}
		else if(!validateUserInfo(user).equalsIgnoreCase("ok")) {
			return validateUserInfo(user); 
		}
		else {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setActive(false);
			this.userRepository.save(user);
			return "ok";
		}
		
	}
	
	private String validateUserInfo(RTGSUser user) {
		if(user.getUsername().length() < 6 || user.getUsername().length() > 20) {
			return "اسم المستخد يجب ان يكون بين 7  و 20 محرف " ; 
		}
		if(user.getPassword().length() < 8 || user.getPassword().length() > 20 ) {
			return "كلمة السر يجب ان تكون بين 8 و 20 محرف" ; 
		}
		if(!user.getGender().equalsIgnoreCase("M")) {
			if(!user.getGender().equalsIgnoreCase("F"))
			return "المستخدم يجب ان يكون ذكر أو اثنى فقط";
		}
		for(char c : user.getUsername().toCharArray()) {
			if(!Character.isAlphabetic(c) ) {
				if(!Character.isDigit(c))
				return "اسم المستخدم يحوي محارف غير مسموح بها";
			}
		}
		if(!validatePassword(user.getPassword())) {
			return "كلمة السر تحتوي على محارف غير مسموح بها";
		}
		return "ok";
	}
	
	private RTGSUser getUserBYId(int id ) {
		for(RTGSUser user : this.userRepo.findAll()) {
			if(user.getId() == id ) {
				return user ; 
			}
		}
		return null ; 
	}
	
	//update current user // 
	@Transactional
	public String updateUser(RTGSUser user) {
		String result = "";
		try {
			if(this.userRepository.findById(user.getId()) != null) {
				RTGSUser dataUser = getUserBYId(user.getId()); 
				result = validateUserInfo(user); 
				if(result.equalsIgnoreCase("ok")){
					user.setPassword(passwordEncoder.encode(user.getPassword()));
					if(dataUser.isActive())
						user.setActive(true);
					this.userRepository.save(user); 
					return "ok";
				}
			}
		}catch(Exception e ) {
			System.out.println("NullPointerException Handled at User Service / Update User -- call for null User ");
			e.printStackTrace();
		}
		return result ;  
	}
	
	//delete user//
	public void deleteUser(RTGSUser user ) {
		this.userRepository.deleteById(user.getId());
	}
	
	
	//User Info Check 
	//check if the user is currently in the system // 
	public boolean checkUserinforDuplication(RTGSUser user ) {
		List<RTGSUser> usersList = this.userRepository.findAll() ; 
		for(int i = 0 ; i < usersList.size() ; i++ ) {
			RTGSUser tempUser = usersList.get(i) ;
			if(tempUser.getUsername().equalsIgnoreCase(user.getUsername())) {
				return true ; 
			}
			if(tempUser.getEmail().equalsIgnoreCase(user.getEmail())){
				return true ; 
			}
		}
		return false ; 
	}

	//check if the password contains illegal characters 
	private boolean validatePassword(String password) {
		boolean num = false ,
				lower = false ,
				upper = false; 
		for(char c : password.toCharArray()) {
			if(Character.isLowerCase(c)) {
				lower = true ; 
			}else if(Character.isUpperCase(c)) {
				upper = true ; 
			}else if(Character.isDigit(c)) {
				num = true ; 
			}else {
				return false ; 
			}
		}
		if(num && lower && upper ) {
			return true ; 
		}
		return false ; 
	}	
	
	
	public List<RTGSUser> getNonActiveUsers() {
		List<RTGSUser> allUsers = this.userRepository.findAll() ; 
		List<RTGSUser> nonActiveUsers = new ArrayList<RTGSUser>(); 
		for(RTGSUser user : allUsers) {
			if(!user.isActive()) {
				nonActiveUsers.add(user);
			}
		}
		return nonActiveUsers; 
	}
	
	public List<RTGSUser> getActiveUsers(){
		List<RTGSUser> allUsers = this.userRepository.findAll() ; 
		List<RTGSUser> ActiveUsers = new ArrayList<RTGSUser>(); 
		for(RTGSUser user : allUsers) {
			if(user.isActive()) {
				ActiveUsers.add(user);
			}
		}
		return ActiveUsers;
	}
	
	public void activateUser(int userid) {
		RTGSUser user = this.getUserByID(userid);
		user.setActive(true);
		this.userRepository.save(user);
	}
	
	public void deActivateUser(int userid) {
		RTGSUser user = this.getUserByID(userid);
		user.setActive(false);
		this.userRepository.save(user);		
	}
	
	public void injectUsers() { 
		RTGSUser admin = new RTGSUser("mohammed_.1996@live.com",passwordEncoder.encode("admin123"),"admin","دمشق", 
				"المركزي","#cbr1","male","ACCESS_TEST1,ACCESS_TEST2","ADMIN",MasterService.getDateAsString(),true);
		this.userRepository.save(admin);
		RTGSUser b1 = new RTGSUser("tadeMail@gmail.com",passwordEncoder.encode("admin123"),"user1","الزراعة", 
				"التجاري","#combr1","male","EMPLOYEE","USER",MasterService.getDateAsString(),true);
		this.userRepository.save(b1);
		RTGSUser b2 = new RTGSUser("build@gmail.com",passwordEncoder.encode("admin123"),"user2","المزة", 
				"العقاري","#abr1","male","EMPLOYEE","USER",MasterService.getDateAsString(),true);
		this.userRepository.save(b2);
	}
	
	public List<String> getSettlementBanks(){
		List<String> allBanks = new ArrayList<String>() ; 
		for(RTGSUser user : this.userRepo.findAll()) {
			if(!allBanks.contains(user.getBankName())) {
				allBanks.add(user.getBankName());
			}
		}
		return allBanks ; 
	}
	
	public List<String> getSettlementBranches(){
		List<String> allBranches = new ArrayList<String>() ; 
		for(RTGSUser user : this.userRepo.findAll()) {
			if(!allBranches.contains(user.getBranchName())) {
				allBranches.add(user.getBranchName());
			}
		}
		return allBranches ; 
	}
	
	public boolean validateUserToken(String token) {
		RTGSUser currUser = super.get_current_User() ; 
		if(currUser.validateToken(token)) {
			currUser.setTokenEntered(true);
			this.userRepo.save(currUser);
			return true ; 
		}
		return false  ;
	}
	
}
