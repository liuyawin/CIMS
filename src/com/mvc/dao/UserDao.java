package com.mvc.dao;

/**
 * User相关Dao层接口
 * 
 * @author zjn
 * @date 2016年9月7日
 */
public interface UserDao {

	// 根据部门id修改状态
	public boolean updateState(Integer id, Integer state);
}
