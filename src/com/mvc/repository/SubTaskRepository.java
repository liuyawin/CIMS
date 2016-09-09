/**
 * 
 */
package com.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mvc.entity.SubTask;

/**
 * 文书子任务
 * 
 * @author zjn
 * @date 2016年9月9日
 */
public interface SubTaskRepository extends JpaRepository<SubTask, Long> {

}
