package com.RTGS.Settlement.settlementReport;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service 
public class SettlementReportsService {

	
	@Autowired 
	private SettlementReportRepository settlementReportsRepo ;
	
	public SettlementReportModel addSettlementModel(SettlementReportModel settlementReportModel ) {
		if(this.settlementReportsRepo.findBytimestamp(settlementReportModel.getTimestamp()) == null ) {
			this.settlementReportsRepo.save(settlementReportModel); 
		}else {
			settlementReportModel = this.settlementReportsRepo.findBytimestamp(settlementReportModel.getTimestamp());
		}
		return settlementReportModel ; 
	}
	
	public List<SettlementReportModel> getAllReports(){
		return this.settlementReportsRepo.findAll();
	}

	
	public SettlementReportModel findById(int settlementReportModelID) {
		return settlementReportsRepo.findById(settlementReportModelID);
	}

	
}
