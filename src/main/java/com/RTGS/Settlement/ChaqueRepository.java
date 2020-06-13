package com.RTGS.Settlement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChaqueRepository extends JpaRepository<Chaque,Integer> {

}
