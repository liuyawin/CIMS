package com.mvc.dao;

/**
 * 文书任务子任务
 * 
 * @author wanghuimin
 * @date 2016年9月9日
 */
public interface SubTaskDao {

	// 根据子任务id修改状态,0改为1表示未完成改为已完成
	public boolean updateState(Integer subTaskId);

}
