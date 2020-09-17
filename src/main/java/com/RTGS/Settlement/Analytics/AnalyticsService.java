package com.RTGS.Settlement.Analytics;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RTGS.MasterService;
import com.RTGS.Facade.Facade;
import com.RTGS.Settlement.Chaque;
import com.RTGS.Settlement.settledChecks.SettledChaque;

@Service
public class AnalyticsService {

		
	@Autowired
	private Facade facade ; 

	private LocalDate localDate = MasterService.getCurrDate();
		
	
	public AnalyticsModel analyticsSequence() {
		//variables
		this.localDate = MasterService.getCurrDate();
		boolean endOfData = false ; 
		int index = 0 ;
		AnalyticsModel analyticsModel = new AnalyticsModel(); 
		
		//process 
		while(!endOfData) {
			List<Chaque> checkList = getDataChuck(index);
			if(checkList == null) {
				endOfData = true ; 
				break ; 
			}
			checkList = calcYearAnalytics(checkList,analyticsModel);  
			checkList = calcMonthAnalytics(checkList,analyticsModel); 
			calcDayAnalytics(checkList,analyticsModel);  
		}
		
		analyticsModel.setBanksCount(facade.getUserService().getAllUsers().size());
		
		//settled Checks process 
		List<SettledChaque> settledCheckList = facade.getSettledChaqueService().findAll();
		settledCheckList = calcSettledChecksYearAnalysis(settledCheckList,analyticsModel);
		settledCheckList = calcSettledChecksMonthAnalysis(settledCheckList,analyticsModel);
		calcSettledChecksDayAnalysis(settledCheckList,analyticsModel);
		
		return analyticsModel ; 
	}
	

	public List<Chaque> getDataChuck(int index ) {
		List<Chaque> checksList = facade.getChaqueService().getAllChecks(index);
		if(checksList.size() != 0 ) {
			return checksList ;  
		}else {
			return null ; 
		}
	}
	
	
	private List<Chaque> calcYearAnalytics(List<Chaque> checkList, AnalyticsModel analyticsModel) {
		List<Chaque> filteredChecksList = new ArrayList<Chaque>();
		for(Chaque check : checkList ) {
			String currYear = MasterService.getYearFromStringDate(localDate.toString());
			String checkYear = MasterService.getYearFromStringDate(check.getCheckEntryDate());
			if(currYear.equalsIgnoreCase(checkYear)) {
				filteredChecksList.add(check);
				analyticsModel.addYearCheck(check);
			}
			
		}
		return filteredChecksList;
	}

	private List<Chaque> calcMonthAnalytics(List<Chaque> checkList, AnalyticsModel analyticsModel) {
		List<Chaque> filteredChecksList = new ArrayList<Chaque>();
		for(Chaque check : checkList ) {
			String currYear = MasterService.getMonthFromStringDate(localDate.toString());
			String checkYear = MasterService.getMonthFromStringDate(check.getCheckEntryDate());
			if(currYear.equalsIgnoreCase(checkYear)) {
				filteredChecksList.add(check);
				analyticsModel.addMonthCheck(check);
			}
		}
		return filteredChecksList;
	}

	private void calcDayAnalytics(List<Chaque> checkList, AnalyticsModel analyticsModel) {
		for(Chaque check : checkList ) {
			String currYear = MasterService.getDayFromStringDate(localDate.toString());
			String checkYear = MasterService.getDayFromStringDate(check.getCheckEntryDate());
			if(currYear.equalsIgnoreCase(checkYear)) {
				analyticsModel.addDayCheck(check);
			}
		}		
	}
	
	
	private List<SettledChaque> calcSettledChecksYearAnalysis(List<SettledChaque> settledCheckList, AnalyticsModel analyticsModel) {
		List<SettledChaque> filteredChecksList = new ArrayList<SettledChaque>();
		for(SettledChaque settledCheck : settledCheckList ) {
			String currYear = MasterService.getYearFromStringDate(localDate.toString());
			String checkYear = MasterService.getYearFromStringDate(settledCheck.getLocalDateTime());
			if(currYear.equalsIgnoreCase(checkYear)) {
				filteredChecksList.add(settledCheck);
				analyticsModel.addYearSettledCheck(settledCheck);
			}
			
		}
		return filteredChecksList;
	}


	private List<SettledChaque> calcSettledChecksMonthAnalysis(List<SettledChaque> settledCheckList,AnalyticsModel analyticsModel) {
		List<SettledChaque> filteredChecksList = new ArrayList<SettledChaque>();
		for(SettledChaque settledCheck : settledCheckList ) {
			String currMonth = MasterService.getMonthFromStringDate(localDate.toString());
			String checkMonth = MasterService.getMonthFromStringDate(settledCheck.getLocalDateTime());
			if(currMonth.equalsIgnoreCase(checkMonth)) {
				filteredChecksList.add(settledCheck);
				analyticsModel.addMonthSettledCheck(settledCheck);
			}
			
		}
		return filteredChecksList;
	}


	private void calcSettledChecksDayAnalysis(List<SettledChaque> settledCheckList,AnalyticsModel analyticsModel) {
		for(SettledChaque settledCheck : settledCheckList ) {
			String currDay = MasterService.getMonthFromStringDate(localDate.toString());
			String checkDay = MasterService.getMonthFromStringDate(settledCheck.getLocalDateTime());
			if(currDay.equalsIgnoreCase(checkDay)) {
				analyticsModel.addDaySettledCheck(settledCheck);
			}
			
		}
	}
	
	
}