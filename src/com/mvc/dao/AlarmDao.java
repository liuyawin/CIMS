package com.mvc.dao;

import java.util.List;

import com.mvc.entity.Alarm;

/**
 * 报警
 * 
 * @author wanghuimin
 * @date 2016年9月26日
 */
public interface AlarmDao {

	// 查找报警信息列表
	public List<Alarm> findAlarmInformationList(Integer user_id, Integer isremove, Integer offset, Integer end);

	// 根据用户名查找报警信息
	List<Alarm> findAlarmByUser(String username, Integer offset, Integer end);

	// 统计报警条数
	Integer countAlarmTotalNum(String searchKey);

	// 根据ID及其类型解除报警
	Boolean updateByIdType(Integer Id, Integer IdType);
}
