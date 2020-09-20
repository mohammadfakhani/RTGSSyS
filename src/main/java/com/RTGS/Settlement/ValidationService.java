package com.RTGS.Settlement;

import java.util.List;

import org.springframework.stereotype.Service;

import com.RTGS.MasterService;
import com.RTGS.security.users.RTGSUser;

@Service
public class ValidationService {

	private String[][] allBanksArray ;
	
	private int BanksArraySize = 0 ; 
	
	
	public String validateCheckData(Chaque check,List<RTGSUser> usersList,Chaque prevCheck) {
		if(check.getAmount() <= 0 ) {
			return "قيمة الشيك يجب أن تكون موجبة" ;
		}
		
		if(check.getFirstBankName().equalsIgnoreCase(check.getSecondBankName())) {
			return "البنك الأول هو نفسه البنك الثاني" ; 
		}
		
		if(check.getFirstBranchCode().equalsIgnoreCase(check.getSecondBranchCode())) {
			return "الفرع الأول هو نفسه الفرع الثاني" ; 
		}
		
		initBanks_BranchesArray(usersList) ; 

		if(!checkBankBranchCodeMatch(check.getFirstBankName() ,check.getFirstBranchName(),check.getFirstBranchCode())) {
			return "خطأ في معلومات البنك الأول"; 
		}
		
		if(!checkBankBranchCodeMatch(check.getSecondBankName() ,check.getSecondBranchName(),check.getSecondBranchCode())) {
			return "خطأ في معلومات البنك الثاني"; 
		}

		if(prevCheck != null) {
			if(prevCheck.getClientAccountNumber() == check.getClientAccountNumber()) {
				if(prevCheck.getFirstBankName().equalsIgnoreCase(check.getFirstBankName())) {
					return "الشيك تم إدخاله مسبقا على النظام";
				}
			}
		}
		
		if(!checkDateValue(check)) {
			return "لا يمكن ادخال شيك بهذا التاريخ";
		}
		
		return "ok" ; 
	}
	
	
	public boolean checkDateValue(Chaque check ) {
		String currDate = MasterService.getDateAsString() ;
		int currYear =Integer.valueOf( MasterService.getYearFromStringDate(currDate));
		int CurrMonth = Integer.valueOf(MasterService.getMonthFromStringDate(currDate));
		int currDay =Integer.valueOf( MasterService.getDayFromStringDate(currDate));
		
		String checkDate = check.getLocalDateTime() ; 
		int checkYear = Integer.valueOf(MasterService.getYearFromStringDate(checkDate)); 
		int checkMonth =Integer.valueOf( MasterService.getMonthFromStringDate(checkDate)) ; 
		int checkDay = Integer.valueOf(MasterService.getDayFromStringDate(checkDate)); 
		
		if(checkYear <= currYear ) {
			if(checkMonth <= CurrMonth) {
				if(checkDay <= currDay) {
					return true ; 
				}
			}
		}
		
		return false ; 
	}
	
	
	private boolean checkBankBranchCodeMatch(String bankName , String branchName , String branchCode) {
		for(int i = 0 ; i < this.BanksArraySize ; i ++ ) {
			if(this.allBanksArray[i][0].equalsIgnoreCase(bankName)) {
				if(this.allBanksArray[i][1].equalsIgnoreCase(branchName)) {
					if(this.allBanksArray[i][2].equalsIgnoreCase(branchCode)) {
					return true;
					} 
				}
			}
		} 
		return false ; 
	}
	
	// banks Branches Pair array to resolve cross branch name conflict 
	private void initBanks_BranchesArray(List<RTGSUser> usersList){
		this.allBanksArray = new String[usersList.size()][3] ; 
		this.BanksArraySize  = usersList.size() ; 
		for(int i = 0 ; i < this.BanksArraySize ; i ++ ) {
			this.allBanksArray[i][0] = usersList.get(i).getBankName(); 
			this.allBanksArray[i][1] = usersList.get(i).getBranchName();
			this.allBanksArray[i][2] = usersList.get(i).getBranchCode();
		}
	}


	
}
