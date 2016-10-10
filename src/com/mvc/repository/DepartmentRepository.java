﻿/**
 * 
 */
package com.mvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mvc.entity.Department;

/**
 * 
 * @author wanghuimin
 * @date 2016年9月26日
 */
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
	// 根据ID查询部门信息
	@Query("select d from Department d where dept_id = :dept_id")
	Department findById(@Param("dept_id") Integer dept_id);

	// 筛选出所有部门列表
	@Query("select d from Department d where dept_state=0")
	List<Department> findAlls();
	
	

	// 根据部门ID和名称查询部门列表
	@Query("select d from Department d where dept_id=:dept_id and dept_name=:dept_name and dept_state=0")
	List<Department> findByName(@Param("dept_id") Integer dept_id, @Param("dept_name") String dept_name);

	// 查询部门总条数
	@Query("select count(*) from Department d where dept_state=0")
	Long countTotal();

	//查询设计部id
	@Query("select dept_id from Department d where dept_name = '设计部'")
	Integer findOnlyUserDesign();
	
	//根据ID查看部门详情
	@Query("select d from Department d where dept_id=:dept_id and dept_state=0")
	Department findDepartmentContentById(@Param("dept_id") Integer dept_id);


}
