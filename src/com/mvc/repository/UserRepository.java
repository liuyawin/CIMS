package com.mvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mvc.entity.User;

/**
 * 用户信息管理
 * 
 * @author wanghuimin
 * @date 2016年9月7日
 */
public interface UserRepository extends JpaRepository<User, Integer> {
	// 根据ID查询用户信息
	@Query("select u from User u where user_id = :id")
	public User findById(@Param("id") Integer id);

	// 根据ID查询全部用户信息
	@Query("select u from User u where user_isdelete=0 ")
	public List<User> findAlls();

	// 根据userNum查询用户账号是否存在,返回1存在，返回0不存在
	@Query("select count(id) from User u where user_num = :user_num and user_isdelete=0")
	public Long countByUserNum(@Param("user_num") String user_num);

	// 根据userNum查询用户信息
	@Query("select u from User u where user_num = :user_num and user_isdelete=0")
	public User findByUserNum(@Param("user_num") String user_num);

	// 根据id删除
	@Query("update User set user_isdelete=1 where user_id = :user_id")
	public boolean deleteByUserId(@Param("user_id") Integer user_id);

	// 查询用户角色条数
	@Query("select count(user_id) from User u where user_id=:user_id and user_isdelete=0")
	public Long countRoleTotal(@Param("user_id") Integer user_id);

	// 判断用户是否存在
	@Query("select count(user_id) from User u where role_id=:role_id and user_isdelete=0")
	public Long countUserByroleid(@Param("role_id") Integer role_id);

	// 根据userNum查询用户信息
	@Query("select u from User u where user_dept = :user_dept")
	public List<User> findUserByDeptName(@Param("user_dept") Integer deptName);

}
