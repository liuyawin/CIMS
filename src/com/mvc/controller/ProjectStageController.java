package com.mvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.constants.SessionKeyConstants;
import com.base.enums.RemoveType;
import com.mvc.entity.ProjectStage;
import com.mvc.entity.User;
import com.mvc.service.AlarmService;
import com.mvc.service.ContractService;
import com.mvc.service.ProjectStageService;
import com.mvc.service.UserService;

import net.sf.json.JSONObject;

/**
 * 工期阶段控制器
 * 
 * @author wangrui
 * @date 2016-09-20
 */
@Controller
@RequestMapping("/projectStage")
public class ProjectStageController {

	@Autowired
	UserService userService;
	@Autowired
	ContractService contractService;
	@Autowired
	ProjectStageService projectStageService;
	@Autowired
	AlarmService alarmService;

	/**
	 * 添加工期阶段
	 * 
	 * @param request
	 * @param session
	 * @return true,false
	 */
	@RequestMapping("/addProjectStage.do")
	public @ResponseBody String addProjectStage(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = JSONObject.fromObject(request.getParameter("projectStage"));
		Integer cont_id = Integer.valueOf(request.getParameter("cont_id"));
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		boolean flag = projectStageService.addProjectStage(jsonObject, cont_id, user);
		return String.valueOf(flag);
	}

	/**
	 * 查询该合同的工期阶段
	 * 
	 * @param request
	 * @param session
	 * @return 工期阶段list
	 */
	@RequestMapping("/selectPrstByContId.do")
	public @ResponseBody String selectPrstByContId(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		List<ProjectStage> list = projectStageService
				.selectPrstByContId(Integer.parseInt(request.getParameter("cont_id")));
		jsonObject.put("list", list);
		return jsonObject.toString();
	}

	/**
	 * 修改成完成工期
	 * 
	 * @param request
	 * @return true、false
	 */
	@RequestMapping("/finishPrst.do")
	public @ResponseBody String finishPrst(HttpServletRequest request) {
		Integer prstId = Integer.parseInt(request.getParameter("prstId"));
		boolean flag = projectStageService.updatePrstState(prstId);
		alarmService.updateByIdType(prstId, RemoveType.PrstAlarm.value);// 解除报警
		return String.valueOf(flag);
	}

	/**
	 * 删除工期
	 * 
	 * @param request
	 * @return true、false
	 */
	@RequestMapping("/delPrst.do")
	public @ResponseBody String delPrst(HttpServletRequest request) {
		Integer prstId = Integer.parseInt(request.getParameter("prstId"));
		boolean flag = projectStageService.deletePrstState(prstId);
		alarmService.updateByIdType(prstId, RemoveType.PrstAlarm.value);// 解除报警
		return String.valueOf(flag);
	}

	/**
	 * 修改工期
	 * 
	 * @param request
	 * @return true、false
	 */
	@RequestMapping("/modifyPrst.do")
	public @ResponseBody String updatePrst(HttpServletRequest request) {
		Integer prst_id = Integer.parseInt(request.getParameter("prstId"));
		JSONObject jsonObject = JSONObject.fromObject(request.getParameter("projectStage"));

		boolean flag = projectStageService.updatePrst(jsonObject, prst_id);
		return String.valueOf(flag);
	}

	/**
	 * 根据ID查找工期阶段
	 * 
	 * @param request
	 * @return projectStage对象
	 */
	@RequestMapping("/selectPrstById.do")
	public @ResponseBody String selectPrstById(HttpServletRequest request) {
		Integer prstId = Integer.parseInt(request.getParameter("prstId"));
		JSONObject jsonObject = new JSONObject();
		ProjectStage projectStage = projectStageService.selectPrstById(prstId);
		jsonObject.put("projectStage", projectStage);
		return jsonObject.toString();
	}
}
