package com.mvc.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.constants.SessionKeyConstants;
import com.base.enums.ContractType;
import com.mvc.entity.Contract;
import com.mvc.entity.User;
import com.mvc.service.ContractService;
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

	/**
	 * 获取指定页面的十条合同信息，总页数
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getContractList.do")
	public @ResponseBody String getContList(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);// 获取Session中的user对象
		int totalRow = Integer.parseInt(
				contractService.countTotal(user.getUser_id(), request.getParameter("contName"), "name").toString());
		Pager pager = new Pager();
		pager.setPage(Integer.parseInt(request.getParameter("page")));// 指定页码
		pager.setTotalRow(totalRow);
		// 和根据名字查找共用一个方法，contName为null
		List<Contract> list = contractService.findConByName(user.getUser_id(), null, pager.getOffset(),
				pager.getPageSize());
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
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);// 获取Session中的user对象
		int totalRow = Integer.parseInt(
				contractService.countTotal(user.getUser_id(), request.getParameter("contName"), "Debt").toString());
		Pager pager = new Pager();
		pager.setPage(Integer.parseInt(request.getParameter("page")));// 指定页码
		pager.setTotalRow(totalRow);
		List<Contract> list = contractService.findAllDebtCont(user.getUser_id(), null, pager.getOffset(),
				pager.getPageSize());
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
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);// 获取Session中的user对象
		int totalRow = Integer.parseInt(
				contractService.countTotal(user.getUser_id(), request.getParameter("contName"), "Overdue").toString());
		Pager pager = new Pager();
		pager.setPage(Integer.parseInt(request.getParameter("page")));// 指定页码
		pager.setTotalRow(totalRow);
		List<Contract> list = contractService.findAllOverdueCont(user.getUser_id(), null, pager.getOffset(),
				pager.getPageSize());
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
		User user = (User) session.getAttribute(SessionKeyConstants.LOGIN);// 获取Session中的user对象
		int totalRow = Integer.parseInt(
				contractService.countTotal(user.getUser_id(), request.getParameter("contName"), "name").toString());
		Pager pager = new Pager();
		pager.setPage(Integer.parseInt(request.getParameter("page")));// 指定页码
		pager.setTotalRow(totalRow);
		List<Contract> list = contractService.findConByName(user.getUser_id(), request.getParameter("contName"),
				pager.getOffset(), pager.getPageSize());// 合同名
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
		JSONObject jsonObject = new JSONObject();
		jsonObject = JSONObject.fromObject(request.getParameter("contract"));
		long time = System.currentTimeMillis();
		Contract contract = new Contract();
		contract.setCont_name(jsonObject.getString("cont_name"));
		contract.setCont_project(jsonObject.getString("cont_project"));
		ContractType ct = Enum.valueOf(ContractType.class, jsonObject.getString("cont_type"));
		contract.setCont_type(ct.value);// 枚举
		contract.setCont_cheader(jsonObject.getString("cont_cheader"));
		contract.setCont_ctel(jsonObject.getString("cont_ctel"));
		contract.setCont_cdept(jsonObject.getString("cont_cdept"));
		contract.setCont_initiation(1);// 已立项
		contract.setCont_ishistory(0);// 未删除
		contract.setCont_ctime(new Date(time));
		contract.setCreator(user);
		contractService.addContract(contract);
		return contract.getCont_id();
	}

}
