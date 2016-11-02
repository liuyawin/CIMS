package com.mvc.dao;

import java.util.Date;
import java.util.List;

import com.mvc.entity.Invoice;

/**
 * 发票
 * 
 * @author zjn
 * @date 2016年9月16日
 */
public interface InvoiceDao {

	// 根据发票id删除发票
	boolean delete(Integer invoiceId);

	// 根据合同ID，页数返回发票列表
	List<Invoice> findByContId(Integer cont_id, Integer offset, Integer end);

	// 根据合同ID查询发票总条数
	Integer countByContId(Integer cont_id);

	// 根据合同ID查询发票总金额
	Float totalMoneyOfInvoice(Integer contId);

	// 根据用户id，页数返回发票列表
	List<Invoice> findByPage(Integer user_id, Integer ifSend, Integer offset, Integer end);

	// 根据用户ID查询发票总条数
	Integer countByParam(Integer user_id, Integer ifSend);

	// 按发票状态获取列表
	Integer WaitingDealCountByParam(Integer user_id, Integer invoiceState);

	// 根据用户id，页数返回发票列表
	List<Invoice> WaitingDealFindByPage(Integer user_id, Integer invoiceState, Integer offset, Integer end);

	// 点击完成更新发票状态
	boolean invoiceFinish(Integer invoiceId, Date invoTime);

	// 主任转发发票
	boolean transmitInvoice(Integer invoiceId, Date invoEtime, Integer receiverId);

	// 根据合同ID，权限，状态，页码 查找发票
	List<Invoice> findByAllAndPerm(String permission, Integer user_id, Integer cont_id, Integer offset, Integer end);

	// 根据合同ID，权限，状态，页码 查找发票
	List<Invoice> findByStateAndPerm(Integer invoState, String permission, Integer user_id, Integer cont_id,
			Integer offset, Integer end);

	// 根据合同ID，权限，全部状态 查询发票总条数
	Integer countByAllAndPerm(String permission, Integer user_id, Integer cont_id);

	// 根据合同ID，权限，状态 查询发票总条数
	Integer countByStateAndPerm(Integer invoState, String permission, Integer user_id, Integer cont_id);
}