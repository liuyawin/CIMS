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
import com.base.enums.InvoiceStatus;
import com.base.enums.IsDelete;
import com.mvc.entity.Contract;
import com.mvc.entity.Invoice;
import com.mvc.entity.User;
import com.mvc.service.InvoiceService;
import com.utils.Pager;

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
	 * 文书二返回收据界面
	 * 
	 * @return
	 */
	@RequestMapping("/toAssistant2InvoicePage.do")
	public String invoiceReceivePage() {
		return "assistant2/invoiceInformation/index";
	}

	/**
	 * 主任返回收据界面
	 * 
	 * @return
	 */
	@RequestMapping("/toZhurenInvoicePage.do")
	public String zhurenInvoiceReceivePage() {
		return "zhuren/invoiceInformation/index";
	}

	/**
	 * 主任获取发票任务列表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getZRInvoice.do")
	public @ResponseBody String getAllInvoiceList(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		Integer ifSend = Integer.valueOf(request.getParameter("ifSend"));// 0:未派发；1：已派发
		Integer totalRow = invoiceService.countByParam(user.getUser_id(), ifSend);
		System.out.println("总数" + totalRow);
		Pager pager = new Pager();
		pager.setPage(Integer.valueOf(request.getParameter("page")));
		pager.setTotalRow(totalRow);
		List<Invoice> list = invoiceService.findByPage(user.getUser_id(), ifSend, pager.getOffset(), pager.getLimit());
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		System.out.println("返回列表:" + jsonObject.toString());
		return jsonObject.toString();
	}

	/**
	 * 根据合同ID查询发票记录
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/selectInvoiceByContId.do")
	public @ResponseBody String getAllInvoiceByContId(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Integer contId = Integer.valueOf(request.getParameter("contId"));
		Integer totalRow = invoiceService.countByContId(contId);
		System.out.println("总数" + totalRow);
		Pager pager = new Pager();
		pager.setPage(Integer.valueOf(request.getParameter("page")));
		pager.setTotalRow(totalRow);
		List<Invoice> list = invoiceService.findByContId(contId, pager.getOffset(), pager.getLimit());
		jsonObject.put("list", list);
		jsonObject.put("totalRow", totalRow);
		jsonObject.put("totalPage", pager.getTotalPage());
		System.out.println("返回列表:" + jsonObject.toString());
		return jsonObject.toString();
	}

	/**
	 * 根据发票ID查询发票详情
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/selectInvoiceById.do")
	public @ResponseBody String findByInvoiceId(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Integer invoiceId = Integer.valueOf(request.getParameter("invoiceId"));
		Invoice invoice = invoiceService.findById(invoiceId);
		jsonObject.put("invoice", invoice);
		System.out.println("返回列表发票:" + jsonObject.toString());
		return jsonObject.toString();
	}

	/**
	 * 根据合同ID查询发票总金额
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/countInvoiceMoneyByContId.do")
	public @ResponseBody String totalMoney(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		Integer contId = Integer.valueOf(request.getParameter("contId"));
		Float totalMoney = invoiceService.totalMoneyOfInvoice(contId);
		jsonObject.put("totalMoney", totalMoney);
		System.out.println("返回列表:" + jsonObject.toString());
		return jsonObject.toString();
	}

	/**
	 * 创建发票
	 * 
	 * @param request
	 * @param session
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/addInvoiceTask.do")
	public @ResponseBody String addInvoice(HttpServletRequest request, HttpSession session) throws ParseException {
		JSONObject result = new JSONObject();
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		JSONObject jsonObject = JSONObject.fromObject(request.getParameter("invoice"));
		Invoice invoice = new Invoice();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		long time = System.currentTimeMillis();
		Contract contract = new Contract();
		contract.setCont_id(Integer.valueOf(request.getParameter("contId")));
		invoice.setContract(contract);
		invoice.setInvo_money(Float.valueOf(jsonObject.getString("invoMoney")));
		invoice.setInvo_firm(jsonObject.getString("invoFirm"));
		User audit = new User();
		audit.setUser_id(Integer.valueOf(jsonObject.getString("auditId")));
		invoice.setAudit(audit);
		Date sTime = format.parse(jsonObject.getString("invoStime"));
		invoice.setInvo_stime(sTime);
		Date eTime = format.parse(jsonObject.getString("invoEtime"));
		invoice.setInvo_etime(eTime);
		invoice.setInvo_remark(jsonObject.getString("invoRemark"));
		User creator = new User();
		creator.setUser_id(user.getUser_id());
		invoice.setCreator(creator);
		invoice.setInvo_isdelete(IsDelete.NO.value);
		invoice.setInvo_ctime(new Date(time));
		invoice.setInvo_state(InvoiceStatus.waitAudit.value);
		boolean invoiceResult = invoiceService.save(invoice);
		if (invoiceResult)
			result.put("result", "true");
		else {
			result.put("result", "false");
		}
		return result.toString();
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
