package com.mvc.dao;

import java.util.List;

import com.mvc.entity.User;
import com.mvc.entity.UserDeptRelation;

/**
 * User相关Dao层接口
 * 
 * @author wanghuimin
 * @date 2016年9月7日
 */
public interface UserDao {

	// 根据用户id修改状态
	public boolean updateState(Integer id);
	
	// 根据页数筛选全部用户列表
	public List<User> findUserAllByPage(Integer offset,Integer end);
	
	//只要设计部人员列表
	public List<UserDeptRelation> findUserFromDesign();
}
