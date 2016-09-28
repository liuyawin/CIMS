package com.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.mvc.entity.AlarmLevel;
import com.mvc.entity.Role;
import com.mvc.service.AlarmLevelService;

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
	 * 添加，修改报警等级
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/addAlarmLevel.do")
	public @ResponseBody String addUser(HttpServletRequest request, HttpSession session) {
		AlarmLevel alarmLevel = new AlarmLevel();
		alarmLevel.setAlle_days(Integer.parseInt(request.getParameter("alle_days")));
		alarmLevel.setAlle_rank(Integer.parseInt(request.getParameter("alle_rank")));
		Role role = new Role();
		role.setRole_id(Integer.parseInt(request.getParameter("role_id")));
		alarmLevel.setRole(role);
		boolean result;
		if (request.getParameter("alle_id") != null) {
			alarmLevel.setAlle_id(Integer.valueOf(request.getParameter("alle_id")));
			result = alarmLevelService.save(alarmLevel);// 修改报警等级
		} else {
			result = alarmLevelService.save(alarmLevel);// 添加报警等级
		}
		return JSON.toJSONString(result);
	}

}
