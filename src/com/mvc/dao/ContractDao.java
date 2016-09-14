package com.mvc.dao;

import java.util.List;

import com.mvc.entity.Contract;

public interface ContractDao {

	// 根据合同id修改状态
	boolean updateState(Integer cont_id, Integer cont_state);

	// 查询所有合同列表
	List<Contract> findAllCont(int creator_id);

	// 查询所有欠款合同列表
	List<Contract> findAllDebtCont(int creator_id);

	// 查询所有逾期合同列表
	List<Contract> findAllOverdueCont(int creator_id);

	// 根据页数选择合同列表
	List<Contract> findByPage(int creator_id, Integer offset, Integer end);
}
