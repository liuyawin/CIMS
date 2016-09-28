package com.mvc.service;

import com.mvc.entity.AlarmLevel;

/**
 * 报警等级
 * @author wanghuimin
 * @date 2016年9月22日
 */
public interface AlarmLevelService {
	//添加报警等级
	boolean save(AlarmLevel alarmLevel);

}
