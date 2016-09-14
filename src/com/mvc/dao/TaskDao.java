package com.mvc.dao;

import java.util.List;

import com.mvc.entity.Task;

/**
 * 文书任务
 * 
 * @author wanghuimin
 * @date 2016年9月9日
 */
public interface TaskDao {

	// 根据任务id修改删除状态,相当于删除
	boolean delete(Integer id, Integer isdelete);

	// 根据任务id修改状态
	boolean updateState(Integer id, Integer state);

	// 根据页数返回任务列表
	List<Task> findByPage(Integer receiver_id, Integer task_state, Integer offset, Integer end);

}
