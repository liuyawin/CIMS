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
	@Query("select count(alar_id) from Alarm a where receiver_id=:receiver_id and alar_isremove=0 and alar_id in (select alar_id from Alarm group by task_id,reno_id,prst_id  having count(*)>1)")
	Long countNumByUserId(@Param("receiver_id") Integer user_id);

	// 根据ID查看报警详情
	@Query("select a from Alarm a where alar_id=:alarm_id ")
	Alarm findAlarmContentById(@Param("alarm_id") Integer alarmid);

}
