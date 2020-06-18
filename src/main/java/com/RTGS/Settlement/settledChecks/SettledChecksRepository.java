package com.RTGS.Settlement.settledChecks;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SettledChecksRepository extends JpaRepository<SettledChaque,Integer>,PagingAndSortingRepository<SettledChaque,Integer> {


}
