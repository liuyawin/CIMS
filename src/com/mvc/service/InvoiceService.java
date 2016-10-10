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

	// 根据合同id，页数返回任务列表
	List<Invoice> findByContId(Integer cont_id, Integer offset, Integer end);

	// 根据合同ID查询任务总条数
	Integer countByContId(Integer cont_id);

	// 根据合同ID查询发票总金额
	Float totalMoneyOfInvoice(Integer contId);

	// 创建发票
	boolean save(Invoice invoice);

	// 根据用户id，页数返回任务列表
	List<Invoice> findByPage(Integer user_id, Integer ifSend, Integer offset, Integer end);

	// 根据用户ID查询任务总条数
	Integer countByParam(Integer user_id, Integer ifSend);
}
