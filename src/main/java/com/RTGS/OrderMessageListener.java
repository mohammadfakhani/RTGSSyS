package com.RTGS;

import com.RTGS.Settlement.Chaque;
import com.RTGS.Settlement.settledChecks.SettledChaque;
import com.RTGS.security.users.RTGSUser;
import org.hibernate.annotations.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrderMessageListener {

    static final Logger logger = LoggerFactory.getLogger(OrderMessageListener.class);

    @RabbitListener(queues = "CheckQ2")
    public void processOrderCheck(Chaque chaque) {
        System.out.println("check recieved with info : " + chaque.toString());

        logger.info("Order Received: " + chaque);
    }
    @RabbitListener(queues = "RTGSUserQ")
    public void processOrderUsers(RTGSUser rtgsUser) {
        System.out.println("check recieved with info : " + rtgsUser.toString());

        logger.info("Order Received: " + rtgsUser);
    }
    @RabbitListener(queues = "SettledChaqueQ")
    public void processOrderSettledCheck(SettledChaque settledChaque) {
        System.out.println("check recieved with info : " + settledChaque.toString());

        logger.info("Order Received: " + settledChaque);
    }
}