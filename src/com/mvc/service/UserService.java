package com.mvc.service;

import java.util.List;

import com.mvc.entity.User;

/**
 * User相关Service层接口
 * 
 * @author zjn
 * @date 2016年9月7日
 */

public interface UserService {
	/**
	 * 
	 * @param user
	 * @return
	 */
	public User save(User user);

	/**
	 * 
	 * @return
	 */
	public List<User> findAllUser(String name);
}
