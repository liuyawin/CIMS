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
	public Long countTotal(String contName, Integer methodType) {
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

	// 查询所有终结合同列表
	@Override
	public List<Contract> findAllEndCont(String contName, Integer offset, Integer end) {
		return contractDao.findAllEndCont(contName, offset, end);
	}

	// 修改合同基本信息
	@Override
	public Boolean updateConById(Integer cont_id, Contract contract) {
		return contractDao.updateConById(cont_id, contract);
	}

	// 张姣娜：根据合同id修改状态
	@Override
	public Boolean updateState(Integer contId, Integer contState) {
		return contractDao.updateState(contId, contState);
	}

	// 张姣娜：查询所有停建合同列表
	@Override
	public List<Contract> findAllStopCont(String contName, Integer offset, Integer end) {
		return contractDao.findAllStopCont(contName, offset, end);
	}

}
