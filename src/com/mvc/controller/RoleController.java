package com.mvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.mvc.entity.Role;
import com.mvc.service.RoleService;
import com.utils.Pager;

import net.sf.json.JSONObject;

/**
 * 角色
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
		System.out.println("进来了");
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
		System.out.println("总数" + totalRow);
		Pager pager = new Pager();
		pager.setPage(Integer.valueOf(request.getParameter("page")));
		pager.setTotalRow(Integer.parseInt(totalRow.toString()));
		List<Role> list = roleService.findUserAllByPage(pager.getOffset(), pager.getLimit());
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		System.out.println("返回列表:" + jsonObject.toString());
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
		role.setRole_state(0);
		role.setRole_permission(request.getParameter("role_permission"));
		// if(permission!=null){
		// for(String permissions : permission){
		// permissions.split(",");
		// role.setRole_permission(permissions);
		// }
		// }
		boolean result;
		if(request.getParameter("role_id") !=null){
			role.setRole_id(Integer.valueOf(request.getParameter("role_id")));
			result = roleService.save(role);//修改角色信息
		}
		else {
			result = roleService.save(role);//添加角色信息
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
	@RequestMapping(value="/selectRoleById.do")
	public @ResponseBody String getRoleContentById(HttpServletRequest request,HttpSession session){
		JSONObject jsonObject=new JSONObject();
		Integer roleid=Integer.valueOf(request.getParameter("roleid"));
		Role role=roleService.findRoleContentById(roleid);
		jsonObject.put("role", role);
		return jsonObject.toString();
		
	}

}
