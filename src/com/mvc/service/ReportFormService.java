package com.mvc.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.mvc.entity.ProjectStatisticForm;
import com.utils.Pager;

import net.sf.json.JSONObject;

import com.mvc.entity.ComoCompareRemo;
import com.mvc.entity.NewComoAnalyse;
import com.mvc.entity.NewRemoAnalyse;
import com.mvc.entity.NoBackContForm;

/**
 * 报表业务层
 * 
 * @author wangrui
 * @date 2016-11-15
 */
public interface ReportFormService {

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

	/************************************************ 张姣娜 ***************************************/

	// 导出未返回合同统计表
	ResponseEntity<byte[]> exportNoBackCont(Map<String, Object> map, String path);

	// 查询未返回合同统计表
	List<NoBackContForm> findNoBackCont(Map<String, Object> map, Pager pager, String path);

	// 根据日期获取合同额到款对比表
	ComoCompareRemo findByDate(String firstDate, String secondDate);

	// 根据日期获取新签合同额分析表
	List<NewComoAnalyse> findComoByDate(String firstDate, String secondDate);

	// 根据日期获取到款分析表
	List<NewRemoAnalyse> findRemoByDate(String firstDate, String secondDate);

}
