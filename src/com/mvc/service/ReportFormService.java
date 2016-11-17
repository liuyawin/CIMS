package com.mvc.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.mvc.entity.ProjectStatisticForm;
import com.utils.Pager;

import net.sf.json.JSONObject;

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

	// 将JSONObject转成Map
	Map<String, Object> JsonObjToMap(JSONObject jsonObject);

	// 查询报表页码相关
	Pager pagerTotal(Map<String, Object> map, Integer page);
}
