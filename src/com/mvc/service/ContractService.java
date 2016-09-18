package com.mvc.service;

import java.util.List;

import com.mvc.entity.Contract;

public interface ContractService {

	// 查询所有欠款合同列表
	List<Contract> findAllDebtCont(int creator_id, String contName, Integer offset, Integer end);

	// 查询所有逾期合同列表
	List<Contract> findAllOverdueCont(int creator_id, String contName, Integer offset, Integer end);

	// 查询合同总条数
	Long countTotal(int creator_id, String contName, String methodType);

	// 根据合同名获取合同信息
	List<Contract> findConByName(int creator_id, String contName, Integer offset, Integer end);

	// 添加合同
	boolean addContract(Contract contract);
}
