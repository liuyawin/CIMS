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

	// 更新任务状态
	public boolean updateState(Integer taskId, Integer task_state) {
		return taskDao.updateState(taskId, task_state);
	}

	// 根据状态，关键字查询任务总条数
	public Integer countByParam(Integer user_id, Integer task_state, String searchKey, Integer sendOrReceive) {
		return taskDao.countByParam(user_id, task_state, searchKey, sendOrReceive);
	}

	// 根据页数,状态，关键字返回任务列表
	public List<Task> findByPage(Integer user_id, Integer task_state, String searchKey, Integer offset, Integer end,
			Integer sendOrReceive) {
		return taskDao.findByPage(user_id, task_state, searchKey, offset, end, sendOrReceive);
	}

	// 根据任务ID查询任务详情
	public Task findById(Integer taskId) {
		return taskRepository.findById(taskId);
	}

	// 保存
	public Task save(Task task) {
		return taskRepository.saveAndFlush(task);
	}

	// 根据任务Id删除任务
	public boolean delete(Integer taskId) {
		return taskDao.delete(taskId);
	}

	// 根据合同ID和任务类型返回任务列表
	public List<Task> findByContIdAndType(Integer user_id, Integer contId, Integer taskType) {
		return taskDao.findByContIdAndType(user_id, contId, taskType);
	}

}
