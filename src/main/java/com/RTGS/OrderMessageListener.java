package com.RTGS;

import com.RTGS.Facade.Facade;
import com.RTGS.Settlement.Chaque;
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
     
    
    private boolean processing = false ; 
    
    @RabbitListener(queues = "CheckQ2")
    public void processOrderCheck(Chaque chaque) { 
    	while(processing) {
    		//wait 
    	}
    	
    	//lock 
    	processing = true ; 
    		
    		//log
    		System.out.println("Normal check recieved with info : " + chaque.toString());
	        logger.info("check Received: " + chaque);
	        System.out.println("check status "+chaque.isActive());
	        System.out.println("srModel "+chaque.getSettlementReportModel().getTimestamp());
	        
	        //process 
	        facade.recieveSettlementResultChecks(chaque);
	        
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