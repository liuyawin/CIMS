package com.mvc.service.impl;

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
	public User save(User user) {
		return userRepository.saveAndFlush(user);
	}

}
