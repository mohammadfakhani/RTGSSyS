package com.RTGS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.mail.MessagingException;
import java.io.IOException;

@SpringBootApplication()
public class Application {

    public static void main(String[] args) throws IOException, MessagingException {
        SpringApplication.run(Application.class, args);
      /*  MessageFactory messageFactory = new MessageFactory();
        Message message =null;

        message =messageFactory.makeMessage("email","fakomohamad@gmail.com");
        System.out.println(message.getMessageReceiver()+message.getMessageSender()+message.getSenderPassword()+message.getOTP()+"\n");
        message.sendMessage();
        */

    }
}
