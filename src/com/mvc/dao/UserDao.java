package com.mvc.dao;

import java.util.List;

import com.mvc.entity.User;

/**
 * User相关Dao层接口
 * 
 * @author wanghuimin
 * @date 2016年9月7日
 */
public interface UserDao {

	// 根据用户id修改状态
	boolean updateState(Integer id);

	// 根据页数筛选全部用户列表
	List<User> findUserAllByPage(String searchKey, Integer offset, Integer end);

	// 查询用户总条数
	Integer countTotal(String searchKey);

}
