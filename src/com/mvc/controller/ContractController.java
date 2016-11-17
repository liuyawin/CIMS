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
import com.base.enums.MethodType;
import com.mvc.entity.Contract;
import com.mvc.entity.User;
import com.mvc.service.ContractService;
import com.mvc.service.UserService;
import com.utils.Pager;

import net.sf.json.JSONObject;

/**
 * 合同控制器
 * 
 * @author wangrui
 * @date 2016-09-10
 */
@Controller
@RequestMapping("/contract")
public class ContractController {

	@Autowired
	ContractService contractService;
	@Autowired
	UserService userService;

	/**
	 * 返回收据界面
	 * 
	 * @return
	 */
	@RequestMapping("/toBillMngContractPage.do")
	public String InvoiceReceivePage() {
		return "billInformation/index";
	}

	/**
	 * 返回主任合同界面
	 * 
	 * @return
	 */
	@RequestMapping("/toContractPage.do")
	public String contractPage() {
		return "contractInformation/index";
	}

	/**
	 * 获取指定页面的十条合同信息，总页数(和"根据合同名获取合同信息"方法完全一样)
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getContractList.do")
	public @ResponseBody String getContList(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		String contName = request.getParameter("contName");
		int totalRow = Integer.parseInt(contractService.countTotal(contName, MethodType.contractList.value).toString());
		Pager pager = new Pager();
		pager.setPage(Integer.parseInt(request.getParameter("page")));// 指定页码
		pager.setTotalRow(totalRow);

		List<Contract> list = contractService.findConByNameAndMType(contName, MethodType.contractList.value, pager);
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/**
	 * 根据合同创建者id获取欠款的合同列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getDebtContract.do")
	public @ResponseBody String getDebtContList(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		String contName = request.getParameter("contName");
		int totalRow = Integer.parseInt(contractService.countTotal(contName, MethodType.debtContract.value).toString());
		Pager pager = new Pager();
		pager.setPage(Integer.parseInt(request.getParameter("page")));// 指定页码
		pager.setTotalRow(totalRow);

		List<Contract> list = contractService.findConByNameAndMType(contName, MethodType.debtContract.value, pager);
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/**
	 * 根据合同创建者id获取逾期的合同列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getOverdueContract.do")
	public @ResponseBody String getOverdueContList(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		String contName = request.getParameter("contName");
		int totalRow = Integer
				.parseInt(contractService.countTotal(contName, MethodType.overdueContract.value).toString());
		Pager pager = new Pager();
		pager.setPage(Integer.parseInt(request.getParameter("page")));// 指定页码
		pager.setTotalRow(totalRow);

		List<Contract> list = contractService.findConByNameAndMType(contName, MethodType.overdueContract.value, pager);
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/**
	 * 根据合同名获取合同信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectConByName.do")
	public @ResponseBody String selectConByName(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		String contName = request.getParameter("contName");
		int totalRow = Integer.parseInt(contractService.countTotal(contName, MethodType.contractList.value).toString());
		Pager pager = new Pager();
		pager.setPage(Integer.parseInt(request.getParameter("page")));// 指定页码
		pager.setTotalRow(totalRow);

		List<Contract> list = contractService.findConByNameAndMType(contName, MethodType.contractList.value, pager);
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/**
	 * 添加合同
	 * 
	 * @param request
	 * @param session
	 * @return 合同ID
	 */
	@RequestMapping("/addContract.do")
	public @ResponseBody Integer addContract(HttpServletRequest request, HttpSession session) {
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		JSONObject jsonObject = JSONObject.fromObject(request.getParameter("contract"));
		Contract contract = contractService.addContract(user, jsonObject);
		int cont_id = contract.getCont_id();
		session.setAttribute("cont_id", cont_id);// 创建合同时将cont_id放入session
		return cont_id;
	}

	/**
	 * 根据合同ID获取合同
	 * 
	 * @param request
	 * @param session
	 * @return Contract对象
	 */
	@RequestMapping("/selectContractById.do")
	public @ResponseBody String selectContById(HttpServletRequest request, HttpSession session) {
		int cont_id = Integer.parseInt(request.getParameter("cont_id"));
		session.setAttribute("cont_id", cont_id);// 将cont_id放入session
		Contract contract = contractService.selectContById(cont_id);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("contract", contract);
		return jsonObject.toString();
	}

	/**
	 * 合同信息补录
	 * 
	 * @param request
	 * @param session
	 * @return Contract对象
	 */
	@RequestMapping("/repeatAddContract.do")
	public @ResponseBody String repeatAddContract(HttpServletRequest request, HttpSession session) {
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		Integer cont_id = Integer.valueOf(request.getParameter("cont_id"));
		JSONObject jsonObject = JSONObject.fromObject(request.getParameter("contract"));
		Contract contract = contractService.updateContract(cont_id, jsonObject, user);
		jsonObject = new JSONObject();
		jsonObject.put("contract", contract);
		return jsonObject.toString();
	}

	/**
	 * 删除合同
	 * 
	 * @param request
	 * @param session
	 * @return 前十条合同信息，总页数
	 */
	@RequestMapping("/deleteContract.do")
	public @ResponseBody String deleteContract(HttpServletRequest request, HttpSession session) {
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		JSONObject jsonObject = new JSONObject();
		Integer cont_id = Integer.valueOf(request.getParameter("conId"));
		String contName = request.getParameter("contName");
		String pageType = request.getParameter("pageType");
		int totalRow = Integer.parseInt(contractService.countTotal(contName, MethodType.contractList.value).toString());
		Pager pager = new Pager();
		pager.setPage(1);// 返回前十条
		pager.setTotalRow(totalRow);

		List<Contract> list = contractService.deleteContract(cont_id, contName, pageType, pager, user);
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/**
	 * 查找合同列表
	 * 
	 * @param request
	 * @return list和总页数
	 */
	@RequestMapping("/selectContract.do")
	public @ResponseBody String selectContract(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		int methodType = Integer.parseInt(request.getParameter("findType"));// 合同方法类别：1-合同信息管理，2-欠款合同信息，3-工程逾期合同，4-终结合同信息，5-停建合同
		String contName = request.getParameter("contName");
		int totalRow = Integer.parseInt(contractService.countTotal(contName, methodType).toString());
		Pager pager = new Pager();
		pager.setPage(Integer.parseInt(request.getParameter("page")));// 指定页码
		pager.setTotalRow(totalRow);

		List<Contract> list = contractService.findConByNameAndMType(contName, methodType, pager);
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/**
	 * 根据合同ID修改合同基本信息
	 * 
	 * @param request
	 * @param session
	 * @return 成功返回1，失败返回0
	 */
	@RequestMapping("/updateConById.do")
	public @ResponseBody Integer updateConById(HttpServletRequest request, HttpSession session) {
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		JSONObject jsonObject = JSONObject.fromObject(request.getParameter("contract"));
		Integer cont_id = null;
		if (jsonObject.containsKey("cont_id")) {
			cont_id = Integer.parseInt(jsonObject.getString("cont_id"));
		}
		Boolean flag = contractService.updateContBase(cont_id, jsonObject, user);
		if (flag == true)
			return 1;
		else
			return 0;
	}

	/**
	 * 设总更新合同状态
	 * 
	 * @param request
	 * @param session
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/modifyStatus.do")
	public @ResponseBody String updateState(HttpServletRequest request, HttpSession session) throws ParseException {
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);
		Integer contId = Integer.valueOf(request.getParameter("contId"));
		Integer contState = Integer.valueOf(request.getParameter("contState"));
		boolean result = contractService.updateState(contId, contState, user);
		return JSON.toJSONString(result);
	}
}
