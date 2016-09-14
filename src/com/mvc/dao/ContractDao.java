package com.mvc.dao;

import java.util.List;

import com.mvc.entity.Contract;

public interface ContractDao {

	public boolean updateState(Integer cont_id, Integer cont_state);
	
	public List<Contract> findAllCont(int creator_id);
	
	public List<Contract> findAllDebtCont(int creator_id);
	
	public List<Contract> findAllOverdueCont(int creator_id);
}
