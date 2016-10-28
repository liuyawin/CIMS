/**
 * 
 */
package com.mvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.dao.AlarmDao;
import com.mvc.entity.Alarm;
import com.mvc.repository.AlarmRepositoty;
import com.mvc.service.AlarmService;

/**
 * 报警相关
 * 
 * @author zjn
 * @date 2016年10月25日
 */
@Service("alarmServiceImpl")
public class AlarmServiceImpl implements AlarmService {
	@Autowired
	AlarmRepositoty alarmRepository;
	@Autowired
	AlarmDao alarmDao;

	// 根据ID查看报警详情
	@Override
	public Alarm findAlarmById(Integer alarmid) {
		return alarmRepository.findAlarmById(alarmid);
	}

	// 根据参数统计报警列表条数，alarmType是数组类型:[2,3]
	@Override
	public Integer countByParam(Integer user_id, String alarmType, String searchKey) {
		return alarmDao.countByParam(user_id, alarmType, searchKey);
	}

	// 查找报警信息列表
	@Override
	public List<Alarm> findAlarmList(Integer user_id, String searchKey, String alarmType, Integer offset, Integer end) {
		return alarmDao.findAlarmList(user_id, searchKey, alarmType, offset, end);
	}

	// 根据ID及其类型解除报警
	@Override
	public boolean updateByIdType(Integer Id, Integer IdType) {
		return alarmDao.updateByIdType(Id, IdType);
	}

}
