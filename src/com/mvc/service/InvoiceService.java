/**
 * 
 */
package com.mvc.service;

import java.util.List;

import com.mvc.entity.Invoice;

/**
 * 发票
 * 
 * @author zjn
 * @date 2016年9月16日
 */
public interface InvoiceService {

	// 根据发票ID查询发票详情
	Invoice findById(Integer invoiceId);

	// 根据发票id删除发票
	boolean delete(Integer invoiceId);

	// 根据合同id，页数,关键字返回任务列表
	List<Invoice> findByPage(Integer cont_id, String searchKey, Integer offset, Integer end);

	// 根据合同ID，关键字查询任务总条数
	Integer countByParam(Integer cont_id, String searchKey);
}
