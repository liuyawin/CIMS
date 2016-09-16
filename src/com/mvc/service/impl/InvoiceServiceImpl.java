/**
 * 
 */
package com.mvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.dao.InvoiceDao;
import com.mvc.entity.Invoice;
import com.mvc.repository.InvoiceRepository;
import com.mvc.service.InvoiceService;

/**
 * 发票
 * 
 * @author zjn
 * @date 2016年9月16日
 */
@Service("invoiceServiceImpl")
public class InvoiceServiceImpl implements InvoiceService {
	@Autowired
	InvoiceRepository invoiceRepository;
	@Autowired
	InvoiceDao invoiceDao;

	// 根据发票ID查询发票详情
	public Invoice findById(Integer invoiceId) {

		return invoiceRepository.findById(invoiceId);
	}

	// 根据发票id删除发票
	public boolean delete(Integer invoiceId) {
		return invoiceDao.delete(invoiceId);
	}

	// 根据合同id，页数,关键字返回任务列表
	public List<Invoice> findByPage(Integer cont_id, String searchKey, Integer offset, Integer end) {
		return invoiceDao.findByPage(cont_id, searchKey, offset, end);
	}

	// 根据合同ID，关键字查询任务总条数
	public Integer countByParam(Integer cont_id, String searchKey) {
		return invoiceDao.countByParam(cont_id, searchKey);
	}

}