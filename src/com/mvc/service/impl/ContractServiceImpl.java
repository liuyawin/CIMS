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

	// 查询所有欠款合同
	@Override
	public List<Contract> findAllDebtCont(int creator_id, String contName, Integer offset, Integer end) {
		return contractDao.findAllDebtCont(creator_id, contName, offset, end);
	}

	// 查询所有逾期合同
	@Override
	public List<Contract> findAllOverdueCont(int creator_id, String contName, Integer offset, Integer end) {
		return contractDao.findAllOverdueCont(creator_id, contName, offset, end);
	}

	// 查询合同总条数
	@Override
	public Long countTotal(int creator_id, String contName, String methodType) {
		return contractDao.countTotal(creator_id, contName, methodType);
	}

	// 根据合同名获取合同信息
	@Override
	public List<Contract> findConByName(int creator_id, String contName, Integer offset, Integer end) {
		return contractDao.findConByName(creator_id, contName, offset, end);
	}

	@Override
	public boolean addContract(Contract contract) {
		Contract result = contractRepository.saveAndFlush(contract);
		if (result.getCont_id() != null)
			return true;
		else
			return false;
	}

}
