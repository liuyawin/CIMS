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

	// 根据状态，关键字查询任务总条数
	Integer countByParam(Integer user_id, Integer task_state, String searchKey, Integer sendOrReceive);

	// 根据页数,状态，关键字返回任务列表
	List<Task> findByPage(Integer user_id, Integer task_state, String searchKey, Integer offset, Integer end,
			Integer sendOrReceive);

	// 根据任务ID查询任务详情
	Task findById(Integer taskId);

	// 保存
	Task save(Task task);

	// 根据任务Id删除任务
	boolean delete(Integer taskId);

	// 根据合同ID和任务类型返回任务列表
	List<Task> findByContIdAndType(Integer user_id, Integer contId, Integer taskType);

	// 根据任务类型获取任务条数
	Integer countByType(Integer userId, Integer taskType);

}
