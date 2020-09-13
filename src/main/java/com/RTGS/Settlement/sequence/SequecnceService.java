package com.RTGS.Settlement.sequence;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SequecnceService {

	@Autowired
	private SequenceRepo sequenceRepo ; 
	
	private static int sequenceVar = -1 ; 
	
	private static boolean SequenceLock = true ; 
	
	@PostConstruct
	public void initSequence() {
		System.out.println("sequence init >>>>>>>>>>>>>>>>>>>>>>>>>>>");
		List<SettlementSequence> sl = sequenceRepo.findAll() ; 
		if(sl.size() == 0 ) {
			sequenceVar = 0 ;
			}
		else { 
			sequenceVar = sl.get(sl.size()-1).getSequenceNum()+1 ;
			}
		System.out.println("sequence value "+sequenceVar);
	}
	
	
	public void saveSequence(SettlementSequence sq) {
		sequenceRepo.save(sq);
	}


	public static int getSequenceVar() {
		return sequenceVar;
	}


	public static void setSequenceVar(int sequenceVar) {
		SequecnceService.sequenceVar = sequenceVar;
	}


	public static boolean isSequenceLock() {
		return SequenceLock;
	}


	public static void setSequenceLock(boolean sequenceLock) {
		SequenceLock = sequenceLock;
	}

	
	public void addNewSequence(int sequence) {
		SettlementSequence sq = new SettlementSequence() ; 
		sq.setSequenceNum(sequence);
		sequenceRepo.save(sq);
	}
	
	
}
