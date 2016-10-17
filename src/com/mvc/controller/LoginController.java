package com.mvc.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.constants.CookieKeyConstants;
import com.base.constants.PageNameConstants;
import com.base.constants.SessionKeyConstants;
import com.mvc.entity.User;
import com.mvc.service.UserService;
import com.utils.CookieUtil;
import com.utils.HttpRedirectUtil;
import net.sf.json.JSONObject;

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
		System.out.println(userNum);
		Long result = userService.isExist(userNum);
		System.out.println(result);
		return result;
	}
	
	@RequestMapping("/getUserPermission.do")
	public @ResponseBody JSONObject getUserPermission(HttpServletRequest request, HttpSession session, ModelMap map) {		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("permission", "zhuren");
		return jsonObject;
	}
	/**
	 * 登录验证用户名和密码是否正确
	 * 
	 * @param session
	 * @param request
	 * @param model
	 * @param res
	 * @return
	 */
	@RequestMapping("/loginValidate.do")
	public @ResponseBody JSONObject loginValidate(HttpSession session, HttpServletRequest request, ModelMap model,
			HttpServletResponse res) {
		System.out.println("loginValidate");
		String userNum = request.getParameter("userName");
		String passWord = request.getParameter("password");
		User user = userService.findByUserNum(userNum);

		JSONObject jsonObject = new JSONObject();

		if (user != null) {
			String passwd = user.getUser_pwd();
			if (passwd != null && passwd.equals(passWord)) {
				jsonObject.put("err_message", "OK");
			} else {
				jsonObject.put("err_message", "err_password");
			}
		} else {
			jsonObject.put("err_message", "err_user");
		}
		return jsonObject;
	}

	/**
	 * 验证登陆之后写入Cookie和Session
	 * 
	 * @param session
	 * @param request
	 * @param model
	 * @param res
	 * @return
	 */
	@RequestMapping("/login.do")
	public String login(HttpSession session, HttpServletRequest request, ModelMap model, HttpServletResponse res) {
		String error_msg = "";
		System.out.println("login");
		String userNum = request.getParameter("userName");
		String password = request.getParameter("password");
		String isRemember = request.getParameter("isRemember"); // 记住密码//值获取不到
		User user = userService.findByUserNum(userNum);
		CookieUtil cookie_u = new CookieUtil();
		if (user != null) { // 用户存在
			String passwd = user.getUser_pwd();
			if (passwd != null && passwd.equals(password)) {
				session.setAttribute(SessionKeyConstants.LOGIN, user);
				model.addAttribute("user", user);
				cookie_u.add_cookie(CookieKeyConstants.USERNAME, userNum, res, 60 * 60 * 24 * 15);
				if (isRemember != null) {
					cookie_u.add_cookie(CookieKeyConstants.PASSWORD, password, res, 60 * 60 * 24 * 7);
				} else {
					cookie_u.del_cookie(CookieKeyConstants.PASSWORD, request, res);
				}
				model.addAttribute("password", password);
				Cookie cookie = new Cookie("userNum", userNum);
				cookie.setMaxAge(30 * 60);
				cookie.setPath("/");
				res.addCookie(cookie);
//				if (user.getUser_name().equals("zhou"))
//					return "assistant2/contractInformation/index";// 返回到文书二主页
//				else if (user.getUser_name().equals("admin"))
//					return "userManagement/userInformation/index";// 返回到管理员主页
//				else if (user.getUser_name().equals("shezong"))
//					return "manager/contractInformation/index";// 返回到管理员主页
//				else if (user.getUser_name().equals("li"))
//					return "assistant1/taskInformation/index";// 返回到文书一主页
//				else {
					return "index";// 返回到index主页
//				}
			} else { // 密码错误
				error_msg = "err_password";
				cookie_u.del_cookie(CookieKeyConstants.PASSWORD, request, res);
				model.addAttribute("error", error_msg);
				return HttpRedirectUtil.redirectStr(PageNameConstants.TOLOGIN);
			}
		} else { // 用户不存在
			error_msg = "err_user";
			model.addAttribute("error", error_msg);
			return HttpRedirectUtil.redirectStr(PageNameConstants.TOLOGIN);
		}
	}

	/**
	 * 退出登录
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("/logout.do")
	public String logout(HttpSession session, HttpServletResponse response) {
		session.removeAttribute(SessionKeyConstants.LOGIN);
		Cookie cookie = new Cookie("userNum", null);
		cookie.setMaxAge(30 * 60);
		cookie.setPath("/");
		response.addCookie(cookie);
		return HttpRedirectUtil.redirectStr(PageNameConstants.TOLOGIN);
	}

	@RequestMapping(value = "/getUserFromSession.do")
	public @ResponseBody String getUserFromSession(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		jsonObject.put("user", user);
		System.out.println("返回用户:" + jsonObject.toString());
		return jsonObject.toString();
	}
}
