package com.mvc.service;

import java.util.List;

import com.mvc.entity.Contract;

public interface ContractService {

	// 查询所有合同列表
	List<Contract> findAllCont(int creator_id);

	// 查询所有欠款合同列表
	List<Contract> findAllDebtCont(int creator_id);

	// 查询所有逾期合同列表
	List<Contract> findAllOverdueCont(int creator_id);

	// 查询合同总条数
	Long countTotal(int creator_id);

	// 根据页数选择合同列表
	List<Contract> findByPage(int creator_id, Integer offset, Integer end);

	// 根据合同名获取合同信息
	List<Contract> findConByName(int creator_id, String contName, Integer offset, Integer end);
}
