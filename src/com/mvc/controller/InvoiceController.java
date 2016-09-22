/**
 * 
 */
package com.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.mvc.entity.Invoice;
import com.mvc.service.InvoiceService;

import net.sf.json.JSONObject;

/**
 * 发票
 * 
 * @author zjn
 * @date 2016年9月16日
 */
@Controller
@RequestMapping("/invoice")
public class InvoiceController {
	@Autowired
	InvoiceService invoiceService;

	/**
	 * 返回收据界面
	 * 
	 * @return
	 */
	@RequestMapping("/toAssistant2InvoicePage.do")
	public String taskReceivePage() {
		return "assistant2/invoiceInformation/index";
	}
	
	/**
	 * 根据发票ID查询发票详情
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/selectInvoiceById.do")
	public @ResponseBody String findByTaskId(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Integer invoiceId = Integer.valueOf(request.getParameter("invoiceId"));
		Invoice invoice = invoiceService.findById(invoiceId);
		jsonObject.put("invoice", invoice);
		System.out.println("返回列表:" + jsonObject.toString());
		return jsonObject.toString();
	}

	/**
	 * 删除发票
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/deleteInvoice.do")
	public @ResponseBody String delete(HttpServletRequest request, HttpSession session) {
		Integer invoiceId = Integer.valueOf(request.getParameter("invoiceId"));
		boolean result = invoiceService.delete(invoiceId);
		return JSON.toJSONString(result);
	}

}
