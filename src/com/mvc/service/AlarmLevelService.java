package com.mvc.service;

import java.util.List;
import com.mvc.entity.AlarmLevel;

/**
 * 报警等级
 * 
 * @author wanghuimin
 * @date 2016年9月22日
 */
public interface AlarmLevelService {
	// 添加报警等级
	boolean save(AlarmLevel alarmLevel);

	// 获取报警等级列表
	List<AlarmLevel> findAlarmLevelList();

	// 根据id获取报警等级对象
	AlarmLevel findAlarmLevelById(Integer alleid);

	// 根据id删除报警等级
	boolean deleteAlarmLevelById(Integer alleid);

}
