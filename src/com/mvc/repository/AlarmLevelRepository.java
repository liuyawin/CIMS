package com.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mvc.entity.AlarmLevel;

/**
 * 报警等级
 * @author wanghuimin
 * @date 2016年9月22日
 */
public interface AlarmLevelRepository extends JpaRepository<AlarmLevel, Long> {
	

}
