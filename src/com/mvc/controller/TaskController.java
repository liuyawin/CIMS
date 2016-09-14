/**
 * 
 */
package com.mvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.constants.SessionKeyConstants;
import com.mvc.entity.Task;
import com.mvc.entity.User;
import com.mvc.service.TaskService;
import com.utils.Pager;

import net.sf.json.JSONObject;

/**
 * 任务相关
 * 
 * @author zjn
 * @date 2016年9月12日
 */
@Controller
@RequestMapping("/task")
public class TaskController {
	@Autowired
	TaskService taskService;

	// 根据用户ID和状态筛选任务列表,task_state:0 表示为接收，1表示执行中，2表示已完成
	@RequestMapping(value = "/selectTaskByState.do")

	public @ResponseBody JSONObject getStores(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		int totalRow = taskService.countTotal();
		Pager pager = new Pager();
		pager.setPage(1);
		pager.setTotalRow(totalRow);
		Integer taskState = Integer.valueOf(request.getParameter("taskState"));
		List<Task> list = taskService.findByPage(user.getUser_id(), taskState, pager.getOffset(), pager.getLimit());
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		System.out.println("返回列表和总页数:" + jsonObject.toString());
		return jsonObject;
	}
}
