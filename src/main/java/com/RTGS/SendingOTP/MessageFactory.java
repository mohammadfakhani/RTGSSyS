package com.RTGS.SendingOTP;

public class MessageFactory {

    public Message makeMessage(String MessageType,String MessageReceiver)
    {
        if(MessageType.equals("email"))
        {
            return new Email(MessageReceiver);
        }
        else
            return null;

    }
}
