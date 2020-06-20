package com.RTGS.security.users;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<RTGSUser,Integer> ,PagingAndSortingRepository<RTGSUser,Integer>{

	public RTGSUser findByUsername(String username);
	
	
}
