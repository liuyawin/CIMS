package com.mvc.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.ss.formula.functions.Replace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.constants.SessionKeyConstants;
import com.base.enums.TaskStatus;
import com.mvc.entity.ComoCompareRemo;
import com.mvc.entity.PlanProjectForm;
import com.mvc.entity.Task;
import com.mvc.entity.User;
import com.mvc.service.ReportFormService;
import com.utils.ExcelHelper;
import com.utils.FileHelper;

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
	 * 返回报表界面
	 * 
	 * @return
	 */
	@RequestMapping("/toReportFormPage.do")
	public String InvoiceReceivePage() {
		return "reportForm/index";
	}

	@RequestMapping("/selectPlanProject.do")
	public ResponseEntity<byte[]> selectPlanProject(HttpServletRequest request) {
		// 前台参数暂留
		// Integer cont_state
		// =Integer.valueOf(request.getParameter("contState"));
		ResponseEntity<byte[]> byteArr = null;
		ExcelHelper<PlanProjectForm> ex = new ExcelHelper<PlanProjectForm>();
		String[] headers = { "序号", "项目名称", "项目设总", "装机容量（MW）", "合同状态", "合同额（万元）", "签订时间" };
		List<PlanProjectForm> list = reportFormService.findPlanProject(null, null, null);
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			int year = c.get(Calendar.YEAR);
			String fileName = year + "年光电院承担规划项目表.xls";
			String path = request.getSession().getServletContext().getRealPath("/WEB-INF/reportForm");// 上传服务器的路径
			path = FileHelper.transPath(fileName, path);// 解析后的上传路径

			OutputStream out = new FileOutputStream(path);
			ex.exportExcel(headers, list, out);
			out.close();
			byteArr = FileHelper.downloadFile(fileName, path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return byteArr;
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
		ComoCompareRemo comoCompareRemo = reportFormService.findByDate(dateOne, dateTwo);
		jsonObject.put("comoCompareRemo", comoCompareRemo);
		return jsonObject.toString();
	}

	/**
	 * 导出word格式的报表
	 * 
	 * @param request
	 * @return
	 */
	// @RequestMapping("/exportWord.do")
	// public ResponseEntity<byte[]> exportWordReport(HttpServletRequest
	// request) {
	// JSONObject jsonObject = new JSONObject();
	// SimpleDateFormat format = new SimpleDateFormat("yyyy");
	// Date dateOne = null;
	// Date dateTwo = null;
	// try {
	// dateOne = format.parse(jsonObject.getString("beginYear"));
	// dateTwo = format.parse(jsonObject.getString("endYear"));
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	// ResponseEntity<byte[]> byteArr = null;
	// ComoCompareRemo comoCompareRemo = reportFormService.findByDate(dateOne,
	// dateTwo);
	// Map<String, String> contentMap = new HashMap<String, String>();
	// contentMap.put("como_one", comoCompareRemo.getComo_one().toString());
	// contentMap.put("remo_one", comoCompareRemo.getRemo_one().toString());
	// contentMap.put("cont_num_one",
	// comoCompareRemo.getCont_num_one().toString());
	// contentMap.put("como_two",comoCompareRemo.getComo_two().toString());
	// contentMap.put("remo_two", comoCompareRemo.getRemo_two().toString());
	// contentMap.put("cont_num_two",
	// comoCompareRemo.getCont_num_two().toString());
	// contentMap.put("ratio_como", comoCompareRemo.getRatio_como());
	// contentMap.put("ratio_remo", comoCompareRemo.getRatio_remo());
	// contentMap.put("ratio_conum",comoCompareRemo.getRatio_conum());
	// HWPFDocument document = Replace.replaceDoc("E:\\template.doc",
	// contentMap);
	// try {
	// Calendar c = Calendar.getInstance();
	// c.setTime(new Date());
	// int year = c.get(Calendar.YEAR);
	// String fileName = year + "年光电院承担规划项目表.xls";
	// String path =
	// request.getSession().getServletContext().getRealPath("/WEB-INF/reportForm");//
	// 上传服务器的路径
	// path = FileHelper.transPath(fileName, path);// 解析后的上传路径
	//
	// OutputStream out = new FileOutputStream(path);
	// ex.exportExcel(headers, list, out);
	// out.close();
	// byteArr = FileHelper.downloadFile(fileName, path);
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return byteArr;
	// }
	/*
	 * ***********************************张姣娜报表结束*******************************
	 */
}
