package com.mvc.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mvc.entity.Contract;
import com.utils.Pager;

public interface ContractDao {

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

	// 张姣娜：根据合同id修改状态
	Boolean updateState(Integer contId, Integer contState);

	// 张姣娜：查询所有停建合同列表
	List<Contract> findAllStopCont(String contName, Integer offset, Integer end);

	/***** 报表相关 *****/
	// 光电院项目分项统计表
	List<Contract> findContByPara(Map<String, Object> map, Pager pager);

	// 查询报表总条数
	Long countTotal(Map<String, Object> map);

	// 根据日期获取合同额到款对比表
	List<Object> findByOneDate(String Date);
}
