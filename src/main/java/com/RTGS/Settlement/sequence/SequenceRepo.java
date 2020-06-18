package com.RTGS.Settlement.sequence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository 
public interface SequenceRepo  extends JpaRepository<SettlementSequence,Integer>{

	
}
