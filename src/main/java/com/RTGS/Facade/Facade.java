package com.RTGS.Facade;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.RTGS.MasterService;
import com.RTGS.OrderMessageSender;
import com.RTGS.Settlement.Chaque;
import com.RTGS.Settlement.ChaqueService;
import com.RTGS.Settlement.ChecksSendingModel;
import com.RTGS.Settlement.ValidationService;
import com.RTGS.Settlement.sequence.SequecnceService;
import com.RTGS.Settlement.settledChecks.SettledChaque;
import com.RTGS.Settlement.settledChecks.SettledChecksService;
import com.RTGS.Settlement.settlementReport.SettlementReportModel;
import com.RTGS.Settlement.settlementReport.SettlementReportsService;
import com.RTGS.security.users.RTGSUser;
import com.RTGS.security.users.UserService;

@Service
public class Facade {

	@Autowired
	private SequecnceService sequenceService;  
	
	@Autowired
	private ChaqueService chaqueService ; 
	
	@Autowired
	private MasterService masterService ; 
	
	@Autowired 
	private ValidationService validationService ;
	
	@Autowired
	private UserService userService ; 
	
	@Autowired
	private OrderMessageSender orderMessageSender ; 
	
	@Autowired
    private SettlementReportsService reportsService ;
	
	@Autowired
	private SettledChecksService settledChaqueService ; 
	
	@Autowired
	private SettlementReportsService settlementReportsService ;  
	
	//Services Flow
	
	public String addCheck(Chaque chaque) {
			RTGSUser user =  masterService.get_current_User() ; 
			chaqueService.setCheckUserData(chaque,user.getBankName(),user.getBranchName(),user.getBranchCode(),user.getId(),user.getUsername());
			chaque = initSequenceVar(chaque) ; 
			String result = validationService.validateCheckData(chaque,userService.getAllUsers(),chaqueService.findByCheckID(chaque.getCheckId()));
			if(result.equalsIgnoreCase("ok")) {
				try {
				orderMessageSender.sendOrder(chaque);
				chaque.setSent(true);
				}catch(Exception e ){
					chaque.setSent(false);
				}
				sequenceService.addNewSequence(chaque.getSequenceNum());
				chaqueService.addCheck(chaque);
			}
		return result ; 
	}
	
	@Scheduled(fixedRate = 600000)
	public void sendOnHoldChecks() {
		List<Chaque> obHoldChecks = chaqueService.getOnHoldChecks();
		try {
			for(Chaque check : obHoldChecks) {
				this.orderMessageSender.sendOrder(check);
				check.setSent(true);
				chaqueService.addCheck(check);
			}
			System.out.println("sending process done !");
			}catch(Exception e ) {
				System.out.println("error happened when sending the checks to MSG Q ");
			}
	}

	public void recieveSettlementResultReports(SettledChaque settledChaque) {
		SettlementReportModel settlementReportFromService = reportsService.addSettlementModel(settledChaque.getSettlementReportModel());
		settledChaque.setSettlementReportModel(settlementReportFromService);
		settledChaqueService.saveSettledCheck(settledChaque);
	}
	
	public void recieveSettlementResultChecks(ChecksSendingModel checkSendingModel) {
		SettlementReportModel settlementReportFromService = reportsService.addSettlementModel(checkSendingModel.getSettlementReportModel());
		System.out.println("TS : "+checkSendingModel.getSettlementReportModel().getTimestamp());
		System.out.println("Sequence : "+checkSendingModel.getChecksSequence());
		for(String sequenceNumber : checkSendingModel.getChecksSequence().split(",")) {
			Chaque check = this.chaqueService.findBySequenceNumber(Integer.valueOf(sequenceNumber));
			if(check != null ) {
				check.setSettlementReportModel(settlementReportFromService);
				check.setActive(true);
				chaqueService.saveCheckFromMsgQ(check);
			}
		}
        
		
	}
	
	public void recieveRTGSUsersFromMaster(RTGSUser rtgsUser) {
		userService.addUserFromMaster(rtgsUser);
	}
	
