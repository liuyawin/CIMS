/**
 * 
 */
package com.mvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.dao.ReceiveMoneyDao;
import com.mvc.entity.ReceiveMoney;
import com.mvc.repository.ReceiveMoneyRepository;
import com.mvc.service.ReceiveMoneyService;

/**
 * 到款
 * 
 * @author zjn
 * @date 2016年10月27日
 */
@Service("receiveMoneyServiceImpl")
public class ReceiveMoneyServiceImpl implements ReceiveMoneyService {
	@Autowired
	ReceiveMoneyRepository receiveMoneyRepository;
	@Autowired
	ReceiveMoneyDao receiveMoneyDao;

	// 根据合同ID获取已到款钱数
	@Override
	public Float receiveMoneyByContId(Integer contId) {
		return receiveMoneyDao.receiveMoneyByContId(contId);
	}

	// 根据ID查询详情
	@Override
	public ReceiveMoney findByRemoId(Integer remoId) {
		return receiveMoneyRepository.findById(remoId);
	}

	// 根据参数获取该合同的所有到款记录
	@Override
	public List<ReceiveMoney> findListByParam(Integer contId, Integer remoState, Integer offset, Integer end) {
		return receiveMoneyDao.findListByParam(contId, remoState, offset, end);
	}

	// 根据参数获取该合同的所有到款记录总条数
	@Override
	public Integer countByParam(Integer contId, Integer remoState) {
		return receiveMoneyDao.countByParam(contId, remoState);
	}

	// 审核到款记录
	@Override
	public Boolean updateRemoStateById(Integer remoId, Float remoAmoney) {
		return receiveMoneyDao.updateRemoStateById(remoId, remoAmoney);
	}

	// 新增到款
	@Override
	public Boolean save(ReceiveMoney receiveMoney) {
		ReceiveMoney Result = receiveMoneyRepository.saveAndFlush(receiveMoney);
		if (Result.getRemo_id() != null) {
			return true;
		} else {
			return false;
		}
	}

	// 根据状态查询到款记录
	@Override
	public List<ReceiveMoney> findListByState(Integer userId, Integer remoState, Integer offset, Integer end) {
		return receiveMoneyDao.findListByState(userId, remoState, offset, end);
	}

	// 根据状态查询到款记录总条数
	@Override
	public Integer countByState(Integer userId, Integer remoState) {
		return receiveMoneyDao.countByState(userId, remoState);
	}

}
