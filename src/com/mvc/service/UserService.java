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

	// 新建用户保存
	boolean save(User user);

	// 根据userNum查询用户账号是否存在,返回1存在，返回0不存在
	Long isExist(String userNum);

	// 根据userNum查询用户信息
	User findByUserNum(String userNum);

	// 根据id删除
	boolean isDelete(Integer user_id);

	// 查询所有用户列表
	List<User> findAll();
	
	// 根据ID查询用户信息
	User findById(Integer user_id);

}
