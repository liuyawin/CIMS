package com.mvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mvc.entity.Role;

/**
 * 角色
 * 
 * @author wanghuimin
 * @date 2016年9月9日
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {
	// 根据ID查询角色详情
	@Query("select r from Role r where role_id = :role_id and role_state=0")
	public Role findRoleContentById(@Param("role_id") Integer role_id);

	// 筛选出所有角色列表
	@Query("select r from Role r where role_state=0")
	public List<Role> findAlls();

	// 查询角色总条数
	@Query("select count(role_id) from Role r where role_state=0")
	public Long countTotal();

}
