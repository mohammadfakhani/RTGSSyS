package com.RTGS.Settlement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.PostConstruct;

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
import com.RTGS.Settlement.settledChecks.SettledChaque;
import com.RTGS.Settlement.settledChecks.SettledChecksRepository;
import com.RTGS.Settlement.settlementReport.SettlementReportModel;
import com.RTGS.Settlement.settlementReport.SettlementReportRepository;
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

	@Autowired 
	private SettlementReportRepository settlementReportRepo ; 
	
	@Autowired 
	private SettledChecksRepository settledChecksRepo ; 
	
	private String[][] allBanksArray ;
	
	private int BanksArraySize = 0 ; 
	
	
	
	@Autowired
	private SequenceRepo sequenceRepo ; 
	
	private static int sequenceVar = -1 ; 
	
	private static boolean SequenceLock = true ; 
	
	@PostConstruct
	public void initSequence() {
		System.out.println("sequence init >>>>>>>>>>>>>>>>>>>>>>>>>>>");
		List<SettlementSequence> sl = sequenceRepo.findAll() ; 
		if(sl.size() == 0 ) {
			sequenceVar = 0 ;
			}
		else { 
			sequenceVar = sl.get(sl.size()-1).getSequenceNum()+1 ;
			}
		System.out.println("sequence value "+sequenceVar);
	}
	
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
		chaque = initSequenceVar(chaque) ; 
		
		SettlementSequence sq = new SettlementSequence() ; 
		//chaque.setSequenceNum(this.sequenceVar);
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
	

	private static synchronized Chaque initSequenceVar(Chaque check) {
		while(!SequenceLock) {
			System.out.println("waiting...");//wait
		}
		SequenceLock = false ;
		check.setSequenceNum(sequenceVar);
		sequenceVar++ ; 
		SequenceLock = true ; 
		return check ; 
	
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
		List<RTGSUser> usersList = this.userSerivce.getTestUsers() ; 
		int maxRandomizer = usersList.size() ; 
		
		for(int i = 0 ; i < 1 ; i ++) {
			//System.out.println("inject check "+i);
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
	//	System.out.println("checks injection finished ");
	}

	//add schedule  to this method 
	public void sendHoldChecks() {
		try {
		for(Chaque check : this.chaqueRepo.findBysentFalse()) {
			this.orderMessageSender.sendOrder(check);
			check.setSent(true);
			this.chaqueRepo.save(check);
		}
		System.out.println("sending process done !");
		}catch(Exception e ) {
			System.out.println("error happened when sending the checks to MSG Q ");
			e.printStackTrace();
		}
	}
	
	public List<SettlementReportModel> getAllReports(){
		return this.settlementReportRepo.findAll();
	}

	public List<Chaque> getUserChecks(int srm){
		SettlementReportModel srModel = this.settlementReportRepo.findById(srm);
		List<Chaque> srmList = this.chaqueRepo.findBysettlementReportModel(srModel);
		List<Chaque> userChecksList = new ArrayList<Chaque>(); 
		RTGSUser currentUser = super.get_current_User() ; 
		if(currentUser == null ) {
			return null ; 
		}
		for(Chaque check : srmList ) {
			if(check.getSecondBranchCode().equalsIgnoreCase(currentUser.getBranchCode()))
				userChecksList.add(check);
		}
		return userChecksList; 
	}
	
	
	List<SettledChaque> getUserSettledChecks(int srm ){
		SettlementReportModel srModel = this.settlementReportRepo.findById(srm);
		List<SettledChaque> allSrmChecks = this.settledChecksRepo.findBysettlementReportModel(srModel);
		RTGSUser currentUser = super.get_current_User() ; 
		if(currentUser == null) {
			return null ; 
		}
		List<SettledChaque> userSettledChecks = new ArrayList<SettledChaque>() ; 
		for(SettledChaque check : allSrmChecks) {
			if(check.getSecondBranchCode().equalsIgnoreCase(currentUser.getBranchCode())) {
				userSettledChecks.add(check);
				}
		}
		return userSettledChecks ;  
	}

	public void saveCheckFromMsgQ(Chaque chaque) {
			Chaque check = this.chaqueRepo.findBysequenceNum(chaque.getSequenceNum());			
			check.setActive(chaque.isActive());
			check.setSettlementReportModel(chaque.getSettlementReportModel());
			this.chaqueRepo.save(check);
	}
	
	
	public void stressTest() {
		 long startTime = System.nanoTime();
		//  System.out.println("thread started");
		  injectData();
		  long stopTime = System.nanoTime();
		  long result = stopTime - startTime ; 
		 // System.out.println("thread finished with exe time : "+result);
		  
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
