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
					if (stage.containsKey("prst_wday")) {
						int days = Integer.parseInt(stage.getString("prst_wday"));// 完工提醒天数
						projectStage.setPrst_wday(days);// 添加完工提醒的天数
						calendar.add(Calendar.DAY_OF_MONTH, -days);// 工作结束提醒时间=阶段截止时间-完工提醒天数
						projectStage.setPrst_wtime(calendar.getTime());// 工作结束提醒时间
					}
				}
				projectStage.setPrst_ctime(new Date(time));// 阶段录入时间
				projectStage.setPrst_state(0);
				User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);// 录入人
				projectStage.setUser(user);
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
		alarmService.updateByIdType(prstId, RemoveType.PrstAlarm.value);
		return String.valueOf(flag);
	}

}
