package com.mvc.dao;

import com.mvc.entity.AlarmStatistic;

/**
 * 报警统计持久层
 * 
 * @author wangrui
 * @date 2016-10-20
 */
public interface AlarmStatisticDao {

	// 报警统计
	AlarmStatistic findAlst(Integer user_id);
}
