package com.RTGS.Settlement.settledChecks;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RTGS.Settlement.settlementReport.SettlementReportModel;

@Service
public class SettledChecksService {
	
	@Autowired 
    private SettledChecksRepository settledChecksRepo ;
	
	
	public void saveSettledCheck(SettledChaque settledChaque) {
		this.settledChecksRepo.save(settledChaque);
	}
	
	public List<SettledChaque> findBysettlementReportModel(SettlementReportModel srModel){
		return settledChecksRepo.findBysettlementReportModel(srModel);
	}
	
	public List<SettledChaque> findAll(){
		return this.settledChecksRepo.findAll();
	}
	
}
