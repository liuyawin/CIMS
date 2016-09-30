package com.mvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.dao.AlarmLevelDao;
import com.mvc.entity.AlarmLevel;
import com.mvc.repository.AlarmLevelRepository;
import com.mvc.service.AlarmLevelService;

/**
 * 报警等级
 * 
 * @author wanghuimin
 * @date 2016年9月22日
 */
@Service("alarmLevelServiceImpl")
public class AlarmLevelServiceImpl implements AlarmLevelService {
	@Autowired
	AlarmLevelRepository alarmLevelRepository;
	@Autowired
	AlarmLevelDao alarmLevelDao;

	// 添加报警等级
	@Override
	public boolean save(AlarmLevel alarmLevel) {
		AlarmLevel result = alarmLevelRepository.saveAndFlush(alarmLevel);
		if (result.getAlle_id() != null) {
			return true;
		} else
			return false;
	}

	// 获取报警等级列表
	@Override
	public List<AlarmLevel> findAlarmLevelList() {
		return alarmLevelRepository.findAlarmLevelList();
	}

	// 根据id获取报警等级对象
	@Override
	public AlarmLevel findAlarmLevelById(Integer alleid) {
		return alarmLevelRepository.findAlarmLevelById(alleid);
	}

	// 根据id删除报警等级
	@Override
	public boolean deleteAlarmLevelById(Integer alleid) {
		return alarmLevelDao.deleteAlarmLevelById(alleid);
	}

}
