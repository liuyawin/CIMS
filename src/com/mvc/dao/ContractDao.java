package com.mvc.dao;

import java.util.List;

import com.mvc.entity.Contract;

public interface ContractDao {

	// 根据合同id修改状态
	Boolean updateState(Integer cont_id, Integer cont_state);

	// 查询所有欠款合同列表
	List<Contract> findAllDebtCont(String contName, Integer offset, Integer end);

	// 查询所有逾期合同列表
	List<Contract> findAllOverdueCont(String contName, Integer offset, Integer end);

	// 根据合同名获取合同信息
	List<Contract> findConByName(String contName, Integer offset, Integer end);

	// 根据创建者ID和合同名查询合同总条数
	Long countTotal(String contName, Integer methodType);

	// 删除合同
	Boolean delete(Integer cont_id);

	// 查询所有终结合同列表
	List<Contract> findAllEndCont(String contName, Integer offset, Integer end);

	// 修改合同基本信息
	Boolean updateConById(Integer cont_id, Contract contract);
}
