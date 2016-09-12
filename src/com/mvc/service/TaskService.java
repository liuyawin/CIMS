/**
 * 
 */
package com.mvc.service;

import java.util.List;

import com.mvc.entity.Task;

/**
 * 文书任务
 * 
 * @author zjn
 * @date 2016年9月12日
 */
public interface TaskService {

	// 根据用户ID和状态筛选任务列表,task_state:0 表示为接收，1表示执行中，2表示已完成
	public List<Task> findTaskByState(Integer receiver_id, Integer task_state);

}
