package com.mvc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	// 添加收款节点
	@Override
	public boolean addReceiveNode(ReceiveNode receiveNode) {
		ReceiveNode result = receiveNodeRepository.saveAndFlush(receiveNode);
		if (result.getReno_id() != null)
			return true;
		else
			return false;
	}

}
