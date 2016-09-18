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
import com.mvc.service.UserService;
import com.utils.Pager;

import net.sf.json.JSONObject;

import com.alibaba.fastjson.JSON;

/**
 * 用户相关
 * 
 * @author wanghuimin
 * @date 2016年9月7日
 */

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;

	//根据页数筛选用户列表
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
	//获取用户列表，无翻页功能
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
	 * 添加合同
	 * 
	 * @param request
	 * @param session
	 * @return 合同ID
	 */
//	@RequestMapping("/addContract.do")
//	public @ResponseBody Integer addContract(HttpServletRequest request, HttpSession session) {
//		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
//		JSONObject jsonObject = new JSONObject();
//		jsonObject = JSONObject.fromObject(request.getParameter("contract"));
//		long time = System.currentTimeMillis();
//		Contract contract = new Contract();
//		contract.setCont_name(jsonObject.getString("cont_name"));// 合同名称
//		contract.setCont_project(jsonObject.getString("cont_project"));// 项目名称
//		ContractType ct = Enum.valueOf(ContractType.class, jsonObject.getString("cont_type"));
//		contract.setCont_type(ct.value);// 枚举,合同的类型
//		contract.setCont_cheader(jsonObject.getString("cont_cheader"));// 业主联系人
//		contract.setCont_ctel(jsonObject.getString("cont_ctel"));// 业主联系方式
//		contract.setCont_cdept(jsonObject.getString("cont_cdept"));// 业主联系部门
//		contract.setCont_rank(jsonObject.getInt("cont_rank"));// 等级
//		contract.setCont_initiation(1);// 已立项
//		contract.setCont_ishistory(0);// 未删除
//		contract.setCont_ctime(new Date(time));// 合同创建时间
//		contract.setCreator(user);// 合同创建者
//		contractService.addContract(contract);
//		return contract.getCont_id();
//	}
//	
//	
	
	
	
	
	
	//添加用户
	@RequestMapping(value = "/addUser.do")
	public @ResponseBody String addDepart(HttpServletRequest request, HttpSession session) {
		User user=new User();
		user.setUser_num(request.getParameter("user_num"));
		user.setUser_name(request.getParameter("user_name"));
		user.setUser_pwd(request.getParameter("user_pwd"));
		user.setUser_sex(Integer.valueOf(request.getParameter("user_sex")));
		user.setUser_tel(request.getParameter("user_tel"));
		user.setUser_email(request.getParameter("user_email"));
		Role role=new Role();
		role.setRole_id(Integer.valueOf(request.getParameter("role_id")));
		user.setRole(role);
		user.setUser_isdelete(0);
//		user.setUser_permission(request.getParameter("user_permission"));		
		boolean result = userService.save(user);
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
		List<User> result = userService.findUserFromDesign();
		System.out.println(JSON.toJSONString(result));
		return JSON.toJSONString(result);
	}
	
}