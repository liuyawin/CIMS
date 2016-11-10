package com.mvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.base.enums.IsDelete;
import com.mvc.entity.AlarmLevel;
import com.mvc.entity.Role;
import com.mvc.service.AlarmLevelService;

import net.sf.json.JSONObject;

/**
 * 报警等级
 * 
 * @author wanghuimin
 * @date 2016年9月22日
 */
@Controller
@RequestMapping("/alarmLevel")
public class AlarmLevelController {
	@Autowired
	AlarmLevelService alarmLevelService;

	/**
	 * 页面跳转
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/toAlarmSetPage.do")
	public String alarmLevelJumpPage() {
		return "systemManagement/index";
	}

	/**
	 * 添加，修改报警等级
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/alarmLevelAdd.do")
	public @ResponseBody String addUser(HttpServletRequest request, HttpSession session) {
		AlarmLevel alarmLevel = new AlarmLevel();
		alarmLevel.setAlle_days(Integer.parseInt(request.getParameter("alle_days")));
		alarmLevel.setAlle_rank(Integer.parseInt(request.getParameter("alle_rank")));
		Role role = new Role();
		role.setRole_id(Integer.parseInt(request.getParameter("role_id")));
		alarmLevel.setRole(role);
		alarmLevel.setAlle_isdelete(IsDelete.NO.value);
		boolean result;
		if (request.getParameter("alle_id") != null) {
			alarmLevel.setAlle_id(Integer.valueOf(request.getParameter("alle_id")));
			result = alarmLevelService.save(alarmLevel);// 修改报警等级
		} else {
			result = alarmLevelService.save(alarmLevel);// 添加报警等级
		}
		return JSON.toJSONString(result);
	}

	/**
	 * 获取报警等级列表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/selectAllAlarmLevel.do")
	public @ResponseBody String getAlarmLevelList(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		List<AlarmLevel> result = alarmLevelService.findAlarmLevelList();
		jsonObject.put("list", result);
		return jsonObject.toString();
	}

	/**
	 * 根据id获取报警等级对象
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getAlarmLevelByID.do")
	public @ResponseBody String getgetAlarmLevelById(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Integer alleid = Integer.valueOf(request.getParameter("alle_id"));
		AlarmLevel alarmLevel = alarmLevelService.findAlarmLevelById(alleid);
		jsonObject.put("alarmLevel", alarmLevel);
		return jsonObject.toString();
	}

	/**
	 * 根据id删除报警等级
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/deleteAlarmLevel.do")
	public @ResponseBody String deleteAlarmLevelById(HttpServletRequest request, HttpSession session) {
		Integer alleid = Integer.valueOf(request.getParameter("alle_id"));
		boolean result = alarmLevelService.deleteAlarmLevelById(alleid);
		return JSON.toJSONString(result);
	}

}
