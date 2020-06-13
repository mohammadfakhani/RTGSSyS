package com.RTGS.Settlement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ChaqueService {

	
	@Autowired 
	private ChaqueRepository chaqueRepo ; 

	
	public List<Chaque> getAllChecks(int PageNumber){
		Pageable paging = PageRequest.of(PageNumber, 20, Sort.by("id"));
		Page<Chaque> pagedResult = this.chaqueRepo.findAll(paging);
		if (pagedResult.hasContent()) {
			return pagedResult.getContent();
		} else {
			return new ArrayList<Chaque>();
		}
	}
	
}
