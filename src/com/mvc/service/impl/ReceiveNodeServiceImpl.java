package com.mvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.enums.RenoStatus;
import com.mvc.dao.ReceiveNodeDao;
import com.mvc.entity.ReceiveNode;
import com.mvc.repository.ReceiveNodeRepository;
import com.mvc.service.ReceiveNodeService;

/**
 * 收款节点业务层实现
 * 
 * @author wangrui
 * @date 2016-09-20
 */
@Service("receiveNodeServiceImpl")
public class ReceiveNodeServiceImpl implements ReceiveNodeService {

	@Autowired
	ReceiveNodeRepository receiveNodeRepository;
	@Autowired
	ReceiveNodeDao receiveNodeDao;

	// 添加收款节点
	@Override
	public Boolean addReceiveNode(ReceiveNode receiveNode) {
		ReceiveNode result = receiveNodeRepository.saveAndFlush(receiveNode);
		if (result.getReno_id() != null)
			return true;
		else
			return false;
	}

	// 根据合同ID查找收款节点
	@Override
	public List<ReceiveNode> selectRenoByContId(Integer cont_id) {
		return receiveNodeRepository.findByContId(cont_id);
	}

	// 根据ID查询收款节点
	@Override
	public ReceiveNode findByRenoId(Integer reno_id) {
		return receiveNodeRepository.findOne(reno_id);
	}

	// 根据收款节点ID删除收款节点
	@Override
	public Boolean deleteReno(Integer reno_id) {
		return receiveNodeDao.deleteReno(reno_id);
	}

	// 更新收款节点状态和金额
	@Override
	public Boolean updateRenoStateAndMoney(Integer cont_id, Float remoAmoney) {
		List<ReceiveNode> renoList = receiveNodeRepository.findByContIdAndState(cont_id);
		float dvalue;
		ReceiveNode receiveNode = null;
		long time = System.currentTimeMillis();// 当前时间
		for (int i = 0; i < renoList.size(); i++) {
			if (remoAmoney > 0) {
				receiveNode = renoList.get(i);
				dvalue = receiveNode.getReno_money() - receiveNode.getReno_amoney();// 差值=应收款-实收款
				if (dvalue > remoAmoney) {// 若差值>本次确认金额
					float nowMoney = dvalue + receiveNode.getReno_amoney();
					receiveNodeDao.updateState(receiveNode.getReno_id(), RenoStatus.noEnough.value, nowMoney);
				} else {// 若差值<=本次确认金额
					if (time < receiveNode.getReno_time().getTime()) {// 提前到款
						receiveNodeDao.updateState(receiveNode.getReno_id(), RenoStatus.beyondActually.value,
								receiveNode.getReno_money());
					} else {// 已付全款
						receiveNodeDao.updateState(receiveNode.getReno_id(), RenoStatus.finish.value,
								receiveNode.getReno_money());
					}
					if ((Math.abs(dvalue - remoAmoney) < 0.00000001)) {// 若差值=本次确认金额，跳出循环
						break;
					}
					remoAmoney -= dvalue;// 本次确认金额-差值
				}
			} else {
				break;
			}
		}
		return true;
	}
}
