/**
 * 
 */
package com.mvc.dao;

import java.util.List;

import com.mvc.entity.Alarm;

/**
 * 报警
 * 
 * @author zjn
 * @date 2016年10月25日
 */
public interface AlarmDao {

	// 根据参数统计报警列表条数，alarmType是数组类型:[2,3]
	Integer countByParam(Integer user_id, String alarmType, String searchKey);

	// 查找报警信息列表
	List<Alarm> findAlarmList(Integer user_id, String searchKey, String alarmType, Integer offset, Integer end);

	// 根据ID及其类型解除报警
	boolean updateByIdType(Integer Id, Integer IdType);

}
