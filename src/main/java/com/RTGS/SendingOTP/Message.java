package com.RTGS.SendingOTP;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public abstract class Message {

    private String messageSender;
    private String messageReceiver;
    private String SenderPassword;
    private String theSubject;
    private String OTP;

    public String getTheSubject() {
        return theSubject;
    }

    public void setTheSubject(String theSubject) {
        this.theSubject = theSubject;
    }

    public String getMessageSender() {
        return messageSender;
    }

    public void setMessageSender(String messageSender) {
        this.messageSender = messageSender;
    }

    public String getMessageReceiver() {
        return messageReceiver;
    }

    public void setMessageReceiver(String messageReceiver) {
        this.messageReceiver = messageReceiver;
    }

    public String getSenderPassword() {
        return SenderPassword;
    }

    public void setSenderPassword(String senderPassword) {
        SenderPassword = senderPassword;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public void sendMessage() throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(messageSender, SenderPassword);
            }
        });
        javax.mail.Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(messageSender, false));
        msg.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(messageReceiver));
        msg.setSubject(theSubject);
        msg.setContent(OTP, "text/html");
        msg.setSentDate(new Date());
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(OTP, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        Transport.send(msg);
    }








}
