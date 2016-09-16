package com.mvc.dao;

import java.util.List;

import com.mvc.entity.Invoice;

/**
 * 发票
 * 
 * @author zjn
 * @date 2016年9月16日
 */
public interface InvoiceDao {

	// 根据发票id修改状态
	boolean updateState(Integer id, Integer state);

	// 根据发票id删除发票
	boolean delete(Integer invoiceId);

	// 根据合同ID，页数，关键字返回任务列表
	List<Invoice> findByPage(Integer cont_id, String searchKey, Integer offset, Integer end);

	// 根据合同ID，关键字查询任务总条数
	Integer countByParam(Integer cont_id, String searchKey);
}
