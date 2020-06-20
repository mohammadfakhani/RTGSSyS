package com.RTGS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrderMessageListener {

    static final Logger logger = LoggerFactory.getLogger(OrderMessageListener.class);

    @RabbitListener(queues = "CheckQ2")
    public void processOrderCheck(Object object) {
        System.out.println("check recieved with info : " + object);

        logger.info("Order Received: " + object);
    }
    @RabbitListener(queues = "RTGSUserQ")
    public void processOrderUsers(Object object) {
        System.out.println("check recieved with info : " + object);

        logger.info("Order Received: " + object);
    }
    @RabbitListener(queues = "SettledChaqueQ")
    public void processOrderSettledCheck(Object object) {
        System.out.println("check recieved with info : " + object);

        logger.info("Order Received: " + object);
    }
}