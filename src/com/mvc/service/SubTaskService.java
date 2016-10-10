/**
 * 
 */
package com.mvc.service;

import java.util.List;

import com.mvc.entity.SubTask;

/**
 * 文书任务子任务
 * 
 * @author zjn
 * @date 2016年9月17日
 */
public interface SubTaskService {

	// 根据任务ID获取子任务列表
	List<SubTask> findByTaskId(Integer taskId);

	// 根据子任务id修改状态,0改为1表示未完成改为已完成
	boolean updateState(Integer subTaskId);

	// 添加保存子任务
	boolean save(SubTask subTask, Integer taskId);

	// 添加保存子任务
	boolean save(SubTask subTask);

}
