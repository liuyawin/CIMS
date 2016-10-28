package com.mvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mvc.entity.ContractRecord;
import com.mvc.service.ContractRecordService;

import net.sf.json.JSONObject;

/**
 * 合同日志控制器
 * 
 * @author wangrui
 * @date 2016-10-25
 */
@Controller
@RequestMapping("/contractRecord")
public class ContractRecordController {

	@Autowired
	ContractRecordService contractRecordService;

	@RequestMapping("/selectContRecordByContId.do")
	public @ResponseBody String selectContRecordByContId(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		int cont_id = Integer.parseInt(request.getParameter("cont_id"));
		List<ContractRecord> list = contractRecordService.selectContRecordByContId(cont_id);
		jsonObject.put("list", list);
		return jsonObject.toString();
	}

}
