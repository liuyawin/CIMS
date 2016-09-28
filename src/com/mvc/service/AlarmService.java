package com.mvc.service;

import java.util.List;

import com.mvc.entity.Alarm;
import com.mvc.entity.User;

/**
 * 报警
 * 
 * @author wanghuimin
 * @date 2016年9月26日
 */
public interface AlarmService {
	// 查找报警信息列表
	List<Alarm> findAlarmInformationList(Integer user_id, Integer isremove, Integer offset, Integer end);

	// 统计报警列表条数
	Long countTotal(Integer user_id);

	// 统计报警条数
	Integer countTotalNum(String searchKey);

	// 根据ID查看报警详情
	Alarm findAlarmContentById(Integer alarmid);

	// 根据用户名查找报警信息
	List<Alarm> findAlarmByUser(String username, Integer offset, Integer end);

}
