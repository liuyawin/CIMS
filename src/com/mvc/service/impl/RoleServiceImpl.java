package com.mvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.dao.RoleDao;
import com.mvc.entity.Role;
import com.mvc.repository.RoleRepository;
import com.mvc.service.RoleService;

/**
 * 角色
 * 
 * @author wanghuimin
 * @date 2016年9月18日
 */
@Service("roleServiceImpl")
public class RoleServiceImpl implements RoleService {
	@Autowired
	RoleDao roleDao;

	@Autowired
	RoleRepository roleRepository;

	// 删除角色列表状态
	@Override
	public boolean deleteState(Integer role_id) {
		return roleDao.updateState(role_id);
	}

	// 筛选角色列表
	@Override
	public List<Role> findRoleAlls() {
		return roleRepository.findAlls();
	}

	// 查询角色总条数
	@Override
	public Long countTotal() {
		return roleRepository.countTotal();
	}

	// 根据页数筛选角色列表
	@Override
	public List<Role> findUserAllByPage(Integer offset, Integer end) {
		return roleDao.findRoleAllByPage(offset, end);
	}

	// 添加角色
	@Override
	public boolean save(Role role) {
		Role result = roleRepository.saveAndFlush(role);
		if (result.getRole_id() != null)
			return true;
		else
			return false;

	}

	// 根据ID查看角色详情
	@Override
	public Role findRoleContentById(Integer role_id) {
		return roleRepository.findRoleContentById(role_id);
	}

}
