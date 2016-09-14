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
	List<Task> findTaskByState(Integer receiver_id, Integer task_state);

	// 更新任务状态
	boolean updateState(Integer taskId, Integer task_state);

	// 查询任务总条数
	Long countTotal();

	// 根据页数返回任务列表
	List<Task> findByPage(Integer receiver_id, Integer task_state, Integer offset, Integer end);
}
