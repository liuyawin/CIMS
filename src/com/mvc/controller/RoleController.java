package com.mvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.base.enums.IsDelete;
import com.mvc.entity.Role;
import com.mvc.service.RoleService;
import com.utils.Pager;

import net.sf.json.JSONObject;

/**
 * 角色
 * 
 * @author wanghuimin
 * @date 2016年9月18日
 */
@Controller
@RequestMapping("/role")
public class RoleController {
	@Autowired
	RoleService roleService;

	/**
	 * 设置进入用户管理起始页 包 20161014
	 * 
	 * @return
	 */
	@RequestMapping("/toUserManagePage.do")
	public String userManagePage() {
		return "systemManagement/index";
	}

	/**
	 * 删除角色列表状态
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/deleteRole.do")
	public @ResponseBody String deleteUser(HttpServletRequest request, HttpSession session) {
		Integer roleid = Integer.valueOf(request.getParameter("roleId"));
		boolean result = roleService.deleteState(roleid);
		return JSON.toJSONString(result);
	}

	/**
	 * 筛选角色列表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getAllRoleList.do")
	public @ResponseBody String getAllStores(HttpServletRequest request, HttpSession session) {
		List<Role> result = roleService.findRoleAlls();
		return JSON.toJSONString(result);
	}

	/**
	 * 根据页数筛选角色列表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getRoleListByPage.do")
	public @ResponseBody String getRoleList(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Long totalRow = roleService.countTotal();
		Pager pager = new Pager();
		pager.setPage(Integer.valueOf(request.getParameter("page")));
		pager.setTotalRow(Integer.parseInt(totalRow.toString()));
		List<Role> list = roleService.findUserAllByPage(pager.getOffset(), pager.getLimit());
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/**
	 * 添加，修改角色
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/addRole.do")
	public @ResponseBody String addRole(HttpServletRequest request, HttpSession session) {
		Role role = new Role();
		role.setRole_name(request.getParameter("role_name"));
		role.setRole_state(IsDelete.NO.value);
		role.setRole_permission(request.getParameter("role_permission"));
		boolean result;
		if (request.getParameter("role_id") != null) {
			role.setRole_id(Integer.valueOf(request.getParameter("role_id")));
			result = roleService.save(role);// 修改角色信息
		} else {
			result = roleService.save(role);// 添加角色信息
		}
		return JSON.toJSONString(result);
	}

	/**
	 * 根据ID查看角色详情
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/selectRoleById.do")
	public @ResponseBody String getRoleContentById(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Integer roleid = Integer.valueOf(request.getParameter("roleid"));
		Role role = roleService.findRoleContentById(roleid);
		jsonObject.put("role", role);
		return jsonObject.toString();
	}

}
