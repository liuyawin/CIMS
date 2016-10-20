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
	boolean delete(Integer TaskId);

	// 根据任务id修改状态
	boolean updateState(Integer id, Integer state);

	// 根据页数,状态，关键字返回任务列表
	List<Task> findByPage(Integer user_id, Integer task_state, String searchKey, Integer offset, Integer end,
			Integer sendOrReceive);

	// 根据状态，关键字查询任务总条数
	Integer countByParam(Integer user_id, Integer task_state, String searchKey, Integer sendOrReceive);

	// 根据合同ID和任务类型返回任务列表
	List<Task> findByContIdAndType(Integer user_id, Integer contId, Integer taskType);

	// 根据任务类型获取任务条数
	Integer countByType(Integer userId, Integer taskType);
}
