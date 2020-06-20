package com.RTGS.Settlement.settledChecks;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.RTGS.Settlement.settlementReport.SettlementReportModel;

public interface SettledChecksRepository extends JpaRepository<SettledChaque,Integer>,PagingAndSortingRepository<SettledChaque,Integer> {

	public List<SettledChaque> findBysettlementReportModel(SettlementReportModel srm);
}
