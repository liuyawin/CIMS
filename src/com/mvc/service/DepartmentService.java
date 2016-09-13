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
	public List<Department> findDepartmentByName(Integer dept_id, String dept_name);
	
	// 筛选全部部门列表
	public List<Department> findDepartmentAlls();
	
	//根据id删除
	public boolean deleteState(Integer dept_id);
	
	//增加一条数据
	public Department save(Department department);

}
