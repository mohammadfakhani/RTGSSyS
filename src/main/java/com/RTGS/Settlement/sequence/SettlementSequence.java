package com.RTGS.Settlement.sequence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SettlementSequence {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int id;
	
	private int sequenceNum ;

	public SettlementSequence() {}
	
	public SettlementSequence(int sequenceNum) {
		this.sequenceNum = sequenceNum ;
	}
	
	public int getSequenceNum() {
		return sequenceNum;
	}

	public void setSequenceNum(int sequenceNum) {
		this.sequenceNum = sequenceNum;
	} 
	
	

}

