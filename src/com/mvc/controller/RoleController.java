package com.mvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.mvc.entity.Department;
import com.mvc.entity.Role;
import com.mvc.service.RoleService;

/**
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
			System.out.println(JSON.toJSONString(result));
			return JSON.toJSONString(result);
		}	
		

}
