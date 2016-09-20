package com.mvc.service;

import java.util.List;
import com.mvc.entity.Role;
/**
 * 
 * @author wanghuimin
 * @date 2016年9月18日
 */
public interface RoleService {

	//根据状态删除
	boolean deleteState(Integer role_id);
	
	//筛选角色列表
	List<Role> findRoleAlls();

}
