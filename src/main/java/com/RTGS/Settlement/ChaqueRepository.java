package com.RTGS.Settlement;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.RTGS.Settlement.settlementReport.SettlementReportModel;


@Repository
public interface ChaqueRepository extends JpaRepository<Chaque,Integer>,PagingAndSortingRepository<Chaque,Integer>  {

	@Query("select c from Chaque c where c.SecondBranchCode = :#{#branchCode} ")
	public Slice<Chaque> findBysecondBranchCode(String branchCode, Pageable pageable);
	
	@Query("select c from Chaque c where c.SecondBranchCode = :#{#branchCode} ")
	public List<Chaque> findBysecondBranchCodeNopage(String branchCode);
	
	
	public List<Chaque> findBysettlementReportModel(SettlementReportModel srm);

	public List<Chaque> findBysentFalse();
	
	public Chaque findBysequenceNum(int sequenceNum);

	public List<Chaque> findBycheckId(int checkId);
}
