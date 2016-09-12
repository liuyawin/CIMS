package com.mvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mvc.entity.Role;

/**
 * 
 * @author wanghuimin
 * @date 2016年9月9日
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {
		//根据ID查询角色信息
		@Query("select r from role r where role_id = :role_id")
		public Role findById(@Param("role_id") Integer role_id);

		//筛选出所有角色列表
		@Query("select r from Role r where role_state=0")
		public List<Role> findAlls();

}
