package com.mvc.service;

import java.util.List;

import com.mvc.entity.Contract;

/**
 * 合同业务层
 * 
 * @author wangrui
 * @date 2016-09-10
 */
public interface ContractService {

	// 查询所有欠款合同列表
	List<Contract> findAllDebtCont(String contName, Integer offset, Integer end);

	// 查询所有逾期合同列表
	List<Contract> findAllOverdueCont(String contName, Integer offset, Integer end);

	// 查询合同总条数
	Long countTotal(String contName, Integer methodType);

	// 根据合同名获取合同信息
	List<Contract> findConByName(String contName, Integer offset, Integer end);

	// 添加合同
	Boolean addContract(Contract contract);

	// 根据合同ID获取合同
	Contract selectContById(Integer cont_id);

	// 根据合同ID删除合同
	Boolean deleteContract(Integer cont_id);

	// 查询所有终结合同列表
	List<Contract> findAllEndCont(String contName, Integer offset, Integer end);

	// 修改合同基本信息
	Boolean updateConById(Integer cont_id, Contract contract);

	// 张姣娜：根据合同id修改状态
	Boolean updateState(Integer contId, Integer contState);
}
