package com.mvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.dao.UserDao;
import com.mvc.entity.User;
import com.mvc.repository.UserRepository;
import com.mvc.service.UserService;

/**
 * User相关Service层接口实现
 * 
 * @author zjn
 * @date 2016年9月7日
 */

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserDao userDao;

	/**
	 * 
	 */
	public boolean save(User user) {
		User result = userRepository.saveAndFlush(user);
		if (result.getUser_id() != null)
			return true;
		else
			return false;
	}

	// 根据userNum查询用户账号是否存在,返回1存在，返回0不存在
	public Long isExist(String userNum) {
		Long result = userRepository.countByUserNum(userNum);
		System.out.println("验证用户名是否存在返回結果" + result);
		return result;
	}

	// 根据userNum查询用户信息
	public User findByUserNum(String user_num) {
		return userRepository.findByUserNum(user_num);
	}

	// 根据id删除
	@Override
	public boolean isDelete(Integer user_id) {
		userRepository.deleteByUserId(user_id);
		return true;
	}

	// 查询所有用户列表
	public List<User> findAll() {
		return userRepository.findAll();
	}

	// 根据ID查询用户信息
	@Override
	public User findById(Integer user_id) {
		return userRepository.findById(user_id);
	}

}
