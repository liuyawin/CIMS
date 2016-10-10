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
import com.mvc.entity.Contract;
import com.mvc.entity.ProjectStage;
import com.mvc.entity.ReceiveNode;
import com.mvc.entity.User;
import com.mvc.service.ContractService;
import com.mvc.service.ProjectStageService;
import com.mvc.service.ReceiveNodeService;

import net.sf.json.JSONArray;
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

	/**
	 * 添加收款节点
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/addReceiveNode.do")
	public @ResponseBody String addReceiveNode(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		jsonObject = JSONObject.fromObject(request.getParameter("receiveNode"));
		JSONArray arr = jsonObject.getJSONArray("nodes");
		Iterator<Object> it = arr.iterator();
		ReceiveNode receiveNode = null;
		JSONObject node = null;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		long time = System.currentTimeMillis();
		boolean flag = true;
		while (it.hasNext()) {
			node = (JSONObject) it.next();
			receiveNode = new ReceiveNode();
			try {
				Date date = format.parse(node.getString("reno_time"));// 收款截止时间
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				int days = Integer.parseInt(node.getString("reno_wday"));// 收款提醒天数
				receiveNode.setReno_wday(days);
				;// 添加收款提醒的天数
				calendar.add(Calendar.DAY_OF_MONTH, -days);// 收款结束提醒时间=收款截止时间-收款提醒天数
				receiveNode.setReno_wtime(calendar.getTime());// 收款结束提醒时间
				receiveNode.setReno_content(node.getString("reno_content"));// 节点内容
				receiveNode.setReno_money(Float.parseFloat(node.getString("reno_money")));// 应收款金额
				receiveNode.setReno_time(date);// 节点截止时间
				receiveNode.setReno_state(0);// 是否已收款，默认未收款；0未收款，1已收款，2未付全款，3提前收到款
				receiveNode.setReno_amoney((float) 0.00);// 实际收款金额
				receiveNode.setReno_ctime(new Date(time));// 节点录入时间
				User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);// 录入人
				receiveNode.setUser(user);
				Contract contract = contractService.selectContById(Integer.parseInt(request.getParameter("cont_id")));// 所属合同
				receiveNode.setContract(contract);

				if (node.has("projectStage")) {
					ProjectStage projectStage = projectStageService
							.selectPrstById(Integer.parseInt(node.getString("projectStage")));
					receiveNode.setProjectStage(projectStage);// 所属阶段
				}
				// 写入数据库
				flag = receiveNodeService.addReceiveNode(receiveNode);
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

}
