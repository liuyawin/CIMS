package com.mvc.service;

import java.util.List;

import com.mvc.entity.Contract;

public interface ContractService {

	public List<Contract> findAllCont(int creator_id);
	
	public List<Contract> findAllDebtCont(int creator_id);
	
	public List<Contract> findAllOverdueCont(int creator_id);
	
}
