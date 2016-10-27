/**
 * 
 */
package com.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mvc.entity.Task;

/**
 * 普通任务、文书任务
 * 
 * @author zjn
 * @date 2016年9月9日
 */
public interface TaskRepository extends JpaRepository<Task, Integer> {

	// 根据任务ID查询任务详情
	@Query("select t from Task t where task_id=:task_id")
	Task findById(@Param("task_id") Integer taskId);
}
