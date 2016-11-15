package com.mvc.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mvc.entity.PlanProjectForm;
import com.mvc.service.ReportFormService;
import com.utils.ExcelHelper;
import com.utils.FileHelper;

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

	@RequestMapping("/selectPlanProject.do")
	public ResponseEntity<byte[]> selectPlanProject(HttpServletRequest request) {
		// 前台参数暂留
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

}
