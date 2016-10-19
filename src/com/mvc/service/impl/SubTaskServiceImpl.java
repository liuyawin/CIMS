/**
 * 
 */
package com.mvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.dao.SubTaskDao;
import com.mvc.entity.SubTask;
import com.mvc.repository.SubTaskRepository;
import com.mvc.service.SubTaskService;

/**
 * 文书任务子任务
 * 
 * @author zjn
 * @date 2016年9月17日
 */
@Service("subTaskServiceImpl")
public class SubTaskServiceImpl implements SubTaskService {
	@Autowired
	SubTaskRepository subTaskRepository;
	@Autowired
	SubTaskDao subTaskDao;

	// 根据任务ID获取子任务列表
	public List<SubTask> findByTaskId(Integer taskId) {
		return subTaskRepository.findByTaskId(taskId);
	}

	// 根据子任务id修改状态,0改为1表示未完成改为已完成
	public boolean updateState(Integer subTaskId) {
		return subTaskDao.updateState(subTaskId);
	}

	// 添加保存子任务
	public boolean save(SubTask subTask, Integer taskId) {
		SubTask result = subTaskRepository.saveAndFlush(subTask);
		if (result.getSuta_id() != 0)
			return true;
		else {
			return false;
		}
	}

	// 添加保存子任务
	public boolean save(SubTask subTask) {
		SubTask result = subTaskRepository.saveAndFlush(subTask);
		if (result.getSuta_id() != 0)
			return true;
		else {
			return false;
		}
	}

}
