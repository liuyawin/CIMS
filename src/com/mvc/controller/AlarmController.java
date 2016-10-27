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
	 * 设置进入报警起始页
	 * 
	 * @return
	 */
	@RequestMapping("/toAlarmPage.do")
	public String departmentReceivePage() {
		return "alarmInformation/index";
	}

	/**
	 * 查找报警信息列表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/selectAlarmByType.do")
	public @ResponseBody String getAlarmInformationList(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		String alarmType = request.getParameter("alarmType");
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		String searchKey = request.getParameter("searchKey");
		Integer totalRow = alarmService.countByParam(user.getUser_id(), alarmType, searchKey);
		Pager pager = new Pager();
		pager.setPage(Integer.valueOf(request.getParameter("page")));
		pager.setTotalRow(totalRow);
		List<Alarm> list = alarmService.findAlarmList(user.getUser_id(), searchKey, alarmType, pager.getOffset(),
				pager.getLimit());
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		jsonObject.put("totalRow", totalRow);
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
		Alarm alarm = alarmService.findAlarmById(alarmid);
		jsonObject.put("alarm", alarm);
		return jsonObject.toString();
	}
}
