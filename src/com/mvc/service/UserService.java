package com.mvc.service;

import java.util.List;
import com.mvc.entity.User;
import com.mvc.entity.UserDeptRelation;

/**
 * User相关Service层接口
 * 
 * @author wanghuimin
 * @date 2016年9月7日
 */

public interface UserService {

	// 添加用户,修改用户信息
	boolean save(User user);	

	// 根据userNum查询用户账号是否存在,返回1存在，返回0不存在
	Long isExist(String userNum);

	// 根据userNum查询用户信息
	User findByUserNum(String userNum);

	// 根据id删除
	boolean deleteIsdelete(Integer user_id);

	//  查询部门总条数
	Long countTotal();
	
	// 根据页数筛选全部用户列表
	List<User> findUserAllByPage(Integer offset, Integer end);
	
	//获取用户列表，无翻页功能
	List<User> findUserAlls();
	
	//只要设计部人员列表
	List<UserDeptRelation> findUserFromDesign();
	
	// 根据ID查询用户信息
	User findById(Integer user_id);
	
	//根据ID查看报警详情
	User findUserContentById(Integer user_id);
	

}
