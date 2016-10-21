package com.mvc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.dao.AlarmStatisticDao;
import com.mvc.entity.AlarmStatistic;
import com.mvc.service.AlarmStatisticService;

/**
 * 报警统计业务实现
 * 
 * @author wangrui
 * @date 2016-10-20
 */
@Service("alarmStatisticServiceImpl")
public class AlarmStatisticServiceImpl implements AlarmStatisticService {

	@Autowired
	AlarmStatisticDao alarmStatisticDao;

	@Override
	public AlarmStatistic findAlst(Integer user_id) {
		return alarmStatisticDao.findAlst(user_id);
	}

}
