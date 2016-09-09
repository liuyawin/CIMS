package com.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.CookieKeyConstants;
import com.base.CookieUtil;
import com.base.SessionKeyConstants;
import com.mvc.entity.User;
import com.mvc.service.UserService;

/**
 * 登陆
 * 
 * @author zjn
 * @date 2016年9月7日
 */

@Controller
@RequestMapping("/login")
public class LoginController {
	@Autowired
	UserService userService;

	/**
	 * 加载默认起始页
	 * 
	 * @return
	 */
	@RequestMapping("/toLoginPage.do")
	public String contractInformationPage() {
		return "login";
	}

	/**
	 * 检查该用户是否存在
	 * 
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	@RequestMapping("/checkUserName.do")
	public @ResponseBody Long checkUserName(HttpServletRequest request, HttpSession session, ModelMap map) {
		String userNum = request.getParameter("userName");
		Long result = userService.isExist(userNum);
		System.out.println(result);
		return result;
	}

	/**
	 * 用户登录验证
	 * 
	 * @param session
	 * @param request
	 * @param model
	 * @param res
	 * @return
	 */
	@RequestMapping("/loginValidate.do")
	public String login(HttpSession session, HttpServletRequest request, ModelMap model, HttpServletResponse res) {
		String result = "";
		String userNum = request.getParameter("username");
		String passWord = request.getParameter("password");
		String isRemember = request.getParameter("isRemember"); // 记住密码
		User user = userService.findByUserNum(userNum);
		CookieUtil cookie_u = new CookieUtil();
		if (user != null) { // 用户存在
			String passwd = user.getUser_pwd();
			if (passwd != null && passwd.equals(passWord)) {
				session.setAttribute(SessionKeyConstants.LOGIN, user);
				model.addAttribute("user", user);
				cookie_u.add_cookie(CookieKeyConstants.USERNAME, userNum, res, 60 * 60 * 24 * 15);
				if (isRemember == "true") {
					cookie_u.add_cookie(CookieKeyConstants.PASSWORD, passWord, res, 60 * 60 * 24 * 7);
				} else {
					cookie_u.del_cookie(CookieKeyConstants.PASSWORD, request, res);
				}
				result = "OK";
			} else { // 密码错误
				cookie_u.del_cookie(CookieKeyConstants.PASSWORD, request, res);
				result = "err_password";
			}
		} else { // 用户不存在
			result = "err_user";
		}
		return result;
	}
}
