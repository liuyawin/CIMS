package com.mvc.service;

import java.util.List;

import com.mvc.entity.ContractRecord;

/**
 * 合同日志业务层
 * 
 * @author wangrui
 * @date 2016-10-25
 */
public interface ContractRecordService {

	// 根据合同ID获取合同操作日志
	List<ContractRecord> selectContRecordByContId(Integer cont_id);

	// 添加合同操作日志
	Boolean addContRecord(ContractRecord contractRecord);
}
