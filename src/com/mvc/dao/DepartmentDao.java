package com.mvc.dao;

/**
 * Department相关Dao层接口
 * 
 * @author wanghuimin
 * @date 2016年9月8日
 */
public interface DepartmentDao {
	// 根据部门id修改状态、相当于删除
	public boolean delete(Integer id, Integer state);

}
