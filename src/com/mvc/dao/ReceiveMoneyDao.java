/**
 * 
 */
package com.mvc.dao;

import java.util.List;

import com.mvc.entity.ReceiveMoney;

/**
 * 到款
 * 
 * @author zjn
 * @date 2016年10月27日
 */
public interface ReceiveMoneyDao {

	// 根据合同ID获取已到款钱数
	Float receiveMoneyByContId(Integer contId);

	// 根据参数获取该合同的所有到款记录
	List<ReceiveMoney> findListByParam(Integer contId, Integer remoState, Integer offset, Integer end);

	// 根据参数获取该合同的所有到款记录总条数
	Integer countByParam(Integer contId, Integer remoState);

	// 审核到款记录
	Boolean updateRemoStateById(Integer remoId, Float remoAmoney);

	// 根据状态查询到款记录
	List<ReceiveMoney> findListByState(Integer userId, Integer remoState, Integer offset, Integer end);

	// 根据状态查询到款记录总条数
	Integer countByState(Integer userId, Integer remoState);
	
	// 根据到款ID删除到款记录
	Boolean delete(Integer remoId);
	
	//报表统计相关，根据日期统计到款情况
	List<Object> findRemoByDate(String firstDate,String secondDate);
	
}
