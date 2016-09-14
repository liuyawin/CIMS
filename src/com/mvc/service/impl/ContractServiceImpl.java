package com.mvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.dao.ContractDao;
import com.mvc.entity.Contract;
import com.mvc.repository.ContractRepository;
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
	@Autowired
	ContractRepository contractRepository;

	// 查询所有合同
	@Override
	public List<Contract> findAllCont(int creator_id) {
		return contractDao.findAllCont(creator_id);
	}

	// 查询所有欠款合同
	@Override
	public List<Contract> findAllDebtCont(int creator_id) {
		return contractDao.findAllDebtCont(creator_id);
	}

	// 查询所有逾期合同
	@Override
	public List<Contract> findAllOverdueCont(int creator_id) {
		return contractDao.findAllOverdueCont(creator_id);
	}

	// 查询合同总条数
	@Override
	public int countTotal(int creator_id) {
		return contractRepository.countTotal(creator_id);
	}

	// 根据页数选择合同列表
	@Override
	public List<Contract> findByPage(int creator_id, Integer offset, Integer end) {
		return contractDao.findByPage(creator_id, offset, end);
	}

	// 根据合同名获取合同信息
	@Override
	public List<Contract> findConByName(int creator_id, String contName) {
		return contractRepository.findConByName(creator_id, contName);
	}

}
