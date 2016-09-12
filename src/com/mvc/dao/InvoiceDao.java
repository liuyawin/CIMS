package com.mvc.dao;
/**
 * 
 * @author wanghuimin
 * @date 2016年9月9日
 */
public interface InvoiceDao {
	// 根据发票id修改状态
		public boolean updateState(Integer id, Integer state);

}
