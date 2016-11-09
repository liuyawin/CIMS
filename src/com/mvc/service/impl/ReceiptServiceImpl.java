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
import com.mvc.dao.ReceiptDao;
import com.mvc.entity.Contract;
import com.mvc.entity.ContractRecord;
import com.mvc.entity.Receipt;
import com.mvc.entity.User;
import com.mvc.repository.ContractRecordRepository;
import com.mvc.repository.ContractRepository;
import com.mvc.repository.ReceiptRepository;
import com.mvc.service.ReceiptService;

import net.sf.json.JSONObject;

/**
 * 收据
 * 
 * @author zjn
 * @date 2016年9月15日
 */
@Service("receiptServiceImpl")
public class ReceiptServiceImpl implements ReceiptService {
	@Autowired
	ReceiptRepository receiptRepository;
	@Autowired
	ReceiptDao receiptDao;
	@Autowired
	ContractRepository contractRepository;
	@Autowired
	ContractRecordRepository contractRecordRepository;

	// 根据合同ID查询该合同对应的所有收据
	public List<Receipt> findAllByContId(Integer cont_id) {
		return receiptRepository.findAllByContId(cont_id);
	}

	// 根据合同ID查询该合同对应的所有收据总条数
	public Integer countTotal(Integer cont_id) {
		Long count = receiptRepository.countTotal(cont_id);
		Integer result = Integer.valueOf(count.toString());
		return result;
	}

	// 根据收据Id查询该条数据的详情
	public Receipt findByReceiptId(Integer rece_id) {
		return receiptRepository.findByReceiptId(rece_id);
	}

	// 保存
	public Boolean save(JSONObject jsonObject, Integer cont_id, User user) {
		Contract contract = contractRepository.selectContById(cont_id);
		Receipt receipt = new Receipt();
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			receipt.setContract(contract);
			receipt.setUser(user);
			if (jsonObject.containsKey("rece_atime")) {
				Date sdate = format.parse(jsonObject.getString("rece_atime"));
				receipt.setRece_atime(sdate);
			}
			if (jsonObject.containsKey("rece_firm")) {
				receipt.setRece_firm(jsonObject.getString("rece_firm"));
			}
			if (jsonObject.containsKey("rece_money")) {
				receipt.setRece_money(Float.valueOf(jsonObject.getString("rece_money")));
			}
			if (jsonObject.containsKey("rece_remark")) {
				receipt.setRece_remark(jsonObject.getString("rece_remark"));
			}
			receipt.setRece_isdelete(IsDelete.NO.value);
			if (jsonObject.containsKey("rece_id")) {
				receipt.setRece_id(Integer.valueOf(jsonObject.getString("rece_id")));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Receipt result = receiptRepository.saveAndFlush(receipt);

		// 合同日志
		ContractRecord contractRecord = new ContractRecord();
		contractRecord.setConre_content(
				user.getUser_name() + "---开收据，金额：" + receipt.getRece_money() + "万元---" + contract.getCont_name());
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		contractRecord.setConre_time(date);
		contractRecord.setContract(contract);
		contractRecord.setUser(user);
		contractRecordRepository.saveAndFlush(contractRecord);

		if (result.getRece_id() != null)
			return true;
		else
			return false;
	}

	// 根据合同ID和页码查询该合同对应的所有收据
	public List<Receipt> findByPage(Integer cont_id, String searchKey, Integer offset, Integer end) {
		return receiptDao.findByPage(cont_id, searchKey, offset, end);
	}

	// 根据合同ID和搜索的关键字查询该合同对应的所有收据总条数
	public Integer countByParam(Integer cont_id, String searchKey) {
		return receiptDao.countByParam(cont_id, searchKey);
	}

	// 根据合同ID查询收据总金额
	public Float totalMoneyOfReceipt(Integer contId) {
		return receiptDao.totalMoneyOfReceipt(contId);
	}

	// 根据收据ID删除收据
	@Override
	public Boolean delete(Integer receId, User user) {
		Receipt receipt = receiptRepository.findByReceiptId(receId);
		Contract contract = receipt.getContract();
		Boolean flag = receiptDao.delete(receId);
		if (flag) {
			// 合同日志
			ContractRecord contractRecord = new ContractRecord();
			contractRecord.setConre_content(
					user.getUser_name() + "---删除收据，金额：" + receipt.getRece_money() + "万元---" + contract.getCont_name());
			long time = System.currentTimeMillis();
			Date date = new Date(time);
			contractRecord.setConre_time(date);
			contractRecord.setContract(contract);
			contractRecord.setUser(user);
			contractRecordRepository.saveAndFlush(contractRecord);
		}
		return receiptDao.delete(receId);
	}

}
