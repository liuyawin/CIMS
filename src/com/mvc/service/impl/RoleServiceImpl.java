package com.mvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.mvc.dao.RoleDao;
import com.mvc.entity.Role;
import com.mvc.repository.DepartmentRepository;
import com.mvc.repository.RoleRepository;
import com.mvc.service.RoleService;

/**
 * 
 * @author wanghuimin
 * @date 2016年9月18日
 */
@Service("roleServiceImpl")
public class RoleServiceImpl implements RoleService{
	@Autowired
	RoleDao roleDao;

	@Autowired
	RoleRepository roleRepository;
	/**
	 * 删除角色列表状态
	 * 
	 */
	@Override
	public boolean deleteState(Integer role_id) {
		return roleDao.updateState(role_id);
	}
	//筛选角色列表
	@Override
	public List<Role> findRoleAlls() {		
		return roleRepository.findAlls();
	}

}
