package com.mvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.dao.ContractDao;
import com.mvc.entity.Contract;
import com.mvc.service.ContractService;

/**
 * 合同业务实现
 * 
 * @author wangrui
 * @date 2016-09-10
 */
@Service("contractServiceImpl")
public class ContractServiceImpl implements ContractService {

	@Autowired
	ContractDao contractDao;

	/**
	 * 根据创建者name获取合同列表
	 */
	@Override
	public List<Contract> findAllCont(int creator_id) {
		return contractDao.findAllCont(creator_id);
	}

	@Override
	public List<Contract> findAllDebtCont(int creator_id) {
		return contractDao.findAllDebtCont(creator_id);
	}

	@Override
	public List<Contract> findAllOverdueCont(int creator_id) {
		return contractDao.findAllOverdueCont(creator_id);
	}

}
