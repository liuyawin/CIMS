package com.mvc.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.constants.SessionKeyConstants;
import com.base.enums.RemoveType;
import com.mvc.entity.Contract;
import com.mvc.entity.ProjectStage;
import com.mvc.entity.User;
import com.mvc.service.AlarmService;
import com.mvc.service.ContractService;
import com.mvc.service.ProjectStageService;
import com.mvc.service.UserService;

import net.sf.json.JSONArray;
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
	@SuppressWarnings("unchecked")
	@RequestMapping("/addProjectStage.do")
	public @ResponseBody String addProjectStage(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = JSONObject.fromObject(request.getParameter("projectStage"));
		JSONArray arr = jsonObject.getJSONArray("stages");
		Iterator<Object> it = arr.iterator();

		ProjectStage projectStage = null;
		JSONObject stage = null;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		long time = System.currentTimeMillis();
		boolean flag = true;
		while (it.hasNext()) {
			stage = (JSONObject) it.next();
			projectStage = new ProjectStage();
			try {
				if (stage.containsKey("prst_content")) {
					projectStage.setPrst_content(stage.getString("prst_content"));// 阶段内容
				}
				if (stage.containsKey("prst_etime")) {
					Date date = format.parse(stage.getString("prst_etime"));// 阶段截止时间
					projectStage.setPrst_etime(date);// 阶段截止时间
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
					int days = 0;
					if (stage.containsKey("prst_wday")) {
						days = Integer.parseInt(stage.getString("prst_wday"));// 完工提醒天数
					}
					projectStage.setPrst_wday(days);// 添加完工提醒的天数
					calendar.add(Calendar.DAY_OF_MONTH, -days);// 工作结束提醒时间=阶段截止时间-完工提醒天数
					projectStage.setPrst_wtime(calendar.getTime());// 工作结束提醒时间
				}
				projectStage.setPrst_ctime(new Date(time));// 阶段录入时间
				projectStage.setPrst_state(0);// 默认未完成
				User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);// 录入人
				projectStage.setUser(user);
				projectStage.setPrst_isdelete(0);// 默认未删除
				Contract contract = contractService.selectContById(Integer.parseInt(request.getParameter("cont_id")));// 所属合同
				projectStage.setContract(contract);
				if (contract.getManager() != null) {// 先判断是否有项目经理
					User manager = userService.findById(contract.getManager().getUser_id());// 项目经理已经包含在合同里面
					projectStage.setManager(manager);
				}
				// 写入数据库
				flag = projectStageService.addProjectStage(projectStage);
				if (flag == false) {
					return "false";
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return "true";
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
		ProjectStage projectStage = projectStageService.selectPrstById(prst_id);
		JSONObject jsonObject = JSONObject.fromObject(request.getParameter("projectStage"));
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (jsonObject != null) {
			try {
				if (jsonObject.containsKey("prst_content")) {
					projectStage.setPrst_content(jsonObject.getString("prst_content"));// 工期阶段内容
				}
				Date date = null;
				if (jsonObject.containsKey("prst_etime")) {
					date = format.parse(jsonObject.getString("prst_etime"));// 阶段截止时间
					projectStage.setPrst_etime(date);
				} else {
					date = projectStage.getPrst_etime();
				}
				if (jsonObject.containsKey("prst_wday")) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
					int days = Integer.parseInt(jsonObject.getString("prst_wday"));// 完工提醒天数
					projectStage.setPrst_wday(days);// 添加完工提醒的天数
					calendar.add(Calendar.DAY_OF_MONTH, -days);// 工作结束提醒时间=阶段截止时间-完工提醒天数
					projectStage.setPrst_wtime(calendar.getTime());// 工作结束提醒时间
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		boolean flag = projectStageService.addProjectStage(projectStage);
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
