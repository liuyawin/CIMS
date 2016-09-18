/**
 * 
 */
package com.mvc.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	 * 设置进入接收任务起始页
	 * 
	 * @return
	 */
	@RequestMapping("/toTaskReceivePage.do")
	public String taskReceivePage() {
		return "assistant2/taskReceiveInformation/index";
	}

	/**
	 * 设置进入发送任务起始页
	 * 
	 * @return
	 */
	@RequestMapping("/toTaskSendPage.do")
	public String taskInSendPage() {
		return "assistant2/taskSendInformation/index";
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
		Integer totalRow = taskService.countByParam(user.getUser_id(), taskState, null, sendOrReceive);
		System.out.println("总数" + totalRow);
		Pager pager = new Pager();
		pager.setPage(Integer.valueOf(request.getParameter("page")));
		pager.setTotalRow(totalRow);
		List<Task> list = taskService.findByPage(user.getUser_id(), taskState, null, pager.getOffset(),
				pager.getLimit(), sendOrReceive);
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		System.out.println("返回列表:" + jsonObject.toString());
		return jsonObject.toString();
	}

	/**
	 * 根据页数,状态，关键字返回任务列表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/selectTaskByContext.do")
	public @ResponseBody String getTasksBySearchKey(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		Integer taskState = Integer.valueOf(request.getParameter("taskState"));
		Integer sendOrReceive = Integer.valueOf(request.getParameter("sendOrReceive"));
		String searchKey = request.getParameter("context");
		Integer totalRow = taskService.countByParam(user.getUser_id(), taskState, searchKey, sendOrReceive);
		System.out.println("总数" + totalRow);
		Pager pager = new Pager();
		pager.setPage(Integer.valueOf(request.getParameter("page")));
		pager.setTotalRow(totalRow);
		List<Task> list = taskService.findByPage(user.getUser_id(), taskState, searchKey, pager.getOffset(),
				pager.getLimit(), sendOrReceive);
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		System.out.println("返回列表:" + jsonObject.toString());
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
		Integer taskId = Integer.valueOf(request.getParameter("ID"));
		Task task = taskService.findById(taskId);
		jsonObject.put("task", task);
		System.out.println("返回列表:" + jsonObject.toString());
		return jsonObject.toString();
	}

	/**
	 * 创建普通任务
	 * 
	 * @param request
	 * @param session
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/createNormalTask.do")
	public @ResponseBody String save(HttpServletRequest request, HttpSession session) throws ParseException {
		JSONObject result = new JSONObject();
		JSONObject jsonObject = JSONObject.fromObject(request.getParameter("task"));
		long time = System.currentTimeMillis();
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		Task task = new Task();
		// if (!request.getParameter("cont_id").equals("")) {
		// Contract contract = new Contract();
		// contract.setCont_id(Integer.valueOf(jsonObject.getString("cont_id")));
		// task.setContract(contract);
		// }
		task.setCreator(user);
		User receiver = new User();
		receiver.setUser_id(Integer.valueOf(jsonObject.getString("receiver_id")));
		task.setReceiver(receiver);
		task.setTask_content(jsonObject.getString("task_content"));
		task.setTask_type(Integer.valueOf(jsonObject.getString("task_type")));
		task.setTask_ctime(new Date(time));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date sdate = format.parse(jsonObject.getString("task_stime"));
		Date edate = format.parse(jsonObject.getString("task_etime"));
		task.setTask_stime(sdate);
		task.setTask_etime(edate);
		task.setTask_isdelete(0);
		task.setTask_state(0);
		task.setTask_alarmnum(0);
		Task taskResult = taskService.save(task);
		if (taskResult.getTask_id() != null) {
			// if (taskType != 1) {
			result.put("result", "true");
			System.out.println("普通任务创建成功");
			// } else {
			// result.put("result", taskResult.getTask_id());
			// SubTask subTask = new SubTask();
			// subTask.setSuta_content(request.getParameter("suta_content"));
			// subTask.setSuta_state(0);
			// subTask.setSuta_remark(request.getParameter("suta_remark"));
			// subTask.setTask(taskResult);
			// result.put("result", subTaskService.save(subTask,
			// taskResult.getTask_id()));
			// System.out.println("文书任务创建成功，任务的id是：" + taskResult.getTask_id());
			// }
		} else {
			result.put("result", "false");
			System.out.println("普通任务创建失败");
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

}
