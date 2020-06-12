package Sender;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Chaque implements Serializable {
    public int id;
    public int CheckId;
    public String FirstBank;
    public int FirstBankSW;
    public String SecondBank;
    public int SecondBankSW;
    public double Amount;

    public Chaque() {

    }

    private LocalDateTime localDateTime ;
    private boolean active = false ;


    public Chaque(int checkId, String firstBank, int firstBankSW, String secondBank, int secondBankSW, double amount) {
        CheckId = checkId;
        FirstBank = firstBank;
        FirstBankSW = firstBankSW;
        SecondBank = secondBank;
        SecondBankSW = secondBankSW;
        Amount = amount;
    }

    public int getCheckId() {
        return CheckId;
    }

    public void setCheckId(int checkId) {
        CheckId = checkId;
    }

    public String getFirstBank() {
        return FirstBank;
    }

    public void setFirstBank(String firstBank) {
        FirstBank = firstBank;
    }

    public int getFirstBankSW() {
        return FirstBankSW;
    }

    public void setFirstBankSW(int firstBankSW) {
        FirstBankSW = firstBankSW;
    }

    public String getSecondBank() {
        return SecondBank;
    }

    public void setSecondBank(String secondBank) {
        SecondBank = secondBank;
    }

    public int getSecondBankSW() {
        return SecondBankSW;
    }

    public void setSecondBankSW(int secondBankSW) {
        SecondBankSW = secondBankSW;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }


//    @Override
//    public String toString() {
//        return "Order{" +
//                "orderNumber='" + orderNumber + '\'' +
//                ", productId='" + productId + '\'' +
//                ", amount=" + amount +
//                '}';
//    }


}