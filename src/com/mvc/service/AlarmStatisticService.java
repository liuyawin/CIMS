package com.mvc.service;

import com.mvc.entity.AlarmStatistic;

/**
 * 报警统计业务
 * 
 * @author wangrui
 * @date 2016-10-20
 */
public interface AlarmStatisticService {

	// 报警统计
	AlarmStatistic findAlst(Integer user_id);
}
