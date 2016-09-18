package com.mvc.dao;

import java.util.List;

import com.mvc.entity.Contract;

public interface ContractDao {

	// 根据合同id修改状态
	boolean updateState(Integer cont_id, Integer cont_state);

	// 查询所有欠款合同列表
	List<Contract> findAllDebtCont(int creator_id, String contName, Integer offset, Integer end);

	// 查询所有逾期合同列表
	List<Contract> findAllOverdueCont(int creator_id, String contName, Integer offset, Integer end);

	// 根据合同名获取合同信息
	List<Contract> findConByName(int creator_id, String contName, Integer offset, Integer end);

	// 根据创建者ID和合同名查询合同总条数
	Long countTotal(int creator_id, String contName, String methodType);

	// 删除合同
	boolean delete(Integer cont_id);
}
