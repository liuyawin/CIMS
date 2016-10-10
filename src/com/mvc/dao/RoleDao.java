package com.mvc.dao;

import java.util.List;

import com.mvc.entity.Role;

/**
 * 角色职位
 * 
 * @author wanghuimin
 * @date 2016年9月9日
 */
public interface RoleDao {

	// 删除，修改角色状态列表
	boolean updateState(Integer role_id);

	// 根据页数筛选角色列表
	List<Role> findRoleAllByPage(Integer offset, Integer end);

}
