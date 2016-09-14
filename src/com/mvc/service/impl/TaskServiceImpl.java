/**
 * 
 */
package com.mvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.dao.TaskDao;
import com.mvc.entity.Task;
import com.mvc.repository.TaskRepository;
import com.mvc.service.TaskService;

/**
 * 文书任务
 * 
 * @author zjn
 * @date 2016年9月12日
 */
@Service("taskServiceImpl")
public class TaskServiceImpl implements TaskService {
	@Autowired
	TaskRepository taskRepository;
	@Autowired
	TaskDao taskDao;

	// 根据用户ID和状态筛选任务列表,task_state:0 表示为接收，1表示执行中，2表示已完成
	public List<Task> findTaskByState(Integer receiver_id, Integer task_state) {
		return taskRepository.findAllByState(receiver_id, task_state);
	}

	// 更新任务状态
	public boolean updateState(Integer taskId, Integer task_state) {
		return taskDao.updateState(taskId, task_state);
	}

	// 查询任务总条数
	public int countTotal() {
		return taskRepository.countTotal();
	}

	@Override
	public List<Task> findByPage(Integer receiver_id, Integer task_state, Integer offset, Integer end) {
		return taskDao.findByPage(receiver_id, task_state, offset, end);
	}

}
