/**
 * 
 */
package com.mvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.dao.ReceiptDao;
import com.mvc.entity.Receipt;
import com.mvc.repository.ReceiptRepository;
import com.mvc.service.ReceiptService;

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
	public boolean save(Receipt receipt) {
		Receipt result = receiptRepository.save(receipt);
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


}
