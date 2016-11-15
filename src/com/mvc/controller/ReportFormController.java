package com.mvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.enums.MethodType;
import com.mvc.entity.Contract;
import com.utils.Pager;

import net.sf.json.JSONObject;

/**
 * 报表统计控制器
 * 
 * @author wangrui
 * @date 2016-11-15
 */
@Controller
@RequestMapping("/reportForm")
public class ReportFormController {

	// @RequestMapping("/getContractList.do")
	// public @ResponseBody String getContList(HttpServletRequest request,
	// HttpSession session) {
	// JSONObject jsonObject = new JSONObject();
	// String contName = request.getParameter("contName");
	// int totalRow = Integer.parseInt(contractService.countTotal(contName,
	// MethodType.contractList.value).toString());
	// Pager pager = new Pager();
	// pager.setPage(Integer.parseInt(request.getParameter("page")));// 指定页码
	// pager.setTotalRow(totalRow);
	//
	// List<Contract> list = contractService.findConByNameAndMType(contName,
	// MethodType.contractList.value, pager);
	// jsonObject.put("list", list);
	// jsonObject.put("totalPage", pager.getTotalPage());
	// return jsonObject.toString();
	// }

}
