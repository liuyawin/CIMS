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
	 * 返回收据界面
	 * 
	 * @return
	 */
	@RequestMapping("/toBillMngInvoicePage.do")
	public String InvoiceReceivePage() {
		return "billInformation/index";
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
		Integer invoState = Integer.valueOf(request.getParameter("invoState"));// 0:未审核；1：已审核
		Integer totalRow = invoiceService.countByParam(user.getUser_id(), invoState);
		Pager pager = new Pager();
		pager.setPage(Integer.valueOf(request.getParameter("page")));
		pager.setTotalRow(totalRow);
		List<Invoice> list = invoiceService.findByPage(user.getUser_id(), invoState, pager.getOffset(),
				pager.getLimit());
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/**
	 * 按发票状态获取列表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getWaitingDealInvoice.do")
	public @ResponseBody String getInvoiceListByState(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		Integer invoiceState = Integer.valueOf(request.getParameter("invoState"));
		Integer totalRow = invoiceService.WaitingDealCountByParam(user.getUser_id(), invoiceState);
		Pager pager = new Pager();
		pager.setPage(Integer.valueOf(request.getParameter("page")));
		pager.setTotalRow(totalRow);
		List<Invoice> list = invoiceService.WaitingDealFindByPage(user.getUser_id(), invoiceState, pager.getOffset(),
				pager.getLimit());
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
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
		Pager pager = new Pager();
		pager.setPage(Integer.valueOf(request.getParameter("page")));
		pager.setTotalRow(totalRow);
		List<Invoice> list = invoiceService.findByContId(contId, pager.getOffset(), pager.getLimit());
		jsonObject.put("list", list);
		jsonObject.put("totalRow", totalRow);
		jsonObject.put("totalPage", pager.getTotalPage());
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
		return jsonObject.toString();
	}

	/**
	 * 根据合同ID查询发票总金额
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/countInvoiceMoneyByContId.do")
	public @ResponseBody String totalMoney(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		Integer contId = Integer.valueOf(request.getParameter("contId"));
		Float totalMoney = invoiceService.totalMoneyOfInvoice(contId);
		Integer totalRow = invoiceService.countTotalRow(contId);
		jsonObject.put("totalMoney", totalMoney);
		jsonObject.put("totalRow", totalRow);
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
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		JSONObject jsonObject = JSONObject.fromObject(request.getParameter("invoice"));
		Integer cont_id = Integer.valueOf(request.getParameter("contId"));
		Boolean invoiceResult = invoiceService.save(jsonObject, cont_id, user);
		return JSON.toJSONString(invoiceResult);
	}

	/**
	 * 点击确认完成开发票任务
	 * 
	 * @param request
	 * @param session
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/updateInvoiceState.do")
	public @ResponseBody String invoiceFinish(HttpServletRequest request, HttpSession session) throws ParseException {
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		Integer invoiceId = Integer.parseInt(request.getParameter("invoiceId"));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date invoTime = format.parse(request.getParameter("invoTime"));
		boolean result = invoiceService.invoiceFinish(invoiceId, invoTime, user);
		return JSON.toJSONString(result);
	}

	/**
	 * 主任转发发票
	 * 
	 * @param request
	 * @param session
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/updateInvoice.do")
	public @ResponseBody String transmitInvoice(HttpServletRequest request, HttpSession session) throws ParseException {
		Integer invoiceId = Integer.valueOf(request.getParameter("invoId"));
		Integer receiverId = Integer.valueOf(request.getParameter("receiverId"));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date invoEtime = format.parse(request.getParameter("invoEtime"));
		boolean result = invoiceService.transmitInvoice(invoiceId, invoEtime, receiverId);
		return JSON.toJSONString(result);
	}

	/**
	 * 根据权限，状态，页码 查找发票
	 * 
	 * @param request
	 * @param session
	 * @return list
	 */
	@RequestMapping(value = "/getInvoTaskListByState.do")
	public @ResponseBody String selectInvoTaskByState(HttpServletRequest request, HttpSession session) {
		Integer invoState = Integer.parseInt(request.getParameter("invoState"));// -1：全部，0：待审核，1：待处理，2：已完成
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		Integer user_id = user.getUser_id();
		String permission = user.getRole().getRole_permission();// 权限
		Integer totalRow = invoiceService.countByStateAndPerm(invoState, permission, user_id);
		Pager pager = new Pager();
		pager.setPage(Integer.valueOf(request.getParameter("page")));
		pager.setTotalRow(totalRow);

		List<Invoice> list = invoiceService.selectInvoByStateAndPerm(invoState, permission, user_id, pager);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/**
	 * 根据合同ID，状态，页码 查找发票
	 * 
	 * @param request
	 * @return list
	 */
	@RequestMapping(value = "/getInvoiceListByContId.do")
	public @ResponseBody String selectInvoiceByContId(HttpServletRequest request) {
		Integer invoState = Integer.parseInt(request.getParameter("invoState"));// -1：全部，0：待审核，1：待处理，2：已完成
		Integer cont_id = Integer.parseInt(request.getParameter("contId"));
		Integer totalRow = invoiceService.countByStateAndContId(invoState, cont_id);
		Pager pager = new Pager();
		pager.setPage(Integer.valueOf(request.getParameter("page")));
		pager.setTotalRow(totalRow);
		List<Invoice> list = invoiceService.selectInvoByStateAndContId(invoState, cont_id, pager);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/**
	 * 删除发票记录
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/deleteInvoice.do")
	public @ResponseBody String deleteInvoice(HttpServletRequest request, HttpSession session) {
		Integer invoiceId = Integer.valueOf(request.getParameter("invoiceId"));
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		Boolean result = invoiceService.delete(invoiceId, user);
		return JSON.toJSONString(result);
	}

}
