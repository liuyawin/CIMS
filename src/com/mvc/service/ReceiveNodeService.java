package com.mvc.service;

import java.util.List;

import com.mvc.entity.ReceiveNode;

/**
 * 收款节点业务层接口
 * 
 * @author wangrui
 * @date 2016-09-20
 */
public interface ReceiveNodeService {

	// 添加收款节点
	Boolean addReceiveNode(ReceiveNode receiveNode);

	// 根据合同ID查找收款节点
	List<ReceiveNode> selectRenoByContId(Integer cont_id);

	// 根据ID查询收款节点
	ReceiveNode findByRenoId(Integer reno_id);

	// 根据收款节点ID删除收款节点
	Boolean deleteReno(Integer reno_id);

	// 更新收款节点状态和金额
	Boolean updateRenoStateAndMoney(Integer cont_id, Float remoAmoney);
}
