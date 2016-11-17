package com.mvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mvc.entity.Role;
import com.mvc.entity.User;
import com.mvc.service.UserService;
import com.utils.MD5;
import com.utils.Pager;

import net.sf.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.base.enums.Dept;

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
	public @ResponseBody String getUsersByPrarm(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		String searchKey = request.getParameter("searchKey");
		Integer totalRow = userService.countTotal(searchKey);
		Pager pager = new Pager();
		pager.setPage(Integer.valueOf(request.getParameter("page")));
		pager.setTotalRow(Integer.parseInt(totalRow.toString()));
		List<User> list = userService.findUserAllByPage(searchKey, pager.getOffset(), pager.getLimit());
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
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
	public @ResponseBody String getAllUsers(HttpServletRequest request, HttpSession session) {
		List<User> result = userService.findUserAlls();
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
		user.setUser_pwd(MD5.encodeByMD5(jsonObject.getString("user_pwd")));
		if (jsonObject.containsKey("user_sex")) {
			user.setUser_sex(Integer.parseInt(jsonObject.getString("user_sex")));
		}
		if (jsonObject.containsKey("user_tel")) {
			user.setUser_tel(jsonObject.getString("user_tel"));
		}
		if (jsonObject.containsKey("user_email")) {
			user.setUser_email(jsonObject.getString("user_email"));
		}
		if (jsonObject.containsKey("user_dept")) {
			user.setUser_dept(Integer.valueOf(jsonObject.getString("user_dept")));
		}
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
	 * 获取设计部人员列表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/selectUsersFromDesign.do")
	public @ResponseBody String getUsersFromDesign(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		List<User> result = userService.findUserByDeptName(Dept.shejibu.value);
		jsonObject.put("list", result);
		return jsonObject.toString();
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
		User user = userService.findById(userid);
		jsonObject.put("user", user);
		return jsonObject.toString();
	}

	/**
	 * 检查用户是否已经存在:返回1存在，返回0不存在
	 * 
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping("/isUserNumExist.do")
	public @ResponseBody Long checkUserName(HttpServletRequest request, HttpSession session, ModelMap map) {
		String userNum = request.getParameter("userNum");
		Long result = userService.isExist(userNum);
		return result;
	}

}