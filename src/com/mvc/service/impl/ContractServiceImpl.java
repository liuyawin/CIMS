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
	public List<Contract> findAllDebtCont(Integer creator_id, String contName, Integer offset, Integer end) {
		return contractDao.findAllDebtCont(creator_id, contName, offset, end);
	}

	// 查询所有逾期合同
	@Override
	public List<Contract> findAllOverdueCont(Integer creator_id, String contName, Integer offset, Integer end) {
		return contractDao.findAllOverdueCont(creator_id, contName, offset, end);
	}

	// 查询合同总条数
	@Override
	public Long countTotal(Integer creator_id, String contName, String methodType) {
		return contractDao.countTotal(creator_id, contName, methodType);
	}

	// 根据合同名获取合同信息
	@Override
	public List<Contract> findConByName(Integer creator_id, String contName, Integer offset, Integer end) {
		return contractDao.findConByName(creator_id, contName, offset, end);
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
