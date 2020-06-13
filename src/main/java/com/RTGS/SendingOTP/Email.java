package com.RTGS.SendingOTP;

import com.RTGS.OTPGENERATOR.OtpMethods;

public class Email extends Message{
    OtpMethods otpMethods=new OtpMethods();


    public Email(String receiver) {
        setMessageSender("fakomohamad2021@gmail.com");
        setSenderPassword("imad@1234");
        setTheSubject("رمز التحقق من مصرف سورية المركزي");
        setOTP(otpMethods.Code());
        setMessageReceiver(receiver);
    }
}
