/**
 * 
 */
package com.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mvc.entity.ReceiveMoney;

/**
 * 到款
 * 
 * @author zjn
 * @date 2016年10月27日
 */
public interface ReceiveMoneyRepository extends JpaRepository<ReceiveMoney, Integer> {

	// 根据任务ID查询任务详情
	@Query("select r from ReceiveMoney r where remo_id=:remo_id")
	ReceiveMoney findById(@Param("remo_id") Integer remoId);
}
