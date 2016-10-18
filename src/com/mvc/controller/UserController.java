package com.mvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mvc.entity.Role;
import com.mvc.entity.User;
import com.mvc.entity.UserDeptRelation;
import com.mvc.service.UserService;
import com.utils.Pager;

import net.sf.json.JSONObject;

import com.alibaba.fastjson.JSON;

/**
 * 用户相关内容
 * 
 * @author wanghuimin
 * @date 2016年9月7日
 */

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;

	/**
	 * 根据页数筛选用户列表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getUserListByPage.do")
	public @ResponseBody String getStores(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Long totalRow = userService.countTotal();
		System.out.println("总数" + totalRow);
		Pager pager = new Pager();
		pager.setPage(Integer.valueOf(request.getParameter("page")));
		pager.setTotalRow(Integer.parseInt(totalRow.toString()));
		List<User> list = userService.findUserAllByPage(pager.getOffset(), pager.getLimit());
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		System.out.println("返回列表:" + jsonObject.toString());
		return jsonObject.toString();
	}

	/**
	 * 获取用户列表，无翻页功能
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getAllUserList.do")
	public @ResponseBody String getAllStores(HttpServletRequest request, HttpSession session) {
		List<User> result = userService.findUserAlls();
		System.out.println(JSON.toJSONString(result));
		return JSON.toJSONString(result);
	}

	/**
	 * 删除用户
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/deleteUser.do")
	public @ResponseBody String deleteUser(HttpServletRequest request, HttpSession session) {
		Integer userid = Integer.valueOf(request.getParameter("userId"));
		boolean result = userService.deleteIsdelete(userid);
		return JSON.toJSONString(result);
	}

	/**
	 * 添加,修改用户信息
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/addUser.do")
	public @ResponseBody String addUser(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = JSONObject.fromObject(request.getParameter("user"));
		User user = new User();
		user.setUser_num(jsonObject.getString("user_num"));
		user.setUser_name(jsonObject.getString("user_name"));
		user.setUser_pwd(jsonObject.getString("user_pwd"));
		user.setUser_sex(Integer.parseInt(jsonObject.getString("user_sex")));
		user.setUser_tel(jsonObject.getString("user_tel"));
		user.setUser_email(jsonObject.getString("user_email"));

		Role role = new Role();
		role.setRole_id(Integer.parseInt(jsonObject.getJSONObject("role").getString("role_id")));
		user.setRole(role);
		user.setUser_isdelete(0);
		boolean result;
		if (jsonObject.containsKey("user_id")) {
			user.setUser_id(Integer.valueOf(jsonObject.getString("user_id")));
			result = userService.save(user);// 修改用户信息
		} else {
			result = userService.save(user);// 添加用户信息
		}
		return JSON.toJSONString(result);
	}

	/**
	 * 只要设计部人员列表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/selectUsersFromDesign.do")
	public @ResponseBody String getUsersFromDesign(HttpServletRequest request, HttpSession session) {
		List<UserDeptRelation> result = userService.findUserFromDesign();
		System.out.println(JSON.toJSONString(result));
		return JSON.toJSONString(result);
	}

	/**
	 * 根据ID查看用户详情
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/selectUserById.do")
	public @ResponseBody String getUserContentById(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Integer userid = Integer.valueOf(request.getParameter("userid"));
		User user = userService.findUserContentById(userid);
		jsonObject.put("user", user);
		return jsonObject.toString();

	}

}