package com.RTGS.OTPGENERATOR;

import java.util.Random;

public class OtpMethods implements OTPGENERATE {
    @Override
    public String Code() {
        long code   =(long)((Math.random()*9*Math.pow(10,15))+Math.pow(10,15));
        String unique_password="";
        for (long i=code;i!=0;i/=100)//a loop extracting 2 digits from the code
        {
            long digit=i%100;//extracting two digits
            if (digit<=90)
                digit=digit+32;
            //converting those two digits(ascii value) to its character value
            char ch=(char) digit;
            // adding 32 so that our least value be a valid character
            unique_password=ch+unique_password;//adding the character to the string
        }
        return unique_password;
    }

    @Override
    public char[] Code2(int len) {
        String numbers = "0123456789";

        // Using random method
        Random rndm_method = new Random();

        char[] otp = new char[len];

        for (int i = 0; i < len; i++)
        {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        return otp;
    }
}
