package com.RTGS.Settlement;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChaqueRepository extends JpaRepository<Chaque,Integer>,PagingAndSortingRepository<Chaque,Integer>  {

	@Query("select c from Chaque c where c.SecondBranchCode = :#{#branchCode} ")
	public Slice<Chaque> findBysecondBranchCode(String branchCode, Pageable pageable);
	
}
