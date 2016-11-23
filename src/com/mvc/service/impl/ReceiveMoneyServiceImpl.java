/**
 * 
 */
package com.mvc.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.base.enums.IsDelete;
import com.base.enums.ReceiveMoneyStatus;
import com.mvc.dao.ReceiveMoneyDao;
import com.mvc.entity.Contract;
import com.mvc.entity.ContractRecord;
import com.mvc.entity.ReceiveMoney;
import com.mvc.entity.User;
import com.mvc.repository.ContractRecordRepository;
import com.mvc.repository.ContractRepository;
import com.mvc.repository.ReceiveMoneyRepository;
import com.mvc.service.ReceiveMoneyService;

import net.sf.json.JSONObject;

/**
 * 到款
 * 
 * @author zjn
 * @date 2016年10月27日
 */
@Service("receiveMoneyServiceImpl")
public class ReceiveMoneyServiceImpl implements ReceiveMoneyService {
	@Autowired
	ReceiveMoneyRepository receiveMoneyRepository;
	@Autowired
	ReceiveMoneyDao receiveMoneyDao;
	@Autowired
	ContractRepository contractRepository;
	@Autowired
	ContractRecordRepository contractRecordRepository;

	// 根据合同ID获取已到款钱数
	@Override
	public Float receiveMoneyByContId(Integer contId) {
		return receiveMoneyDao.receiveMoneyByContId(contId);
	}

	// 根据ID查询详情
	@Override
	public ReceiveMoney findByRemoId(Integer remoId) {
		return receiveMoneyRepository.findById(remoId);
	}

	// 根据参数获取该合同的所有到款记录
	@Override
	public List<ReceiveMoney> findListByParam(Integer contId, Integer remoState, Integer offset, Integer end) {
		return receiveMoneyDao.findListByParam(contId, remoState, offset, end);
	}

	// 根据参数获取该合同的所有到款记录总条数
	@Override
	public Integer countByParam(Integer contId, Integer remoState) {
		return receiveMoneyDao.countByParam(contId, remoState);
	}

	// 审核到款记录
	@Override
	public Boolean updateRemoStateById(Integer remoId, Float remoAmoney, User user) {
		Boolean result = receiveMoneyDao.updateRemoStateById(remoId, remoAmoney);
		ReceiveMoney receiveMoney = receiveMoneyRepository.findById(remoId);
		Contract contract = receiveMoney.getContract();
		if (result) {
			// 合同日志
			ContractRecord contractRecord = new ContractRecord();
			contractRecord.setConre_content(user.getUser_name() + "---核对到款，实收：" + receiveMoney.getRemo_amoney()
					+ "万元---" + contract.getCont_name());
			long time = System.currentTimeMillis();
			Date date = new Date(time);
			contractRecord.setConre_time(date);
			contractRecord.setContract(contract);
			contractRecord.setUser(user);
			contractRecordRepository.saveAndFlush(contractRecord);
		}
		return result;
	}

	// 新增到款
	@Override
	public Boolean save(JSONObject jsonObject, Integer cont_id, User user) {
		Contract contract = contractRepository.selectContById(cont_id);
		ReceiveMoney receiveMoney = new ReceiveMoney();
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			receiveMoney.setContract(contract);
			receiveMoney.setCreater(user);
			if (jsonObject.containsKey("remo_time")) {
				Date remoTime = format.parse(jsonObject.getString("remo_time"));
				receiveMoney.setRemo_time(remoTime);
			}
			if (jsonObject.containsKey("remo_money")) {
				receiveMoney.setRemo_money(Float.valueOf(jsonObject.getString("remo_money")));
			}
			if (jsonObject.containsKey("remo_remark")) {
				receiveMoney.setRemo_remark(jsonObject.getString("remo_remark"));
			}
			if (jsonObject.containsKey("operater")) {
				JSONObject tmp = JSONObject.fromObject(jsonObject.getString("operater"));
				User operater = new User();
				operater.setUser_id(Integer.valueOf(tmp.getString("user_id")));
				receiveMoney.setOperater(operater);
			}
			receiveMoney.setRemo_state(ReceiveMoneyStatus.waitAudit.value);
			receiveMoney.setRemo_isdelete(IsDelete.NO.value);
			receiveMoney.setRemo_amoney(Float.valueOf(0));
			if (jsonObject.containsKey("remo_id")) {
				receiveMoney.setRemo_id(Integer.valueOf(jsonObject.getString("remo_id")));
			}
			receiveMoney.setCont_stime(contract.getCont_stime());
			receiveMoney.setProvince(contract.getProvince());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		ReceiveMoney result = receiveMoneyRepository.saveAndFlush(receiveMoney);

		// 合同日志
		ContractRecord contractRecord = new ContractRecord();
		contractRecord.setConre_content(
				user.getUser_name() + "---到款，应收：" + receiveMoney.getRemo_money() + "万元---" + contract.getCont_name());
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		contractRecord.setConre_time(date);
		contractRecord.setContract(contract);
		contractRecord.setUser(user);
		contractRecordRepository.saveAndFlush(contractRecord);

		if (result.getRemo_id() != null) {
			return true;
		} else {
			return false;
		}
	}

	// 根据状态查询到款记录
	@Override
	public List<ReceiveMoney> findListByState(Integer userId, Integer remoState, Integer offset, Integer end) {
		return receiveMoneyDao.findListByState(userId, remoState, offset, end);
	}

	// 根据状态查询到款记录总条数
	@Override
	public Integer countByState(Integer userId, Integer remoState) {
		return receiveMoneyDao.countByState(userId, remoState);
	}

	// 根据到款ID删除到款记录
	@Override
	public Boolean delete(Integer remoId, User user) {
		Boolean flag = receiveMoneyDao.delete(remoId);
		ReceiveMoney receiveMoney = receiveMoneyRepository.findById(remoId);
		Contract contract = receiveMoney.getContract();
		if (flag) {
			// 合同日志
			ContractRecord contractRecord = new ContractRecord();
			contractRecord.setConre_content(user.getUser_name() + "---删除到款，应收：" + receiveMoney.getRemo_money()
					+ "万元，实收：" + receiveMoney.getRemo_amoney() + "万元---" + contract.getCont_name());
			long time = System.currentTimeMillis();
			Date date = new Date(time);
			contractRecord.setConre_time(date);
			contractRecord.setContract(contract);
			contractRecord.setUser(user);
			contractRecordRepository.saveAndFlush(contractRecord);
		}
		return flag;
	}

}
