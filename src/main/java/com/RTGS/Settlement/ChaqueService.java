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
	
	
	public void injectData() {
			/*
		List<RTGSUser> usersList = this.userSerivce.getTestUsers() ; 
		int maxRandomizer = usersList.size() ; 
		
		for(int i = 0 ; i < 1 ; i ++) {
			System.out.println("inject check "+i);
			int indexfrom = ThreadLocalRandom.current().nextInt(1,maxRandomizer);
			int indexto = -1 ; 
			if(indexto == -1 ) {
				indexto = ThreadLocalRandom.current().nextInt(1,maxRandomizer);
				while(indexto == indexfrom) {
					indexto = ThreadLocalRandom.current().nextInt(1,maxRandomizer);
				}
			}
			long amount = ThreadLocalRandom.current().nextLong(100000,800000000) ; 
			Chaque check = new  Chaque(i, usersList.get(indexfrom).getBankName(),usersList.get(indexto).getBankName()
					, usersList.get(indexfrom).getBranchName(),
					usersList.get(indexfrom).getBranchCode(),usersList.get(indexto).getBranchName(),usersList.get(indexto).getBranchCode()
					,amount,
					MasterService.getDateTimeAsString(),usersList.get(indexto).getUsername(),usersList.get(indexto).getId()
					, false);
			check = initSequenceVar(check) ; 
			SettlementSequence sq = new SettlementSequence() ; 
			//check.setSequenceNum(this.sequenceVar);
			sq.setSequenceNum(check.getSequenceNum());
			sequenceRepo.save(sq);
			this.chaqueRepo.save(check);
		
		}
		System.out.println("checks injection finished ");
		*/
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

	public void saveCheckFromMsgQ(Chaque chaque) {
			Chaque check = this.chaqueRepo.findBysequenceNum(chaque.getSequenceNum());			
			check.setActive(chaque.isActive());
			check.setSettlementReportModel(chaque.getSettlementReportModel());
			this.chaqueRepo.save(check);
	}
	
	
	public void stressTest() {
		 long startTime = System.nanoTime();
		  System.out.println("thread started");
		  injectData();
		  long stopTime = System.nanoTime();
		  long result = stopTime - startTime ; 
		  System.out.println("thread finished with exe time : "+result);
		  
		/*
		for(int threadNum = 0 ; threadNum < 500 ; threadNum ++ ) {
			final int threadN = threadNum ; 
		(new Thread() {
			  public void run() {
				  long startTime = System.nanoTime();
				  System.out.println("thread "+threadN +" started");
				  injectData();
				  long stopTime = System.nanoTime();
				  long result = stopTime - startTime ; 
				  System.out.println("thread "+threadN+" finished with exe time : "+result);
			  }
			 }).start();
		}
	*/
	}
	
}
