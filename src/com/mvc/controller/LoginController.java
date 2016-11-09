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

import com.alibaba.fastjson.JSON;
import com.base.constants.CookieKeyConstants;
import com.base.constants.PageNameConstants;
import com.base.constants.PermissionConstants;
import com.base.constants.SessionKeyConstants;
import com.mvc.entity.AlarmStatistic;
import com.mvc.entity.User;
import com.mvc.service.AlarmService;
import com.mvc.service.AlarmStatisticService;
import com.mvc.service.InvoiceService;
import com.mvc.service.TaskService;
import com.mvc.service.UserService;
import com.utils.CookieUtil;
import com.utils.HttpRedirectUtil;
import com.utils.MD5;

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
	@Autowired
	InvoiceService invoiceService;
	@Autowired
	TaskService taskService;
	@Autowired
	AlarmService alarmService;
	@Autowired
	AlarmStatisticService alarmStatisticService;

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
	 * 跳转到起始页
	 * 
	 * @return
	 */
	@RequestMapping("/toIndex.do")
	public String name() {
		return "index";
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
		return result;
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
		String userNum = request.getParameter("userName");
		String passWord = MD5.encodeByMD5(request.getParameter("password"));
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
		String userNum = request.getParameter("userName");
		String password = MD5.encodeByMD5(request.getParameter("password"));
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
				// cookie.setMaxAge(60);
				cookie.setPath("/");
				res.addCookie(cookie);
				cookie = new Cookie("role", user.getRole().getRole_id().toString());
				cookie.setMaxAge(60);
				cookie.setPath("/");
				res.addCookie(cookie);
				return "index";// 返回到index主页
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
		return jsonObject.toString();
	}

	/**
	 * 初始化首页的数据
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getInitData.do")
	public @ResponseBody String getInitData(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		if (user != null) {
			AlarmStatistic alarmStatistic = alarmStatisticService.findAlst(user.getUser_id());
			jsonObject.put("waitAuditBillTaskNum", alarmStatistic.getWait_audit_bill_task_num());// 待审核发票任务
			jsonObject.put("assistantTaskNum", alarmStatistic.getAssistant_task_num());// 文书任务
			jsonObject.put("managerControlTaskNum", alarmStatistic.getManager_control_task_num());// 执行管控任务
			jsonObject.put("billTaskNum", alarmStatistic.getBill_task_num());// 发票任务
			jsonObject.put("otherTaskNum", alarmStatistic.getOther_task_num());// 普通任务
			jsonObject.put("debtAlarmNum", alarmStatistic.getDebt_alarm_num());// 收款超时
			jsonObject.put("overdueAlarmNum", alarmStatistic.getOverdue_alarm_num());// 工程逾期
			jsonObject.put("taskAlarmNum", alarmStatistic.getTask_alarm_num());// 任务超时
			jsonObject.put("remoTaskNum", alarmStatistic.getRemo_task_num());// 待核对到款任务
			jsonObject.put("totalReceiveTaskNum", calTotalNum(alarmStatistic, user));// 当前用户接收的所有任务
		}
		return jsonObject.toString();
	}

	/**
	 * 根据权限计算总任务数
	 * 
	 * @param alarmStatistic
	 * @param user
	 * @return
	 */
	private Integer calTotalNum(AlarmStatistic alarmStatistic, User user) {
		Integer wait_audit_bill_task_num = alarmStatistic.getWait_audit_bill_task_num();// 待审核发票任务
		Integer assistant_task_num = alarmStatistic.getAssistant_task_num();// 文书任务
		Integer manager_control_task_num = alarmStatistic.getManager_control_task_num();// 执行管控任务
		Integer bill_task_num = alarmStatistic.getBill_task_num();// 发票任务
		Integer other_task_num = alarmStatistic.getOther_task_num();// 普通任务
		Integer remo_task_num = alarmStatistic.getRemo_task_num();// 待核对到款任务

		String result = "";
		String permission = user.getRole().getRole_permission();
		if (permission != null && !permission.equals("")) {
			result = numToPermissionStr(permission);
		}

		Integer total_num = other_task_num;// 每个人都有普通任务
		if (result.contains("iAssiTask")) {
			total_num += assistant_task_num;
		}
		if (result.contains("iEditTask")) {
			total_num += manager_control_task_num;
		}
		if (result.contains("iAudiInvoTask")) {
			total_num += wait_audit_bill_task_num;
		}
		if (result.contains("iFiniInvoTask")) {
			total_num += bill_task_num;
		}
		if (result.contains("iFiniRemoTask")) {
			total_num += remo_task_num;
		}

		return total_num;
	}

	/**
	 * 获取当前用户权限
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getUserPermission.do")
	public @ResponseBody String getUserPermission(HttpServletRequest request, HttpSession session) {
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		String result = "";
		String permission = "";
		if (user.getRole().getRole_permission() != null && !user.getRole().getRole_permission().equals("")) {
			permission = user.getRole().getRole_permission();
			result = numToPermissionStr(permission);
		}
		return JSON.toJSONString(result + " ");
	}

	/**
	 * 获取当前用户权限
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getLeftbarPermission.do")
	public @ResponseBody String getLeftbarPermission(HttpServletRequest request, HttpSession session) {
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		String result = "";
		String permission = "";
		if (user.getRole().getRole_permission() != null && !user.getRole().getRole_permission().equals("")) {
			permission = user.getRole().getRole_permission();
			result = leftPermissionStr(permission);
		}
		return JSON.toJSONString(result + " ");
	}

	public static String leftPermissionStr(String permissionNum) {
		String result = "";
		JSONObject jsonObject = JSONObject.fromObject(permissionNum);
		if (jsonObject.containsKey("left_per"))
			result = toPermissionStr(jsonObject.getString("left_per"), PermissionConstants.left, result);
		if (result.equals("")) {
			return " ";
		} else {
			return result.substring(0, result.length() - 1);
		}
	}

	public static String numToPermissionStr(String permissionNum) {
		String result = "";
		JSONObject jsonObject = JSONObject.fromObject(permissionNum);
		if (jsonObject.containsKey("con_per"))
			result = toPermissionStr(jsonObject.getString("con_per"), PermissionConstants.contract, result);
		if (jsonObject.containsKey("task_per"))
			result = toPermissionStr(jsonObject.getString("task_per"), PermissionConstants.task, result);
		if (jsonObject.containsKey("bill_per"))
			result = toPermissionStr(jsonObject.getString("bill_per"), PermissionConstants.bill, result);
		if (jsonObject.containsKey("system_per"))
			result = toPermissionStr(jsonObject.getString("system_per"), PermissionConstants.system, result);
		if (jsonObject.containsKey("index_per"))
			result = toPermissionStr(jsonObject.getString("index_per"), PermissionConstants.index, result);
		return result + " ";
	}

	private static String toPermissionStr(String str, String type, String result) {
		String subStr = str.substring(1, str.length() - 1);
		String strArr[] = subStr.split(",");
		StringBuilder strb = new StringBuilder();
		strb.append(result);
		for (int i = 0; i < strArr.length; i++) {
			if (strArr[i].equals("1")) {
				switch (type) {
				case "contPer":
					strb.append(" " + PermissionConstants.contPer[i]);
					break;
				case "taskPer":
					strb.append(" " + PermissionConstants.taskPer[i]);
					break;
				case "billPer":
					strb.append(" " + PermissionConstants.billPer[i]);
					break;
				case "systemPer":
					strb.append(" " + PermissionConstants.systemPer[i]);
					break;
				case "indexPer":
					strb.append(" " + PermissionConstants.indexPer[i]);
					break;
				case "leftPer":
					strb.append(PermissionConstants.leftPer[i] + ",");
					break;
				default:
					break;
				}
			}
		}
		return strb.toString();
	}
}
