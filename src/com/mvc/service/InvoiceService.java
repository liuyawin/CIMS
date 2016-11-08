/**
 * 
 */
package com.mvc.service;

import java.util.Date;
import java.util.List;

import com.mvc.entity.Invoice;
import com.mvc.entity.User;
import com.utils.Pager;

import net.sf.json.JSONObject;

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
	Boolean delete(Integer invoiceId, User user);

	// 根据合同id，页数返回发票列表
	List<Invoice> findByContId(Integer cont_id, Integer offset, Integer end);

	// 根据合同ID查询发票总条数
	Integer countByContId(Integer cont_id);

	// 根据合同ID查询发票总金额
	Float totalMoneyOfInvoice(Integer contId);

	// 创建发票
	Boolean save(JSONObject jsonObject, Integer cont_id, User user);

	// 根据用户id，页数返回发票列表
	List<Invoice> findByPage(Integer user_id, Integer invoState, Integer offset, Integer end);

	// 根据用户ID查询任务总条数
	Integer countByParam(Integer user_id, Integer invoState);

	// 按发票状态获取列表
	Integer WaitingDealCountByParam(Integer user_id, Integer invoiceState);

	// 根据用户id，页数返回发票列表
	List<Invoice> WaitingDealFindByPage(Integer user_id, Integer invoiceState, Integer offset, Integer end);

	// 点击完成更新发票状态
	Boolean invoiceFinish(Integer invoiceId, Date invoTime, User user);

	// 主任转发发票
	boolean transmitInvoice(Integer invoiceId, Date invoEtime, Integer receiverId);

	// 根据权限，状态，页码 查找发票
	List<Invoice> selectInvoByStateAndPerm(Integer invoState, String permission, Integer user_id, Pager pager);

	// 根据权限，状态 查询发票总条数
	Integer countByStateAndPerm(Integer invoState, String permission, Integer user_id);

	// 根据合同ID，状态，页码 查找发票
	List<Invoice> selectInvoByStateAndContId(Integer invoState, Integer cont_id, Pager pager);

	// 根据合同ID，状态 查询发票总条数
	Integer countByStateAndContId(Integer invoState, Integer cont_id);

	// 根据合同ID获取已完成发票总条数
	Integer countTotalRow(Integer cont_id);
}
