package com.mvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mvc.entity.User;
import com.mvc.repository.DepartmentRepository;
import com.mvc.service.UserService;
import com.alibaba.fastjson.JSON;

import net.sf.json.JSONObject;

/**
 * 
 * @author zjn
 * @date 2016年9月7日
 */

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;
	// @RequestMapping("/getStaffInfo")
	// public @ResponseBody JSONObject getStaffInfo(HttpServletRequest req) {
	// System.out.println("进入controller");
	// // User user = new User();
	// // user.setName("name");
	// // user.setPwd("pwd");
	// // user.setAge(36);
	// // User user2 = userService.save(user);
	// // System.out.println(user2.getName());
	// JSONObject jsonObject = new JSONObject();
	// jsonObject.put("now", 1);
	// jsonObject.put("he", 2);
	// return jsonObject;
	// }

	/**
	 * 
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	// @RequestMapping("/getStaffInfo")
	// public @ResponseBody String addStore(HttpServletRequest request,
	// HttpSession session, ModelMap map) {
	// System.out.println("进入getStaffInfo");
	// User user = new User();
	// System.out.println(request.getParameter("name") +
	// request.getParameter("pwd") + request.getParameter("age"));
	// user.setName(request.getParameter("name"));
	// user.setPwd(request.getParameter("pwd"));
	// user.setAge(Integer.valueOf(request.getParameter("age")));
	// User user2 = userService.save(user);
	// user2.setName("张");
	// System.out.println(user2.getName());
	// return JSON.toJSONString(user2);
	// }

	@RequestMapping(value = "/getConByName.do")
	public @ResponseBody String getStores(HttpServletRequest request, HttpSession session) {
		String name = request.getParameter("cName");
		List<User> result = userService.findAllUser(name);
		return JSON.toJSONString(result);
	}
}