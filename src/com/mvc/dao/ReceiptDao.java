/**
 * 
 */
package com.mvc.dao;

import java.util.List;

import com.mvc.entity.Receipt;

/**
 * 收据
 * 
 * @author zjn
 * @date 2016年9月15日
 */
public interface ReceiptDao {

	// 根据合同ID和页码查询该合同对应的所有收据
	List<Receipt> findByPage(Integer cont_id, String searchKey, Integer offset, Integer end);

	// 根据合同ID和搜索的关键字查询该合同对应的所有收据总条数
	Integer countByParam(Integer cont_id, String searchKey);

	// 根据合同ID查询收据总金额
	Float totalMoneyOfReceipt(Integer contId);

	// 根据收据ID删除收据
	Boolean delete(Integer receId);
}
