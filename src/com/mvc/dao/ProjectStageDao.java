package com.mvc.dao;

/**
 * 工期阶段持久层接口
 * 
 * @author wanghuimin
 * @date 2016年9月9日
 */
public interface ProjectStageDao {

	// 根据工期阶段id修改状态
	public boolean updateState(Integer id, Integer state);

}
