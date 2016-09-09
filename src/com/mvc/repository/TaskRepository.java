/**
 * 
 */
package com.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mvc.entity.Task;

/**
 * 普通任务、文书任务
 * 
 * @author zjn
 * @date 2016年9月9日
 */
public interface TaskRepository extends JpaRepository<Task, Long> {

}
