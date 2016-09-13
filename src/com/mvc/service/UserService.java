package com.mvc.service;

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

	// 根据userNum查询用户账号是否存在,返回1存在，返回0不存在
	public Long isExist(String userNum);

	// 根据userNum查询用户信息
	public User findByUserNum(String userNum);
	
	//根据id删除
	public boolean isDelete(Integer dept_id);
	

}
