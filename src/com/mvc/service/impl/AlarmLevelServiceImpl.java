package com.mvc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.entity.AlarmLevel;
import com.mvc.repository.AlarmLevelRepository;
import com.mvc.service.AlarmLevelService;

/**
 * 报警等级
 * @author wanghuimin
 * @date 2016年9月22日
 */
@Service("alarmLevelServiceImpl")
public class AlarmLevelServiceImpl implements AlarmLevelService {
	@Autowired
	AlarmLevelRepository alarmLevelRepository;

	//添加报警等级
	@Override
	public boolean save(AlarmLevel alarmLevel) {
		AlarmLevel result=alarmLevelRepository.saveAndFlush(alarmLevel);
		if(result.getAlle_id() !=null){
			return true;			
		}
		else
			return false;
	}

}
