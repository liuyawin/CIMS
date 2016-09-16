package com.mvc.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mvc.entity.Contract;
import com.mvc.entity.Receipt;
import com.mvc.entity.ReceiveNode;
import com.mvc.entity.User;
import com.mvc.service.ReceiptService;
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

	/**
	 * 根据页数,合同，关键字返回任务列表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/selectReceiptByContId.do")
	public @ResponseBody String getTasks(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Integer cont_id = Integer.valueOf(request.getParameter("contId"));
		Integer totalRow = receiptService.countByParam(cont_id, null);
		System.out.println("总数" + totalRow);
		Pager pager = new Pager();
		pager.setPage(Integer.valueOf(request.getParameter("page")));
		pager.setTotalRow(Integer.parseInt(totalRow.toString()));
		List<Receipt> list = receiptService.findByPage(cont_id, null, pager.getOffset(), pager.getLimit());
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		System.out.println("返回列表:" + jsonObject.toString());
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
		Integer receiptId = Integer.valueOf(request.getParameter("ID"));
		Receipt receipt = receiptService.findByReceiptId(receiptId);
		jsonObject.put("receipt", receipt);
		System.out.println("返回列表:" + jsonObject.toString());
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
	public @ResponseBody String save(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Receipt receipt = new Receipt();
		receipt.setRece_firm(request.getParameter("receFirm"));
		receipt.setRece_money(Float.valueOf(request.getParameter("money")));
		receipt.setRece_time(new Date(Long.valueOf(request.getParameter("receTime"))));
		receipt.setRece_atime(new Date(Long.valueOf(request.getParameter("receAtime"))));
		receipt.setRece_remark(request.getParameter("receRemark"));
		User user = new User();
		user.setUser_id(Integer.valueOf(request.getParameter("userId")));
		receipt.setUser(user);
		Contract contract = new Contract();
		contract.setCont_id(Integer.valueOf(request.getParameter("contId")));
		receipt.setContract(contract);
		ReceiveNode receiveNode = new ReceiveNode();
		receiveNode.setReno_id(Integer.valueOf(request.getParameter("renoId")));
		receipt.setReceiveNode(receiveNode);
		jsonObject.put("result", receiptService.save(receipt));
		return jsonObject.toString();
	}
}
