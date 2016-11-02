package com.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mvc.entity.ContractRecord;

/**
 * 合同记录JPA
 * 
 * @author wangrui
 * @date 2016-11-01
 */
public interface ContractRecordRepository extends JpaRepository<ContractRecord, Integer> {

}
