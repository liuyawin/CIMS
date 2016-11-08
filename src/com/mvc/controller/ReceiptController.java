package com.mvc.controller;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.base.constants.SessionKeyConstants;
import com.mvc.entity.Receipt;
import com.mvc.entity.User;
import com.mvc.service.AlarmService;
import com.mvc.service.ReceiptService;
import com.mvc.service.ReceiveNodeService;
import com.utils.Pager;

import net.sf.json.JSONObject;

/**
 * 收据
 * 
 * @author zjn
 * @date 2016年9月15日
 */
@Controller
@RequestMapping("/receipt")
public class ReceiptController {
	@Autowired
	ReceiptService receiptService;
	@Autowired
	ReceiveNodeService receiveNodeService;
	@Autowired
	AlarmService alarmService;

	/**
	 * 根据页数,合同，关键字返回任务列表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/selectReceiptByContId.do")
	public @ResponseBody String getReceipt(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Integer cont_id = Integer.valueOf(request.getParameter("contId"));
		Integer totalRow = receiptService.countByParam(cont_id, null);
		Pager pager = new Pager();
		pager.setPage(Integer.valueOf(request.getParameter("page")));
		pager.setTotalRow(Integer.parseInt(totalRow.toString()));
		List<Receipt> list = receiptService.findByPage(cont_id, null, pager.getOffset(), pager.getLimit());
		jsonObject.put("list", list);
		jsonObject.put("totalRow", totalRow);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/**
	 * 根据收据ID查询收据详情
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/findByReceiptId.do")
	public @ResponseBody String findByReceiptId(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Integer receiptId = Integer.valueOf(request.getParameter("receId"));
		Receipt receipt = receiptService.findByReceiptId(receiptId);
		jsonObject.put("receipt", receipt);
		return jsonObject.toString();
	}

	/**
	 * 根据合同ID查询收据总金额
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/countReceiptMoneyByContId.do")
	public @ResponseBody String totalMoney(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Integer contId = Integer.valueOf(request.getParameter("contId"));
		Float totalMoney = receiptService.totalMoneyOfReceipt(contId);
		jsonObject.put("totalMoney", totalMoney);
		return jsonObject.toString();
	}

	/**
	 * 创建收据
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/createReceipt.do")
	public @ResponseBody String addReceipt(HttpServletRequest request, HttpSession session) throws ParseException {
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		JSONObject jsonObject = JSONObject.fromObject(request.getParameter("receipt"));
		Integer cont_id = Integer.valueOf(request.getParameter("contId"));
		Boolean result = receiptService.save(jsonObject, cont_id, user);
		return JSON.toJSONString(result);
	}

	/**
	 * 删除收据记录
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/deleteReceipt.do")
	public @ResponseBody String deleteReceipt(HttpServletRequest request, HttpSession session) {
		Integer receId = Integer.valueOf(request.getParameter("receId"));
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		Boolean result = receiptService.delete(receId, user);
		return JSON.toJSONString(result);
	}
}
