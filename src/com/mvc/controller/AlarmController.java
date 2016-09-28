package com.mvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.constants.SessionKeyConstants;
import com.mvc.entity.Alarm;
import com.mvc.entity.User;
import com.mvc.service.AlarmService;
import com.utils.Pager;

import net.sf.json.JSONObject;

/**
 * 报警
 * 
 * @author wanghuimin
 * @date 2016年9月26日
 */
@Controller
@RequestMapping("/alarm")
public class AlarmController {
	@Autowired
	AlarmService alarmService;

	/**
	 * 报警页面跳转
	 * 
	 * @return
	 */
	@RequestMapping("/toAssistant2AlarmPage.do")
	public String alarmJumpPage() {
		return "assistant2/alarmInformation/index";
	}
	/**
	 * 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping("/alarm/toalarmPage.do")
	public String  alarmJumpToPage(){
		return "userManagement/alarmInfo/index";
	}

	/**
	 * 查找报警信息列表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/selectAlarmByState.do")
	public @ResponseBody String getAlarmInformationList(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Integer isremove=Integer.valueOf(request.getParameter("isRemove"));
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		Long totalPage = alarmService.countTotal(user.getUser_id());
		Pager pager = new Pager();
		pager.setPage(Integer.valueOf(request.getParameter("page")));
		pager.setTotalRow(Integer.valueOf(totalPage.toString()));
		List<Alarm> list = alarmService.findAlarmInformationList(user.getUser_id(), isremove,pager.getOffset(),
				pager.getLimit());
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/**
	 * 根据ID查看报警详情
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/selectAlarmById.do")
	public @ResponseBody String getAlarmContentById(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Integer alarmid = Integer.valueOf(request.getParameter("alarId"));
		Alarm alarm = alarmService.findAlarmContentById(alarmid);
		jsonObject.put("alarm", alarm);
		return jsonObject.toString();
	}
	
	/**
	 * 根据用户名查找报警信息
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/getAlarmByUser.do")
	public @ResponseBody String getAlarmByUser(HttpServletRequest request,HttpSession session){
		JSONObject jsonObject=new JSONObject();
		String username=request.getParameter("userName");
		Integer totalPage=alarmService.countTotalNum(username);
		Pager pager=new Pager();
		pager.setPage(Integer.valueOf(request.getParameter("page")));
		pager.setTotalRow(Integer.valueOf(totalPage.toString()));
		List<Alarm> list=alarmService.findAlarmByUser(username,pager.getOffset(),pager.getLimit());
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

}
