/**
 * 
 */
package com.mvc.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.base.enums.ConExecStatus;
import com.base.enums.RemoveType;
import com.base.enums.TaskStatus;
import com.mvc.entity.SubTask;
import com.mvc.entity.Task;
import com.mvc.service.AlarmService;
import com.mvc.service.ContractService;
import com.mvc.service.SubTaskService;
import com.mvc.service.TaskService;

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
	@Autowired
	TaskService taskService;
	@Autowired
	AlarmService alarmService;
	@Autowired
	ContractService contractService;

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
		Task task = taskService.findById(taskId);
		if (task.getTask_state() == 0)
			taskService.updateState(taskId, TaskStatus.dealing.value);
		List<SubTask> list = subTaskService.findByTaskId(taskId);
		jsonObject.put("list", list);
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
		JSONObject jsonObject = new JSONObject();
		jsonObject = JSONObject.fromObject(request.getParameter("task"));
		JSONObject jsonObject1 = JSONObject.fromObject(jsonObject.getString("task1"));
		Integer taskId = Integer.parseInt(jsonObject.getString("taskId"));
		JSONObject jsonObject2 = JSONObject.fromObject(jsonObject.getString("subTaskId"));
		List<Integer> subTasks = new ArrayList<Integer>();
		if (jsonObject1.getString("print").equals("true")) // 打印
		{
			subTasks.add(Integer.valueOf(jsonObject2.getString("print")));
			updateContIsback(taskId, ConExecStatus.print.value);
		}
		if (jsonObject1.getString("sign").equals("true")) // 签字
		{
			subTasks.add(Integer.valueOf(jsonObject2.getString("sign")));
			updateContIsback(taskId, ConExecStatus.sign.value);
		}
		if (jsonObject1.getString("seal").equals("true")) // 盖章
		{
			subTasks.add(Integer.valueOf(jsonObject2.getString("seal")));
			updateContIsback(taskId, ConExecStatus.seal.value);
		}
		if (jsonObject1.getString("post").equals("true")) // 邮寄
		{
			subTasks.add(Integer.valueOf(jsonObject2.getString("post")));
			updateContIsback(taskId, ConExecStatus.post.value);
		}
		if (jsonObject1.getString("file").equals("true")) // 归档(已返回)
		{
			subTasks.add(Integer.valueOf(jsonObject2.getString("file")));
			updateContIsback(taskId, ConExecStatus.file.value);
		}
		boolean result = false;
		for (int j = 0; j < subTasks.size(); j++) {
			result = subTaskService.updateState(subTasks.get(j));
		}

		List<SubTask> list = subTaskService.findByTaskId(taskId);
		boolean flag = true;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getSuta_state() == 0) {
				flag = false;
				break;
			}
		}
		if (flag) {
			taskService.updateState(taskId, TaskStatus.finish.value);
			alarmService.updateByIdType(taskId, RemoveType.TaskAlarm.value);
		}
		return JSON.toJSONString(result);
	}

	// 根据任务Id和子任务完成情况更新合同cont_isback字段
	private void updateContIsback(Integer taskId, Integer state) {
		Task task = taskService.findById(taskId);
		Integer contId = task.getContract().getCont_id();
		contractService.updateContIsback(contId, state);
	}
}
