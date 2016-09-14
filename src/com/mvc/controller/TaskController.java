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

import com.alibaba.fastjson.JSON;
import com.base.constants.SessionKeyConstants;
import com.mvc.entity.Task;
import com.mvc.entity.User;
import com.mvc.service.TaskService;

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
	public @ResponseBody String getStores(HttpServletRequest request, HttpSession session) {
		
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		Integer taskState = Integer.valueOf(request.getParameter("taskState"));
		System.out.println("进来了"+taskState+"用户ID"+user.getUesr_id());
		List<Task> result = taskService.findTaskByState(user.getUesr_id(), taskState);
		System.out.println("任务的个数"+result.size());
		return JSON.toJSONString(result);
	}

}
