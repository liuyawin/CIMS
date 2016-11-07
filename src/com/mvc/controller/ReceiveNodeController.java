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
import com.mvc.entity.ReceiveNode;
import com.mvc.entity.User;
import com.mvc.service.AlarmService;
import com.mvc.service.ContractService;
import com.mvc.service.ProjectStageService;
import com.mvc.service.ReceiveNodeService;

import net.sf.json.JSONObject;

/**
 * 收款节点控制器
 * 
 * @author wangrui
 * @date 2016-09-20
 */
@Controller
@RequestMapping("/receiveNode")
public class ReceiveNodeController {

	@Autowired
	ContractService contractService;
	@Autowired
	ProjectStageService projectStageService;
	@Autowired
	ReceiveNodeService receiveNodeService;
	@Autowired
	AlarmService alarmService;

	/**
	 * 添加收款节点
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping("/addReceiveNode.do")
	public @ResponseBody String addReceiveNode(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = JSONObject.fromObject(request.getParameter("receiveNode"));
		Integer cont_id = Integer.parseInt(request.getParameter("cont_id"));
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);

		boolean flag = receiveNodeService.addReceiveNode(jsonObject, cont_id, user);
		return String.valueOf(flag);
	}

	/**
	 * 根据合同ID查找收款节点
	 * 
	 * @param request
	 * @param session
	 * @return list
	 */
	@RequestMapping("/selectRenoByContId.do")
	public @ResponseBody String selectRenoByContId(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		List<ReceiveNode> list = receiveNodeService
				.selectRenoByContId(Integer.parseInt(request.getParameter("cont_id")));
		jsonObject.put("list", list);
		return jsonObject.toString();
	}

	/**
	 * 删除收款节点
	 * 
	 * @param request
	 * @return true、false
	 */
	@RequestMapping("/delReno.do")
	public @ResponseBody String delReno(HttpServletRequest request) {
		Integer reno_id = Integer.parseInt(request.getParameter("renoId"));
		boolean flag = receiveNodeService.deleteReno(reno_id);
		alarmService.updateByIdType(reno_id, RemoveType.RenoAlarm.value);// 解除报警
		return String.valueOf(flag);
	}

	/**
	 * 修改收款节点
	 * 
	 * @param request
	 * @return true、false
	 */
	@RequestMapping("/modifyReno.do")
	public @ResponseBody String updateReno(HttpServletRequest request) {
		Integer reno_id = Integer.parseInt(request.getParameter("renoId"));
		JSONObject jsonObject = JSONObject.fromObject(request.getParameter("receiveNode"));

		boolean flag = receiveNodeService.updateReno(jsonObject, reno_id);
		return String.valueOf(flag);
	}

	/**
	 * 根据合同ID查找收款节点
	 * 
	 * @param request
	 * @return ReceiveNode
	 */
	@RequestMapping("/selectRenoById.do")
	public @ResponseBody String selectRenoById(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		ReceiveNode receiveNode = receiveNodeService.findByRenoId(Integer.parseInt(request.getParameter("renoId")));
		jsonObject.put("receiveNode", receiveNode);
		return jsonObject.toString();
	}
}
