package com.RTGS.Settlement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.RTGS.security.users.UserService;

@Service
public class ChaqueService {

	@Autowired 
	private ChaqueRepository chaqueRepo ; 

	@Autowired
	private UserService userSerivce; 
	
	public List<Chaque> getAllChecks(int PageNumber){
		Pageable paging = PageRequest.of(PageNumber, 20, Sort.by("id"));
		Page<Chaque> pagedResult = this.chaqueRepo.findAll(paging);
		if (pagedResult.hasContent()) {
			return pagedResult.getContent();
		} else {
			return new ArrayList<Chaque>();
		}
	}
	
	public String addCheck(Chaque chaque) {
		String result = validateChaqueData(chaque) ; 
		if(!result.equalsIgnoreCase("ok")) {
			return result ; 
		}else {
		this.chaqueRepo.save(chaque);
		return "ok";
		}
	}
	
	private String validateChaqueData(Chaque check) {
		List<Chaque> allChecks = this.chaqueRepo.findAll() ; 
		if(check.getAmount() <= 0 ) {
			return "قيمة الشيك يجب أن تكون موجبة" ;
		}
		
		if(check.getFirstBankName().equalsIgnoreCase(check.getSecondBankName())) {
			return "البنك الأول هو نفسه البنك الثاني" ; 
		}
		
		if(check.getFirstBranchCode().equalsIgnoreCase(check.getSecondBranchCode())) {
			return "الفرع الأول هو نفسه الفرع الثاني" ; 
		}
		
		
		List<String> allBanks = this.userSerivce.getSettlementBanks() ; 
		List<String> allBranches = this.userSerivce.getSettlementBranches() ; 

		//check the first bank data 
		if(!allBanks.contains(check.getFirstBankName())){
			return "البنك الأول غير مسجل على النظام"; 
		}
		//check branch 
		if(!allBranches.contains(check.getFirstBranchName())) {
			return "الفرع الأول غير موجود";
		}
		
		
		//check the second bank data 
		 
		if(!allBanks.contains(check.getSecondBankName()) ) {
			return "البنك الثاني غير موجود" ; 
		}
		
		//check branch
		if(!allBranches.contains(check.getSecondBranchName())) {
			return "الفرع الثاني غير موجود" ;
		}
		
		for(Chaque tempCheck : allChecks ) {
			if(check.getCheckId() == tempCheck.getCheckId() ) {
				return "الرقم التسلسلي للشيك مدخل مسبقا";  
			}
		}	
		return "ok" ; 
	}
	
}
