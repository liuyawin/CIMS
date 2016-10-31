package com.mvc.dao;

/**
 * 工期阶段持久层接口
 * 
 * @author wanghuimin
 * @date 2016年9月9日
 */
public interface ProjectStageDao {

	// 修改成完成工期
	Boolean updatePrstState(Integer prst_id);

	// 删除工期
	Boolean deletePrstState(Integer prst_id);

}
