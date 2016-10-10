package com.mvc.dao;

import java.util.List;

import com.mvc.entity.Department;

/**
 * Department相关Dao层接口
 * 
 * @author wanghuimin
 * @date 2016年9月8日
 */
public interface DepartmentDao {
	
	// 根据部门id修改状态、相当于删除
	 boolean delete(Integer id);
	
	//根据起始位置筛选部门列表
	 List<Department> findDepartmentAllByPage(Integer offset, Integer end);
	 
	//筛选所有部门列表
	List<Department> findDepartmentAll();

}
