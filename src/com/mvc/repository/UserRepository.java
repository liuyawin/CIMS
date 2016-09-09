package com.mvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mvc.entity.User;

/**
 * 用户信息管理
 * 
 * @author zjn
 * @date 2016年9月7日
 */
public interface UserRepository extends JpaRepository<User, Long> {
	// 根据ID查询用户信息
	@Query("select u from User u where user_id = :id")
	public User findById(@Param("id") Long id);

	// 根据ID查询全部用户信息
	@Query("select u from User u where user_isdelete=0 ")
	public List<User> findAlls();

	//
	// 根据userNum查询用户账号是否存在,返回1存在，返回0不存在
	@Query("select count(id) from User u where user_num = :user_num and status=0")
	public Integer countByUserNum(@Param("user_num") String user_num);

	// 根据userNum查询用户信息
	@Query("select u from User u where user_num = :user_num")
	public User findByUserNum(@Param("user_num") String user_num);
}
