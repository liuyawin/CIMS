package com.mvc.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.mvc.entity.ProjectStatisticForm;
import com.mvc.entity.SummarySheet;
import com.utils.Pager;

import net.sf.json.JSONObject;

import com.mvc.entity.ComoCompareRemo;
import com.mvc.entity.NewComoAnalyse;
import com.mvc.entity.NewRemoAnalyse;
import com.mvc.entity.NoBackContForm;
import com.mvc.entity.PaymentPlanListForm;

/**
 * 报表业务层
 * 
 * @author wangrui
 * @date 2016-11-15
 */
public interface ReportFormService {

	/************************************************ 王睿 ***************************************/
	// 导出光电院项目分项统计表
	ResponseEntity<byte[]> exportProjectStatistic(Map<String, Object> map, String path);

	// 查询光电院项目分项统计表
	List<ProjectStatisticForm> findProjectStatistic(Map<String, Object> map, Pager pager, String path);

	// 将JSONObject转成Map分项统计表
	Map<String, Object> JsonObjToMap(JSONObject jsonObject);

	// 将JSONObject转成Map未返回合同统计表
	Map<String, Object> JsonObjToMapNoBack(JSONObject jsonObject);

	// 查询分项统计表页码相关
	Pager pagerTotal(Map<String, Object> map, Integer page);

	// 查询未返回合同统计表页码相关
	Pager pagerTotalNoBack(Map<String, Object> map, Integer page);

	// 导出未返回合同统计表
	ResponseEntity<byte[]> exportNoBackCont(Map<String, Object> map, String path);

	// 查询未返回合同统计表
	List<NoBackContForm> findNoBackCont(Map<String, Object> map, Pager pager, String path);

	/************************************************ 张姣娜 ***************************************/
	// 根据日期获取合同额到款对比表
	ComoCompareRemo findByDate(String firstDate, String secondDate);

	// 根据日期获取新签合同额分析表
	List<NewComoAnalyse> findComoByDate(String firstDate, String secondDate);

	// 根据日期获取到款分析表
	List<NewRemoAnalyse> findRemoByDate(String firstDate, String secondDate);

	// 查询光伏项目统计列表
	List<SummarySheet> findSummaryByDate(String date, Pager pager);

	// 查询光伏项目明细表页码
	Pager pagerTotalSummary(String date, Integer page);

	// 根据日期导出当年光伏项目明细表
	ResponseEntity<byte[]> exportSummarySheet(String date, String path);

	// 根据日期导出多年光伏项目明细表
	ResponseEntity<byte[]> exportSummarySheetList(Map<String, String> map, String path);
	
	

	/************************************************ 王慧敏 ***************************************/
	// 导出光伏自营项目催款计划表
	ResponseEntity<byte[]> exportProvisionPlan(Map<String, Object> map, String path);

	// 查询光伏自营项目催款计划表
	List<PaymentPlanListForm> findPaymentPlanList(Map<String, Object> map, Pager pager, String path);

	// 查询报表页码相关
	Pager pagerTotal_payment(Map<String, Object> map, Integer page);
}
