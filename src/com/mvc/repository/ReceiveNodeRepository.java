package com.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mvc.entity.ReceiveNode;

/**
 * 收款节点JPA
 * 
 * @author wangrui
 * @date 2016-09-20
 */
public interface ReceiveNodeRepository extends JpaRepository<ReceiveNode, Integer> {

}
