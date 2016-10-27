package com.mvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
