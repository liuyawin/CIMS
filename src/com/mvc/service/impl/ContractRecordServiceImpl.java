package com.mvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.dao.ContractRecordDao;
import com.mvc.entity.ContractRecord;
import com.mvc.service.ContractRecordService;

/**
 * 合同日志业务层实现
 * 
 * @author wangrui
 * @date 2016-10-25
 */
@Service("contractRecordServiceImpl")
public class ContractRecordServiceImpl implements ContractRecordService {

	@Autowired
	ContractRecordDao contractRecordDao;

	@Override
	public List<ContractRecord> selectContRecordByContId(Integer cont_id) {
		return contractRecordDao.selectContRecordByContId(cont_id);
	}

}
