package com.mvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.dao.AlarmDao;
import com.mvc.entity.Alarm;
import com.mvc.repository.AlarmRepository;
import com.mvc.service.AlarmService;

/**
 * 报警
 * 
 * @author wanghuimin
 * @date 2016年9月26日
 */
@Service("alarmServiceImpl")
public class AlarmServiceImpl implements AlarmService {
	@Autowired
	AlarmRepository alarmRepository;
	@Autowired
	AlarmDao alarmDao;

	// 查找报警信息列表
	@Override
	public List<Alarm> findAlarmInformationList(Integer user_id, Integer isremove, Integer offset, Integer end) {
		return alarmDao.findAlarmInformationList(user_id, isremove, offset, end);
	}

	// 统计报警列表条数
	@Override
	public Long countTotal(Integer user_id) {
		return alarmRepository.countAlarmTotal(user_id);
	}

	// 根据ID查看报警详情
	@Override
	public Alarm findAlarmContentById(Integer alarmid) {
		return alarmRepository.findAlarmContentById(alarmid);
	}

	// 根据用户名查找报警信息
	@Override
	public List<Alarm> findAlarmByUser(String username, Integer offset, Integer end) {
		return alarmDao.findAlarmByUser(username, offset, end);
	}

	// 统计报警条数
	@Override
	public Integer countTotalNum(String searchKey) {
		return alarmDao.countAlarmTotalNum(searchKey);
	}

	// 根据ID及其类型解除报警
	public boolean updateByIdType(Integer Id, Integer IdType) {
		return alarmDao.updateByIdType(Id, IdType);
	}

}
