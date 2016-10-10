package com.mvc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mvc.entity.ReceiveNode;

/**
 * 收款节点JPA
 * 
 * @author wangrui
 * @date 2016-09-20
 */
public interface ReceiveNodeRepository extends JpaRepository<ReceiveNode, Integer> {

	// 根据合同ID查找收款节点列表
	@Query("select rn from ReceiveNode rn where cont_id = :cont_id ")
	List<ReceiveNode> findByContId(@Param("cont_id") Integer cont_id);

}
