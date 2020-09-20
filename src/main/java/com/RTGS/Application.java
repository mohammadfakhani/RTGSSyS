package com.RTGS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.mail.MessagingException;
import java.io.IOException;

@SpringBootApplication()
@EnableScheduling
public class Application {

    public static void main(String[] args) throws IOException, MessagingException {
        SpringApplication.run(Application.class, args);
    }
}
