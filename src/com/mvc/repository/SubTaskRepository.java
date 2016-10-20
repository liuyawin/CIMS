/**
 * 
 */
package com.mvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mvc.entity.SubTask;

/**
 * 文书子任务
 * 
 * @author zjn
 * @date 2016年9月9日
 */
public interface SubTaskRepository extends JpaRepository<SubTask, Integer> {

	// 根据任务ID获取子任务列表
	@Query("select s from SubTask s where task_id = :task_id ")
	List<SubTask> findByTaskId(@Param("task_id") Integer taskId);

}
