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
import com.base.constants.ReportFormConstants;
import com.mvc.entity.ComoCompareRemo;
import com.mvc.entity.NewComoAnalyse;
import com.mvc.entity.NoBackContForm;
import com.mvc.entity.PaymentPlanListForm;
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
		String province = null;
		String startTime = null;
		String endTime = null;

		if (StringUtil.strIsNotEmpty(request.getParameter("userId"))) {
			handler = Integer.valueOf(request.getParameter("userId"));// 经手人
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
		System.out.println("dateOne:" + request.getParameter("beginYear"));
		String dateTwo = request.getParameter("endYear");
		System.out.println("dateTwo:" + request.getParameter("endYear"));
		ComoCompareRemo comoCompareRemo = reportFormService.findByDate(dateOne, dateTwo);
		List<NewComoAnalyse> newComoAnalyseList = reportFormService.findComoByDate(dateOne, dateTwo);
		jsonObject.put("comoCompareRemo", comoCompareRemo);
		jsonObject.put("newComoAnalyseList", newComoAnalyseList);
		return jsonObject.toString();
	}

	/**
	 * 导出自营项目合同额及到款分析表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/exportWord.do")
	public ResponseEntity<byte[]> exportWordReport(HttpServletRequest request) {

		String firstDate = request.getParameter("beginYear");
		String secondDate = request.getParameter("endYear");
		String svg1 = request.getParameter("chart1SVGStr");
		String svg2 = request.getParameter("chart2SVGStr");
		System.out.print("svg1:" + svg1 + "\n");
		System.out.print("svg2:" + svg2 + "\n");
		// String firstDate = "2015";
		// String secondDate = "2016";

		String picFileName = "pic.png";// 2007版
		String picPath = request.getSession().getServletContext().getRealPath(ReportFormConstants.PIC_PATH);
		picPath = FileHelper.transPath(picFileName, picPath);// 解析后的上传路径
		try {
			SvgPngConverter.convertToPng(svg1, picPath + "\\" + picFileName);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (TranscoderException e1) {
			e1.printStackTrace();
		}

		WordHelper<NewComoAnalyse> wh = new WordHelper<NewComoAnalyse>();
		String fileName = "自营项目合同额及到款分析表.docx";// 2007版
		String path = request.getSession().getServletContext().getRealPath(ReportFormConstants.SAVE_PATH);
		path = FileHelper.transPath(fileName, path);// 解析后的上传路径
		String modelPath = request.getSession().getServletContext().getRealPath(ReportFormConstants.WORD_MODEL_PATH);// 模板路径
		// 获取表二（合同额分析表）的数据
		List<NewComoAnalyse> newComoAnalyseList = reportFormService.findComoByDate(firstDate, secondDate);
		String total_one = newComoAnalyseList.get(0).getTotal_one().toString();// 第一年合同总金额
		String total_two = newComoAnalyseList.get(0).getTotal_two().toString();// 第二年合同总金额
		// 获取表一（合同额到款分析表）的数据
		ComoCompareRemo comoCompareRemo = reportFormService.findByDate(firstDate, secondDate);
		Map<String, Object> contentMap = EntryToMap(comoCompareRemo, firstDate, secondDate, total_one, total_two);

		String picCataPath = request.getSession().getServletContext().getRealPath(ReportFormConstants.PIC_PATH) + "\\";// 图片目录路径
		String[] picPath11 = { picCataPath + "a.png", picCataPath + "b.png" };
		Map<String, Object> picMap = null;

		for (int i = 0; i < 2; i++) {
			picMap = new HashMap<String, Object>();
			picMap.put("width", 300);
			picMap.put("height", 300);
			picMap.put("type", "png");
			try {
				picMap.put("content", FileHelper.inputStream2ByteArray(new FileInputStream(picPath11[i]), true));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			contentMap.put("${pic" + i + "}", picMap);
		}
		try {
			OutputStream out = new FileOutputStream(path);// 保存路径
			wh.export2007Word(modelPath, newComoAnalyseList, contentMap, out, 1);
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
	private Map<String, Object> EntryToMap(ComoCompareRemo comoCompareRemo, String firstDate, String secondDate,
			String total_one, String total_two) {
		Map<String, Object> contentMap = new HashMap<String, Object>();
		// 表一相关数据
		contentMap.put("${date_one}", firstDate);
		contentMap.put("${date_two}", secondDate);
		contentMap.put("${como_one}", comoCompareRemo.getComo_one().toString());
		contentMap.put("${remo_one}", comoCompareRemo.getRemo_one().toString());
		contentMap.put("${cont_num_one}", comoCompareRemo.getCont_num_one().toString());
		contentMap.put("${como_two}", comoCompareRemo.getComo_two().toString());
		contentMap.put("${remo_two}", comoCompareRemo.getRemo_two().toString());
		contentMap.put("${cont_num_two}", comoCompareRemo.getCont_num_two().toString());
		contentMap.put("${ratio_como}", comoCompareRemo.getRatio_como());
		contentMap.put("${ratio_remo}", comoCompareRemo.getRatio_remo());
		contentMap.put("${ratio_conum}", comoCompareRemo.getRatio_conum());
		// 表二相关数据
		contentMap.put("${total_one}", total_one);
		contentMap.put("${total_two}", total_two);
		return contentMap;
	}

	/*
	 * ***********************************张姣娜报表结束*******************************
	 */
	
	/*
	 * ***********************************王慧敏报表开始*******************************
	 */
	/**
	 * 查询光伏自营项目催款计划表
	 */
	@RequestMapping("/selectPaymentPlanList.do")
	public @ResponseBody String selectPaymentPlanList(HttpServletRequest request){
		JSONObject jsonObject=JSONObject.fromObject(request.getParameter("limit"));
		Integer page=Integer.parseInt(request.getParameter("page"));//分页
		Map<String, Object> map=reportFormService.JsonObjToMap(jsonObject);
		Pager pager=reportFormService.pagerTotal_payment(map, page);
		String path = request.getSession().getServletContext().getRealPath("/WEB-INF/reportForm");// 上传服务器的路径
		List<PaymentPlanListForm> list=reportFormService.findPaymentPlanList(map, pager, path);
		
		jsonObject=new JSONObject();
		jsonObject.put("list", list);
		jsonObject.put("totalPage", pager.getTotalPage());
		return jsonObject.toString();
		


	}
	/**
	 * 导出光伏自营项目催款计划表
	 */
	@RequestMapping("/exportPaymentPlanList.do")
	public ResponseEntity<byte[]> exportPaymentPlanList(HttpServletRequest request){
		String province = null;// 行政区域
		String cont_project=null;// 工程名称 && 项目名称
		String cont_client=null;// 业主名称 && 业主公司名称
		Float cont_money = null;// 合同金额
		Float remo_totalmoney=null;// 2015年累计已到款
		Float balance_money=null;// 余额
		Float invo_totalmoney=null;// 已开发票金额
		Float noinvo_totalmoney=null;// 未开发票金额
		String startTime = null;
		String endTime = null;
		
		if(StringUtil.strIsNotEmpty(request.getParameter("province"))){
			province=request.getParameter("province");//行政区域
		}
		if(StringUtil.strIsNotEmpty(request.getParameter("contProject"))){
			cont_project=request.getParameter("contProject");//工程名称 && 项目名称			
		}
		if(StringUtil.strIsNotEmpty(request.getParameter("contClient"))){
			cont_client=request.getParameter("contClient");//业主名称 && 业主公司名称
		}
		if(StringUtil.strIsNotEmpty(request.getParameter("contMoney"))){
			cont_money=Float.valueOf(request.getParameter("contMoney"));//合同金额
		}
		if(StringUtil.strIsNotEmpty(request.getParameter("remoTotalmoney"))){
			remo_totalmoney=Float.valueOf(request.getParameter("remoTotalmoney"));//累计已到款
		}
		if(StringUtil.strIsNotEmpty(request.getParameter("balanceMoney"))){
			balance_money=Float.valueOf(request.getParameter("balanceMoney"));//余额
		}
		if(StringUtil.strIsNotEmpty(request.getParameter("invoTotalmoney"))){
			invo_totalmoney=Float.valueOf(request.getParameter("invoTotalmoney"));// 已开发票金额
		}
		if(StringUtil.strIsNotEmpty(request.getParameter("noinvoTotalmoney"))){
			noinvo_totalmoney=Float.valueOf(request.getParameter("noinvoTotalmoney"));// 未开发票金额
		}
		if (StringUtil.strIsNotEmpty(request.getParameter("startDate"))) {
			startTime = request.getParameter("startDate") + "-01";// 开始时间
		}
		if (StringUtil.strIsNotEmpty(request.getParameter("endDate"))) {
			endTime = request.getParameter("endDate") + "-01";// 结束时间
		}

		Map<String, Object> map=new HashMap<String,Object>();
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
		
		String path=request.getSession().getServletContext().getRealPath("/WEB-INF/reportForm");// 上传服务器的路径
		ResponseEntity<byte[]> byteww=reportFormService.exportProvisionPlan(map, path);
		return byteww;
	}
	
	
	
	
	/*
	 * ***********************************王慧敏报表结束*******************************
	 */
}
