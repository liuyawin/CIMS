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
 * @author wanghuimin
 * @date 2016年9月7日
 */

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserDao userDao;

	/**
	 * 添加用户, 修改用户信息
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
		return result;
	}

	// 根据userNum查询用户信息
	public User findByUserNum(String user_num) {
		return userRepository.findByUserNum(user_num);
	}

	// 查询所有用户列表
	public List<User> findAll() {
		return userRepository.findAll();
	}

	// 查询部门总条数
	@Override
	public Integer countTotal(String searchKey) {
		return userDao.countTotal(searchKey);
	}

	// 根据页数筛选全部用户列表
	@Override
	public List<User> findUserAllByPage(String searchKey, Integer offset, Integer end) {
		return userDao.findUserAllByPage(searchKey, offset, end);
	}

	// 获取用户列表，无翻页功能
	@Override
	public List<User> findUserAlls() {
		return userRepository.findAlls();
	}

	// 根据id删除
	@Override
	public boolean deleteIsdelete(Integer user_id) {
		return userDao.updateState(user_id);
	}

	// 根据ID查询用户信息
	@Override
	public User findById(Integer user_id) {
		return userRepository.findById(user_id);
	}

	// 根据用户部门筛选用户
	@Override
	public List<User> findUserByDeptName(Integer deptName) {
		return userRepository.findUserByDeptName(deptName);
	}

}
