package com.RTGS.SendingOTP;

import com.RTGS.OTPGENERATOR.OtpMethods;

public class Email extends Message{
    OtpMethods otpMethods=new OtpMethods();


    public Email(String receiver) {
        setMessageSender("fakomohamad2021@gmail.com");
        setSenderPassword("Asdfg12f");
        setTheSubject("رمز التحقق من مصرف سورية المركزي");
        setOTP(otpMethods.Code());
        setMessageReceiver(receiver);
    }
}
