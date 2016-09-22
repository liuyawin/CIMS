package com.mvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
public class ContractServiceImpl implements ContractService {

	@Autowired
	ContractDao contractDao;
	@Autowired
	ContractRepository contractRepository;

	// 查询所有欠款合同
	@Override
	public List<Contract> findAllDebtCont(String contName, Integer offset, Integer end) {
		return contractDao.findAllDebtCont(contName, offset, end);
	}

	// 查询所有逾期合同
	@Override
	public List<Contract> findAllOverdueCont(String contName, Integer offset, Integer end) {
		return contractDao.findAllOverdueCont(contName, offset, end);
	}

	// 查询合同总条数
	@Override
	public Long countTotal(String contName, String methodType) {
		return contractDao.countTotal(contName, methodType);
	}

	// 根据合同名获取合同信息
	@Override
	public List<Contract> findConByName(String contName, Integer offset, Integer end) {
		return contractDao.findConByName(contName, offset, end);
	}

	// 添加合同
	@Override
	public Boolean addContract(Contract contract) {
		Contract result = contractRepository.saveAndFlush(contract);
		if (result.getCont_id() != null)
			return true;
		else
			return false;
	}

	// 根据合同ID获取合同
	@Override
	public Contract selectContById(Integer cont_id) {
		return contractRepository.selectContById(cont_id);
	}

	// 根据合同ID删除合同
	@Override
	public Boolean deleteContract(Integer cont_id) {
		return contractDao.delete(cont_id);
	}

}
