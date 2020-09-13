package com.RTGS.Facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.RTGS.MasterService;
import com.RTGS.OrderMessageSender;
import com.RTGS.Settlement.Chaque;
import com.RTGS.Settlement.ChaqueService;
import com.RTGS.Settlement.ValidationService;
import com.RTGS.Settlement.sequence.SequecnceService;
import com.RTGS.Settlement.settledChecks.SettledChaque;
import com.RTGS.Settlement.settlementReport.SettlementReportModel;
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
	
	
	
	
	public String addCheck(Chaque chaque) {
			RTGSUser user =  masterService.get_current_User() ; 
			chaqueService.setCheckUserData(chaque,user.getBankName(),user.getBranchName(),user.getBranchCode(),user.getId(),user.getUsername());
			chaque = initSequenceVar(chaque) ; 
			sequenceService.addNewSequence(chaque.getSequenceNum());
			String result = validationService.validateCheckData(chaque,userService.getAllUsers(),chaqueService.findByCheckID(chaque.getCheckId()));
			if(result.equalsIgnoreCase("ok")) {
				try {
				orderMessageSender.sendOrder(chaque);
				chaque.setSent(true);
				}catch(Exception e ){
					chaque.setSent(false);
				}
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
		return chaqueService.getAllReports();
	}
	
	public List<Chaque> getUserChecksFromReportID(int reportId){
		return chaqueService.getUserChecks(reportId);
	}
	
	public List<SettledChaque> getUserSettledChecksFromReportID(int reportID){
		return chaqueService.getUserSettledChecks(reportID);
	}
	
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
	
	
	
}
