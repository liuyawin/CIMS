package com.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mvc.entity.Alarm;

/**
 * 报警
 * 
 * @author wanghuimin
 * @date 2016年9月26日
 */
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
	// 统计报警列表条数
	@Query("select count(alar_id) from Alarm a where receiver_id=:receiver_id and alar_isremove=:isremove")
	Long countAlarmTotal(@Param("receiver_id") Integer user_id,@Param("isremove") Integer isremove);
	
	//统计报警条数
	@Query("select count(alar_id) from Alarm a ")
	Integer countAlarmTotalNum();

	// 根据ID查看报警详情
	@Query("select a from Alarm a where alar_id=:alarm_id ")
	Alarm findAlarmContentById(@Param("alarm_id") Integer alarmid);

}
