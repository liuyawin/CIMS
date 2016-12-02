package com.mvc.service;

import java.util.List;

import com.mvc.entity.Contract;
import com.mvc.entity.User;
import com.utils.Pager;

import net.sf.json.JSONObject;

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
	Contract addContract(User user, JSONObject jsonObject);

	// 根据合同ID获取合同
	Contract selectContById(Integer cont_id);

	// 根据合同ID删除合同
	List<Contract> deleteContract(Integer cont_id, String contName, String methodType, Pager pager, User user);

	// 查询所有终结合同列表
	List<Contract> findAllEndCont(String contName, Integer offset, Integer end);

	// 修改合同基本信息
	Boolean updateContBase(Integer cont_id, JSONObject jsonObject, User user);

	// 张姣娜：根据合同id修改状态
	Boolean updateState(Integer contId, Integer contState, User user);

	// 张姣娜：查询所有停建合同列表
	List<Contract> findAllStopCont(String contName, Integer offset, Integer end);

	// 合同信息补录
	Contract updateContract(Integer cont_id, JSONObject jsonObject, User user);

	// 根据合同名和方法类别获取合同列表
	List<Contract> findConByNameAndMType(String contName, Integer methodType, Pager pager);

	// 张姣娜：完成文书任务后更新合同状态
	Boolean updateContIsback(Integer contId, Integer state);

	// 根据日期获取合同总金额
	Float getTotalMoney(String date);

}
