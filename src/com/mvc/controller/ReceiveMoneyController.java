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
import com.base.enums.IsDelete;
import com.base.enums.ReceiveMoneyStatus;
import com.mvc.entity.Contract;
import com.mvc.entity.ReceiveMoney;
import com.mvc.entity.User;
import com.mvc.service.ReceiveMoneyService;
import com.mvc.service.ReceiveNodeService;
import com.utils.Pager;

import net.sf.json.JSONObject;

/**
 * 到款
 * 
 * @author zjn
 * @date 2016年10月27日
 */
@Controller
@RequestMapping("/receiveMoney")
public class ReceiveMoneyController {
	@Autowired
	ReceiveMoneyService receiveMoneyService;
	@Autowired
	ReceiveNodeService receiveNodeService;

	/**
	 * 返回收据界面
	 * 
	 * @return
	 */
	@RequestMapping("/toBillMngInvoicePage.do")
	public String InvoiceReceivePage() {
		return "billInformation/index";
	}

	/**
	 * 根据状态获取到款列表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/selectRemoTasksByState.do")
	public @ResponseBody String getReceiveMoneyList(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		Integer remoState = Integer.valueOf(request.getParameter("remoState"));
		Integer totalRow = receiveMoneyService.countByState(user.getUser_id(), remoState);// -1表示全部，0表示未核对，1表示已核对
		Pager pager = new Pager();
		pager.setPage(Integer.valueOf(request.getParameter("page")));
		pager.setTotalRow(totalRow);
		List<ReceiveMoney> list = receiveMoneyService.findListByState(user.getUser_id(), remoState, pager.getOffset(),
				pager.getLimit());
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/**
	 * 根据合同ID获取到款列表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/selectReceiveMoneysByContId.do")
	public @ResponseBody String getReceiveMoneyListByContId(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Integer contId = Integer.valueOf(request.getParameter("contId"));
		Integer remoState = Integer.valueOf(request.getParameter("remoState"));
		Integer totalRow = receiveMoneyService.countByParam(contId, remoState);// -1表示全部，0表示未核对，1表示已核对
		Pager pager = new Pager();
		pager.setPage(Integer.valueOf(request.getParameter("page")));
		pager.setTotalRow(totalRow);
		List<ReceiveMoney> list = receiveMoneyService.findListByParam(contId, remoState, pager.getOffset(),
				pager.getLimit());
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/**
	 * 根据Id查询详情
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/selectReceiveMoneyById.do")
	public @ResponseBody String findReceiveMoneyById(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Integer remoId = Integer.valueOf(request.getParameter("remoId"));
		ReceiveMoney receiveMoney = receiveMoneyService.findByRemoId(remoId);
		jsonObject.put("receiveMoney", receiveMoney);
		return jsonObject.toString();
	}

	/**
	 * 根据合同Id查询到款总金额
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/receiveMoneyByContId.do")
	public @ResponseBody String totalMoneyByContId(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Integer contId = Integer.valueOf(request.getParameter("contId"));
		Float totalMoney = receiveMoneyService.receiveMoneyByContId(contId);
		jsonObject.put("totalMoney", totalMoney);
		return jsonObject.toString();
	}

	/**
	 * 校核完成更改到款状态
	 * 
	 * @param request
	 * @param session
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/auditReceiveMoney.do")
	public @ResponseBody String updateState(HttpServletRequest request, HttpSession session) throws ParseException {
		Integer remoId = Integer.valueOf(request.getParameter("remoId"));
		Float remoAmoney = Float.valueOf(request.getParameter("remoAmoney"));
		boolean result = receiveMoneyService.updateRemoStateById(remoId, remoAmoney);
		ReceiveMoney receiveMoney = receiveMoneyService.findByRemoId(remoId);
		int cont_id = receiveMoney.getContract().getCont_id();
		// 更新收款节点的收款状态
		if (remoAmoney > 0) {
			receiveNodeService.updateRenoStateAndMoney(cont_id, remoAmoney);
		}
		return JSON.toJSONString(result);
	}

	/**
	 * 新增到款
	 * 
	 * @param request
	 * @param session
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/addReMoneyTask.do")
	public @ResponseBody String addReMoney(HttpServletRequest request, HttpSession session) throws ParseException {
		User creater = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		JSONObject jsonObject = JSONObject.fromObject(request.getParameter("receiveMoney"));
		ReceiveMoney receiveMoney = new ReceiveMoney();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Contract contract = new Contract();
		contract.setCont_id(Integer.valueOf(request.getParameter("contId")));
		receiveMoney.setContract(contract);
		receiveMoney.setCreater(creater);
		if (jsonObject.containsKey("remo_time")) {
			Date remoTime = format.parse(jsonObject.getString("remo_time"));
			receiveMoney.setRemo_time(remoTime);
		}
		if (jsonObject.containsKey("remo_money")) {
			receiveMoney.setRemo_money(Float.valueOf(jsonObject.getString("remo_money")));
		}
		if (jsonObject.containsKey("remo_remark")) {
			receiveMoney.setRemo_remark(jsonObject.getString("remo_remark"));
		}
		if (jsonObject.containsKey("operater")) {
			JSONObject tmp = JSONObject.fromObject(jsonObject.getString("operater"));
			User operater = new User();
			operater.setUser_id(Integer.valueOf(tmp.getString("user_id")));
			receiveMoney.setOperater(operater);
		}
		receiveMoney.setRemo_state(ReceiveMoneyStatus.waitAudit.value);
		receiveMoney.setRemo_isdelete(IsDelete.NO.value);
		receiveMoney.setRemo_amoney(Float.valueOf(0));
		boolean result = false;
		if (jsonObject.containsKey("remo_id")) {
			receiveMoney.setRemo_id(Integer.valueOf(jsonObject.getString("remo_id")));
			result = receiveMoneyService.save(receiveMoney);
		} else {
			result = receiveMoneyService.save(receiveMoney);
		}

		return JSON.toJSONString(result);
	}

	/**
	 * 删除到款记录
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/deleteReceMoney.do")
	public @ResponseBody String deleteReceMoney(HttpServletRequest request, HttpSession session) {
		Integer remoId = Integer.valueOf(request.getParameter("remoId"));
		boolean result = receiveMoneyService.delete(remoId);
		return JSON.toJSONString(result);
	}
}
