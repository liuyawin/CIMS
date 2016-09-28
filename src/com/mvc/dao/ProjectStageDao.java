package com.mvc.dao;

/**
 * 工期阶段持久层接口
 * 
 * @author wanghuimin
 * @date 2016年9月9日
 */
public interface ProjectStageDao {

	// 根据工期阶段id修改状态
	Boolean updateState(Integer id, Integer state);

	// 修改成完成工期
	Boolean updatePrstState(Integer prst_id);
	
}
