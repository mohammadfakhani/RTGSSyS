package com.RTGS.Settlement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.RTGS.MasterService;
import com.RTGS.OrderMessageSender;
import com.RTGS.Settlement.sequence.SequenceRepo;
import com.RTGS.Settlement.sequence.SettlementSequence;
import com.RTGS.security.users.RTGSUser;
import com.RTGS.security.users.UserService;

@Service
public class ChaqueService extends MasterService{

	@Autowired 
	private ChaqueRepository chaqueRepo ; 

	@Autowired
	private UserService userSerivce; 
	
	@Autowired 
	private OrderMessageSender orderMessageSender ; 
	
	// @Autowired 
	//private SettlementR
	
	private String[][] allBanksArray ;
	
	private int BanksArraySize = 0 ; 
	
	
	
	@Autowired
	private SequenceRepo sequenceRepo ; 
	
	private int sequenceVar = -1 ; 
	
	
	
	public List<Chaque> getAllChecks(int PageNumber){
		Pageable paging = PageRequest.of(PageNumber, 20, Sort.by("id"));
		Slice<Chaque> pagedResult = this.chaqueRepo.findBysecondBranchCode(super.get_current_User().getBranchCode(), paging);
		if (pagedResult.hasContent()) {
			return pagedResult.getContent();
		} else {
			return new ArrayList<Chaque>();
		}	
	}
	
	public String addCheck(Chaque chaque) {
		RTGSUser user =  super.get_current_User() ; 
		chaque.setSecondBankName(user.getBankName());
		chaque.setSecondBranchName(user.getBranchName());
		chaque.setSecondBranchCode(user.getBranchCode());
		initSequenceVar() ; 
		SettlementSequence sq = new SettlementSequence() ; 
		chaque.setSequenceNum(this.sequenceVar);
		sq.setSequenceNum(chaque.getSequenceNum());
		this.sequenceRepo.save(sq);
		String result = validateChaqueData(chaque) ; 
		
		if(!result.equalsIgnoreCase("ok")) {
			return result ; 
		}else {
			//change to one settlement report 
			chaque.setUserID(super.get_current_User().getId());
			chaque.setUserName(super.get_current_User().getUsername());
			try {
				this.orderMessageSender.sendOrder(chaque);
				chaque.setSent(true);
			}
			// if rabbit mq server is down the check will be stored to try sending it later 
			catch(Exception e ) {   
				chaque.setSent(false);	
			}
			this.chaqueRepo.save(chaque);
			return "ok";
		}
	}
	
	private int initSequenceVar() {
		List<SettlementSequence> sl = this.sequenceRepo.findAll() ; 
		if(sl.size() == 0 ) 
			this.sequenceVar = 0 ; 
		else 
			this.sequenceVar = sl.get(sl.size()-1).getSequenceNum()+1 ;
		return this.sequenceVar ; 
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
		
		initBanks_BranchesArray() ; 

		if(!checkBankBranchCodeMatch(check.getFirstBankName() ,check.getFirstBranchName(),check.getFirstBranchCode())) {
			return "خطأ في معلومات البنك الأول"; 
		}
		
		if(!checkBankBranchCodeMatch(check.getSecondBankName() ,check.getSecondBranchName(),check.getSecondBranchCode())) {
			return "خطأ في معلومات البنك الثاني"; 
		}

		for(Chaque tempCheck : allChecks ) {
			if(check.getCheckId() == tempCheck.getCheckId() ) {
				return "الرقم التسلسلي للشيك مدخل مسبقا";  
			}
		}	
		return "ok" ; 
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
	private void initBanks_BranchesArray(){
		List<RTGSUser> usersList = this.userSerivce.getAllUsers() ; 
		this.allBanksArray = new String[usersList.size()][3] ; 
		this.BanksArraySize  = usersList.size() ; 
		for(int i = 0 ; i < this.BanksArraySize ; i ++ ) {
			this.allBanksArray[i][0] = usersList.get(i).getBankName(); 
			this.allBanksArray[i][1] = usersList.get(i).getBranchName();
			this.allBanksArray[i][2] = usersList.get(i).getBranchCode();
		}
	}

	public void injectData() {
		/*for(int i = 0 ; i < 30 ; i ++) {
		Chaque check = new  Chaque(i, "التجاري", "المركزي", "الزراعة",
				"#combr1","دمشق", "#cbr1", 1000+i,
				MasterService.getCurrDateTime(), "admin",i, false); 
		this.chaqueRepo.save(check);
		}
		for(int i = 0 ; i < 30 ; i ++) {
			Chaque check = new  Chaque(i,"المركزي" ,"التجاري","دمشق",
					"#cbr1","الزراعة", "#combr1", 200+i,
					MasterService.getCurrDateTime(), "test",i, false); 
			this.chaqueRepo.save(check);
			}*/
	}

	
}
