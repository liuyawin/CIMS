package com.mvc.dao;

import java.util.List;

import com.mvc.entity.ReceiveNode;

/**
 * 收款节点
 * 
 * @author wanghuimin
 * @date 2016年9月9日
 */
public interface ReceiveNodeDao {

	// 根据收款节点id修改状态
	Boolean updateState(Integer reno_id, Integer state, Float reno_amoney);

	// 根据合同ID查找收款节点
	List<ReceiveNode> selectRenoByContId(Integer cont_id);

	// 根据收款节点ID删除收款节点
	Boolean deleteReno(Integer reno_id);
}
