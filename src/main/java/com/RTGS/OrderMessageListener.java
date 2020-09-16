package com.RTGS;

import com.RTGS.Facade.Facade;
import com.RTGS.Settlement.Chaque;
import com.RTGS.Settlement.ChecksSendingModel;
import com.RTGS.Settlement.settledChecks.SettledChaque;
import com.RTGS.security.users.RTGSUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderMessageListener {

    static final Logger logger = LoggerFactory.getLogger(OrderMessageListener.class);
      
    @Autowired 
    private Facade facade ; 
     
    
    private static boolean processing = false ; 
    
    @RabbitListener(queues = "CheckQ2")
    public void processOrderCheck(ChecksSendingModel checkSendingModel) { 
    	while(processing) {
    		//wait 
    	}
    	
    	//lock 
    	processing = true ; 
    		
    		//log
    		System.out.println("report Received: " + checkSendingModel.getSettlementReportModel().getTimestamp());
	        System.out.println("seq : "+checkSendingModel.getChecksSequence());
    		logger.info("report Received: " + checkSendingModel.getSettlementReportModel().getTimestamp());
	        
	        //process 
	        facade.recieveSettlementResultChecks(checkSendingModel);
	        
	        //release lock 
	        processing = false ; 
    }

    
    @RabbitListener(queues = "SettledChaqueQ")
    public void processOrderSettledCheck(SettledChaque settledChaque) {
    	//sync
    	while(processing) {
    		//wait
    		}
    	
    	//lock 
    	processing = true ; 
        
    	//log
    	System.out.println("Settled check recieved with info : " + settledChaque.toString());
    	System.out.println("null test : "+settledChaque.getSettlementReportModel().getTimestamp());
        logger.info("Settled check Received: " + settledChaque);
        
        //process
        facade.recieveSettlementResultReports(settledChaque);
        
        //release lock
        processing = false ; 
    }
      
    @RabbitListener(queues = "RTGSUserQ")
    public void processOrderUsers(RTGSUser rtgsUser) {
        System.out.println("check recieved with info : " + rtgsUser.toString());
        facade.recieveRTGSUsersFromMaster(rtgsUser);
        logger.info("user Received: " + rtgsUser);
    }
    
}