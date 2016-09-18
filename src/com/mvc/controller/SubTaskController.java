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
import com.mvc.entity.SubTask;
import com.mvc.service.SubTaskService;

import net.sf.json.JSONObject;

/**
 * 文书任务子任务
 * 
 * @author zjn
 * @date 2016年9月17日
 */
@Controller
@RequestMapping("/subTask")
public class SubTaskController {
	@Autowired
	SubTaskService subTaskService;

	/**
	 * 根据任务ID获取子任务列表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/selectSubTask.do")
	public @ResponseBody String getTasks(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Integer taskId = Integer.valueOf(request.getParameter("taskId"));
		List<SubTask> list = subTaskService.findByTaskId(taskId);
		jsonObject.put("list", list);
		System.out.println("返回列表:" + jsonObject.toString());
		return jsonObject.toString();
	}

	/**
	 * 根据子任务id修改状态,0改为1表示未完成改为已完成
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/updateState.do")
	public @ResponseBody String updateState(HttpServletRequest request, HttpSession session) {
		Integer subTaskId = Integer.valueOf(request.getParameter("subTaskId"));
		boolean result = subTaskService.updateState(subTaskId);
		return JSON.toJSONString(result);
	}
}
