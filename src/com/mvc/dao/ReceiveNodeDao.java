package com.mvc.dao;

/**
 * 收款节点
 * 
 * @author wanghuimin
 * @date 2016年9月9日
 */
public interface ReceiveNodeDao {

	// 根据收款节点id修改状态
	public boolean updateState(Integer id, Integer state);

}
