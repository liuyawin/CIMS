package com.mvc.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.batik.transcoder.TranscoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.mvc.entity.ProjectStatisticForm;
import com.mvc.entity.SummarySheet;
import com.base.constants.ReportFormConstants;
import com.base.constants.SessionKeyConstants;
import com.mvc.entity.ComoCompareRemo;
import com.mvc.entity.NewComoAnalyse;
import com.mvc.entity.NewRemoAnalyse;
import com.mvc.entity.NoBackContForm;
import com.mvc.entity.PaymentPlanListForm;
import com.mvc.service.ContractService;
import com.mvc.service.ReportFormService;
import com.utils.FileHelper;
import com.utils.Pager;
import com.utils.StringUtil;
import com.utils.SvgPngConverter;
import com.utils.WordHelper;
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
	@Autowired
	ContractService contractService;

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

		String path = request.getSession().getServletContext().getRealPath(ReportFormConstants.SAVE_PATH);// 上传服务器的路径
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
		String path = request.getSession().getServletContext().getRealPath(ReportFormConstants.SAVE_PATH);// 上传服务器的路径
		List<ProjectStatisticForm> list = reportFormService.findProjectStatistic(map, pager, path);

		jsonObject = new JSONObject();
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/**
	 * 导出未返回合同统计表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/exportUnGetContListBylimits.do")
	public ResponseEntity<byte[]> exportNoBackCont(HttpServletRequest request) {
		Integer handler = null;
		Integer header = null;
		String province = null;
		String startTime = null;
		String endTime = null;

		if (StringUtil.strIsNotEmpty(request.getParameter("userId"))) {
			handler = Integer.valueOf(request.getParameter("userId"));// 经手人(设总)
		}
		if (StringUtil.strIsNotEmpty(request.getParameter("headerId"))) {
			header = Integer.valueOf(request.getParameter("headerId"));// 负责人(主任)
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
		map.put("handler", handler);
		map.put("header", header);
		map.put("province", province);
		map.put("startTime", startTime);
		map.put("endTime", endTime);

		String path = request.getSession().getServletContext().getRealPath(ReportFormConstants.SAVE_PATH);// 上传服务器的路径
		ResponseEntity<byte[]> byteArr = reportFormService.exportNoBackCont(map, path);
		return byteArr;
	}

	/**
	 * 查询未返回合同统计表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectUnGetContListBylimits.do")
	public @ResponseBody String selectNoBackCont(HttpServletRequest request) {
		JSONObject jsonObject = JSONObject.fromObject(request.getParameter("limit"));
		Integer page = Integer.parseInt(request.getParameter("page"));// 指定页码

		Map<String, Object> map = reportFormService.JsonObjToMapNoBack(jsonObject);
		Pager pager = reportFormService.pagerTotalNoBack(map, page);
		String path = request.getSession().getServletContext().getRealPath(ReportFormConstants.SAVE_PATH);// 上传服务器的路径
		List<NoBackContForm> list = reportFormService.findNoBackCont(map, pager, path);

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
		String dateOne = request.getParameter("beginYear");
		String dateTwo = request.getParameter("endYear");
		session.setAttribute(SessionKeyConstants.BEGIN_YEAR, dateOne);
		session.setAttribute(SessionKeyConstants.END_YEAR, dateTwo);

		ComoCompareRemo comoCompareRemo = reportFormService.findByDate(dateOne, dateTwo);
		List<NewComoAnalyse> newComoAnalyseList = reportFormService.findComoByDate(dateOne, dateTwo);
		List<NewRemoAnalyse> newRemoAnalysesList = reportFormService.findRemoByDate(dateOne, dateTwo);

		jsonObject.put("comoCompareRemo", comoCompareRemo);
		jsonObject.put("newComoAnalyseList", newComoAnalyseList);
		jsonObject.put("newRemoAnalysesList", newRemoAnalysesList);
		return jsonObject.toString();
	}

	/**
	 * 导出自营项目合同额及到款分析表
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/exportWord.do")
	public ResponseEntity<byte[]> exportWordReport(HttpServletRequest request, HttpSession session) {
		String firstDate = (String) session.getAttribute(SessionKeyConstants.BEGIN_YEAR);
		String secondDate = (String) session.getAttribute(SessionKeyConstants.END_YEAR);

		WordHelper wh = new WordHelper();// 此处不要将实体传入，否则无法解析多个不同类型的list
		String fileName = "自营项目合同额及到款分析表.docx";// 2007版
		String path = request.getSession().getServletContext().getRealPath(ReportFormConstants.SAVE_PATH);
		path = FileHelper.transPath(fileName, path);// 解析后的上传路径
		String modelPath = request.getSession().getServletContext().getRealPath(ReportFormConstants.WORD_MODEL_PATH);// 模板路径

		// 获取表三（到款分析表）的相关数据
		List<NewRemoAnalyse> newRemoAnalyseList = reportFormService.findRemoByDate(firstDate, secondDate);
		// 获取表二（合同额分析表）的数据
		List<NewComoAnalyse> newComoAnalyseList = reportFormService.findComoByDate(firstDate, secondDate);
		Map<String, Object> listMap = new HashMap<String, Object>();// 多个不同实体list放到Map中，在WordHelper中解析
		listMap.put("1", newComoAnalyseList);// 注意（一定不能错）：key存放该list在word中表格的索引，value存放list
		listMap.put("2", newRemoAnalyseList);

		// 获取表一（合同额到款分析表）的数据
		ComoCompareRemo comoCompareRemo = reportFormService.findByDate(firstDate, secondDate);
		Map<String, Object> contentMap = EntryToMap(comoCompareRemo, firstDate, secondDate);

		// 图片相关
		String picCataPath = request.getSession().getServletContext().getRealPath(ReportFormConstants.PIC_PATH + "\\");
		String[] svgs = new String[4];
		String[] picNames = new String[4];
		String[] picPaths = new String[4];
		for (int i = 0; i < 4; i++) {
			switch (i) {
			case 0:
				svgs[i] = request.getParameter("chart1SVGStr");
				break;
			case 1:
				svgs[i] = request.getParameter("chart2SVGStr");
				break;
			case 2:
				svgs[i] = request.getParameter("chart3SVGStr");
				break;
			case 3:
				svgs[i] = request.getParameter("chart4SVGStr");
				break;
			default:
				break;
			}
			if (StringUtil.strIsNotEmpty(svgs[i])) {
				picNames[i] = "pic" + i + ".png";
				picPaths[i] = FileHelper.transPath(picNames[i], picCataPath);// 解析后的上传路径
				try {
					// 图片svgCode转化为png格式，并保存到picPath[i]
					SvgPngConverter.convertToPng(svgs[i], picPaths[i]);
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (TranscoderException e1) {
					e1.printStackTrace();
				}
				Map<String, Object> picMap = null;
				picMap = new HashMap<String, Object>();
				picMap.put("width", 420);
				picMap.put("height", 280);
				picMap.put("type", "png");
				try {
					picMap.put("content", FileHelper.inputStream2ByteArray(new FileInputStream(picPaths[i]), true));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				contentMap.put("${pic" + i + "}", picMap);
			}
		}
		try {
			OutputStream out = new FileOutputStream(path);// 保存路径
			wh.export2007Word(modelPath, listMap, contentMap, out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ResponseEntity<byte[]> byteArr = FileHelper.downloadFile(fileName, path);
		return byteArr;
	}

	/**
	 * 给模板中需要替换的变量赋值，打包成Map格式
	 * 
	 * @param comoCompareRemo
	 * @param firstDate
	 * @param secondDate
	 * @param total_one
	 * @param total_two
	 * @return
	 */
	private Map<String, Object> EntryToMap(ComoCompareRemo comoCompareRemo, String firstDate, String secondDate) {
		Map<String, Object> contentMap = new HashMap<String, Object>();
		// 表一相关数据
		Integer thirdDate = Integer.valueOf(secondDate) + 1;
		contentMap.put("${date_one}", firstDate);
		contentMap.put("${date_two}", secondDate);
		contentMap.put("${date_three}", thirdDate.toString());

		contentMap.put("${como_one}", comoCompareRemo.getComo_one().toString());
		contentMap.put("${remo_one}", comoCompareRemo.getRemo_one().toString());
		contentMap.put("${cont_num_one}", comoCompareRemo.getCont_num_one().toString());
		contentMap.put("${como_two}", comoCompareRemo.getComo_two().toString());
		contentMap.put("${remo_two}", comoCompareRemo.getRemo_two().toString());
		contentMap.put("${cont_num_two}", comoCompareRemo.getCont_num_two().toString());
		contentMap.put("${ratio_como}", comoCompareRemo.getRatio_como());
		contentMap.put("${ratio_remo}", comoCompareRemo.getRatio_remo());
		contentMap.put("${ratio_conum}", comoCompareRemo.getRatio_conum());
		return contentMap;
	}

	/**
	 * 查询光伏项目明细表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/selectSummarySheetList.do")
	public @ResponseBody String selectSummarySheetList(HttpServletRequest request) {
		Float totalMoney = (float) 0;
		String date = "";
		JSONObject jsonObject = JSONObject.fromObject(request.getParameter("summaryLimit"));

		if (jsonObject.containsKey("year")) {
			date = jsonObject.getString("year");
		} else {
			date = "";
		}
		Integer page = Integer.parseInt(request.getParameter("page"));// 分页
		Pager pager = reportFormService.pagerTotalSummary(date, page);
		List<SummarySheet> list = reportFormService.findSummaryByDate(date, pager);
		totalMoney = contractService.getTotalMoney(date);

		jsonObject = new JSONObject();
		jsonObject.put("list", list);
		jsonObject.put("totalMoney", String.format("%.2f", totalMoney));
		jsonObject.put("totalRow", pager.getTotalRow());
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
	}

	/**
	 * // 根据日期导出当年光伏项目明细表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/exportSummarySheet.do")
	public ResponseEntity<byte[]> exportSummarySheet(HttpServletRequest request) {
		String date = "";

		if (StringUtil.strIsNotEmpty(request.getParameter("year"))) {
			date = request.getParameter("year");
		}
		String path = request.getSession().getServletContext().getRealPath(ReportFormConstants.SAVE_PATH);// 上传服务器的路径
		ResponseEntity<byte[]> byteww = reportFormService.exportSummarySheet(date, path);
		return byteww;
	}

	/**
	 * 根据日期导出多年光伏项目明细表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/exportSummarySheetList.do")
	public ResponseEntity<byte[]> exportSummarySheetList(HttpServletRequest request) {

		String startTime = "";
		String endTime = "";

		if (StringUtil.strIsNotEmpty(request.getParameter("startYear"))) {
			startTime = request.getParameter("startYear");// 开始时间
		}
		if (StringUtil.strIsNotEmpty(request.getParameter("endYear"))) {
			endTime = request.getParameter("endYear");// 结束时间
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("startTime", startTime);
		map.put("endTime", endTime);

		String path = request.getSession().getServletContext().getRealPath(ReportFormConstants.SAVE_PATH);// 上传服务器的路径
		ResponseEntity<byte[]> byteArr = reportFormService.exportSummarySheetList(map, path);
		return byteArr;
	}

	/*
	 * ***********************************王慧敏报表开始*******************************
	 */
	/**
	 * 查询光伏自营项目催款计划表
	 */
	@RequestMapping("/selectPaymentPlanList.do")
	public @ResponseBody String selectPaymentPlanList(HttpServletRequest request) {
		JSONObject jsonObject = JSONObject.fromObject(request.getParameter("limit"));
		Integer page = Integer.parseInt(request.getParameter("page"));// 分页
		Map<String, Object> map = reportFormService.JsonObjToMap(jsonObject);
		Pager pager = reportFormService.pagerTotal_payment(map, page);
		String path = request.getSession().getServletContext().getRealPath(ReportFormConstants.SAVE_PATH);// 上传服务器的路径
		List<PaymentPlanListForm> list = reportFormService.findPaymentPlanList(map, pager, path);

		jsonObject = new JSONObject();
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();

	}

	/**
	 * 导出光伏自营项目催款计划表
	 */
	@RequestMapping("/exportPaymentPlanList.do")
	public ResponseEntity<byte[]> exportPaymentPlanList(HttpServletRequest request) {
		String province = null;// 行政区域
		String cont_project = null;// 工程名称 && 项目名称
		String cont_client = null;// 业主名称 && 业主公司名称
		Float cont_money = null;// 合同金额
		Float remo_totalmoney = null;// 2015年累计已到款
		Float balance_money = null;// 余额
		Float invo_totalmoney = null;// 已开发票金额
		Float noinvo_totalmoney = null;// 未开发票金额
		String startTime = null;
		String endTime = null;
		String planTime = null;

		if (StringUtil.strIsNotEmpty(request.getParameter("province"))) {
			province = request.getParameter("province");// 行政区域
		}
		if (StringUtil.strIsNotEmpty(request.getParameter("contProject"))) {
			cont_project = request.getParameter("contProject");// 工程名称 && 项目名称
		}
		if (StringUtil.strIsNotEmpty(request.getParameter("contClient"))) {
			cont_client = request.getParameter("contClient");// 业主名称 && 业主公司名称
		}
		if (StringUtil.strIsNotEmpty(request.getParameter("contMoney"))) {
			cont_money = Float.valueOf(request.getParameter("contMoney"));// 合同金额
		}
		if (StringUtil.strIsNotEmpty(request.getParameter("remoTotalmoney"))) {
			remo_totalmoney = Float.valueOf(request.getParameter("remoTotalmoney"));// 累计已到款
		}
		if (StringUtil.strIsNotEmpty(request.getParameter("balanceMoney"))) {
			balance_money = Float.valueOf(request.getParameter("balanceMoney"));// 余额
		}
		if (StringUtil.strIsNotEmpty(request.getParameter("invoTotalmoney"))) {
			invo_totalmoney = Float.valueOf(request.getParameter("invoTotalmoney"));// 已开发票金额
		}
		if (StringUtil.strIsNotEmpty(request.getParameter("noinvoTotalmoney"))) {
			noinvo_totalmoney = Float.valueOf(request.getParameter("noinvoTotalmoney"));// 未开发票金额
		}
		if (StringUtil.strIsNotEmpty(request.getParameter("startDate"))) {
			startTime = request.getParameter("startDate");// 开始时间
		}
		if (StringUtil.strIsNotEmpty(request.getParameter("endDate"))) {
			endTime = request.getParameter("endDate");// 结束时间
		}
		if (StringUtil.strIsNotEmpty(request.getParameter("planDate"))) {
			planTime = request.getParameter("planDate");// 计划催款年份
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("province", province);
		map.put("cont_project", cont_project);
		map.put("cont_client", cont_client);
		map.put("cont_money", cont_money);
		map.put("remo_totalmoney", remo_totalmoney);
		map.put("balance_money", balance_money);
		map.put("invo_totalmoney", invo_totalmoney);
		map.put("noinvo_totalmoney", noinvo_totalmoney);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("planTime", planTime);

		String path = request.getSession().getServletContext().getRealPath(ReportFormConstants.SAVE_PATH);// 上传服务器的路径
		ResponseEntity<byte[]> byteww = reportFormService.exportProvisionPlan(map, path);
		return byteww;
	}

	/*
	 * ***********************************王慧敏报表结束*******************************
	 */
}
