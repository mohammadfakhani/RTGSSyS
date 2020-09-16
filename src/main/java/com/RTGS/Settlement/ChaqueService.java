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
import com.RTGS.Facade.Facade;
import com.RTGS.Settlement.settledChecks.SettledChaque;
import com.RTGS.Settlement.settlementReport.SettlementReportModel;

@Service
public class ChaqueService extends MasterService{

	@Autowired 
	private ChaqueRepository chaqueRepo ; 
	
	@Autowired 
	private Facade facade ; 
	
	public List<Chaque> getAllChecks(int PageNumber){
		Pageable paging = PageRequest.of(PageNumber, 20, Sort.by("id"));
		Slice<Chaque> pagedResult = this.chaqueRepo.findBysecondBranchCode(super.get_current_User().getBranchCode(), paging);
		if (pagedResult.hasContent()) {
			return pagedResult.getContent();
		} else {
			return new ArrayList<Chaque>();
		}	
	}
	
	public void setCheckUserData(Chaque chaque , String secondBankName , String secondBranchName , String secondBranchCode ,int userId, String userName) {
		chaque.setSecondBankName(secondBankName);
		chaque.setSecondBranchName(secondBranchName);
		chaque.setSecondBranchCode(secondBranchCode);
		chaque.setUserID(userId);
		chaque.setUserName(userName);
	}
	
	public void addCheck(Chaque chaque) {	
		this.chaqueRepo.save(chaque);
		
	}
	
	public Chaque findByCheckID(int checkID) {
		return this.chaqueRepo.findBycheckId(checkID) ;
	}
	


	
	public List<Chaque> getOnHoldChecks(){
		return this.chaqueRepo.findBysentFalse() ; 
	}
	

	public List<Chaque> getUserChecks(int srm){
		return facade.getUserChecks(srm);
	}
	
	public List<Chaque> findBySettlementReportModel(SettlementReportModel srModel){
		return chaqueRepo.findBysettlementReportModel(srModel);
	}
	
	
	public List<SettledChaque> getUserSettledChecks(int srm ){
		return facade.getUserSettledChecks(srm);
	}
	
	public Chaque findBySequenceNumber(int sequenceNumber) {
		return this.chaqueRepo.findBysequenceNum(sequenceNumber);
	}

	public void saveCheckFromMsgQ(Chaque chaque) {
			//Chaque check = this.chaqueRepo.findBysequenceNum(chaque.getSequenceNum());			
			//check.setActive(chaque.isActive());
			//check.setSettlementReportModel(chaque.getSettlementReportModel());
			this.chaqueRepo.save(chaque);
	}

	
}
