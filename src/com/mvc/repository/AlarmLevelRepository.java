package com.mvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mvc.entity.AlarmLevel;

/**
 * 报警等级
 * 
 * @author wanghuimin
 * @date 2016年9月22日
 */
public interface AlarmLevelRepository extends JpaRepository<AlarmLevel, Integer> {
	// 获取报警等级列表
	@Query(" select a from AlarmLevel a where alle_isdelete=0")
	List<AlarmLevel> findAlarmLevelList();

	// 根据id获取报警等级对象
	@Query("select a from AlarmLevel a where alle_id=:alle_id ")
	AlarmLevel findAlarmLevelById(@Param("alle_id") Integer alleid);

}
