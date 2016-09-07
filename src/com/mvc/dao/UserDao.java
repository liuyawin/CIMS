package com.mvc.dao;

import java.util.List;

import com.mvc.entity.User;

/**
 * User相关Dao层接口
 * 
 * @author zjn
 * @date 2016年9月7日
 */
public interface UserDao {

	/**
	 * 根据用户名查找用户信息
	 * 
	 * @param name
	 * @return
	 */
	public User findByUsername(String name);

	/**
	 * 
	 * @return
	 */
	public List<User> findAllUser(String name);
}
