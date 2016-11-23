/**
 * 
 */
package com.mvc.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.base.constants.SessionKeyConstants;
import com.base.enums.IsDelete;
import com.base.enums.TaskStatus;
import com.base.enums.TaskType;
import com.mvc.entity.Contract;
import com.mvc.entity.SubTask;
import com.mvc.entity.Task;
import com.mvc.entity.User;
import com.mvc.service.SubTaskService;
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
	@Autowired
	SubTaskService subTaskService;

	/**
	 * zq接收任务起始页
	 * 
	 * @return
	 */
	@RequestMapping("/toTaskPage.do")
	public String managerReceivePage() {
		return "taskInformation/index";
	}

	/**
	 * 根据用户ID和状态筛选任务列表,task_state:0 表示为接收，1表示执行中，2表示已完成
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/selectTaskByState.do")
	public @ResponseBody String getTasks(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		Integer taskState = Integer.valueOf(request.getParameter("taskState"));
		Integer sendOrReceive = Integer.valueOf(request.getParameter("sendOrReceive"));
		Integer totalRow = taskService.countByParam(user.getUser_id(), taskState, null, sendOrReceive);// 0表示发送，1表示接收
		Pager pager = new Pager();
		pager.setPage(Integer.valueOf(request.getParameter("page")));
		pager.setTotalRow(totalRow);
		List<Task> list = taskService.findByPage(user.getUser_id(), taskState, null, pager.getOffset(),
				pager.getLimit(), sendOrReceive);
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/**
	 * 根据页数,状态，关键字返回任务列表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/selectTaskByKeys.do")
	public @ResponseBody String getTasksBySearchKey(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		Integer taskState = Integer.valueOf(request.getParameter("taskState"));
		Integer sendOrReceive = Integer.valueOf(request.getParameter("sendOrReceive"));
		String searchKey = request.getParameter("context");
		Integer totalRow = taskService.countByParam(user.getUser_id(), taskState, searchKey, sendOrReceive);
		Pager pager = new Pager();
		pager.setPage(Integer.valueOf(request.getParameter("page")));
		pager.setTotalRow(totalRow);
		List<Task> list = taskService.findByPage(user.getUser_id(), taskState, searchKey, pager.getOffset(),
				pager.getLimit(), sendOrReceive);
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/**
	 * 根据合同ID和任务类型返回任务列表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/selectTaskByConId.do")
	public @ResponseBody String getTasksByContId(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		Integer contId = Integer.valueOf(request.getParameter("contId"));
		Integer taskType = Integer.valueOf(request.getParameter("taskType"));
		List<Task> list = taskService.findByContIdAndType(user.getUser_id(), contId, taskType);
		jsonObject.put("list", list);
		return jsonObject.toString();
	}

	/**
	 * 根据任务ID查询任务详情
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/selectTaskById.do")
	public @ResponseBody String findByTaskId(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		Integer taskId = Integer.valueOf(request.getParameter("ID"));
		Task task = taskService.findById(taskId);
		if (task.getTask_state() == TaskStatus.waitingReceipt.value
				&& task.getReceiver().getUser_id() == user.getUser_id()) {
			taskService.updateState(taskId, TaskStatus.dealing.value);
		}
		jsonObject.put("task", task);
		return jsonObject.toString();
	}

	/**
	 * 创建任务
	 * 
	 * @param request
	 * @param session
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/addTask.do")
	public @ResponseBody String addTask(HttpServletRequest request, HttpSession session) throws ParseException {
		JSONObject result = new JSONObject();
		JSONObject jsonObject = new JSONObject();
		Task task = new Task();
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		long time = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Contract contract = new Contract();
		if (request.getParameter("conId") != "") {
			contract.setCont_id(Integer.valueOf(request.getParameter("conId")));
			task.setContract(contract);
		}
		Integer taskType = Integer.valueOf(request.getParameter("taskType"));
		task.setTask_type(taskType);

		task.setCreator(user);
		jsonObject = JSONObject.fromObject(request.getParameter("task"));
		User receiver = new User();
		receiver.setUser_id(Integer.valueOf(jsonObject.getString("receiver_id")));
		task.setReceiver(receiver);
		task.setTask_ctime(new Date(time));
		Date sdate = format.parse(jsonObject.getString("task_stime"));
		Date edate = format.parse(jsonObject.getString("task_etime"));
		if (jsonObject.containsKey("task_content")) {
			task.setTask_content(jsonObject.getString("task_content"));// 备注，张群刘亚赢不统一
		}
		task.setTask_stime(sdate);
		task.setTask_etime(edate);
		task.setTask_isdelete(IsDelete.NO.value);
		task.setTask_state(TaskStatus.waitingReceipt.value);
		task.setTask_alarmnum(0);
		if (taskType != TaskType.assistants.value) {// 0代表普通任务；2代表执行管控任务
			Task taskResult = taskService.save(task);
			if (taskResult.getTask_id() != null) {
				result.put("result", "true");
			} else {
				result.put("result", "false");
			}
		} else {// 1代表文书任务
			List<String> subTasks = new ArrayList<String>();
			if (jsonObject.containsKey("print")) // 打印
				subTasks.add("print");
			if (jsonObject.containsKey("sign")) // 签字
				subTasks.add("sign");
			if (jsonObject.containsKey("seal")) // 盖章
				subTasks.add("seal");
			if (jsonObject.containsKey("post")) // 邮寄
				subTasks.add("post");
			if (jsonObject.containsKey("file")) // 归档（已返回）
				subTasks.add("file");
			Task taskResult = taskService.save(task);
			boolean flag = false;
			for (int j = 0; j < subTasks.size(); j++) {
				SubTask subTask = new SubTask();
				subTask.setSuta_content(subTasks.get(j));
				subTask.setSuta_state(0);
				subTask.setTask(taskResult);
				flag = subTaskService.save(subTask);
			}
			if (flag) {
				result.put("result", "true");
			} else
				result.put("result", "false");
		}
		return result.toString();
	}

	/**
	 * 删除任务
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/deleteTask.do")
	public @ResponseBody String delete(HttpServletRequest request, HttpSession session) {
		Integer taskId = Integer.valueOf(request.getParameter("taskId"));
		boolean result = taskService.delete(taskId);
		return JSON.toJSONString(result);
	}

	/**
	 * 完成任务确认
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/finishTask.do")
	public @ResponseBody String finishTask(HttpServletRequest request, HttpSession session) {
		Integer taskId = Integer.valueOf(request.getParameter("taskId"));
		boolean result = taskService.updateState(taskId, TaskStatus.finish.value);
		return JSON.toJSONString(result);

	}

}
