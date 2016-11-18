package com.mvc.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mvc.entity.ProjectStatisticForm;

import com.mvc.entity.ComoCompareRemo;

import com.mvc.service.ReportFormService;
import com.utils.Pager;
import com.utils.StringUtil;

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

	@Autowired
	ReportFormService reportFormService;

	/**
	 * 返回收据界面
	 * 
	 * @return
	 */
	@RequestMapping("/toReportFormPage.do")
	public String InvoiceReceivePage() {
		return "reportForm/index";
	}

	/**
	 * 导出光电院项目分项统计表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/exportProjectListBylimits.do")
	public ResponseEntity<byte[]> exportProjectStatistic(HttpServletRequest request) {
		Integer cont_type = null;
		String pro_stage = null;
		Integer managerId = null;
		Integer cont_status = null;
		String province = null;
		String startTime = null;
		String endTime = null;

		if (StringUtil.strIsNotEmpty(request.getParameter("contType"))) {
			cont_type = Integer.valueOf(request.getParameter("contType"));// 合同类型
		}
		if (StringUtil.strIsNotEmpty(request.getParameter("proStage"))) {
			pro_stage = request.getParameter("proStage");// 项目阶段
		}
		if (StringUtil.strIsNotEmpty(request.getParameter("userId"))) {
			System.out.println("dsag" + request.getParameter("userId"));
			managerId = Integer.valueOf(request.getParameter("userId"));// 设总
		}
		if (StringUtil.strIsNotEmpty(request.getParameter("contStatus"))) {
			cont_status = Integer.valueOf(request.getParameter("contStatus"));// 合同状态
		}
		if (StringUtil.strIsNotEmpty(request.getParameter("province"))) {
			province = request.getParameter("province");// 省份
		}
		if (StringUtil.strIsNotEmpty(request.getParameter("startDate"))) {
			startTime = request.getParameter("startDate") + "-01";// 开始时间
		}
		if (StringUtil.strIsNotEmpty(request.getParameter("endDate"))) {
			endTime = request.getParameter("endDate") + "-01";// 结束时间
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cont_type", cont_type);
		map.put("pro_stage", pro_stage);
		map.put("managerId", managerId);
		map.put("cont_status", cont_status);
		map.put("province", province);
		map.put("startTime", startTime);
		map.put("endTime", endTime);

		String path = request.getSession().getServletContext().getRealPath("/WEB-INF/reportForm");// 上传服务器的路径
		ResponseEntity<byte[]> byteArr = reportFormService.exportProjectStatistic(map, path);
		return byteArr;
	}

	/**
	 * 查询光电院项目分项统计表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectProjectListBylimits.do")
	public @ResponseBody String selectProjectStatistic(HttpServletRequest request) {
		JSONObject jsonObject = JSONObject.fromObject(request.getParameter("limit"));
		Integer page = Integer.parseInt(request.getParameter("page"));// 指定页码

		Map<String, Object> map = reportFormService.JsonObjToMap(jsonObject);
		Pager pager = reportFormService.pagerTotal(map, page);
		String path = request.getSession().getServletContext().getRealPath("/WEB-INF/reportForm");// 上传服务器的路径
		List<ProjectStatisticForm> list = reportFormService.findProjectStatistic(map, pager, path);

		jsonObject = new JSONObject();
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/*
	 * ***********************************张姣娜报表开始*******************************
	 */
	/**
	 * 根据日期查询合同额到款对比表
	 * 
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/selectComoRemoAnalyse.do")
	public @ResponseBody String findComoRemoAnalyse(HttpServletRequest request, HttpSession session) {
		JSONObject jsonObject = new JSONObject();
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		Date dateOne = null;
		Date dateTwo = null;
		try {
			dateOne = format.parse(request.getParameter("beginYear"));
			dateTwo = format.parse(request.getParameter("endYear"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//ComoCompareRemo comoCompareRemo = reportFormService.findByDate(dateOne, dateTwo);
		jsonObject.put("comoCompareRemo", "");
		return jsonObject.toString();
	}
	/*
	 * ***********************************张姣娜报表结束*******************************
	 */

}
