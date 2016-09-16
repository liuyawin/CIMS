package com.mvc.service;

import java.util.List;

import com.mvc.entity.Department;

/**
 * 部门
 * 
 * @author wanghuimin
 * @date 2016年9月13日
 */
public interface DepartmentService {
	
	// 根据用户ID和部门名称筛选部门列表
	 List<Department> findDepartmentByName(Integer dept_id, String dept_name);
	
	// 根据页数筛选全部部门列表
	List<Department> findDepartmentAllByPage(Integer offset, Integer end);
	
	// 筛选全部部门列表
	 List<Department> findDepartmentAlls();
	//根据id删除
	boolean deleteState(Integer dept_id);
	
	//增加一条数据
	 boolean save(Department department);
	
	// 查询任务总条数
	Long countTotal();

}
