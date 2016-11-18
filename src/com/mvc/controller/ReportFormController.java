package com.mvc.controller;

import java.io.ByteArrayOutputStream;
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
import com.utils.ReplaceDoc;

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
		String dateOne = request.getParameter("beginYear");
		System.out.println("riqi111:" + request.getParameter("beginYear"));
		String dateTwo = request.getParameter("endYear");
		ComoCompareRemo comoCompareRemo = reportFormService.findByDate(dateOne, dateTwo);
		System.out.println("调取结果成功：" + comoCompareRemo.getComo_two() + "+" + comoCompareRemo.getComo_one());
		jsonObject.put("comoCompareRemo", comoCompareRemo);
		return jsonObject.toString();
	}

	/**
	 * 导出word格式的报表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/exportWord.do")
	public ResponseEntity<byte[]> exportWordReport(HttpServletRequest request) {
		ResponseEntity<byte[]> byteArr = null;
		String dateOne = request.getParameter("beginYear");
		String dateTwo = request.getParameter("endYear");
		// String dateOne = "2015";
		// String dateTwo = "2016";
		ComoCompareRemo comoCompareRemo = reportFormService.findByDate(dateOne, dateTwo);
		Map<String, String> contentMap = new HashMap<String, String>();
		contentMap.put("date_one", dateOne);
		contentMap.put("date_two", dateTwo);
		contentMap.put("como_one", comoCompareRemo.getComo_one().toString());
		contentMap.put("remo_one", comoCompareRemo.getRemo_one().toString());
		contentMap.put("cont_num_one", comoCompareRemo.getCont_num_one().toString());
		contentMap.put("como_two", comoCompareRemo.getComo_two().toString());
		contentMap.put("remo_two", comoCompareRemo.getRemo_two().toString());
		contentMap.put("cont_num_two", comoCompareRemo.getCont_num_two().toString());
		contentMap.put("ratio_como", comoCompareRemo.getRatio_como());
		contentMap.put("ratio_remo", comoCompareRemo.getRatio_remo());
		contentMap.put("ratio_conum", comoCompareRemo.getRatio_conum());

		// 获取模版路径模版
		String modelPath = request.getSession().getServletContext().getRealPath("/WEB-INF/wordTemp/template.doc");// 上传服务器的路径
		// 给模版中的变量赋值
		HWPFDocument document = ReplaceDoc.replaceDoc(modelPath, contentMap);
		if (document != null) {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			try {
				document.write(byteArrayOutputStream);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
				String fileName = sdf.format(new Date()) + ".doc";
				String path = request.getSession().getServletContext().getRealPath("/WEB-INF/word");// 上传服务器的路径
				path = FileHelper.transPath(fileName, path);// 解析后的上传路径
				OutputStream outputStream = new FileOutputStream(path);
				outputStream.write(byteArrayOutputStream.toByteArray());
				outputStream.close();
				// 下载文档
				byteArr = FileHelper.downloadFile(fileName, path);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return byteArr;
	}
	/*
	 * ***********************************张姣娜报表结束*******************************
	 */
}
