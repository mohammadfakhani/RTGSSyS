package com.RTGS;

import com.RTGS.Settlement.Chaque;
import com.RTGS.Settlement.ChaqueRepository;
import com.RTGS.Settlement.settledChecks.SettledChaque;
import com.RTGS.Settlement.settledChecks.SettledChecksRepository;
import com.RTGS.Settlement.settlementReport.SettlementReportRepository;
import com.RTGS.security.users.RTGSUser;
import com.RTGS.security.users.UserRepository;

import org.hibernate.annotations.Check;
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
    private ChaqueRepository checkRepo ; 
    
    @Autowired
    private SettlementReportRepository reportsRepo ; 
    
    @Autowired 
    private SettledChecksRepository settledChecksRepo ; 
    
    @RabbitListener(queues = "CheckQ2")
    public void processOrderCheck(Chaque chaque) {
        System.out.println("check recieved with info : " + chaque.toString());
        this.reportsRepo.save(chaque.getSettlementReportModel());
        this.checkRepo.save(chaque);
        logger.info("check Received: " + chaque);
    }
    @RabbitListener(queues = "RTGSUserQ")
    public void processOrderUsers(RTGSUser rtgsUser) {
        System.out.println("check recieved with info : " + rtgsUser.toString());
        this.userRepo.save(rtgsUser);
        logger.info("user Received: " + rtgsUser);
    }
    @RabbitListener(queues = "SettledChaqueQ")
    public void processOrderSettledCheck(SettledChaque settledChaque) {
        System.out.println("check recieved with info : " + settledChaque.toString());
        this.reportsRepo.save(settledChaque.getSettlementReportModel());
        this.settledChecksRepo.save(settledChaque);
        logger.info("Settled check Received: " + settledChaque);
    }
}