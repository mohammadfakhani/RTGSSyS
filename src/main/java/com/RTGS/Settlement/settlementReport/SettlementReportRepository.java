package com.RTGS.Settlement.settlementReport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettlementReportRepository extends JpaRepository<SettlementReportModel,Integer>{
	public SettlementReportModel findById(int id );
	
	public SettlementReportModel findBytimestamp(String timeStamp);
	
}
