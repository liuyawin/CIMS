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
		JSONObject jsonObject = new JSONObject();
		long time = System.currentTimeMillis();
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		Task task = new Task();
		if (!request.getParameter("cont_id").equals("")) {
			Contract contract = new Contract();
			contract.setCont_id(Integer.valueOf(request.getParameter("cont_id")));
			task.setContract(contract);
		}
		task.setCreator(user);
		User receiver = new User();
		receiver.setUser_id(Integer.valueOf(request.getParameter("user_id")));
		task.setReceiver(receiver);
		task.setTask_content(request.getParameter("task_content"));
		task.setTask_remark(request.getParameter("task_remark"));
		task.setTask_type(Integer.valueOf(request.getParameter("task_type")));
		task.setTask_ctime(new Date(time));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date sdate = format.parse(request.getParameter("task_stime"));
		Date edate = format.parse(request.getParameter("task_etime"));
		task.setTask_stime(sdate);
		task.setTask_etime(edate);
		task.setTask_isdelete(0);
		task.setTask_state(0);
		task.setTask_alarmnum(0);
		jsonObject.put("result", taskService.save(task));
		System.out.println("返回列表:" + jsonObject.toString());
		return jsonObject.toString();
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
