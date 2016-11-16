package com.mvc.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@SuppressWarnings("rawtypes")
	@RequestMapping("/selectPlanProject.do")
	public ResponseEntity<byte[]> selectPlanProject(HttpServletRequest request) {
		// 前台参数暂留
		// Integer cont_state
		// =Integer.valueOf(request.getParameter("contState"));
		ResponseEntity<byte[]> byteArr = null;
		ExcelHelper<PlanProjectForm> ex = new ExcelHelper<PlanProjectForm>();
		String[] titles = { "光电院承担规划项目表", "光电院分布式光伏项目统计表", "光电院光伏项目统计表（不含分布式）" };
		String[] header1 = { "序号", "项目名称", "项目设总", "装机容量（MW）", "合同状态", "合同额（万元）", "签订时间" };
		String[] header2 = { "序号", "项目名称", "项目设总", "装机容量（MW）", "合同状态", "合同额（万元）", "签订时间" };
		String[] header3 = { "序号", "项目名称", "项目设总", "装机容量（MW）", "合同状态", "合同额（万元）", "签订时间" };
		List<PlanProjectForm> list = reportFormService.findPlanProject(null, null, null);

		Map<Integer, String[]> headerMap = new HashMap<Integer, String[]>();// 每个sheet的标题
		headerMap.put(0, header1);
		headerMap.put(1, header2);
		headerMap.put(2, header3);

		Map<Integer, List> map = new HashMap<Integer, List>();// 每个sheet中内容
		map.put(0, list);
		map.put(1, list);
		map.put(2, list);

		try {
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			int year = c.get(Calendar.YEAR);
			String fileName = year + "年光电院承担规划项目表.xlsx";// 2007版(2003版受限)
			String path = request.getSession().getServletContext().getRealPath("/WEB-INF/reportForm");// 上传服务器的路径
			path = FileHelper.transPath(fileName, path);// 解析后的上传路径

			OutputStream out = new FileOutputStream(path);
			ex.exportMutiExcel(titles, headerMap, map, out, "yyyy-MM-dd");
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
