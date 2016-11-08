/**
 * 
 */
package com.mvc.service;

import java.util.List;

import com.mvc.entity.Receipt;
import com.mvc.entity.User;

import net.sf.json.JSONObject;

/**
 * 收据
 * 
 * @author zjn
 * @date 2016年9月15日
 */
public interface ReceiptService {

	// 根据合同ID查询该合同对应的所有收据
	List<Receipt> findAllByContId(Integer cont_id);

	// 根据合同ID和页码查询该合同对应的所有收据
	List<Receipt> findByPage(Integer cont_id, String searchKey, Integer offset, Integer end);

	// 根据合同ID查询该合同对应的所有收据总条数
	Integer countTotal(Integer cont_id);

	// 根据合同ID和搜索的关键字查询该合同对应的所有收据总条数
	Integer countByParam(Integer cont_id, String searchKey);

	// 根据收据Id查询该条数据的详情
	Receipt findByReceiptId(Integer rece_id);

	// 保存
	Boolean save(JSONObject jsonObject, Integer cont_id, User user);

	// 根据合同ID查询收据总金额
	Float totalMoneyOfReceipt(Integer contId);

	// 根据收据ID删除收据
	Boolean delete(Integer receId, User user);
}