	public List<Chaque> getUserChecks(int settlementReportModelID){
		
		 SettlementReportModel srModel = settlementReportsService.findById(settlementReportModelID);
		
		List<Chaque> srmList = chaqueService.findBySettlementReportModel(srModel);
		
		List<Chaque> userChecksList = new ArrayList<Chaque>(); 
		
		RTGSUser currentUser = masterService.get_current_User(); 
		
		if(currentUser == null ) {
			return null ; 
		}
		for(Chaque check : srmList ) {
			if(check.getSecondBranchCode().equalsIgnoreCase(currentUser.getBranchCode()))
				userChecksList.add(check);
		}
		return userChecksList;
	}
	
	public List<SettledChaque> getUserSettledChecks(int settlementReportModelID ){
		
		SettlementReportModel srModel = settlementReportsService.findById(settlementReportModelID);
		
		List<SettledChaque> allSrmChecks = settledChaqueService.findBysettlementReportModel(srModel);
		
		RTGSUser currentUser = masterService.get_current_User() ; 
		
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

	
	
	//Operational 
	public List<Chaque> getAllChecks(int index ){
		return chaqueService.getAllChecks(index);
		
	}
	
	public ModelAndView addSequenceVariable(int index,ModelAndView mav ,List<Chaque> allChecksList) {
		if(allChecksList.size() > 0 ) {
			MasterService.addSequesnceVaraibles(mav, index);
		}else {
			MasterService.addSequesnceVaraibles(mav, -1);
		}
		return mav ; 
	}
	
	public List<SettlementReportModel> getAllSettlementReports(){
		return settlementReportsService.getAllReports();
	}
	
	public List<Chaque> getUserChecksFromReportID(int reportId){
		return chaqueService.getUserChecks(reportId);
	}
	
	public List<SettledChaque> getUserSettledChecksFromReportID(int reportID){
		return chaqueService.getUserSettledChecks(reportID);
	}
	
	
	
	//synchronized
	private static synchronized Chaque initSequenceVar(Chaque check) {
			while(!SequecnceService.isSequenceLock()) {
				System.out.println("waiting...");//wait
			}
			SequecnceService.setSequenceLock(false);
			check.setSequenceNum(SequecnceService.getSequenceVar());
			int seqVar = SequecnceService.getSequenceVar() ; 
			seqVar++ ; 
			SequecnceService.setSequenceVar(seqVar); 
			SequecnceService.setSequenceLock(true);
			return check ; 
		}

	
	
	
	
	
	//getters 
	
	public SequecnceService getSequenceService() {
		return sequenceService;
	}

	public ChaqueService getChaqueService() {
		return chaqueService;
	}

	public ValidationService getValidationService() {
		return validationService;
	}

	public UserService getUserService() {
		return userService;
	}

	public SettlementReportsService getReportsService() {
		return reportsService;
	}

	public SettledChecksService getSettledChaqueService() {
		return settledChaqueService;
	}

	public SettlementReportsService getSettlementReportsService() {
		return settlementReportsService;
	}

	/////test section 

	public void stressTest() {
		 long startTime = System.nanoTime();
		  System.out.println("process started");
		  injectData();
		  long stopTime = System.nanoTime();
		  long result = stopTime - startTime ; 
		  System.out.println("process finished with exe time : "+result);
	}
	
	
	public void injectData() {
		
		List<RTGSUser> usersList = userService.getTestUsers() ; 
		int maxRandomizer = usersList.size() ; 
		
		for(int i = 0 ; i < 50 ; i ++) {
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
					, false,0000001+i);
			check = initSequenceVar(check) ; 
			sequenceService.addNewSequence(check.getSequenceNum());
			check.setCheckId(check.getSequenceNum());
			orderMessageSender.sendOrder(check);
			check.setSent(true);
			chaqueService.addCheck(check);
		}
		System.out.println("checks injection finished ");
	}

	
	
	
}
