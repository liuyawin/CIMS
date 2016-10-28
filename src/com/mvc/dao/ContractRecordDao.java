package com.mvc.dao;

import java.util.List;

import com.mvc.entity.ContractRecord;

/**
 * 合同日志持久层
 * 
 * @author wangrui
 * @date 2016-10-25
 */
public interface ContractRecordDao {

	// 根据合同ID获取合同操作日志
	List<ContractRecord> selectContRecordByContId(Integer cont_id);
}
