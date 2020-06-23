package com.RTGS;

import com.RTGS.Settlement.Chaque;
import com.RTGS.Settlement.ChaqueService;
import com.RTGS.Settlement.settledChecks.SettledChaque;
import com.RTGS.Settlement.settledChecks.SettledChecksRepository;
import com.RTGS.Settlement.settlementReport.SettlementReportModel;
import com.RTGS.Settlement.settlementReport.SettlementReportsService;
import com.RTGS.security.users.RTGSUser;
import com.RTGS.security.users.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderMessageListener {

    static final Logger logger = LoggerFactory.getLogger(OrderMessageListener.class);
    
    @Autowired 
    private UserRepository userRepo ; 
    
    @Autowired 
    private ChaqueService checkService ; 
    
    @Autowired
    private SettlementReportsService reportsService ; 
    
    @Autowired 
    private SettledChecksRepository settledChecksRepo ; 
    
    private boolean processing = false ; 
    
    @RabbitListener(queues = "CheckQ2")
    public void processOrderCheck(Chaque chaque) { 
    	while(processing) {}
    	processing = true ; 
    		System.out.println("Normal check recieved with info : " + chaque.toString());
	        //logger.info("check Received: " + chaque);
	        System.out.println("check status "+chaque.isActive());
	        System.out.println("srModel "+chaque.getSettlementReportModel().getTimestamp());
	        SettlementReportModel settlementReportFromService = this.reportsService.addSettlementModel(chaque.getSettlementReportModel());
	        chaque.setSettlementReportModel(settlementReportFromService);
	        this.checkService.saveCheckFromMsgQ(chaque);
	        processing = false ; 
    }

    
    @RabbitListener(queues = "SettledChaqueQ")
    public void processOrderSettledCheck(SettledChaque settledChaque) {
    	while(processing) {}
    	processing = true ; 
        System.out.println("Settled check recieved with info : " + settledChaque.toString());
        //logger.info("Settled check Received: " + settledChaque);
        
        SettlementReportModel settlementReportFromService = this.reportsService.addSettlementModel(settledChaque.getSettlementReportModel());
        settledChaque.setSettlementReportModel(settlementReportFromService);
        this.settledChecksRepo.save(settledChaque);
        processing = false ; 
    }
      
    @RabbitListener(queues = "RTGSUserQ")
    public void processOrderUsers(RTGSUser rtgsUser) {
        System.out.println("check recieved with info : " + rtgsUser.toString());
        this.userRepo.save(rtgsUser);
        logger.info("user Received: " + rtgsUser);
    }
    
}