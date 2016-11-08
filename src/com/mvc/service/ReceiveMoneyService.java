/**
 * 
 */
package com.mvc.service;

import java.util.List;

import com.mvc.entity.ReceiveMoney;
import com.mvc.entity.User;

import net.sf.json.JSONObject;

/**
 * 到款
 * 
 * @author zjn
 * @date 2016年10月27日
 */
public interface ReceiveMoneyService {

	// 根据合同ID获取已到款钱数
	Float receiveMoneyByContId(Integer contId);

	// 根据ID查询详情
	ReceiveMoney findByRemoId(Integer remoId);

	// 根据参数获取该合同的所有到款记录
	List<ReceiveMoney> findListByParam(Integer contId, Integer remoState, Integer offset, Integer end);

	// 根据参数获取该合同的所有到款记录总条数
	Integer countByParam(Integer contId, Integer remoState);

	// 审核到款记录
	Boolean updateRemoStateById(Integer remoId, Float remoAmoney, User user);

	// 新增到款
	Boolean save(JSONObject jsonObject, Integer cont_id, User user);

	// 根据状态查询到款记录
	List<ReceiveMoney> findListByState(Integer userId, Integer remoState, Integer offset, Integer end);

	// 根据状态查询到款记录总条数
	Integer countByState(Integer userId, Integer remoState);

	// 根据到款ID删除到款记录
	Boolean delete(Integer remoId, User user);

}
