/**
 * 
 */
package com.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mvc.entity.Alarm;

/**
 * 报警相关
 * 
 * @author zjn
 * @date 2016年10月25日
 */
public interface AlarmRepositoty extends JpaRepository<Alarm, Integer> {

	// 根据ID查看报警详情
	@Query("select a from Alarm a where alar_id=:alarm_id ")
	Alarm findAlarmById(@Param("alarm_id") Integer alarmid);
}
