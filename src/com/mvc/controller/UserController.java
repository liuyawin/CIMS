package com.mvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mvc.entity.User;
import com.mvc.service.UserService;
import com.alibaba.fastjson.JSON;

/**
 * 用户相关
 * 
 * @author zjn
 * @date 2016年9月7日
 */

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;

	@RequestMapping(value = "/selectAllUsers.do")
	public @ResponseBody String getStores(HttpServletRequest request, HttpSession session) {
		List<User> result = userService.findAll();
		return JSON.toJSONString(result);
	}
}