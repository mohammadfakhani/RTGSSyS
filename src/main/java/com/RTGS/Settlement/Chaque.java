package com.RTGS.Settlement;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.RTGS.Aspect.enc.IntEncryptDecryptConverter;
import com.RTGS.Aspect.enc.StringEncryptDecryptConverter;
import com.RTGS.Settlement.settlementReport.SettlementReportModel;

@Entity
public class Chaque implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Convert(converter = IntEncryptDecryptConverter.class)
    private int id;
    
    @Column(nullable = false )
    @Convert(converter = IntEncryptDecryptConverter.class)
    private  int checkId;
    
    @Column(nullable = false )
    @Convert(converter = StringEncryptDecryptConverter.class)
    private  String firstBankName;
    
    @Column(nullable = false )
    @Convert(converter = StringEncryptDecryptConverter.class)
    private  String secondBankName;
    
    @Column(nullable = false )
    @Convert(converter = StringEncryptDecryptConverter.class)
    private  String FirstBranchName;
    
    @Column(nullable = false )
    @Convert(converter = StringEncryptDecryptConverter.class)
    private  String FirstBranchCode;
    
    @Column(nullable = false )
    @Convert(converter = StringEncryptDecryptConverter.class)
    private  String SecondBranchName;
    
    @Column(nullable = false )
    @Convert(converter = StringEncryptDecryptConverter.class)
    private  String SecondBranchCode;
    
    @Column(nullable = false )
    //@Convert(converter = DoubleEncryptDecryptConverter.class)
    private  long Amount;
	
    @Column(nullable = false )
    private  String localDateTime ;
    
    @Column(nullable = false )
    private  String CheckEntryDate ;
    
    @Column(nullable = false )
    @Convert(converter = StringEncryptDecryptConverter.class)
    private  String UserName ; 
    
    @Column(nullable = false )
    @Convert(converter = IntEncryptDecryptConverter.class)
    private  int UserID ;

    @Column(nullable = false )
    private long ClientAccountNumber ; 

    private boolean sent = false ;

	private boolean active = false ;
	
	private int sequenceNum ; 
	
	@ManyToOne
	private SettlementReportModel settlementReportModel = null ;
	
	
	public Chaque() {
		this.CheckEntryDate = LocalDate.now().toString();
	}
	
	public Chaque(int checkId, String firstBankName, String secondBankName, String firstBranchName,
			String firstBranchCode, String secondBranchName, String secondBranchCode, long amount,
			String localDateTime, String userName, int userID, boolean active,long ClientAccountNumber) {
		super();
		this.checkId = checkId;
		this.firstBankName = firstBankName;
		this.secondBankName = secondBankName;
		FirstBranchName = firstBranchName;
		FirstBranchCode = firstBranchCode;
		SecondBranchName = secondBranchName;
		SecondBranchCode = secondBranchCode;
		Amount = amount;
		this.localDateTime = localDateTime;
		UserName = userName;
		UserID = userID;
		this.active = active;
		this.ClientAccountNumber = ClientAccountNumber ; 
		this.CheckEntryDate = LocalDate.now().toString();
	}


	public int getCheckId() {
		return checkId;
	}

	public void setCheckId(int checkId) {
		this.checkId = checkId;
	}

	public String getFirstBankName() {
		return firstBankName;
	}

	public void setFirstBankName(String firstBankName) {
		this.firstBankName = firstBankName;
	}

	public String getSecondBankName() {
		return secondBankName;
	}

	public void setSecondBankName(String secondBankName) {
		this.secondBankName = secondBankName;
	}

	public String getFirstBranchName() {
		return FirstBranchName;
	}

	public void setFirstBranchName(String firstBranchName) {
		FirstBranchName = firstBranchName;
	}

	public String getFirstBranchCode() {
		return FirstBranchCode;
	}

	public void setFirstBranchCode(String firstBranchCode) {
		FirstBranchCode = firstBranchCode;
	}

	public String getSecondBranchName() {
		return SecondBranchName;
	}

	public void setSecondBranchName(String secondBranchName) {
		SecondBranchName = secondBranchName;
	}

	public String getSecondBranchCode() {
		return SecondBranchCode;
	}

	public void setSecondBranchCode(String secondBranchCode) {
		SecondBranchCode = secondBranchCode;
	}

	public long getAmount() {
		return Amount;
	}

	public void setAmount(long amount) {
		Amount = amount;
	}

	public String getLocalDateTime() {
		return localDateTime;
	}

	public void setLocalDateTime(String localDateTime) {
		this.localDateTime = localDateTime;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}


	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}
	
	public int getId() {
		return id;
	}
	
	public int getSequenceNum() {
		return sequenceNum;
	}

	public void setSequenceNum(int sequenceNum) {
		this.sequenceNum = sequenceNum;
	}

	
	public SettlementReportModel getSettlementReportModel() {
		return settlementReportModel;
	}

	public void setSettlementReportModel(SettlementReportModel settlementReportModel) {
		this.settlementReportModel = settlementReportModel;
	}

	public long getClientAccountNumber() {
		return ClientAccountNumber;
	}

	public void setClientAccountNumber(long clientAccountNumber) {
		ClientAccountNumber = clientAccountNumber;
	}
	
	public String getCheckEntryDate() {
		return CheckEntryDate;
	}

	public void setCheckEntryDate(String checkEntryDate) {
		CheckEntryDate = checkEntryDate;
	}

	@Override 
	public String toString() {
		return "check id : "+this.checkId + "first bank name  : "+this.firstBankName+
				"first branch name : "+this.FirstBranchName +"fist branch code : "+this.FirstBranchCode
				+" second bank name : "+this.secondBankName+" second branch name : "+this.SecondBranchName 
				+" second branch code : "+this.SecondBranchCode+" check ammount : "+this.Amount ; 
	}


}